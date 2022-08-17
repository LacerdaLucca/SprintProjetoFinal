package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
import com.adobe.aem.guides.wknd.core.dao.ProdutoDaoImpl;

import java.util.ArrayList;
import java.util.Collection;

public class RelatorioDTO {

    private Collection<Produto> produtos;
    private int idCliente;


    public RelatorioDTO(int idCliente) {

        this.produtos = new ArrayList<>();
        this.idCliente = idCliente;
    }

    public void addProdutoRelatorio(int idProduto){
        ProdutoDao produtoDao = new ProdutoDaoImpl();
        Produto produto = produtoDao.buscaProduto(idProduto);
//        Produto produto = new Produto();
        this.produtos.add(produto);
    }

    public int getIdCliente() {
        return idCliente;
    }


    public Collection<Produto> getProdutos() {
        return produtos;
    }
}
