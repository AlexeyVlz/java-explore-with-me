package explore.with.me.controllers.adminControllers;

import explore.with.me.models.category.CategoryDto;
import explore.with.me.models.category.NewCategoryDto;
import explore.with.me.services.adminServices.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/categories")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public CategoryDto addNewCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Получен запрос к эндпоинту: POST: /admin/categories; newCategoryDto = " + newCategoryDto);
        return adminCategoryService.addNewCategory(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Получен запрос к эндпоинту: PATCH: /admin/categories; category = " + categoryDto);

        return adminCategoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Получен запрос к эндпоинту: DELETE: /admin/categories/{catId}; catId = " + catId);
        adminCategoryService.deleteCategory(catId);
    }
}
