package com.brunosong.sample.archunit.fixture.web.order.client;

import com.brunosong.sample.archunit.fixture.dataaccess.order.persistence.FixtureOrderJpaEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * 일부러 어긴 코드. 컨트롤러가 application 을 건너뛰고 영속 엔티티를 직접 잡았다.
 * LayerRules.LAYER_DEPENDENCY 가 잡는다.
 */
@RestController
public class LeakyOrderController {

    public Long idOf(FixtureOrderJpaEntity entity) {
        return entity.getId();
    }
}
