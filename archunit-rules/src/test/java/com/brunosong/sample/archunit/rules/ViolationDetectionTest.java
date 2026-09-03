package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
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
    void 컨트롤러가_영속_엔티티를_직접_잡으면_레이어_규칙이_잡는다() {
        assertThat(위반내역(LayerRules.LAYER_DEPENDENCY))
                .contains("LeakyOrderController");
    }

    @Test
    void 도메인에_JPA_애너테이션을_붙이면_잡는다() {
        assertThat(위반내역(LayerRules.DOMAIN_IS_FRAMEWORK_FREE))
                .contains("FrameworkAwareOrder");
    }

    @Test
    void dataaccess_밖에서_영속_애너테이션을_쓰면_잡는다() {
        assertThat(위반내역(LayerRules.PERSISTENCE_STAYS_IN_DATAACCESS))
                .contains("FrameworkAwareOrder")
                .doesNotContain("FixtureOrderJpaEntity");
    }

    @Test
    void 레이어는_지켰지만_다른_서브도메인_내부를_잡으면_잡는다() {
        assertThat(위반내역(SubdomainRules.ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT))
                .contains("CrossSubdomainOrderService");
    }

    @Test
    void in_포트_이름이_UseCase_로_끝나지_않으면_잡는다() {
        assertThat(위반내역(NamingRules.IN_PORTS_ARE_USECASE_INTERFACES))
                .contains("OrderFinder");
    }

    @Test
    void out_포트_이름이_Port_로_끝나지_않으면_잡는다() {
        assertThat(위반내역(NamingRules.OUT_PORTS_ARE_PORT_INTERFACES))
                .contains("OrderStore");
    }

    @Test
    void 컨트롤러가_소비자_폴더_밖에_있으면_잡는다() {
        assertThat(위반내역(NamingRules.CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE))
                .contains("MisplacedOrderController")
                .doesNotContain("LeakyOrderController");
    }

    private static String 위반내역(ArchRule rule) {
        EvaluationResult result = rule.evaluate(FIXTURE);
        assertThat(result.hasViolation())
                .as("규칙이 위반을 잡아내야 한다: %s", rule.getDescription())
                .isTrue();
        return String.join("\n", result.getFailureReport().getDetails());
    }
}
