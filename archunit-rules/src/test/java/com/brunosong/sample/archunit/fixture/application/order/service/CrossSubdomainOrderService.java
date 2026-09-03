package com.brunosong.sample.archunit.fixture.application.order.service;

import com.brunosong.sample.archunit.fixture.domain.product.entity.FixtureProduct;

/**
 * 일부러 어긴 코드. order 가 product 의 도메인 엔티티를 직접 잡았다.
 * 레이어(application -> domain)는 지켰으므로 LAYER_DEPENDENCY 로는 안 잡히고,
 * SubdomainRules.ORDER_TOUCHES_PRODUCT_ONLY_THROUGH_IN_PORT 가 잡는다.
 */
public class CrossSubdomainOrderService {

    public long priceOf(FixtureProduct product) {
        return product.price();
    }
}
