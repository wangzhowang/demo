package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.ProductQueryDTO;
import com.example.demo.dto.ProductVo;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final CategoryService categoryService;

    public ProductServiceImpl(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    // ========== 方法1：查询所有商品（小数据量场景） ==========
    @Override
    public List<ProductVo> getProductList() {
        // 1. 查询所有商品
        List<Product> productList = this.list();
        // 2. 转换成 VO
        return convertToVoList(productList);
    }

    // ========== 方法2：分页查询商品（推荐） ==========
    @Override
    public IPage<ProductVo> getProductPage(Integer pageNum, Integer pageSize, String keyword) {
        // 1. 构建分页对象
        Page<Product> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword)
                    .or()
                    .like(Product::getBarcode, keyword);
        }
        wrapper.orderByDesc(Product::getUpdateTime);

        // 3. 执行分页查询
        IPage<Product> productPage = this.page(page, wrapper);

        // 4. 转换为 VO 分页对象
        IPage<ProductVo> voPage = new Page<>(pageNum, pageSize, productPage.getTotal());
        voPage.setRecords(convertToVoList(productPage.getRecords()));

        return voPage;
    }

    // ========== 方法3：根据ID查询商品详情 ==========
    @Override
    public ProductVo getProductDetail(Long id) {
        Product product = this.getById(id);
        if (product == null) {
            return null;
        }
        // 单个商品转换
        return convertToVo(product);
    }

    // ========== 方法4：动态查询方法 ==========
    @Override
    public IPage<ProductVo> searchProducts(ProductQueryDTO queryDTO) {
        Page<Product> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（模糊匹配名称或条码）
        if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Product::getName, queryDTO.getKeyword())
                    .or()
                    .like(Product::getBarcode, queryDTO.getKeyword())
            );
        }

        // 分类ID（精确匹配）
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, queryDTO.getCategoryId());
        }

        wrapper.orderByDesc(Product::getUpdateTime);

        IPage<Product> productPage = this.page(page, wrapper);

        // 转换成 VO（复用之前的转换方法）
        IPage<ProductVo> voPage = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize(), productPage.getTotal());
        voPage.setRecords(productPage.getRecords().stream()
                .map(this::convertToVo)
                .collect(Collectors.toList()));

        return voPage;
    }

    // ========== 私有方法：批量转换成 VO ==========
    private List<ProductVo> convertToVoList(List<Product> productList) {
        if (productList == null || productList.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. 收集所有分类 ID
        List<Long> categoryIds = productList.stream()
                .map(Product::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 2. 批量查询分类（一次性从数据库查出所有分类）
        Map<Long, String> categoryMap = null;
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryService.listByIds(categoryIds);
            categoryMap = categories.stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));
        }

        // 3. 组装 VO
        Map<Long, String> finalCategoryMap = categoryMap;
        return productList.stream()
                .map(product -> convertToVo(product, finalCategoryMap))
                .collect(Collectors.toList());
    }

    // ========== 私有方法：单个转换（带分类 Map） ==========
    private ProductVo convertToVo(Product product, Map<Long, String> categoryMap) {
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);

        // 填充分类名称
        if (product.getCategoryId() != null && categoryMap != null) {
            vo.setCategoryName(categoryMap.get(product.getCategoryId()));
        }
        return vo;
    }

    // ========== 私有方法：单个转换（重载，兼容直接调用） ==========
    private ProductVo convertToVo(Product product) {
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);

        // 单个查询时，单独查分类名称
        if (product.getCategoryId() != null) {
            Category category = categoryService.getById(product.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        return vo;
    }
}

