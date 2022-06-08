package br.com.zup.edu.universidade.controller.request;

import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.*;

public class AlunoAMatricularRequest {
    @NotNull
    @Positive
    private Long idAluno;

    public AlunoAMatricularRequest(Long idAluno) {
        this.idAluno = idAluno;
    }

    public AlunoAMatricularRequest() {
    }

    public Aluno paraAluno(AlunoRepository repository) {
        return repository.findById(idAluno)
                .orElseThrow(() -> new ResponseStatusException(UNPROCESSABLE_ENTITY, "Aluno nao cadastrado"));
    }

    public Long getIdAluno() {
        return idAluno;
    }
}
