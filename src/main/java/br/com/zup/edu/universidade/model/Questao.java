package br.com.zup.edu.universidade.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String resposta;

    @Column(nullable = false)
    private BigDecimal valor;

    public Questao(String descricao, String resposta, BigDecimal valor) {
        this.descricao = descricao;
        this.resposta = resposta;
        this.valor = valor;
    }

    @Deprecated
    public Questao() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Questao{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", resposta='" + resposta + '\'' +
                ", valor=" + valor +
                '}';
    }
}
