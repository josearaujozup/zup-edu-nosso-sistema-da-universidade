package br.com.zup.edu.universidade.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sigla;

    @Column(nullable = false)
    private String ementa;

    @Column(nullable = false)
    private Integer cargaHorariaEmHoras;

    @Column(nullable = false)
    private LocalDateTime criadoEm=LocalDateTime.now();

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.PERSIST)
    private List<Turma> turmas = new ArrayList<>();

    public Disciplina(String nome, String sigla, String ementa, Integer cargaHorariaEmHoras) {
        this.nome = nome;
        this.sigla = sigla;
        this.ementa = ementa;
        this.cargaHorariaEmHoras = cargaHorariaEmHoras;
    }

    @Deprecated
    public Disciplina() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void adicionar(Turma turma) {
        this.turmas.add(turma);
    }
}
