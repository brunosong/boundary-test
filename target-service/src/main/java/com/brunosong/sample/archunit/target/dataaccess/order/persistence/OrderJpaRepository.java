package com.brunosong.sample.archunit.target.dataaccess.order.persistence;

import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * JpaRepository 를 상속하지 않는다. 상속하면 deleteAll, findAll 같은 표면이 딸려 온다.
 * 이 리포지토리가 쓰는 것만 직접 선언한다.
 */
public interface OrderJpaRepository extends Repository<OrderJpaEntity, Long> {

    OrderJpaEntity save(OrderJpaEntity entity);

    Optional<OrderJpaEntity> findByOrderNo(String orderNo);
}
