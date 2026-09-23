package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ProductService productService;

    public CategoryServiceImpl(@Lazy ProductService productService) {
        this.productService = productService;
    }


    /**
     * 删除分类（带级联校验）
     *
     * @param id 分类ID
     * @return true: 删除成功，false: 删除失败
     * @throws RuntimeException 如果分类有子分类或关联商品，抛出异常
     */
    @Override
    public boolean deleteCategoryWithCheck(Long id) {
        // 1. 检查是否存在子分类
        LambdaQueryWrapper<Category> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(Category::getParentId, id);
        long childCount = this.count(childWrapper);

        if (childCount > 0) {
            throw new RuntimeException("该分类下还有子分类，请先删除子分类");
        }

        // 2. 检查分类下是否有商品
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(Product::getCategoryId, id);
        long productCount = productService.count(productWrapper);

        if (productCount > 0) {
            throw new RuntimeException("该分类下还有商品，请先移除或删除商品");
        }

        // 3. 执行删除
        return this.removeById(id);
    }
}
