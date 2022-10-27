package explore.with.me.services.adminServices;

import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.category.Category;
import explore.with.me.models.category.CategoryMapper;
import explore.with.me.models.event.Event;
import explore.with.me.repositories.CategoryRepository;
import explore.with.me.exeption.ConflictDataException;
import explore.with.me.models.category.CategoryDto;
import explore.with.me.models.category.NewCategoryDto;
import explore.with.me.services.publicServices.PublicEventService;
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
        if (events.size() > 0) {
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
