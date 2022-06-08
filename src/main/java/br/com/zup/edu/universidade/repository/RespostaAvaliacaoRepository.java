package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.RespostaAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaAvaliacaoRepository extends JpaRepository<RespostaAvaliacao, Long> {

}
