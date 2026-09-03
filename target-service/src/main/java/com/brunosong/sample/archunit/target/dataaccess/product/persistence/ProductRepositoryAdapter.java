package com.brunosong.sample.archunit.target.dataaccess.product.persistence;

import com.brunosong.sample.archunit.target.application.product.port.out.ProductRepositoryPort;
import com.brunosong.sample.archunit.target.domain.product.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return productJpaRepository.findBySku(sku)
                .map(entity -> new Product(entity.getSku(), entity.getName(), entity.getPrice(), entity.isSellable()));
    }
}
