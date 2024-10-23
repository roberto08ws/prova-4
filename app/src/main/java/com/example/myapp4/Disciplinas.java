package com.example.myapp4;

public class Disciplinas {

    private int id;
    private String nome_disciplina, descricao_disciplina;

    public Disciplinas() {
    }

    public Disciplinas(int id, String nome_disciplina, String descricao_disciplina) {
        this.id = id;
        this.nome_disciplina = nome_disciplina;
        this.descricao_disciplina = descricao_disciplina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_disciplina() {
        return nome_disciplina;
    }

    public void setNome_disciplina(String nome_disciplina) {
        this.nome_disciplina = nome_disciplina;
    }

    public String getDescricao_disciplina() {
        return descricao_disciplina;
    }

    public void setDescricao_disciplina(String descricao_disciplina) {
        this.descricao_disciplina = descricao_disciplina;
    }
}
