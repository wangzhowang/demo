package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("category")
public class Category {
    @TableId(type= IdType.AUTO)
    private Long id;              // 分类编号

    private String name;          // 分类名称

    private Long parentId;        // 父分类ID（0表示顶级分类）

    private Integer sort;         // 排序号（越小越靠前）

    private Integer status;       // 状态：1启用 0禁用

    private String icon;          // 分类图标

    private LocalDateTime createTime;   //创建时间

    private LocalDateTime updateTime;      //修改时间


}
