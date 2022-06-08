package br.com.zup.edu.universidade.controller.request;

import br.com.zup.edu.universidade.model.Disciplina;
import br.com.zup.edu.universidade.model.Professor;
import br.com.zup.edu.universidade.model.Turma;
import br.com.zup.edu.universidade.repository.ProfessorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import static org.springframework.http.HttpStatus.*;

public class CriarTurmaRequest {
    @NotNull
    @Positive
    private Long idProfessor;

    @NotNull
    @Future
    private LocalDate dataInicio;

    @NotNull
    @Future
    private LocalDate dataFim;

    public CriarTurmaRequest(Long idProfessor, LocalDate dataInicio, LocalDate dataFim) {
        this.idProfessor = idProfessor;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public CriarTurmaRequest() {
    }

    public Turma paraTurma(Disciplina disciplina, ProfessorRepository professorRepository){
        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new ResponseStatusException(UNPROCESSABLE_ENTITY, "Professor não cadastrado"));

        return new Turma(disciplina,dataInicio,dataFim,professor);
    }

    public Long getIdProfessor() {
        return idProfessor;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
}
