package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao,Long> {
}
