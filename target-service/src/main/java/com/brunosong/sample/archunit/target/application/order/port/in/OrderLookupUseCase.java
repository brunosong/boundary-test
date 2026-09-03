package com.brunosong.sample.archunit.target.application.order.port.in;

import com.brunosong.sample.archunit.target.application.order.dto.result.PlacedOrder;

import java.util.Optional;

public interface OrderLookupUseCase {

    Optional<PlacedOrder> findByOrderNo(String orderNo);
}
