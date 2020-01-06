package com.how2java.tmall.service;

import com.how2java.tmall.dao.OrderDAO;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单service
 */
@Service
public class OrderService {
    /*状态常量*/
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    @Autowired
    private OrderDAO orderDAO;

    /**
     * 分页
     * @param start
     * @param size
     * @param navigatePage
     * @return
     */
    public Page4Navigator<Order> list(int start, int size, int navigatePage){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Order> page = orderDAO.findAll(pageable);
        return new Page4Navigator<>(page, navigatePage);
    }

    public void removeOrderFromOrderItem(List<Order> orders){
        for (Order order : orders){
            removeOrderFromOrderItem(order);
        }
    }

    private void removeOrderFromOrderItem(Order order){
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems){
            orderItem.setOrder(null);
        }
    }

    public Order get(int oid){
        return orderDAO.findOne(oid);
    }

    public void update(Order bean){
        orderDAO.save(bean);
    }
}
