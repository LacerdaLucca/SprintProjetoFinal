package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Produto;

import java.util.List;

public interface ProdutoDao {
    List<Produto> getProdutos();
    void addProduto(Produto produto);
    void rmvProduto(Produto produto);
    void attProduto(Produto produto);
    Produto buscaProduto(int id);
}
