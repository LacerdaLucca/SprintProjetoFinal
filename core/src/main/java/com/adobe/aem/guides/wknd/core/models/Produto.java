package com.adobe.aem.guides.wknd.core.models;

public class Produto {
    private int id;
    private String nome;
    private String categoria;
    private double preco;

    public Produto(int id, String nome, String categoria, double preco) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
    }

    public Produto() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPreco() {
        return preco;
    }

    public String toHTML() {
        return "Produto:" + "</br>" +
                "id=" + id +
                ", nome=" + nome +
                ", categoria=" + categoria +
                ", preco=" + preco +
                '}' + "</br>";
    }
}
