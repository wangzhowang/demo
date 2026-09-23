package com.example.demo.controller.front;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.result.Result;
import com.example.demo.common.result.ResultCode;
import com.example.demo.dto.ProductQueryDTO;
import com.example.demo.dto.ProductVo;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 查询所有商品（不分页）
     */
    @GetMapping("/list")
    public Result<List<ProductVo>> getProductList() {
        List<ProductVo> list = productService.getProductList();
        return Result.success(list);
    }

    /**
     * 分页查询商品
     */
    @GetMapping("/page")
    public Result<IPage<ProductVo>> getProductPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        IPage<ProductVo> page = productService.getProductPage(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    /**
     * 查询商品详情
     */
    @GetMapping("/detail/{id}")
    public Result<ProductVo> getProductDetail(@PathVariable Long id) {
        ProductVo vo = productService.getProductDetail(id);
        if (vo == null) {
            return Result.fail(505, "商品信息不存在");
        }
        return Result.success(vo);
    }

    /*
    * 商品动态查询接口
    * */
    @PostMapping("/search")
    public Result<IPage<ProductVo>> searchProducts(@RequestBody ProductQueryDTO queryDTO) {
        // ⭐ 如果前端没传 pageNum 或 pageSize，给默认值
        if (queryDTO.getPageNum() == null) queryDTO.setPageNum(1);
        if (queryDTO.getPageSize() == null) queryDTO.setPageSize(10);

        IPage<ProductVo> page = productService.searchProducts(queryDTO);
        return Result.success(page);
    }
}