package com.brunosong.sample.modulith.order.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class OrderRepository {

    public void save() {
        log.info("save");
    }
}
