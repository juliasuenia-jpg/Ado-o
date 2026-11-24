package sistema.adocao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.adocao.model.Animal;
import sistema.adocao.model.Solicitacao;
import sistema.adocao.model.StatusSolicitacao;
import sistema.adocao.model.Usuario;

import java.util.Collection;
import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findAllByOrderByCriadoEmDesc();
    List<Solicitacao> findByStatusOrderByCriadoEmDesc(StatusSolicitacao status);
    List<Solicitacao> findByAdotanteOrderByCriadoEmDesc(Usuario adotante);
    List<Solicitacao> findByAnimalAndIdNot(Animal animal, Long id);
    boolean existsByAnimalAndAdotanteAndStatusIn(Animal animal, Usuario adotante, Collection<StatusSolicitacao> status);
    boolean existsByAnimal(Animal animal);
    long countByStatus(StatusSolicitacao status);
}
