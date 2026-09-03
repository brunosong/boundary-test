package com.brunosong.sample.archunit.fixture.dataaccess.order.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** 규칙을 지킨 클래스. dataaccess 안이므로 JPA 애너테이션이 허용된다. */
@Entity
public class FixtureOrderJpaEntity {

    @Id
    private Long id;

    public Long getId() {
        return id;
    }
}
