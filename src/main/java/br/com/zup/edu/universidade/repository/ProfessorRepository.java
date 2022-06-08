package br.com.zup.edu.universidade.repository;

import br.com.zup.edu.universidade.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
