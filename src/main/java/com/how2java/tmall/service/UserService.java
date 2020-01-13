package com.how2java.tmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.UserDAO;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.util.Page4Navigator;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * 分页查询
     * @param start
     * @param size
     * @param navigatePages
     * @return
     */
    public Page4Navigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = userDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    /**
     * 根据名字查找用户
     * @param name
     * @return
     */
    public User getByName(String name){
        return userDAO.findByName(name);
    }

    /**
     * 判断用户名是否存在
     * @param name
     * @return
     */
    public boolean isExist(String name){
        User user = getByName(name);
        return null != user;
    }

    /**
     * 添加用户
     * @param user
     */
    public void add(User user){
        userDAO.save(user);
    }

    /**
     * 获取用户名和账号
     * @param name
     * @param password
     * @return
     */
    public User get(String name, String password) {
        return userDAO.getByNameAndPassword(name,password);
    }
}
