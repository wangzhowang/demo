package com.example.demo.dto;

import lombok.Data;

/**
 * 商品查询参数 DTO
 */
@Data
public class ProductQueryDTO {
    private String keyword;      // JSON 传 "keyword"
    private Long categoryId;     // JSON 传 "categoryId"
    private Integer pageNum = 1;
    private Integer pageSize = 10;


}
