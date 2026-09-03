# boundary-test

아키텍처 경계를 빌드로 강제하는 두 가지 방법을 나란히 두고 비교하는 저장소.
ArchUnit 과 Spring Modulith 를 같은 소재(order, product 같은 도메인)로 각각 구현했다.

```
boundary-test/
  target-service/     ArchUnit 검사 대상. 레이어(domain/application/dataaccess/web) 안에 서브도메인을 중첩한 부트 앱
  archunit-rules/     ArchUnit 규칙 정의(main) + 규칙을 target-service 에 거는 테스트(test)
  modulith-sample/    Spring Modulith 예제. 모듈 경계를 패키지 구조로 표현한 부트 앱
```

```bash
./mvnw test
```

Java 21, Spring Boot 3.5.6, ArchUnit 1.5.0, Spring Modulith 1.4.13.

## 두 방식의 차이

| | ArchUnit | Spring Modulith |
|---|---|---|
| 규칙 | 직접 쓴다 | 이미 정해져 있다 |
| 패키지 구조 | 아무 구조나 | 관례를 따라야 한다 |
| 검사 범위 | 원하는 만큼 | 모듈 경계와 순환만 |
| 규칙과 대상 분리 | 된다(모듈을 나눌 수 있다) | 안 된다(앱 자신을 검사한다) |
| 새 프로젝트 | 규칙 쓰는 품이 든다 | 즉시 얻는다 |
| 기존 프로젝트 | 현 구조에 맞춰 쓴다 | 구조를 맞춰야 한다 |

경쟁 관계는 아니다. Spring Modulith 는 내부적으로 ArchUnit 을 쓰고, 한 프로젝트에서 둘 다 돌릴 수 있다.
경계와 순환은 Modulith 에 맡기고 레이어, 이름, 프레임워크 격리는 ArchUnit 으로 직접 쓰는 식이다.

# ArchUnit

## 왜 모듈을 둘로 나눴나

규칙을 검사 대상과 같은 모듈에 두면 그 모듈 밖에서는 못 쓴다. 규칙을 따로 두면 대상이 늘어도(앱 하나에서 서비스 여럿으로) 규칙 모듈은 그대로 두고 대상만 test 의존으로 추가하면 된다.

`archunit-rules` 의 main 은 ArchUnit 코어에만 의존한다. JUnit 도 스프링도 모른다. 그래서 이 모듈은 다른 저장소에 그대로 복사해도 돌아간다. JUnit 을 쓰는 것은 test 쪽뿐이다.

`target-service` 는 실행 가능한 부트 앱이다. 실행 가능한 jar 는 `spring-boot-maven-plugin` 의 `classifier` 를 붙여 따로 만든다. 기본 설정이면 repackage 결과가 주 아티팩트를 덮어써서 `archunit-rules` 가 이 모듈의 클래스를 못 읽는다.

```bash
./mvnw -pl target-service spring-boot:run

curl -X POST localhost:8080/api/v1/orders -H 'Content-Type: application/json' -d '{"sku":"SKU-1","quantity":2}'
curl localhost:8080/api/v1/admin/orders/{주문번호}
```

## 규칙

패키지 패턴을 `..domain..` 처럼 상대적으로 쓴다. base 패키지가 무엇이든 레이어 이름만 같으면 그대로 붙는다. 덕분에 같은 규칙 객체를 실제 코드와 위반 픽스처 양쪽에 걸 수 있다.

### LayerRules

| 규칙 | 무엇을 막나 |
|---|---|
| `LAYER_DEPENDENCY` | web 과 dataaccess 가 서로를 보는 것. 어댑터가 application 을 건너뛰는 것 |
| `DOMAIN_IS_FRAMEWORK_FREE` | 도메인에 스프링, JPA, 잭슨이 들어오는 것 |
| `PERSISTENCE_STAYS_IN_DATAACCESS` | 영속 애너테이션이 dataaccess 밖으로 새는 것 |

