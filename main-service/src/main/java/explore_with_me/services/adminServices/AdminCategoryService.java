package explore_with_me.services.adminServices;

import explore_with_me.exeption.ConflictDataException;
import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.category.Category;
import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.category.CategoryMapper;
import explore_with_me.models.category.NewCategoryDto;
import explore_with_me.models.event.Event;
import explore_with_me.repositories.CategoryRepository;
import explore_with_me.services.publicServices.PublicEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final PublicEventService publicEventService;

    @Autowired
    public AdminCategoryService(CategoryRepository categoryRepository, PublicEventService publicEventService) {
        this.categoryRepository = categoryRepository;
        this.publicEventService = publicEventService;
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
        List<Event> events = publicEventService.getEventsByCategoryId(catId);
        if(events.size() < 1) {
            throw new ConflictDataException(String.format("Категория, в которой есть события не может быть удалена. " +
                    "В категории с id = %d есть собыия со следующими id: %s", catId, events));
        }
        categoryRepository.deleteById(catId);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new DataNotFound(
                String.format("Категория с id %d в базе данных не обнаржена", id)));
    }
}
