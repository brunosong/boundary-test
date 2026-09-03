package com.brunosong.sample.archunit.fixture.web.order;

import org.springframework.web.bind.annotation.RestController;

/**
 * 일부러 어긴 위치. 소비자 폴더(admin, client) 밖에 있는 컨트롤러다.
 * NamingRules.CONTROLLERS_LIVE_IN_CONSUMER_PACKAGE 가 잡는다.
 */
@RestController
public class MisplacedOrderController {
}
