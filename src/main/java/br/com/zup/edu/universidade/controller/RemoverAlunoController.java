package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class RemoverAlunoController {
    private final AlunoRepository repository;

    public RemoverAlunoController(AlunoRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/alunos/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "aluno nao cadastrado"));

        repository.delete(aluno);

        return ResponseEntity.noContent().build();
    }
}
