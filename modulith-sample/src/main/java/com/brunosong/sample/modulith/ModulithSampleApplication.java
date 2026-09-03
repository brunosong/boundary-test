package com.brunosong.sample.modulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 부트 진입점이자 모듈 경계의 기준점이다.
 *
 * Spring Modulith 는 이 클래스가 있는 패키지를 base 로 삼고,
 * 그 바로 아래 패키지 하나하나를 애플리케이션 모듈로 본다.
 *
 *   com.brunosong.sample.modulith          <- base (이 클래스가 있는 곳)
 *   com.brunosong.sample.modulith.order    <- 모듈
 *   com.brunosong.sample.modulith.product  <- 모듈
 *
 * 모듈의 base 패키지에 있는 타입만 공개 API 다. 그 아래 하위 패키지는 전부 internal 이라
 * 다른 모듈이 잡으면 위반이 된다. 이 관례가 ArchUnit 쪽과 가장 크게 다른 점이다.
 */
@SpringBootApplication
public class ModulithSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModulithSampleApplication.class, args);
    }
}
