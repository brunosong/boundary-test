package com.brunosong.sample.archunit.target.application.product.port.in;

import java.util.Optional;

public interface ProductLookupUseCase {

    Optional<ProductSnapshot> findSellable(String sku);
}
