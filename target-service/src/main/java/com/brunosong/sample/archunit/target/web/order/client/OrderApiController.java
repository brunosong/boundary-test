package com.brunosong.sample.archunit.target.web.order.client;

import com.brunosong.sample.archunit.target.application.order.dto.command.PlaceOrderCommand;
import com.brunosong.sample.archunit.target.application.order.port.in.PlaceOrderUseCase;
import com.brunosong.sample.archunit.target.web.order.client.request.PlaceOrderRequest;
import com.brunosong.sample.archunit.target.web.order.client.response.OrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 사용자 채널. 컨트롤러는 in 포트만 부르고 dataaccess 를 모른다. */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderApiController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    public OrderResponse place(@RequestBody PlaceOrderRequest request) {
        return OrderResponse.from(placeOrderUseCase.place(new PlaceOrderCommand(request.sku(), request.quantity())));
    }
}
