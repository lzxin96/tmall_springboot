package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台功能映射
 * @Author
 */
@RestController
public class ForeRestController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    /**
     * home()方法映射首页访问路径 "forehome"
     * 1. 查询所有分类
     * 2. 为这些分类填充产品集合
     * 3. 为这些分类填充推荐产品集合
     * 4. 移除产品里的分类信息，以免出现重复递归
     *
     * @return
     */
    @GetMapping("/forehome")
    public Object home() {
        List<Category> CategoryList = categoryService.list();
        productService.fill(CategoryList);
        productService.fillByRow(CategoryList);
        categoryService.removeCategoryFromProduct(CategoryList);
        return CategoryList;
    }

}
