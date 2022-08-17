package com.adobe.aem.guides.wknd.core.models;

public class NotaFiscal {
    private int numero;
    private int idProduto;
    private int idCliente;
    private double valor;

    public NotaFiscal(int numero, int idProduto, int idCliente, double valor) {
        this.numero = numero;
        this.idProduto = idProduto;
        this.idCliente = idCliente;
        this.valor = valor;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public double getValor() {
        return valor;
    }
}
