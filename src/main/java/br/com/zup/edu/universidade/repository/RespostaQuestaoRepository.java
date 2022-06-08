package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.RespostaAvaliacao;
import br.com.zup.edu.universidade.model.RespostaQuestao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaQuestaoRepository extends JpaRepository<RespostaQuestao, Long> {

}
