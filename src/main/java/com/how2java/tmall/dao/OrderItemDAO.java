package com.how2java.tmall.dao;

import java.util.List;

import com.how2java.tmall.pojo.Product;
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

    /**
     * 根据产品获取订单子项
     * @param product
     * @return
     */
    List<OrderItem> findByProduct(Product product);
}
