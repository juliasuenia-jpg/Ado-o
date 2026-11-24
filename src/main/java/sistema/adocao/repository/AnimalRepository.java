package sistema.adocao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sistema.adocao.model.Animal;
import sistema.adocao.model.StatusAnimal;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {
    List<Animal> findTop6ByStatusOrderByCriadoEmDesc(StatusAnimal status);
    long countByStatus(StatusAnimal status);
    List<Animal> findAllByOrderByCriadoEmDesc();
}