`LAYER_DEPENDENCY` 는 `consideringOnlyDependenciesInLayers()` 를 쓴다. 레이어에 속하지 않는 클래스(부트 진입점, JDK, 스프링)로 향하는 의존을 셈에서 빼고 레이어 사이 관계만 본다. 이것을 빼먹으면 `@SpringBootApplication` 클래스 하나 때문에 규칙이 붉어진다.

### SubdomainRules

레이어 경계는 메이븐 모듈이 컴파일 타임에 막아 준다. 하지만 한 모듈 안의 서브도메인끼리는 아무거나 import 해도 빌드가 통과한다. 여기가 ArchUnit 이 아니면 못 막는 자리다.

| 규칙 | 무엇을 막나 |
|---|---|
| `ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT` | order 가 product 의 도메인, 서비스, 어댑터를 직접 잡는 것 |
| `PRODUCT_DOES_NOT_KNOW_ORDER` | 반대 방향 의존(순환) |

정책이 대칭이 아니라는 점이 핵심이다. order 는 product 를 `application.product.port.in` 패키지로만 부른다. `ProductLookupUseCase` 와 그 반환 타입 `ProductSnapshot` 이 거기 있고, 그것이 product 가 밖으로 내보내는 전부다. 나중에 product 를 별도 서비스로 떼어낼 때 갈라지는 선이 바로 그 패키지다.

`ProductSnapshot` 을 왜 in 포트 패키지에 두는지도 이 규칙이 설명한다. 도메인 엔티티 `Product` 를 그대로 넘기면 받는 쪽이 상태를 바꿀 수 있고, `application.product.dto` 같은 곳에 두면 그 패키지도 공개 표면이 되어 경계가 흐려진다.

### NamingRules

| 규칙 | 무엇을 막나 |
|---|---|
| `IN_PORTS_ARE_USECASE_INTERFACES` | in 포트 이름이 제각각인 것 |
| `OUT_PORTS_ARE_PORT_INTERFACES` | out 포트 이름이 제각각인 것 |
| `CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE` | 컨트롤러가 소비자 폴더(admin, client) 밖에 있는 것 |

in 포트 규칙은 `.and().areInterfaces()` 로 대상을 좁힌다. 포트 패키지에는 공개 계약 DTO(`ProductSnapshot`)도 함께 사는데, 그것까지 UseCase 로 끝나라고 하면 규칙이 틀린다.

## 규칙이 실제로 막고 있는지 확인한다

테스트가 둘이다.

**`TargetServiceArchitectureTest`** 는 규칙을 `target-service` 에 건다. `@AnalyzeClasses` 로 클래스를 한 번 읽고 `@ArchTest` 필드마다 규칙을 적용한다. 초록이면 검사 대상이 규칙을 지킨 것이다.

**`ViolationDetectionTest`** 는 일부러 어긴 코드에 같은 규칙을 걸어 실패하는지 본다. 이게 없으면 규칙이 늘 참인지 아닌지 알 수 없다. 오타 난 패키지 패턴은 아무것도 못 잡으면서 계속 초록이다.

위반 픽스처는 `archunit-rules/src/test/java/.../fixture` 에 있다. base 패키지가 `...archunit.fixture` 라 `...archunit.target` 을 읽는 쪽에 섞이지 않는다.

| 픽스처 | 어긴 것 |
|---|---|
| `LeakyOrderController` | 컨트롤러가 영속 엔티티를 직접 잡는다 |
| `FrameworkAwareOrder` | 도메인 엔티티에 JPA 애너테이션을 붙였다 |
| `CrossSubdomainOrderService` | order 가 product 의 도메인 엔티티를 직접 잡는다 |
| `OrderFinder`, `OrderStore` | 포트 이름 규칙을 어겼다 |
| `MisplacedOrderController` | 소비자 폴더 밖에 있는 컨트롤러 |

