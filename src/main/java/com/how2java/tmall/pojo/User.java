package com.how2java.tmall.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String password;
    private String name;
    /*加密盐*/
    private String salt;
    /*匿名*/
    @Transient
    private String anonymousName;

    /**
     * anonymousName没有和数据库关联，用于获取匿名
     * 其实就是前后保留，中间换成星星，如果长度只有2或者1
     * @return
     */
    public String getAnonymousName() {
        if (null != anonymousName) {
            return anonymousName;
        }
        if (null == name) {
            anonymousName = null;
        } else if (name.length() <= 1) {
            anonymousName = "*";
        } else if (name.length() == 2) {
            anonymousName = name.substring(0, 1) + "*";
        } else {
            char[] cs = name.toCharArray();
            for (int i = 1; i < cs.length - 1; i++) {
                cs[i] = '*';
            }
            anonymousName = new String(cs);
        }
        return anonymousName;
    }

}