package com.backend.orderservice.controllers;

import com.backend.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:9090",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

@RestController
@RequestMapping("/api/v1/static")
@Tag(name = "Order Query", description = "Order API")
public class StaticOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/static-venue")
    public Map<Integer, Double> staticVenue(@RequestParam("year") Integer year){
        return orderService.staticIncome(year);
    }

    @GetMapping("/static-order")
    public Map<Integer, Long> staticOrder(@RequestParam("year") Integer year){
        return orderService.staticOrder(year);
    }
}
