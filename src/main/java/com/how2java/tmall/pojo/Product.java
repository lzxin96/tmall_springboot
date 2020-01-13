package com.how2java.tmall.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 产品类
 */
@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;
    private String name;
    private String subTitle;
    private float originalPrice;
    private float promotePrice;
    private int stock;
    private Date createDate;


    @Transient // 注释没有映射到数据库的字段
    private ProductImage firstProductImage;
    /**
     * 单个产品图片集合
     */
    @Transient
    private List<ProductImage> productSingleImages;
    /**
     * 详情产品图片集合
     */
    @Transient
    private List<ProductImage> productDetailImages;
    /**
     * 销量
     */
    @Transient
    private int saleCount;
    /**
     * 累计评价
     */
    @Transient
    private int reviewCount;
}