`CrossSubdomainOrderService` 는 레이어(application -> domain)를 지켰다. 레이어 규칙만으로는 안 잡히고 서브도메인 규칙이 잡는다. 규칙 둘이 서로를 대신하지 못한다는 것을 이 픽스처가 보여 준다.

## 기존 코드에 처음 붙일 때

이미 위반이 수백 개인 저장소에 규칙을 걸면 빌드가 처음부터 붉다. 그러면 규칙을 지우거나 주석 처리하게 된다.

`FreezingArchRule.freeze(rule)` 로 감싸면 현재 위반을 기준선으로 저장하고, 새로 생기는 위반만 실패시킨다. 고친 만큼 기준선이 줄어든다. 여기서는 대상이 처음부터 깨끗해서 쓰지 않았다.

## 한계

- 컴파일된 바이트코드를 읽는다. 리플렉션이나 문자열 빈 이름으로 도는 의존은 안 보인다.
- 규칙이 틀려도 초록이다. `ViolationDetectionTest` 같은 대응 테스트가 있어야 규칙 자체를 검증할 수 있다.
- 클래스 임포트가 느리다. 대상이 커지면 `@AnalyzeClasses` 캐시(같은 클래스에서 한 번 읽고 여러 `@ArchTest` 가 공유)를 쓰는 편이 낫다. 위의 두 테스트가 각각 그 방식과 수동 임포트 방식이다.

# Spring Modulith

## 관례가 곧 규칙이다

규칙을 쓰지 않는다. 패키지 구조가 규칙이고, `verify()` 가 그것을 검사한다.

```
base 패키지 = @SpringBootApplication 클래스가 있는 패키지
모듈        = base 패키지의 직계 하위 패키지 하나
공개 API    = 그 모듈 base 패키지에 직접 있는 타입만
internal    = 그 밖 전부
```

```java
class ModuleStructureTest {

    static final ApplicationModules MODULES = ApplicationModules.of(ModulithSampleApplication.class);

    @Test
    void 모듈_구조를_출력한다() {
        MODULES.forEach(System.out::println);
    }

    @Test
    void 모듈_경계를_검증한다() {
        MODULES.verify();
    }
}
```

구조를 출력하는 테스트를 같이 두는 이유가 있다. 모듈이 0개면 `verify()` 는 그냥 통과한다.
ArchUnit 쪽에서 본 것과 같은 성질이라, 초록인 것만으로는 아무것도 보장되지 않는다.

## 모듈 셋

| 모듈 | 공개 API | 내부 |
|---|---|---|
| `order` | `OrderService` | `internal/OrderRepository` |
| `recruiting` | `JobPostingReferenceUseCase`, `JobPostingBasicInfo` | `internal/` 아래 도메인, 저장소, 구현 |
| `product` | `ProductLookupUseCase` (결과 record 를 안에 중첩) | `domain/`, `application/`, `dataaccess/` |

`recruiting` 과 `product` 가 일부러 다르게 생겼다.

## internal 은 이름이 아니라 위치다

`recruiting` 은 하위 패키지 하나를 `internal` 로 뭉쳤고, `product` 는 `domain`, `application`, `dataaccess` 로 나눴다.
Modulith 는 패키지 이름을 보지 않는다. 모듈 base 패키지 바로 아래인지만 본다. 그래서 둘 다 똑같이 닫힌다.
`internal` 은 스프링 문서가 쓰는 관례일 뿐 예약어가 아니다.

컨트롤러도 같은 이유로 하위 패키지에 둔다. 컨트롤러를 부르는 것은 다른 모듈이 아니라 HTTP 라서,
코드상 아무도 참조하지 않으니 internal 로 둬도 문제가 없다.
오히려 다른 모듈이 컨트롤러를 직접 부르면 `verify()` 가 잡아 준다.

## 공개 계약은 시그니처로 닿는 타입까지다

