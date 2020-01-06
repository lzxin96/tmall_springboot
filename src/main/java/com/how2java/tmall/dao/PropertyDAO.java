package com.how2java.tmall.dao;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 属性dao
 */
public interface PropertyDAO extends JpaRepository<Property, Integer> {
    /**
     * 根据分类进行查询
     *
     * @param category
     * @param pageable
     * @return
     */
    Page<Property> findByCategory(Category category, Pageable pageable);

    /**
     * 通过分类查找所有属性集合
     * @param category
     * @return
     */
    List<Property> findByCategory(Category category);
}