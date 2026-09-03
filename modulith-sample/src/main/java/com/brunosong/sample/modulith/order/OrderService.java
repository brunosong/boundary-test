package com.brunosong.sample.modulith.order;

import com.brunosong.sample.modulith.order.internal.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void order() {
        orderRepository.save();
    }
}
