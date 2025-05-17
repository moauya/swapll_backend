package com.swapll.gradu;

import com.swapll.gradu.model.Category;
import com.swapll.gradu.repository.CategoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GraduApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraduApplication.class, args);
	}

	@Bean
	public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
		return args -> {
			if (categoryRepository.count() == 0) {
				List<String> defaultCategories = List.of(
						"Tutoring", "Programming", "Design", "Writing", "Translation",
						"Gardening", "Repairs", "Pet Care", "Music", "Cooking",
						"Fitness", "Cleaning", "Photography", "Crafts", "Language Learning",
						"Business Help", "Legal Help", "Marketing", "IT Support", "Other"
				);

				defaultCategories.forEach(title -> {
					Category category = new Category();
					category.setTitle(title);
					categoryRepository.save(category);
				});

				System.out.println("  categories inserted.");
			}
		};
	}
}
