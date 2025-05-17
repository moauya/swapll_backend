package com.swapll.gradu.controller;

import com.swapll.gradu.model.Category;
import com.swapll.gradu.model.dto.CategoryDTO;
import com.swapll.gradu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {

        return categoryService.getCategories();
    }

    public CategoryDTO getCategoryById(int id){
        return categoryService.getCategoryById(id);
    }


}
