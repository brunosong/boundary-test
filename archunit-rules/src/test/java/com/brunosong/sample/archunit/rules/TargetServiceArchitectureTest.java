package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * 규칙을 실제 코드(target-service)에 건다. 이 테스트가 초록이면 검사 대상이 규칙을 지킨 것이다.
 *
 * 픽스처(com.brunosong.sample.archunit.fixture)는 base 패키지가 달라서 여기 안 잡힌다.
 *
 * @ArchTest 는 필드 이름이 그대로 테스트 이름이 된다. JUnit 의 @DisplayName 이 안 먹으므로
 * 필드 이름 자체를 읽을 수 있게 짓는다.
 */
@AnalyzeClasses(
        packages = "com.brunosong.sample.archunit.target",
        importOptions = ImportOption.DoNotIncludeTests.class)
class TargetServiceArchitectureTest {

    @ArchTest
    static final ArchRule 레이어는_안쪽으로만_의존한다 = LayerRules.LAYER_DEPENDENCY;

    @ArchTest
    static final ArchRule 도메인은_프레임워크를_모른다 = LayerRules.DOMAIN_IS_FRAMEWORK_FREE;

    @ArchTest
    static final ArchRule 영속_애너테이션은_dataaccess_안에만_있다 = LayerRules.PERSISTENCE_STAYS_IN_DATAACCESS;

    @ArchTest
    static final ArchRule order_는_product_를_in_포트로만_부른다 =
            SubdomainRules.ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT;

    @ArchTest
    static final ArchRule product_는_order_를_모른다 = SubdomainRules.PRODUCT_DOES_NOT_KNOW_ORDER;

    @ArchTest
    static final ArchRule in_포트는_UseCase_로_끝난다 = NamingRules.IN_PORTS_ARE_USECASE_INTERFACES;

    @ArchTest
    static final ArchRule out_포트는_Port_로_끝난다 = NamingRules.OUT_PORTS_ARE_PORT_INTERFACES;

    @ArchTest
    static final ArchRule 컨트롤러는_소비자_폴더_안에_있다 = NamingRules.CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE;
}
