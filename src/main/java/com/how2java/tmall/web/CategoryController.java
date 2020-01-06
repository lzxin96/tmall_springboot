package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * 分类controller
 */
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 获取分类列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                         @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        Page4Navigator<Category> page = categoryService.list(start, size, 5);
        return page;
    }

    /**
     * 添加分类
     * @param category
     * @param image 接受前台传过来的文件
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/categories")
    public Object add(Category category, MultipartFile image, HttpServletRequest request) throws Exception {
        categoryService.add(category);
        categoryService.saveOrUpdateImageFile(category, image, request);
        return category;
    }

    /**
     * 删除分类
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
        categoryService.delete(id);
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, id+"jpg");
        file.delete();
        return null;
    }

    /**
     * 根据id查找
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception {
        return categoryService.get(id);
    }


    @PutMapping("/categories/{id}")
    public Object update(Category category, MultipartFile image, HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        category.setName(name);
        categoryService.update(category);
        // 检查是否上传图片
        if(image!=null) {
            categoryService.saveOrUpdateImageFile(category, image, request);
        }
        return category;
    }
}