package com.brunosong.sample.archunit.fixture.domain.product.entity;

/** 규칙을 어기지 않는 클래스. 아래 CrossSubdomainOrderService 가 이것을 직접 잡는 것이 위반이다. */
public class FixtureProduct {

    public long price() {
        return 0L;
    }
}
