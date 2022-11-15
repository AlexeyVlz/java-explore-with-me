package explore.with.me.services.publicServices;

import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.category.Category;
import explore.with.me.models.category.CategoryMapper;
import explore.with.me.repositories.CategoryRepository;
import explore.with.me.models.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryDtoById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new DataNotFound(
                String.format("Категория с id %d в базе данных не обнаржена", catId)));
        return CategoryMapper.toCategoryDto(category);
    }

    public Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new DataNotFound(
                String.format("Категория с id %d в базе данных не обнаржена", catId)));
    }
}