인터페이스만 공개해서는 못 쓴다. 파라미터, 반환 타입, 던지는 예외가 전부 공개여야 계약이 성립한다.
두 모듈이 이것을 다르게 푼다.

`recruiting` 은 별도 파일로 둔다.

```java
public interface JobPostingReferenceUseCase {
    Map<String, JobPostingBasicInfo> getBasicInfoByUuids(List<String> jobPostingUuids);
}

public record JobPostingBasicInfo(String jobPostingUuid, String title,
                                  LocalDateTime createdAt, String agencyUuid) {}
```

`product` 는 인터페이스 안에 중첩한다. 중첩 타입의 패키지는 바깥 클래스와 같으므로 자동으로 공개가 된다.

```java
public interface ProductLookupUseCase {
    List<Summary> findSellable();
    Optional<Detail> findByCode(String productCode);

    record Summary(String productCode, String name, long price) {}
    record Detail(String productCode, String name, long price,
                  boolean sellable, LocalDateTime registeredAt) {}
}
```

별도 파일은 여러 계약이 DTO 를 공유할 수 있는 대신 모듈 base 패키지에 파일이 쌓인다.
중첩은 계약이 파일 하나로 닫혀 base 패키지가 계약 수만큼만 늘어나는 대신, 그 DTO 를 다른 계약과 공유하는 순간 별도 파일로 빼야 한다.

두 도메인 엔티티(`JobPosting`, `Product`)에는 상태 변경 메서드를 일부러 넣었다.
`hideChat()`, `stopSelling()` 이 있는 물건을 공개 계약에 실으면 제목 하나 읽으려던 쪽이 상태를 바꿀 수 있다.
그래서 서비스가 record 로 바꿔서 내준다.

공개 DTO 는 원시 타입과 JDK 타입으로만 짜는 편이 낫다.
필드에 internal 타입이 들어가면 그것을 만지는 쪽에서 다시 위반이 난다.
`JobPostingBasicInfo` 가 `Agency` 객체 대신 `agencyUuid` 를 `String` 으로 받는 이유다.

## verify() 가 보는 것

셋뿐이고 여기에 내 규칙을 추가할 수 없다.

| 검사 | 내용 |
|---|---|
| 모듈 간 순환 | order -> product -> order 같은 것 |
| internal 접근 | 다른 모듈의 base 패키지 밖 타입을 잡는 것 |
| 선언한 의존 | `@ApplicationModule(allowedDependencies = ...)` 를 붙였을 때만 |

레이어 방향, 이름 규칙, 프레임워크 격리는 보지 않는다. `target-service` 에 건 ArchUnit 규칙 8개 중 2개 축만 덮는 셈이다.

조절 손잡이는 넷이다. 모듈의 `package-info.java` 에 붙이는 `@ApplicationModule`,
하위 패키지를 공개로 여는 `@NamedInterface`, 애플리케이션 클래스에 붙이는 `@Modulithic`,
모듈 탐지 방식을 바꾸는 `ApplicationModuleDetectionStrategy`.

## 모듈을 하나만 둔 이유

ArchUnit 쪽은 규칙(`archunit-rules`)과 검사 대상(`target-service`)을 메이븐 모듈로 갈랐다. 규칙이 대상과 독립이라 다른 저장소에 복사할 수도 있다.

Modulith 는 못 가른다. 모듈 경계를 앱의 패키지 구조에서 읽어내고, 검증도 그 앱의 test 소스에서 자기 자신을 대상으로 돌린다. 이 차이 자체가 두 방식의 성질이라 억지로 맞추지 않았다.

## 버전 주의

Spring Boot 의 BOM 은 Spring Modulith 를 관리하지 않는다. 루트 pom 에서 직접 고정해야 하고, 메이저 버전이 Boot 버전에 묶여 있다.

```
Modulith 1.4.x  ->  Spring Boot 3.5
Modulith 2.x    ->  Spring Boot 4.1
```

Boot 를 올리면 Modulith 도 같이 올려야 한다.
