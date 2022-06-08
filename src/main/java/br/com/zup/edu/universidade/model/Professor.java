package br.com.zup.edu.universidade.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String matricula;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.REMOVE)
    private List<Turma> turmas = new ArrayList<>();

    public Professor(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    @Deprecated
    public Professor() {
    }

    public Long getId() {
        return id;
    }

    public void adicionar(Turma turma) {
        this.turmas.add(turma);
    }

    public void remover(Turma turma) {
        this.turmas.remove(turma);
    }
}
