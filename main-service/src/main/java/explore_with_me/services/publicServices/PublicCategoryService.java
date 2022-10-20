package explore_with_me.services.publicServices;

import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.category.Category;
import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.category.CategoryMapper;
import explore_with_me.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public PublicCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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