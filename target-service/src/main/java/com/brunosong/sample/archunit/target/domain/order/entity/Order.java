package com.brunosong.sample.archunit.target.domain.order.entity;

import com.brunosong.sample.archunit.target.domain.order.valueobject.OrderStatus;

/**
 * 주문 애그리거트 루트. 프레임워크를 모른다.
 * JPA 애너테이션도 스프링 애너테이션도 여기 붙지 않는다(LayerRules 가 막는다).
 */
public class Order {

    private final String orderNo;
    private final String sku;
    private final int quantity;
    private final long unitPrice;
    private OrderStatus status;

    private Order(String orderNo, String sku, int quantity, long unitPrice, OrderStatus status) {
        this.orderNo = orderNo;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = status;
    }

    public static Order place(String orderNo, String sku, int quantity, long unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1 이상이어야 한다: " + quantity);
        }
        return new Order(orderNo, sku, quantity, unitPrice, OrderStatus.PLACED);
    }

    /** 저장소에서 꺼낸 상태를 그대로 복원한다. 불변식 검사는 place 에서만 한다. */
    public static Order restore(String orderNo, String sku, int quantity, long unitPrice, OrderStatus status) {
        return new Order(orderNo, sku, quantity, unitPrice, status);
    }

    public void cancel() {
        if (status != OrderStatus.PLACED) {
            throw new IllegalStateException("접수 상태의 주문만 취소할 수 있다: " + status);
        }
        status = OrderStatus.CANCELLED;
    }

    public long totalPrice() {
        return unitPrice * quantity;
    }

    public String orderNo() {
        return orderNo;
    }

    public String sku() {
        return sku;
    }

    public int quantity() {
        return quantity;
    }

    public long unitPrice() {
        return unitPrice;
    }

    public OrderStatus status() {
        return status;
    }
}
