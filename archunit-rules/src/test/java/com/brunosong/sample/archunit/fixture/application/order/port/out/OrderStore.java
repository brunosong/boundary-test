package com.brunosong.sample.archunit.fixture.application.order.port.out;

/** 일부러 어긴 이름. out 포트인데 Port 로 끝나지 않는다. */
public interface OrderStore {

    void save(String orderNo);
}
