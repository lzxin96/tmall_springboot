package com.how2java.tmall.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    /**
     * 首页导航栏分类列表
     * 一个分类下的多个产品
     */
    @Transient
    private List<Product> products;
    /**
     * 产品列表
     */
    @Transient
    private List<List<Product>> productsByRow;
}