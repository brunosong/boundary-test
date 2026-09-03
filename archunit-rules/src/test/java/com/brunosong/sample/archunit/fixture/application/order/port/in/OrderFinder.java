package com.brunosong.sample.archunit.fixture.application.order.port.in;

/** 일부러 어긴 이름. in 포트인데 UseCase 로 끝나지 않는다. */
public interface OrderFinder {

    String find(String orderNo);
}
