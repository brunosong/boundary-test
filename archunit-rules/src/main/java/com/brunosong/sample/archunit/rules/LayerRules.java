package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * 헥사고날 레이어 규칙.
 *
 * 패키지 패턴을 "..domain.." 처럼 상대적으로 쓰기 때문에 base 패키지가 무엇이든
 * 레이어 이름만 같으면 그대로 적용된다. 그래서 같은 규칙을 target-service 에도,
 * 테스트용 위반 픽스처에도 걸 수 있다.
 */
public final class LayerRules {

    private LayerRules() {
    }

    /**
     * web 과 dataaccess 는 서로를 모르고, 둘 다 application 을 통해서만 안쪽으로 들어간다.
     * consideringOnlyDependenciesInLayers 는 레이어에 속하지 않는 클래스(부트 진입점, JDK,
     * 스프링 등)로 향하는 의존을 셈에서 빼서 레이어 사이 관계만 본다.
     */
    public static final ArchRule LAYER_DEPENDENCY = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("domain").definedBy("..domain..")
            .layer("application").definedBy("..application..")
            .layer("dataaccess").definedBy("..dataaccess..")
            .layer("web").definedBy("..web..")
            .whereLayer("web").mayNotBeAccessedByAnyLayer()
            .whereLayer("dataaccess").mayNotBeAccessedByAnyLayer()
            .whereLayer("application").mayOnlyBeAccessedByLayers("web", "dataaccess")
            .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "dataaccess", "web")
            .because("어댑터(web, dataaccess)는 안쪽을 향해서만 의존한다");

    /** 도메인은 프레임워크 없이 컴파일되고 테스트돼야 한다. */
    public static final ArchRule DOMAIN_IS_FRAMEWORK_FREE = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..",
                    "jakarta.persistence..",
                    "com.fasterxml.jackson..")
            .because("도메인이 프레임워크를 알면 규칙을 검증하려고 컨텍스트를 띄우게 된다");

    /** 영속 애너테이션은 dataaccess 밖으로 새지 않는다. */
    public static final ArchRule PERSISTENCE_STAYS_IN_DATAACCESS = noClasses()
            .that().resideOutsideOfPackage("..dataaccess..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "jakarta.persistence..",
                    "org.hibernate..")
            .because("영속 모델이 새면 화면과 도메인이 테이블 모양에 묶인다");
}
