package com.brunosong.sample.archunit.fixture.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * 일부러 어긴 코드. 도메인 엔티티에 JPA 를 붙였다.
 * LayerRules.DOMAIN_IS_FRAMEWORK_FREE 와 PERSISTENCE_STAYS_IN_DATAACCESS 가 잡는다.
 */
@Entity
public class FrameworkAwareOrder {

    @Id
    private Long id;
}
