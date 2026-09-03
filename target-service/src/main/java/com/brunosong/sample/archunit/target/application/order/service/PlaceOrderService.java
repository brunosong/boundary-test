package com.brunosong.sample.archunit.target.application.order.service;

import com.brunosong.sample.archunit.target.application.order.dto.command.PlaceOrderCommand;
import com.brunosong.sample.archunit.target.application.order.dto.result.PlacedOrder;
import com.brunosong.sample.archunit.target.application.order.port.in.PlaceOrderUseCase;
import com.brunosong.sample.archunit.target.application.order.port.out.OrderRepositoryPort;
import com.brunosong.sample.archunit.target.application.product.port.in.ProductLookupUseCase;
import com.brunosong.sample.archunit.target.application.product.port.in.ProductSnapshot;
import com.brunosong.sample.archunit.target.domain.order.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * 다른 서브도메인(product)은 in 포트로만 부른다.
     * ProductRepositoryPort 나 Product 엔티티를 직접 잡으면 SubdomainRules 가 잡는다.
     */
    private final ProductLookupUseCase productLookupUseCase;

    public PlaceOrderService(OrderRepositoryPort orderRepositoryPort, ProductLookupUseCase productLookupUseCase) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.productLookupUseCase = productLookupUseCase;
    }

    @Override
    public PlacedOrder place(PlaceOrderCommand command) {
        ProductSnapshot product = productLookupUseCase.findSellable(command.sku())
                .orElseThrow(() -> new IllegalArgumentException("판매 중인 상품이 아니다: " + command.sku()));

        Order order = Order.place(newOrderNo(), product.sku(), command.quantity(), product.price());
        Order saved = orderRepositoryPort.save(order);

        return new PlacedOrder(saved.orderNo(), saved.sku(), saved.quantity(), saved.totalPrice(),
                saved.status().name());
    }

    private static String newOrderNo() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
