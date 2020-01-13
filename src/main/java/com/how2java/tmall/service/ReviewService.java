package com.how2java.tmall.service;

import com.how2java.tmall.dao.ReviewDAO;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论Service
 * @Author
 */
@Service
public class ReviewService {
    @Autowired
    private ReviewDAO reviewDAO;

    /**
     * 添加评论
     * @param review
     */
    public void add(Review review){
        reviewDAO.save(review);
    }

    /**
     * 根据产品获取评论
     * @param product
     * @return
     */
    public List<Review> list(Product product){
        List<Review> result = reviewDAO.findByProductOrderByIdDesc(product);
        return result;
    }

    /**
     * 获取产品评论数
     * @param product
     * @return
     */
    public int getCount(Product product){
        return reviewDAO.countByProduct(product);
    }
}
