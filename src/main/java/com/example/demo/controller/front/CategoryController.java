package com.example.demo.controller.front;

import com.example.demo.common.result.Result;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apitest")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
    * 获取所有分组
    * */
    @GetMapping("/getlist")
    public Result<List<Category>> getcategorylist() {
        List<Category> categorylist = categoryService.list();
        return Result.success(categorylist);
    }

    /*
    * 添加分组
    * */
    @PostMapping("addcategory")
    public Result<Category> addcategory(@RequestBody Category category) {
        boolean test = categoryService.save(category);
        if (test) {
            return Result.success();
        } else {
            return Result.fail(501, "添加分组失败");
        }
    }


    /*
     *修改商品分组
     *  */
    @PostMapping("/updacategory")
    public Result<Category> updacategory(@RequestBody Category category) {
        if (category.getId() == null) {
            return Result.fail(402, "分类id不能为空");
        }
        boolean test = categoryService.updateById(category);
        if (test) {
            return Result.success();
        } else {
            return Result.fail(501, "修改分组失败，请重试");
        }
    }

    /**
     * 删除分类（带级联校验）
     * @param id 分类ID
     * @return true: 删除成功，false: 删除失败
     * @throws RuntimeException 如果分类有子分类或关联商品，抛出异常
     */
    @DeleteMapping("/delete/{id}")
    public  Result<String> listCategory(@PathVariable Long id) {
        try {
            boolean success = categoryService.deleteCategoryWithCheck(id);
            if (success) {
                return Result.success("删除成功");
            }
            return Result.fail(501, "删除失败");
        } catch (RuntimeException e) {
            // 捕获业务异常，返回友好提示
            return Result.fail(400, e.getMessage());
        }
    }
}

