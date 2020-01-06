package com.how2java.tmall.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.how2java.tmall.service.OrderService;
import lombok.Data;

@Entity
@Table(name = "order_")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String orderCode;
    private String address;
    private String post;
    private String receiver;
    private String mobile;
    private String userMessage;
    private Date createDate;
    private Date payDate;
    private Date deliveryDate;
    private Date confirmDate;
    private String status;
    /**
     * 该订单对应的用户
     */
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
    /**
     * 该订单下的订单项列表
     */
    @Transient
    private List<OrderItem> orderItems;
    /**
     * 该订单的总计金额
     */
    @Transient
    private float total;
    /**
     * 该订单的总计数量
     */
    @Transient
    private int totalNumber;
    /**
     * 用于把英文表达的Status信息转换为中文
     */
    @Transient
    private String statusDesc;

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusDesc() {
        if (null != statusDesc){
            return statusDesc;
        }
        String desc = "未知";
        switch (status) {
            case OrderService.waitPay:
                desc = "待付";
                break;
            case OrderService.waitDelivery:
                desc = "待发";
                break;
            case OrderService.waitConfirm:
                desc = "待收";
                break;
            case OrderService.waitReview:
                desc = "等评";
                break;
            case OrderService.finish:
                desc = "完成";
                break;
            case OrderService.delete:
                desc = "刪除";
                break;
            default:
                desc = "未知";
        }
        statusDesc = desc;
        return statusDesc;
    }


}
