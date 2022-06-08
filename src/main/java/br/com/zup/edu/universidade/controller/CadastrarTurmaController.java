package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.controller.request.CriarTurmaRequest;
import br.com.zup.edu.universidade.model.Disciplina;
import br.com.zup.edu.universidade.model.Turma;
import br.com.zup.edu.universidade.repository.DisciplinaRepository;
import br.com.zup.edu.universidade.repository.ProfessorRepository;
import org.springframework.http.HttpStatus;
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

@RestController
public class CadastrarTurmaController {
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;

    public CadastrarTurmaController(DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
    }

    @PostMapping("/disciplinas/{id}/turmas")
    @Transactional
    private ResponseEntity<?> cadastrar(
            @PathVariable Long id,
            @RequestBody @Valid CriarTurmaRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina nao cadastrada"));

        Turma turma = request.paraTurma(disciplina, professorRepository);

        disciplina.adicionar(turma);

        disciplinaRepository.flush();

        URI localtion = uriComponentsBuilder.path("/disciplinas/{id}/turmas/{idTurma}")
                .buildAndExpand(disciplina.getId(), turma.getId())
                .toUri();

        return ResponseEntity.created(localtion).build();
    }


}
