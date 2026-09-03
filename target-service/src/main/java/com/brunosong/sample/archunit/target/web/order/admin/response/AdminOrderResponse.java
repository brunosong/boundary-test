package com.brunosong.sample.archunit.target.web.order.admin.response;

import com.brunosong.sample.archunit.target.application.order.dto.result.PlacedOrder;

/** 어드민 전용 응답. 단가처럼 사용자 응답에 없는 값이 들어가므로 DTO 를 공유하지 않는다. */
public record AdminOrderResponse(String orderNo, String sku, int quantity, long totalPrice, String status) {

    public static AdminOrderResponse from(PlacedOrder placed) {
        return new AdminOrderResponse(placed.orderNo(), placed.sku(), placed.quantity(),
                placed.totalPrice(), placed.status());
    }
}
