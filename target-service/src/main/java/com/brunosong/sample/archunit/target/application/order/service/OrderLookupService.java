package com.brunosong.sample.archunit.target.application.order.service;

import com.brunosong.sample.archunit.target.application.order.dto.result.PlacedOrder;
import com.brunosong.sample.archunit.target.application.order.port.in.OrderLookupUseCase;
import com.brunosong.sample.archunit.target.application.order.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderLookupService implements OrderLookupUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public OrderLookupService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Optional<PlacedOrder> findByOrderNo(String orderNo) {
        return orderRepositoryPort.findByOrderNo(orderNo)
                .map(order -> new PlacedOrder(order.orderNo(), order.sku(), order.quantity(),
                        order.totalPrice(), order.status().name()));
    }
}
