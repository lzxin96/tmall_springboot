package com.how2java.tmall.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;

/**
 * 订单详情DAO
 */
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {
    /**
     * 根据订单倒序查询
     * @param order
     * @return
     */
    List<OrderItem> findByOrderOrderByIdDesc(Order order);
}
