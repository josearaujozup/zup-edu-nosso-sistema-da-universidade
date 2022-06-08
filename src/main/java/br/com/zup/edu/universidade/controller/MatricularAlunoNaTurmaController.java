package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.controller.request.AlunoAMatricularRequest;
import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.Turma;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.TurmaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
public class MatricularAlunoNaTurmaController {
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public MatricularAlunoNaTurmaController(AlunoRepository alunoRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    @PostMapping("/turmas/{id}/alunos")
    @Transactional
    public ResponseEntity<?> matricular(
            @PathVariable Long id,
            @RequestBody @Valid AlunoAMatricularRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Turma nao cadastrada"));

        Aluno aluno = request.paraAluno(alunoRepository);

        if (turma.isMatriculado(aluno)) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Aluno já matriculado na turma");
        }

        turma.adicionar(aluno);

        URI location = uriComponentsBuilder.path("/turmas/{id}/alunos/{idAluno}")
                .buildAndExpand(turma.getId(), aluno.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
