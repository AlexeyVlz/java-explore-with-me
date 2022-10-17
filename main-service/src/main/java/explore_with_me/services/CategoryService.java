package explore_with_me.services;

import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.category.Category;
import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.category.CategoryMapper;
import explore_with_me.models.category.NewCategoryDto;
import explore_with_me.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto addNewCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = findCategory(categoryDto.getId());
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long catId) {
        findCategory(catId);
        //TODO написать проверку, что в категории нет ни одного события, иначе исключение.
        categoryRepository.deleteById(catId);
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(findCategory(catId));
    }

    public Category findOrCreateCategory(CategoryDto categoryDto) {
        return categoryRepository.findById(categoryDto.getId()).
                orElse(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new DataNotFound(
                String.format("Категория с id %d в базе данных не обнаржена", id)));
    }
}
