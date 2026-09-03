package com.brunosong.sample.archunit.target.application.order.port.out;

import com.brunosong.sample.archunit.target.domain.order.entity.Order;

import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findByOrderNo(String orderNo);
}
