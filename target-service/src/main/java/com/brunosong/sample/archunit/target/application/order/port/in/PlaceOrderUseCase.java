package com.brunosong.sample.archunit.target.application.order.port.in;

import com.brunosong.sample.archunit.target.application.order.dto.command.PlaceOrderCommand;
import com.brunosong.sample.archunit.target.application.order.dto.result.PlacedOrder;

public interface PlaceOrderUseCase {

    PlacedOrder place(PlaceOrderCommand command);
}
