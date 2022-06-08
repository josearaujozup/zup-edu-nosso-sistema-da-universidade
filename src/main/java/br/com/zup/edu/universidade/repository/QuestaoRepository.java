package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {
}
