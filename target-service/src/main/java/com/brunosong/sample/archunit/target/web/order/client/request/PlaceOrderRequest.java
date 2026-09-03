package com.brunosong.sample.archunit.target.web.order.client.request;

/** 사용자 채널 전용 요청 DTO. 어드민 채널과 공유하지 않는다. */
public record PlaceOrderRequest(String sku, int quantity) {
}
