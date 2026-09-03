package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * 규칙을 실제 코드(target-service)에 건다. 이 테스트가 초록이면 검사 대상이 규칙을 지킨 것이다.
 *
 * 픽스처(com.brunosong.sample.archunit.fixture)는 base 패키지가 달라서 여기 안 잡힌다.
 */
@AnalyzeClasses(
        packages = "com.brunosong.sample.archunit.target",
        importOptions = ImportOption.DoNotIncludeTests.class)
class TargetServiceArchitectureTest {

    @ArchTest
    static final ArchRule layerDependency = LayerRules.LAYER_DEPENDENCY;

    @ArchTest
    static final ArchRule domainIsFrameworkFree = LayerRules.DOMAIN_IS_FRAMEWORK_FREE;

    @ArchTest
    static final ArchRule persistenceStaysInDataaccess = LayerRules.PERSISTENCE_STAYS_IN_DATAACCESS;

    @ArchTest
    static final ArchRule orderTouchesProductOnlyThroughInPort =
            SubdomainRules.ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT;

    @ArchTest
    static final ArchRule productDoesNotKnowOrder = SubdomainRules.PRODUCT_DOES_NOT_KNOW_ORDER;

    @ArchTest
    static final ArchRule inPortsAreUseCaseInterfaces = NamingRules.IN_PORTS_ARE_USECASE_INTERFACES;

    @ArchTest
    static final ArchRule outPortsArePortInterfaces = NamingRules.OUT_PORTS_ARE_PORT_INTERFACES;

    @ArchTest
    static final ArchRule controllersLiveInConsumerPackage = NamingRules.CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE;
}
