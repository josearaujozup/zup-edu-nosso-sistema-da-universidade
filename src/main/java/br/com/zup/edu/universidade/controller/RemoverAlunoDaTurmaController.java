package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.controller.request.AlunoAMatricularRequest;
import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.Turma;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.TurmaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
public class RemoverAlunoDaTurmaController {
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public RemoverAlunoDaTurmaController(AlunoRepository alunoRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    @DeleteMapping("/turmas/{idTurma}/alunos/{idAluno}")
    @Transactional
    public ResponseEntity<?> matricular(
            @PathVariable Long idTurma,
            @PathVariable Long idAluno
    ) {
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Turma nao cadastrada"));

        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Aluno nao cadastrado"));

        if (!turma.isMatriculado(aluno)) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Não é posssivel desfazer uma matricula inexistente");
        }

        turma.remover(aluno);

        return ResponseEntity.noContent().build();
    }
}
