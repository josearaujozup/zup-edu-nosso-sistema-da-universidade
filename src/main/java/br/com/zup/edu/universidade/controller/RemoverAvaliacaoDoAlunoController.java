package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.RespostaAvaliacao;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.RespostaAvaliacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
public class RemoverAvaliacaoDoAlunoController {
    private final RespostaAvaliacaoRepository respostaAvaliacaoRepository;
    private final AlunoRepository alunoRepository;

    public RemoverAvaliacaoDoAlunoController(RespostaAvaliacaoRepository respostaAvaliacaoRepository, AlunoRepository alunoRepository) {
        this.respostaAvaliacaoRepository = respostaAvaliacaoRepository;
        this.alunoRepository = alunoRepository;
    }

    @DeleteMapping("/aluno/{idAluno}/respostas/{idResposta}")
    @Transactional
    public ResponseEntity<?> remover(
            @PathVariable Long idAluno,
            @PathVariable Long idResposta
    ) {
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "aluno nao cadastrado"));

        RespostaAvaliacao resposta = respostaAvaliacaoRepository.findById(idResposta)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Resposta da avaliacao nao existente"));

        if(!resposta.pertence(aluno)){
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY,"Esta reposta de avaliacao não pertence a este aluno");
        }

        respostaAvaliacaoRepository.delete(resposta);

        return ResponseEntity.noContent().build();
    }
}