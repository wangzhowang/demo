package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo {
    private Long id;              // 商品编号

    private String barcode;       // 商品条码

    private String name;          // 商品名称

    private BigDecimal price;     // 商品单价

    private String imageUrl;      // 商品图片地址

    private String categoryName;  // ⭐ 分类名称（从分类表查出来的，不是分类ID）

    private String unit;          // 计量单位

    private Integer stock;        // 库存（展示用）

    private Integer status;       // 状态：1上架 0下架
}
