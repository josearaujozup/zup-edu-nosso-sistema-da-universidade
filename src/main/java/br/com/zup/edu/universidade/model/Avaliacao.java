package br.com.zup.edu.universidade.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private Set<Questao> questoes = new LinkedHashSet<>();

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Avaliacao(Set<Questao> questoes) {
        this.questoes = questoes;
    }

    public Avaliacao() {
    }

    public Long getId() {
        return id;
    }

    public Set<Questao> getQuestoes() {
        return questoes;
    }
}
