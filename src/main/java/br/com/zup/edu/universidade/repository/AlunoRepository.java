package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
