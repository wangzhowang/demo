package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.ProductVo;
import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category>
{
    /**
     * 删除分类（带关联校验）
     * @param id 分类ID
     * @return true: 删除成功
     * @throws RuntimeException 如果有子分类或关联商品
     */
    boolean deleteCategoryWithCheck(Long id);
}
