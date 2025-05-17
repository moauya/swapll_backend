package com.swapll.gradu.service;

import com.swapll.gradu.model.Category;
import com.swapll.gradu.model.dto.CategoryDTO;
import com.swapll.gradu.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByTitle(categoryName);
    }



    public List<CategoryDTO> getCategories() {
        return  categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getTitle()))
                .collect(Collectors.toList());
    };

    public CategoryDTO getCategoryById(int id) {
        Category category=categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("there is no category like this"));
        CategoryDTO dto=new CategoryDTO(category.getId(),category.getTitle());

        return dto;
    }


}
