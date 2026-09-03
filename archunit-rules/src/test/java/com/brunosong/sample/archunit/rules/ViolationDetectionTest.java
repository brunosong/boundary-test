package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 규칙이 "통과한다"는 것만 보면 규칙이 실제로 무언가를 막고 있는지 알 수 없다.
 * 늘 참인 규칙도 초록이기 때문이다.
 *
 * 그래서 일부러 어긴 픽스처를 따로 두고, 같은 규칙을 거기에 걸어 실패하는지 확인한다.
 * 규칙을 고칠 때 이 테스트가 안전망이 된다.
 */
class ViolationDetectionTest {

    private static final JavaClasses FIXTURE = new ClassFileImporter()
            .importPackages("com.brunosong.sample.archunit.fixture");

    @Test
    @DisplayName("컨트롤러가 영속 엔티티를 직접 잡으면 레이어 규칙이 잡는다")
    void layerDependency() {
        assertThat(violationsOf(LayerRules.LAYER_DEPENDENCY))
                .contains("LeakyOrderController");
    }

    @Test
    @DisplayName("도메인에 JPA 애너테이션을 붙이면 잡는다")
    void domainIsFrameworkFree() {
        assertThat(violationsOf(LayerRules.DOMAIN_IS_FRAMEWORK_FREE))
                .contains("FrameworkAwareOrder");
    }

    @Test
    @DisplayName("dataaccess 밖에서 jakarta.persistence 를 쓰면 잡는다")
    void persistenceStaysInDataaccess() {
        assertThat(violationsOf(LayerRules.PERSISTENCE_STAYS_IN_DATAACCESS))
                .contains("FrameworkAwareOrder")
                .doesNotContain("FixtureOrderJpaEntity");
    }

    @Test
    @DisplayName("레이어는 지켰지만 다른 서브도메인 내부를 잡으면 서브도메인 규칙이 잡는다")
    void subdomainBoundary() {
        assertThat(violationsOf(SubdomainRules.ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT))
                .contains("CrossSubdomainOrderService");
    }

    @Test
    @DisplayName("in 포트 이름이 UseCase 로 끝나지 않으면 잡는다")
    void inPortNaming() {
        assertThat(violationsOf(NamingRules.IN_PORTS_ARE_USECASE_INTERFACES))
                .contains("OrderFinder");
    }

    @Test
    @DisplayName("out 포트 이름이 Port 로 끝나지 않으면 잡는다")
    void outPortNaming() {
        assertThat(violationsOf(NamingRules.OUT_PORTS_ARE_PORT_INTERFACES))
                .contains("OrderStore");
    }

    @Test
    @DisplayName("컨트롤러가 소비자 폴더 밖에 있으면 잡는다")
    void controllerLocation() {
        assertThat(violationsOf(NamingRules.CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE))
                .contains("MisplacedOrderController")
                .doesNotContain("LeakyOrderController");
    }

    private static String violationsOf(ArchRule rule) {
        EvaluationResult result = rule.evaluate(FIXTURE);
        assertThat(result.hasViolation())
                .as("규칙이 위반을 잡아내야 한다: %s", rule.getDescription())
                .isTrue();
        return String.join("\n", result.getFailureReport().getDetails());
    }
}
