package com.how2java.tmall.service;

import com.how2java.tmall.dao.OrderItemDAO;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单详情Service
 * @Author
 */
@Service
public class OrderItemService {
    @Autowired
    private OrderItemDAO orderItemDAO;
    @Autowired
    private ProductImageService productImageService;

    public void fill(List<Order> orders){
        for (Order order : orders){
            fill(order);
        }
    }
     public void fill(Order order){
         List<OrderItem> orderItems = listByOrder(order);
         // 总价
         float total = 0;
         // 总数量
         int totalNumber = 0;
         for (OrderItem oi : orderItems){
             total += oi.getNumber() * oi.getProduct().getPromotePrice();
             totalNumber += oi.getNumber();
             productImageService.setFirstProdutImage(oi.getProduct());
         }
         order.setTotal(total);
         order.setOrderItems(orderItems);
         order.setTotalNumber(totalNumber);
     }

    /**
     * 根据订单查找订单子项
     * @param order
     * @return
     */
     public List<OrderItem> listByOrder(Order order){
        return orderItemDAO.findByOrderOrderByIdDesc(order);
     }

    /**
     * 根据产品查找订单子项
     * @param product
     * @return
     */
     public List<OrderItem> listByProduct(Product product){
         return orderItemDAO.findByProduct(product);
     }

    /**
     * 产品销量
     * @param product
     * @return
     */
     public int getSaleCount(Product product){
         List<OrderItem> orderItems = listByProduct(product);
         int sum = 0;
         for (OrderItem orderItem : orderItems){
            if (null != orderItem.getOrder() && null != orderItem.getOrder().getPayDate()){
                sum += orderItem.getNumber();
            }
         }
         return sum;
     }
}
