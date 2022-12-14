package explore.with.me.controllers.publicControllers;

import explore.with.me.models.category.CategoryDto;
import explore.with.me.services.publicServices.PublicCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam (name = "size", defaultValue = "10") @Positive Integer size) {
        return publicCategoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Positive Long catId) {
        return publicCategoryService.getCategoryDtoById(catId);
    }
}
