package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.model.*;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.AvaliacaoRepository;
import br.com.zup.edu.universidade.repository.RespostaAvaliacaoRepository;
import br.com.zup.edu.universidade.repository.RespostaQuestaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class RemoverAlunoControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private RespostaAvaliacaoRepository respostaAvaliacaoRepository;

    @Autowired
    private RespostaQuestaoRepository respostaQuestaoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    private Aluno aluno;

    private Avaliacao avaliacao;

    private RespostaAvaliacao respostaAvaliacao;

    private Questao questao1;
    private RespostaQuestao respostaQuestao1;
    private Questao questao2;
    private RespostaQuestao respostaQuestao2;
    private Questao questao3;
    private RespostaQuestao respostaQuestao3;

    private Set<RespostaQuestao> respostaQuestoes;

    @BeforeEach
    void setUp() {
        alunoRepository.deleteAll();

        this.questao1 = new Questao("Exemplo Linguagem Funcional","Haskell e Python",new BigDecimal("1"));
        this.questao2 = new Questao("Exemplo Linguagem Lógica","Prolog",new BigDecimal("1"));
        this.questao3 = new Questao("Exemplo Linguagem Orientada a Objetos","Java e Ruby",new BigDecimal("1"));

        Set<Questao> questoes = new java.util.HashSet<>();
        questoes.add(this.questao1);
        questoes.add(this.questao2);
        questoes.add(this.questao3);

        this.avaliacao = new Avaliacao(questoes);
        avaliacaoRepository.save(this.avaliacao);


        this.aluno = new Aluno("Denes","20220155we", LocalDate.of(1991,9,05));

        this.respostaQuestao1 = new RespostaQuestao(this.aluno,questao1,"Haskell e Python");
        this.respostaQuestao2 = new RespostaQuestao(this.aluno,questao2,"Prolog");
        this.respostaQuestao3 = new RespostaQuestao(this.aluno,questao3,"Java e Ruby");
        this.respostaQuestoes = Set.of(respostaQuestao1,respostaQuestao2,respostaQuestao3);

        this.respostaAvaliacao = new RespostaAvaliacao(this.aluno,this.avaliacao,this.respostaQuestoes);
        this.aluno.adicionar(this.respostaAvaliacao);

        alunoRepository.save(this.aluno);
    }

    @Test
    @DisplayName("não deve remover um aluno não cadastrado")
    void naoDeveRemoverAlunoNaoCadastrado() throws Exception {

        MockHttpServletRequestBuilder request = delete("/alunos/{id}",
                Long.MAX_VALUE).
                contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class,resolvedException.getClass());
        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("aluno nao cadastrado",exception.getReason());
    }

    @Test
    @DisplayName("deve remover um aluno com respostas")
    void naoDeveRemoverAlunoComRespostas() throws Exception {

        MockHttpServletRequestBuilder request = delete("/alunos/{id}",
                this.aluno.getId()).
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(
                        status().isNoContent()
                );

        assertFalse(
                alunoRepository.existsById(this.aluno.getId()),
                "não deveria existir um registro de album com esse id"
        );

        assertFalse(
                respostaAvaliacaoRepository.existsById(this.respostaAvaliacao.getId()),
                "não deveria um registro de resposta avaliação com esse id"
        );

        assertThat(respostaQuestoes)
                .allMatch(
                        r -> !respostaQuestaoRepository.existsById(r.getId()),
                        "não deveria existir um registro de resposta questão para esse id"
                );
    }

}