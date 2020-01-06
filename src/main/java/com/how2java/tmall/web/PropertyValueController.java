package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性值Controller
 * @Author
 */
@RestController
public class PropertyValueController {
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ProductService productService;

    /**
     * 属性值列表
     * @param pid
     * @return
     * @throws Exception
     */
    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid) throws Exception {
        Product product = productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> propertyValues = propertyValueService.list(product);
        return propertyValues;
    }

    @PutMapping("/propertyValues")
    public Object update(@RequestBody PropertyValue propertyValue) throws Exception{
        propertyValueService.update(propertyValue);
        return propertyValue;
    }
}
