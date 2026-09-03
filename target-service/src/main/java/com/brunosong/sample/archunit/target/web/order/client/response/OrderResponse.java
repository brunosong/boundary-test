package com.brunosong.sample.archunit.target.web.order.client.response;

import com.brunosong.sample.archunit.target.application.order.dto.result.PlacedOrder;

public record OrderResponse(String orderNo, long totalPrice, String status) {

    public static OrderResponse from(PlacedOrder placed) {
        return new OrderResponse(placed.orderNo(), placed.totalPrice(), placed.status());
    }
}
