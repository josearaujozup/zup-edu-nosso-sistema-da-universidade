package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.controller.request.AvaliacaoAlunoRequest;
import br.com.zup.edu.universidade.controller.request.RespostaQuestaoRequest;
import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.Avaliacao;
import br.com.zup.edu.universidade.model.Questao;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.AvaliacaoRepository;
import br.com.zup.edu.universidade.repository.QuestaoRepository;
import br.com.zup.edu.universidade.repository.RespostaAvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class FazerAvaliacaoControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RespostaAvaliacaoRepository respostaAvaliacaoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    private Avaliacao avaliacao;

    private Questao questao1;
    private Questao questao2;
    private Questao questao3;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        respostaAvaliacaoRepository.deleteAll();
        avaliacaoRepository.deleteAll();

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
        alunoRepository.save(this.aluno);
    }

    @Test
    @DisplayName("aluno não cadastrado não deve responder uma avaliacao")
    void alunoNaoCadastradoNaoDeveResponderUmaAvaliacao() throws Exception {

        RespostaQuestaoRequest respostaQuestao1 = new RespostaQuestaoRequest(questao1.getId(), "Haskell e Python");
        RespostaQuestaoRequest respostaQuestao2 = new RespostaQuestaoRequest(questao2.getId(), "Prolog");
        RespostaQuestaoRequest respostaQuestao3 = new RespostaQuestaoRequest(questao3.getId(), "Java e Ruby");

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(List.of(respostaQuestao1, respostaQuestao2, respostaQuestao3));

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                Long.MAX_VALUE,avaliacao.getId()).
                contentType(MediaType.APPLICATION_JSON)
                .content(payload);

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
    @DisplayName("aluno não deve responder uma avaliacao não cadastrada")
    void alunoNaoDeveResponderUmaAvaliacaoNaoCadastrada() throws Exception {

        RespostaQuestaoRequest respostaQuestao1 = new RespostaQuestaoRequest(questao1.getId(), "Haskell e Python");
        RespostaQuestaoRequest respostaQuestao2 = new RespostaQuestaoRequest(questao2.getId(), "Prolog");
        RespostaQuestaoRequest respostaQuestao3 = new RespostaQuestaoRequest(questao3.getId(), "Java e Ruby");

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(List.of(respostaQuestao1, respostaQuestao2, respostaQuestao3));

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                aluno.getId(),Long.MAX_VALUE).
                contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class,resolvedException.getClass());
        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Avaliacao não cadastrada",exception.getReason());
    }

    @Test
    @DisplayName("aluno não deve responder uma avaliacao com questões não cadastradas")
    void alunoNaoDeveResponderUmaAvaliacaoComQuestoesNaoCadastradas() throws Exception {

        RespostaQuestaoRequest respostaQuestao1 = new RespostaQuestaoRequest(questao1.getId(), "Haskell e Python");
        RespostaQuestaoRequest respostaQuestao2 = new RespostaQuestaoRequest(questao2.getId(), "Prolog");
        RespostaQuestaoRequest respostaQuestao3 = new RespostaQuestaoRequest(questao3.getId(), "Java e Ruby");
        RespostaQuestaoRequest respostaQuestao4 = new RespostaQuestaoRequest(Long.MAX_VALUE, "Kotlin");

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(List.of(
                respostaQuestao1,
                respostaQuestao2,
                respostaQuestao3,
                respostaQuestao4));

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                aluno.getId(),avaliacao.getId()).
                contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class,resolvedException.getClass());
        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Nao existe cadastro para questao com id 9223372036854775807",exception.getReason());
    }


    @Test
    @DisplayName("aluno não deve responder uma avaliacao sem as respostas")
    void alunoNaoDeveResponderUmaAvaliacaoSemRespostas() throws Exception {

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(null);

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                aluno.getId(),avaliacao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language","pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> mensagensDeErro = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(mensagensDeErro)
                .hasSize(1)
                .contains("O campo respostas não deve ser nulo");

    }

    @Test
    @DisplayName("aluno não deve responder uma avaliacao sem os dados das questões")
    void alunoNaoDeveResponderUmaAvaliacaoSemDadosDasQuestoes() throws Exception {

        RespostaQuestaoRequest respostaQuestao1 = new RespostaQuestaoRequest(null, null);
        RespostaQuestaoRequest respostaQuestao2 = new RespostaQuestaoRequest(null, null);
        RespostaQuestaoRequest respostaQuestao3 = new RespostaQuestaoRequest(null, null);

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(List.of(respostaQuestao1, respostaQuestao2, respostaQuestao3));

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                aluno.getId(),avaliacao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language","pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> mensagensDeErro = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(mensagensDeErro)
                .hasSize(6)
                .contains("O campo respostas[1].resposta não deve estar em branco",
                        "O campo respostas[2].resposta não deve estar em branco",
                        "O campo respostas[0].idQuestao não deve ser nulo",
                        "O campo respostas[1].idQuestao não deve ser nulo",
                        "O campo respostas[2].idQuestao não deve ser nulo",
                        "O campo respostas[0].resposta não deve estar em branco");

    }

    @Test
    @DisplayName("aluno não deve responder uma avaliacao com os dados das questões inválidos")
    void alunoNaoDeveResponderUmaAvaliacaoComDadosDasQuestoesInvalidos() throws Exception {

        RespostaQuestaoRequest respostaQuestao1 = new RespostaQuestaoRequest(-1L, "Haskell e Python");
        RespostaQuestaoRequest respostaQuestao2 = new RespostaQuestaoRequest(-1L, "Prolog");
        RespostaQuestaoRequest respostaQuestao3 = new RespostaQuestaoRequest(-1L, "Java e Ruby");

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(List.of(respostaQuestao1, respostaQuestao2, respostaQuestao3));

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                aluno.getId(),avaliacao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language","pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> mensagensDeErro = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(mensagensDeErro)
                .hasSize(3)
                .contains("O campo respostas[2].idQuestao deve ser maior que 0",
                        "O campo respostas[1].idQuestao deve ser maior que 0",
                        "O campo respostas[0].idQuestao deve ser maior que 0");

    }

    @Test
    @DisplayName("aluno deve responder uma avaliacao")
    void alunoDeveResponderUmaAvaliacao() throws Exception {

        RespostaQuestaoRequest respostaQuestao1 = new RespostaQuestaoRequest(questao1.getId(), "Haskell e Python");
        RespostaQuestaoRequest respostaQuestao2 = new RespostaQuestaoRequest(questao2.getId(), "Prolog");
        RespostaQuestaoRequest respostaQuestao3 = new RespostaQuestaoRequest(questao3.getId(), "Java e Ruby");

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(List.of(
                respostaQuestao1,
                respostaQuestao2,
                respostaQuestao3
        ));

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",
                aluno.getId(),avaliacao.getId()).
                contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        String location = mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/alunos/*/avaliacoes/*/respostas/*")
                )
                .andReturn()
                .getResponse()
                .getHeader("location");

        assertNotNull(location);

        int posicaoAposAUltimaBarra = location.lastIndexOf("/") + 1;
        Long idResposta = Long.valueOf(location.substring(posicaoAposAUltimaBarra));

        assertTrue(
                respostaAvaliacaoRepository.existsById(idResposta),
                "Deveria existir um registro de resposta para esse id"
        );

    }

}