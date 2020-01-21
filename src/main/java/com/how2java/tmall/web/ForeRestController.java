package com.how2java.tmall.web;

import com.how2java.tmall.comparator.*;
import com.how2java.tmall.dao.OrderItemDAO;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.how2java.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

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
    @Autowired
    private OrderItemService orderItemService;

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
        if (null != user) {
            return Result.success();
        }
        return Result.fail("未登录");
    }

    @GetMapping("forecategory/{cid}")
    public Object category(@PathVariable("cid") int cid, String sort) {
        Category category = categoryService.get(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());
        categoryService.removeCategoryFromProduct(category);

        if (null != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new ProductDateComparator());
                    break;

                case "saleCount":
                    Collections.sort(category.getProducts(), new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(category.getProducts(), new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(category.getProducts(), new ProductAllComparator());
                    break;
            }
        }
        return category;
    }

    /**
     * 产品名模糊查询
     *
     * @param keyword
     * @return
     */
    @PostMapping("foresearch")
    public Object search(String keyword) {
        if (null == keyword) {
            keyword = "";
        }
        List<Product> products = productService.search(keyword, 0, 20);
        productImageService.setFirstProdutImages(products);
        productService.setSaleAndReviewNumber(products);
        return products;
    }

    /**
     * 购买
     *
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @GetMapping("forebuyone")
    public Object buyone(int pid, int num, HttpSession session) {
        return buyoneAndAddCart(pid, num, session);
    }

    /**
     * 立即购买
     * 进入购物车并购买
     *
     * @param pid
     * @param num
     * @param session
     * @return
     */
    private int buyoneAndAddCart(int pid, int num, HttpSession session) {
        Product product = productService.get(pid);
        int oiid = 0;
        User user = (User) session.getAttribute("user");
        // 标记是否存在
        boolean found = false;
        List<OrderItem> ois = orderItemService.listByUser(user);
        for (OrderItem oi : ois) {
            // 判断购物车里产品id是否一致
            if (oi.getProduct().getId() == product.getId()) {
                // 修改数量
                oi.setNumber(oi.getNumber() + num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }
        // 不存在创建新子项
        if (!found) {
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setProduct(product);
            oi.setNumber(num);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return oiid;
    }

    /**
     * 添加到购物车
     *
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @GetMapping("foreaddCart")
    public Object addCart(int pid, int num, HttpSession session) {
        buyoneAndAddCart(pid, num, session);
        return Result.success();
    }

    /**
     * 购买、结算
     *
     * @param oiid
     * @param session
     * @return
     */
    @GetMapping("forebuy")
    public Object buy(String[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi = orderItemService.get(id);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            orderItems.add(oi);
        }

        productImageService.setFirstProdutImagesOnOrderItems(orderItems);
        session.setAttribute("ois", orderItems);
        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);
        return Result.success(map);
    }

    /**
     * 购物车列表
     *
     * @param session
     * @return
     */
    @GetMapping("forecart")
    public Object cart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user);
        productImageService.setFirstProdutImagesOnOrderItems(ois);
        return ois;
    }

    /**
     * 购物车修改商品数量
     * @param session
     * @param pid
     * @param num
     * @return
     */
    @GetMapping("forechangeOrderItem")
    public Object changeOrderItem(HttpSession session, int pid, int num) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        List<OrderItem> ois = orderItemService.listByUser(user);
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == pid) {
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
        return Result.success();
    }

    /**
     * 删除商品
     * @param session
     * @param oiid
     * @return
     */
    @GetMapping("foredeleteOrderItem")
    public Object deleteOrderItem(HttpSession session, int oiid) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        orderItemService.delete(oiid);
        return Result.success();
    }
}
