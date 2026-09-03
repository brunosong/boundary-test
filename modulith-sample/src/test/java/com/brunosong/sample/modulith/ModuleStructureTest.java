package com.brunosong.sample.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Spring Modulith 의 경계 검증. ArchUnit 과 달리 규칙을 직접 쓰지 않는다.
 * 패키지 구조가 곧 규칙이고, verify() 가 그것을 검사한다.
 *
 * 지금은 base 패키지 아래에 모듈이 하나도 없어서 검사할 것이 없다.
 * 대상이 0개면 verify() 는 그냥 통과한다. ArchUnit 에서 본 것과 같은 성질이라,
 * 모듈을 만들기 전까지 이 테스트가 초록인 것은 아무것도 보장하지 않는다.
 * 그래서 구조를 출력하는 테스트를 같이 둔다. 몇 개가 잡혔는지 눈으로 보라는 뜻이다.
 */
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
