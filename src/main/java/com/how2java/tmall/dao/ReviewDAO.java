package com.how2java.tmall.dao;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 评论DAO
 * @Author
 */
public interface ReviewDAO extends JpaRepository<Review, Integer> {
    /**
     * 返回产品对应的评论集合
     * @param product
     * @return
     */
    List<Review> findByProductOrderByIdDesc(Product product);

    /**
     * 返回产品对应评论数
     * @param product
     * @return
     */
    int countByProduct(Product product);
}
