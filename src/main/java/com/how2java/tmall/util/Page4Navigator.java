package com.how2java.tmall.util;

import java.util.List;

import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * 分页处理
 *
 * @param <T>
 */
@Data
public class Page4Navigator<T> {
    // jpa传递出来的分页对象， Page4Navigator 类就是对它进行封装以达到扩展的效果
    private Page<T> pageFromJPA;
    // 分页的时候 ,如果总页数比较多，那么显示出来的分页超链一个有几个。
    // 比如如果分页出来的超链是这样的： [8,9,10,11,12], 那么 navigatePages 就是5
    private int navigatePages;
    // 总页面数
    private int totalPages;
    // 第几页（基0）
    private int number;
    // 总共有多少条数据
    private long totalElements;
    // 一页最多有多少条数据
    private int size;
    // 当前页有多少条数据 (与 size，不同的是，最后一页可能不满 size 个)
    private int numberOfElements;
    // 数据集合
    private List<T> content;

    // 是否有数据
    private boolean isHasContent;
    // 是否是首页
    private boolean first;
    // 是否是末页
    private boolean last;
    // 是否有下一页
    private boolean isHasNext;
    // 是否有上一页
    private boolean isHasPrevious;
    // 分页的时候 ,如果总页数比较多，那么显示出来的分页超链一个有几个。
    // 比如如果分页出来的超链是这样的： [8,9,10,11,12]，
    // 那么 navigatepageNums 就是这个数组：[8,9,10,11,12]，这样便于前端展示
    private int[] navigatepageNums;

    // 空构造函数
    public Page4Navigator() {
        //这个空的分页是为了 Redis 从 json格式转换为 Page4Navigator 对象而专门提供的
    }

    public Page4Navigator(Page<T> pageFromJPA, int navigatePages) {
        this.pageFromJPA = pageFromJPA;
        this.navigatePages = navigatePages;
        totalPages = pageFromJPA.getTotalPages();
        number = pageFromJPA.getNumber();
        totalElements = pageFromJPA.getTotalElements();
        size = pageFromJPA.getSize();
        numberOfElements = pageFromJPA.getNumberOfElements();
        content = pageFromJPA.getContent();
        isHasContent = pageFromJPA.hasContent();
        first = pageFromJPA.isFirst();
        last = pageFromJPA.isLast();
        isHasNext = pageFromJPA.hasNext();
        isHasPrevious = pageFromJPA.hasPrevious();
        calcNavigatepageNums();
    }

    private void calcNavigatepageNums() {
        int navigatepageNums[];// 存储导航栏数字数组
        int totalPages = getTotalPages();// 获取总页数
        int num = getNumber() + 1;// 获取当前页
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
        this.navigatepageNums = navigatepageNums;
    }
}