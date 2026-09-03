package com.brunosong.sample.archunit.target.web.order.admin;

import com.brunosong.sample.archunit.target.application.order.port.in.OrderLookupUseCase;
import com.brunosong.sample.archunit.target.web.order.admin.response.AdminOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 어드민 채널. 사용자 채널과 같은 유스케이스를 부르고 대상 선택과 응답 DTO 만 다르다.
 * 유스케이스를 채널로 쪼개지 않는다.
 */
@RestController
@RequestMapping("/api/v1/admin/orders")
public class OrderAdminApiController {

    private final OrderLookupUseCase orderLookupUseCase;

    public OrderAdminApiController(OrderLookupUseCase orderLookupUseCase) {
        this.orderLookupUseCase = orderLookupUseCase;
    }

    @GetMapping("/{orderNo}")
    public ResponseEntity<AdminOrderResponse> findOne(@PathVariable String orderNo) {
        return orderLookupUseCase.findByOrderNo(orderNo)
                .map(AdminOrderResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
