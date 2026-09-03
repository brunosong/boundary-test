package com.brunosong.sample.archunit.target;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 부트 진입점. 어떤 레이어에도 속하지 않는 얇은 합성 루트라
 * 여기서는 컴포넌트 스캔 외에 아무것도 하지 않는다.
 */
@SpringBootApplication
public class TargetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TargetServiceApplication.class, args);
    }
}
