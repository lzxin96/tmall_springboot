package com.how2java.tmall.service;

import com.how2java.tmall.dao.PropertyValueDAO;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.pojo.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 属性值Service
 * @Author
 */
@Service
public class PropertyValueService {
    @Autowired
    private PropertyValueDAO propertyValueDAO;
    @Autowired
    private PropertyService propertyService;
    /**
     * 更新
     * @param propertyValue
     */
    public void update(PropertyValue propertyValue){
        propertyValueDAO.save(propertyValue);
    }

    /**
     * 1 对于PropertyValue的管理，没有增加，只有修改。 所以需要通过初始化来进行自动地增加，以便于后面的修改。
     * 2 首先根据产品获取分类，然后获取这个分类下的所有属性集合
     * 3 然后用属性id和产品id去查询，看看这个属性和这个产品，是否已经存在属性值了。
     * 4 如果不存在，那么就创建一个属性值，并设置其属性和产品，接着插入到数据库中。
     * @param product
     */
    public void init(Product product){
        List<Property> properties = propertyService.listByCategory(product.getCategory());
        for (Property property : properties){
            PropertyValue propertyValue = getByPropertyAndProduct(product, property);
            if (null == propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueDAO.getByPropertyAndProduct(property,product);
    }

    public List<PropertyValue> list(Product product) {
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }
}
