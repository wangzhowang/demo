package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;                 // 商品编号（主键自增）

    private String barcode;          // 国际商品条码

    private String name;             // 商品名称

    private BigDecimal price;        // 商品单价（用BigDecimal避免精度丢失）

    private BigDecimal costPrice;    // 成本价（计算利润用）

    private Integer stock;           // 当前库存数量

    private Integer stockWarning;    // 库存预警值（低于此值提醒补货）

    private String imageUrl;         // 商品图片地址

    private Long categoryId;         // 分类ID

    private String unit;             // 计量单位（个/斤/箱/瓶...）

    private Integer status;          // 状态：1上架 0下架

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 修改时间
}

