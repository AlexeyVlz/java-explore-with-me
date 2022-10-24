package explore_with_me.repositories;

import explore_with_me.models.compilation.Compilation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, JpaSpecificationExecutor<Compilation> {

    default List<Compilation> findAllWithPinned(Boolean pinned, PageRequest pageable){
        return findAll(specWithPinned(pinned, pageable));
    }

    default Specification<Compilation> specWithPinned(Boolean pinned, PageRequest pageable){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pinned"), pinned));
    }
}
