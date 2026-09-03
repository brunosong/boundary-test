package com.brunosong.sample.archunit.target.dataaccess.product.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sample_product")
public class ProductJpaEntity {

    @Id
    @Column(name = "sku")
    private String sku;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private boolean sellable;

    protected ProductJpaEntity() {
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public boolean isSellable() {
        return sellable;
    }
}
