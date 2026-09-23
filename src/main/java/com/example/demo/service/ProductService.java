package com.example.demo.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.ProductQueryDTO;
import com.example.demo.dto.ProductVo;
import com.example.demo.entity.Product;


import java.util.List;

public interface ProductService extends IService<Product> {
    /**
     * 查询所有商品（返回 VO）—— 适合下拉框等小数据量场景
     */
    List<ProductVo> getProductList();

    /**
     * 分页查询商品（返回 VO）—— 适合表格展示，推荐！
     */
    IPage<ProductVo> getProductPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 根据ID查询商品详情（返回 VO）
     */
    ProductVo getProductDetail(Long id);

    // ⭐ 新增搜索方法
    IPage<ProductVo> searchProducts(ProductQueryDTO queryDTO);
}
