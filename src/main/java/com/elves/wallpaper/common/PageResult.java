package com.elves.wallpaper.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;         //  总记录数
    private Integer pageNum;    //  当前页码
    private Integer pageSize;   //  每页条数
    private List<T> records;    //  当前页的数据列表

    // 静态转换方法：直接把 PageHelper 的 PageInfo 转换成我们的 PageResult
    public static <T> PageResult<T> of(com.github.pagehelper.PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setRecords(pageInfo.getList());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        return result;
    }
}
