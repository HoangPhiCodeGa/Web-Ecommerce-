package com.backend.orderservice.repository;

import com.backend.orderservice.domain.Order;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
@Hidden
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select ord from Order ord where year (ord.ngayDatHang) = :year")
    List<Order> findIncome(@Param("year") Integer year);
}
