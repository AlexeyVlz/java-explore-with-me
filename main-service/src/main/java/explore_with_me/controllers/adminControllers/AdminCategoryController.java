package explore_with_me.controllers.adminControllers;

import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.category.NewCategoryDto;
import explore_with_me.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/categories")
@Validated
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto addNewCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Получен запрос к эндпоинту: POST: /admin/categories; newCategoryDto = " + newCategoryDto);
        return categoryService.addNewCategory(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Получен запрос к эндпоинту: PATCH: /admin/categories; categoryDto = " + categoryDto);

        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Получен запрос к эндпоинту: DELETE: /admin/categories/{catId}; catId = " + catId);
        categoryService.deleteCategory(catId);
    }
}
