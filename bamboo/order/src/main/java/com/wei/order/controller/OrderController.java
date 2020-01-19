package com.wei.order.controller;

import com.wei.common.base.model.ResponseResult;
import com.wei.order.model.Order;
import com.wei.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/info")
    public void orderInfo() {
        log.info("==== call order ====");
        orderService.getOrderInfo();
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseResult<Order> getInfo(@RequestBody Order order, BindingResult bindingResult) {

        return ResponseResult.success();
    }

}
