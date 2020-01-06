package com.how2java.tmall.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.CategoryDAO;
import com.how2java.tmall.pojo.Category;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

/**
 * 分类service
 */
@Service
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    /**
     * 根据id进行排序
     *
     * @return
     */
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    /**
     * 分页查询列表
     *
     * @param start
     * @param size
     * @param navigatePages
     * @return
     */
    public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> pageFromJPA = categoryDAO.findAll(pageable);
        Page4Navigator<Category> categoryPage4Navigator = new Page4Navigator<>(pageFromJPA, navigatePages);
        return categoryPage4Navigator;
    }

    /**
     * 增加分类
     *
     * @param category
     */
    public void add(Category category) {
        categoryDAO.save(category);
    }

    /**
     * 保存、更新图片
     * 更换图片后缀为jpg
     *
     * @param category
     * @param image
     * @param request
     * @throws IOException
     */
    public void saveOrUpdateImageFile(Category category, MultipartFile image, HttpServletRequest request) throws IOException {
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));// 指定上传目录
        File file = new File(imageFolder, category.getId() + ".jpg");// 图片格式
        if (!file.getParentFile().exists()) { // 检查上传目录是否存在
            file.getParentFile().mkdirs();
        }
        // 保存文件
        image.transferTo(file);
        // 修改文件格式
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    /**
     * 删除图片
     *
     * @param id
     */
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    public Category get(int id) {
        Category c= categoryDAO.findOne(id);
        return c;
    }

    /**
     * 修改分类信息
     * @param category
     */
    public void update(Category category) {
        categoryDAO.save(category);
    }
}