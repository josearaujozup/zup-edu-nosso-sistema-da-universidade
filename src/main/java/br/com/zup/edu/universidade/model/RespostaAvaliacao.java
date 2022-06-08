package br.com.zup.edu.universidade.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class RespostaAvaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    private Aluno aluno;

    @ManyToOne(optional = false)
    private Avaliacao avaliacao;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<RespostaQuestao> respostas = new LinkedHashSet<>();

    public RespostaAvaliacao(Aluno aluno, Avaliacao avaliacao, Set<RespostaQuestao> respostas) {
        this.aluno = aluno;
        this.avaliacao = avaliacao;
        this.respostas = respostas;
    }

    @Deprecated
    public RespostaAvaliacao() {
    }

    public Set<RespostaQuestao> getRespostas() {
        return respostas;
    }

    public Long getId() {
        return id;
    }

    public boolean pertence(Aluno aluno) {
        return this.aluno.equals(aluno);
    }
}
