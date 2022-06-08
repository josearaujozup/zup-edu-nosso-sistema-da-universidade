package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}
