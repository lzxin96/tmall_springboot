package com.how2java.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.PropertyDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.util.Page4Navigator;

/**
 * 属性service
 */
@Service
public class PropertyService {

    @Autowired
    private PropertyDAO propertyDAO;
    @Autowired
    private CategoryService categoryService;

    public void add(Property bean) {
        propertyDAO.save(bean);
    }

    public void delete(int id) {
        propertyDAO.delete(id);
    }

    public Property get(int id) {
        return propertyDAO.findOne(id);
    }

    public void update(Property bean) {
        propertyDAO.save(bean);
    }

    public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages) {
        // 根据id查找分类
        Category category = categoryService.get(cid);
        // 设置分页条件
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        // 查询属性
        Page<Property> pageFromJPA = propertyDAO.findByCategory(category, pageable);
        // 分页包装
        return new Page4Navigator<>(pageFromJPA, navigatePages);

    }

    /**
     * 通过分类获取所有属性集合的方法
     * @param category
     * @return
     */
    public List<Property> listByCategory(Category category){
        return propertyDAO.findByCategory(category);
    }

}