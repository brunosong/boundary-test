package com.brunosong.sample.archunit.target.dataaccess.order.persistence;

import com.brunosong.sample.archunit.target.domain.order.valueobject.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 영속 엔티티. domain 의 Order 와 다른 물건이다.
 * JPA 애너테이션은 이 레이어 밖으로 나가지 않는다(LayerRules.PERSISTENCE_STAYS_IN_DATAACCESS).
 */
@Entity
@Table(name = "sample_order")
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true)
    private String orderNo;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private long unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    protected OrderJpaEntity() {
    }

    public OrderJpaEntity(String orderNo, String sku, int quantity, long unitPrice, OrderStatus status) {
        this.orderNo = orderNo;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
