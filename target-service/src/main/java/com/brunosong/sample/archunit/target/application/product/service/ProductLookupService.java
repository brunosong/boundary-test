package com.brunosong.sample.archunit.target.application.product.service;

import com.brunosong.sample.archunit.target.application.product.port.in.ProductLookupUseCase;
import com.brunosong.sample.archunit.target.application.product.port.in.ProductSnapshot;
import com.brunosong.sample.archunit.target.application.product.port.out.ProductRepositoryPort;
import com.brunosong.sample.archunit.target.domain.product.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductLookupService implements ProductLookupUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductLookupService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Optional<ProductSnapshot> findSellable(String sku) {
        return productRepositoryPort.findBySku(sku)
                .filter(Product::isSellable)
                .map(product -> new ProductSnapshot(product.sku(), product.name(), product.price()));
    }
}
