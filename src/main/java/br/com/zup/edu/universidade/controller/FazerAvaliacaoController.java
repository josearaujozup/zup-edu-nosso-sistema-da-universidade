package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.controller.request.AvaliacaoAlunoRequest;
import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.Avaliacao;
import br.com.zup.edu.universidade.model.RespostaAvaliacao;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.AvaliacaoRepository;
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

import static org.springframework.http.HttpStatus.*;

@RestController
public class FazerAvaliacaoController {
    private final AvaliacaoRepository avaliacaoRepository;
    private final AlunoRepository alunoRepository;


    public FazerAvaliacaoController(AvaliacaoRepository avaliacaoRepository, AlunoRepository alunoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.alunoRepository = alunoRepository;
    }

    @PostMapping("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas")
    @Transactional
    public ResponseEntity<?> avaliar(
            @PathVariable Long id,
            @PathVariable Long idAvaliacao,
            @RequestBody @Valid AvaliacaoAlunoRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ){
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "aluno nao cadastrado"));

        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Avaliacao não cadastrada"));

        RespostaAvaliacao respostaAvaliacao = request.paraRespostaAvaliacao(aluno, avaliacao);

        aluno.adicionar(respostaAvaliacao);

        avaliacaoRepository.flush();

        URI location = uriComponentsBuilder.path("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas/{idResposta}")
                .buildAndExpand(aluno.getId(), avaliacao.getId(), respostaAvaliacao.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
