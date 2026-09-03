package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 서브도메인 경계 규칙.
 *
 * 레이어 경계는 메이븐 모듈이 컴파일 타임에 막아 주지만, 한 모듈 안에 있는 서브도메인끼리는
 * 아무거나 import 해도 빌드가 통과한다. 그 경계를 지키게 하는 것이 여기의 몫이다.
 *
 * 정책은 대칭이 아니다. order 는 product 를 in 포트로만 부르고, product 는 order 를 아예 모른다.
 */
public final class SubdomainRules {

    private SubdomainRules() {
    }

    private static final DescribedPredicate<JavaClass> PRODUCT_INTERNALS =
            resideInAPackage("..product..")
                    .and(not(resideInAPackage("..product.port.in..")))
                    .as("product 서브도메인 내부(in 포트 패키지 제외)");

    public static final ArchRule ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT = noClasses()
            .that().resideInAPackage("..order..")
            .should().dependOnClassesThat(PRODUCT_INTERNALS)
            .because("다른 서브도메인은 in 포트라는 공개 계약으로만 부른다. "
                    + "나중에 서비스를 떼어낼 때 갈라지는 선이 그 계약이다");

    public static final ArchRule PRODUCT_DOES_NOT_KNOW_ORDER = noClasses()
            .that().resideInAPackage("..product..")
            .should().dependOnClassesThat().resideInAPackage("..order..")
            .because("의존 방향을 한쪽으로 고정해야 순환이 생기지 않는다");
}
