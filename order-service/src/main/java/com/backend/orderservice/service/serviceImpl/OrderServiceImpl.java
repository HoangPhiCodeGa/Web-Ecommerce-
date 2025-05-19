package com.backend.orderservice.service.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.orderservice.domain.Order;
import com.backend.orderservice.domain.OrderDetail;
import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.OrderDetailDTO;
import com.backend.orderservice.dtos.response.OrderResponse;
import com.backend.orderservice.enums.OrderStatus;
//import com.backend.orderservice.event.OrderProducer;
import com.backend.orderservice.repository.OrderDetailRepository;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.OrderDetailService;
import com.backend.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository productRep;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final OrderDetailRepository orderDetailRepository;
//    private final OrderProducer orderProducer;

//    public OrderServiceImpl(OrderRepository productRep, ModelMapper modelMapper, OrderProducer orderProducer) {
//        this.productRep = productRep;
//        this.modelMapper = modelMapper;
//        this.orderProducer = orderProducer;
//    }

    public OrderServiceImpl(OrderRepository productRep, ModelMapper modelMapper,
                            OrderRepository orderRepository, OrderDetailService orderDetailService,
                            OrderDetailRepository orderDetailRepository) {
        this.productRep = productRep;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.orderDetailRepository = orderDetailRepository;
    }

    //    Convert Entity to DTO
    public Order convertToEntity(OrderDTO product) {
        return modelMapper.map(product, Order.class);
    }

    //    Convert DTO to Entity
    public OrderResponse convertToDTO(Order product) {
        return modelMapper.map(product, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAll() {
        return productRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public OrderResponse getById(Long id) {
        OrderResponse orderResponse = productRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));

        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();

        if(productRep.findById(id).isPresent() && productRep.findById(id).get().getOrderDetails() != null){
            for (var it: productRep.findById(id).get().getOrderDetails()){
                OrderDetailDTO orderDetailDTOTmp = OrderDetailDTO
                        .builder()
                        .orderId(it.getOrder().getId())
                        .giaBan(it.getGiaBan())
                        .giaGoc(it.getGiaGoc())
                        .productId(it.getProductId())
                        .soLuong(it.getSoLuong())
                        .build();

                orderDetailDTOS.add(orderDetailDTOTmp);
            }
        }

        orderResponse.setOrderDetailDTOS(orderDetailDTOS);
        return orderResponse;
    }

    @Transactional
    @Override
    public OrderResponse save(OrderDTO product) {
        Order order = convertToEntity(product);
        if (order.getNgayDatHang() == null) {
            order.setNgayDatHang(LocalDate.now());
        }
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.DANG_XU_LY);
        }
        Order savedOrder = productRep.save(order);

        Long idOrder = savedOrder.getId();
        Order orderTmp = orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Order"));

            List<OrderDetailDTO> orderDetailDTOS = product.getOrderDetailDTOS();
            for (OrderDetailDTO orderDetailDTO: orderDetailDTOS){
                orderDetailDTO.setOrderId(orderTmp.getId());
                OrderDetail orderDetail = OrderDetail.builder()
                        .productId(orderDetailDTO.getProductId())
                        .order(orderTmp)
                        .giaBan(orderDetailDTO.getGiaBan())
                        .giaGoc(orderDetailDTO.getGiaGoc())
                        .soLuong(orderDetailDTO.getSoLuong())
                        .build();
                orderDetailRepository.save(orderDetail);
            }

        
        // Gửi sự kiện đơn hàng mới đến Kafka
//        orderProducer.sendOrderEvent(
//                savedOrder.getId(),
//                savedOrder.getCustomerId(),
//                savedOrder.getStatus(),
//                savedOrder.getTongTien());
                
        return convertToDTO(savedOrder);
    }

    @Transactional
    @Override
    public OrderResponse update(Long id, OrderDTO product) {
        Order existingOrder = productRep.findById(id).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        
        // Lưu trạng thái cũ để kiểm tra xem có thay đổi không
        OrderStatus oldStatus = existingOrder.getStatus();
        
        Order orderToUpdate = convertToEntity(product);
        orderToUpdate.setId(id);
        Order updatedOrder = productRep.save(orderToUpdate);
        
        // Nếu trạng thái đơn hàng thay đổi, gửi sự kiện đến Kafka
//        if (updatedOrder.getStatus() != oldStatus) {
//            orderProducer.sendOrderEvent(
//                    updatedOrder.getId(),
//                    updatedOrder.getCustomerId(),
//                    updatedOrder.getStatus(),
//                    updatedOrder.getTongTien());
//        }
        
        return convertToDTO(updatedOrder);
    }

    @Transactional
    @Override
    public OrderResponse updateStatus(Long id, String status) {
        Order existingOrder = productRep.findById(id).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));

        // Lưu trạng thái cũ để kiểm tra xem có thay đổi không
        OrderStatus oldStatus = existingOrder.getStatus();

        OrderStatus orderStatus = OrderStatus.fromVietnameseLabel(status);
        existingOrder.setStatus(orderStatus);

        orderRepository.save(existingOrder);

        return convertToDTO(existingOrder);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        productRep.deleteById(id);
        return true;
    }

    @Override
    public Map<Integer, Double> staticIncome(int year) {
        List<Order> orders = orderRepository.findIncome(year);

        Map<Integer, Double> incomeByMonth = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            incomeByMonth.put(i, 0.0);
        }

        // Cộng dồn tổng tiền theo tháng
        for (Order order : orders) {
            if (order.getNgayDatHang() != null && order.getTongTien() != null) {
                int month = order.getNgayDatHang().getMonthValue(); // 1-12
                incomeByMonth.put(month, incomeByMonth.get(month) + order.getTongTien());
            }
        }
        return incomeByMonth;
//        return Map.of();
    }

    @Override
    public Map<Integer, Long> staticOrder(int year) {

        List<Order> orders = orderRepository.findIncome(year);

        Map<Integer, Long> orderByMonth = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            orderByMonth.put(i, 0L);
        }

        // Cộng dồn tổng tiền theo tháng
        for (Order order : orders) {
            if (order.getNgayDatHang() != null && order.getOrderDetails() != null) {
                int month = order.getNgayDatHang().getMonthValue();
                orderByMonth.put(month, orderByMonth.get(month) + order.getOrderDetails().size());
            }
        }
        return orderByMonth;

    }
}
