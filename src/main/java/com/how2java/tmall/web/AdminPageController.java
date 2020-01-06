package com.how2java.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 地址跳转controller
 * 提供 RESTFUL 服务器
 * 每个方法的返回值都会直接转换为 json 数据格式
 */
@Controller
public class AdminPageController {
    /**
     * 分类页面跳转
     *
     * @return
     */
    @GetMapping(value = "/admin")
    public String admin() {
        return "redirect:admin_category_list";
    }

    /**
     * 分页列表跳转
     *
     * @return
     */
    @GetMapping(value = "/admin_category_list")
    public String listCategory() {
        return "admin/listCategory";
    }

    /**
     * 编辑页面跳转
     *
     * @return
     */
    @GetMapping(value = "/admin_category_edit")
    public String editCategory() {
        return "admin/editCategory";
    }

    @GetMapping(value = "/admin_order_list")
    public String listOrder() {
        return "admin/listOrder";
    }

    @GetMapping(value = "/admin_product_list")
    public String listProduct() {
        return "admin/listProduct";
    }

    @GetMapping(value = "/admin_product_edit")
    public String editProduct() {
        return "admin/editProduct";
    }

    @GetMapping(value = "/admin_productImage_list")
    public String listProductImage() {
        return "admin/listProductImage";
    }

    @GetMapping(value = "/admin_property_list")
    public String listProperty() {
        return "admin/listProperty";
    }

    @GetMapping(value = "/admin_property_edit")
    public String editProperty() {
        return "admin/editProperty";
    }

    @GetMapping(value = "/admin_propertyValue_edit")
    public String editPropertyValue() {
        return "admin/editPropertyValue";
    }

    @GetMapping(value = "/admin_user_list")
    public String listUser() {
        return "admin/listUser";
    }
}