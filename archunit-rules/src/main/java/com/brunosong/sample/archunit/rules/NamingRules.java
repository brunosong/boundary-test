package com.brunosong.sample.archunit.rules;

import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * 이름과 위치 규칙. 사람이 리뷰에서 잡아 주던 것을 빌드가 잡게 한다.
 */
public final class NamingRules {

    private NamingRules() {
    }

    /** in 포트 인터페이스는 UseCase 로 끝난다. 포트 패키지의 record(공개 계약 DTO)는 대상이 아니다. */
    public static final ArchRule IN_PORTS_ARE_USECASE_INTERFACES = classes()
            .that().resideInAPackage("..application..port.in..")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("UseCase")
            .because("in 포트는 상호작용 하나를 뜻한다. 이름이 그것을 드러내야 한다");

    /** out 포트 인터페이스는 Port 로 끝난다. */
    public static final ArchRule OUT_PORTS_ARE_PORT_INTERFACES = classes()
            .that().resideInAPackage("..application..port.out..")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("Port")
            .because("구현이 어디에 있든 application 이 보는 것은 포트다");

    /** 컨트롤러는 소비자별 패키지(admin, client) 아래에만 둔다. */
    public static final ArchRule CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE = classes()
            .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .should().resideInAnyPackage("..web..admin..", "..web..client..")
            .because("한 채널을 걷어낼 때 그 폴더만 지우면 되게 한다");
}
