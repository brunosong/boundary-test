# boundary-test

ArchUnit 으로 아키텍처 규칙을 빌드에 박아 두는 예제. 멀티 모듈이고, 규칙을 건 대상까지 같이 들어 있다.

```
boundary-test/
  target-service/    검사 대상. 레이어(domain/application/dataaccess/web) 안에 서브도메인(order/product)을 중첩한 스프링 부트 앱
  archunit-rules/     규칙 정의(main) + 규칙을 target-service 에 거는 테스트(test)
```

```bash
./mvnw test
```

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
