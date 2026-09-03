package com.brunosong.sample.archunit.target.application.order.dto.result;

public record PlacedOrder(String orderNo, String sku, int quantity, long totalPrice, String status) {
}
