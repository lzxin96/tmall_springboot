package com.how2java.tmall.dao;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 产品dao
 */
public interface ProductDAO extends JpaRepository<Product, Integer> {
    /**
     * 分页查询
     * @param category
     * @param pageable
     * @return
     */
    Page<Product> findByCategory(Category category, Pageable pageable);

    /**
     * 新增一个通过分类查询所有产品的方法
     * @param category
     * @return
     */
    List<Product> findByCategoryOrderById(Category category);

    /**
     * 模糊查询产品名
     * @param keywork
     * @param pageable
     * @return
     */
    List<Product> findByNameLike(String keywork, Pageable pageable);
}
