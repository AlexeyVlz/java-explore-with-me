package explore_with_me.services.publicServices;

import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.compilation.Compilation;
import explore_with_me.models.compilation.CompilationDto;
import explore_with_me.models.compilation.CompilationMapper;
import explore_with_me.repositories.CompilationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCompilationService {

    private final CompilationRepository compilationRepository;

    @Autowired
    public PublicCompilationService(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from/size, size, Sort.by("title"));
        List<Compilation> compilations = compilationRepository.findAllWithPinned(pinned, pageRequest);
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new DataNotFound(
                String.format("Подборка с id = %d в базе не обнаружена", compId)));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
