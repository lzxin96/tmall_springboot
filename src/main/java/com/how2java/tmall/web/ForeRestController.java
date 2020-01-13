package com.how2java.tmall.web;

import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.how2java.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台功能映射
 *
 * @Author xin
 */
@RestController
public class ForeRestController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReviewService reviewService;

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

    /**
     * 注册
     * HtmlUtils.htmlEscape(name);把账号里的特殊符号进行转义
     *
     * @param user
     * @return
     */
    @PostMapping("/foreregister")
    public Object register(@RequestBody User user) {
        String name = HtmlUtils.htmlEscape(user.getName());
        user.setName(name);
        boolean exist = userService.isExist(name);
        if (exist) {
            // ture
            return Result.fail("用户名已被使用，不能使用。");
        }
        userService.add(user);
        return Result.success();
    }

    /**
     * 登录
     *
     * @param userParam
     * @param session
     * @return
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name = HtmlUtils.htmlEscape(userParam.getName());
        User user = userService.get(name, userParam.getPassword());
        if (null == user) {
            return Result.fail("账号密码错误");
        } else {
            session.setAttribute("user", user);
            return Result.success();
        }
    }

    /**
     * 产品详情页
     *
     * @param pid
     * @return
     */
    @GetMapping("/foreproduct/{pid}")
    public Object product(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);
        // 设置产品图片
        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        // 设置产品所有属性值
        List<PropertyValue> propertyValueList = propertyValueService.list(product);
        // 设置评论
        List<Review> reviewList = reviewService.list(product);
        // 获取销售数量、评论数
        productService.setSaleAndReviewNumber(product);
        // 获取产品缩略图（第一张）
        productImageService.setFirstProdutImage(product);

        // 返回Map
        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", propertyValueList);
        map.put("reviews", reviewList);
        return Result.success(map);
    }

    /**
     * 模态登录
     *
     * @param session
     * @return
     */
    @GetMapping("forecheckLogin")
    public Object checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null != user){
            return Result.success();
        }
        return Result.fail("未登录");
    }
}
