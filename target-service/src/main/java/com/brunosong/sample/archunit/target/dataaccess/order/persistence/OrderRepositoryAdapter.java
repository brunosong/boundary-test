package com.brunosong.sample.archunit.target.dataaccess.order.persistence;

import com.brunosong.sample.archunit.target.application.order.port.out.OrderRepositoryPort;
import com.brunosong.sample.archunit.target.domain.order.entity.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity saved = orderJpaRepository.save(new OrderJpaEntity(
                order.orderNo(), order.sku(), order.quantity(), order.unitPrice(), order.status()));
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        return orderJpaRepository.findByOrderNo(orderNo).map(OrderRepositoryAdapter::toDomain);
    }

    private static Order toDomain(OrderJpaEntity entity) {
        return Order.restore(entity.getOrderNo(), entity.getSku(), entity.getQuantity(),
                entity.getUnitPrice(), entity.getStatus());
    }
}
