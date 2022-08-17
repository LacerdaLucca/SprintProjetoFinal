package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
import com.adobe.aem.guides.wknd.core.dao.ProdutoDaoImpl;
import com.adobe.aem.guides.wknd.core.models.Produto;
import com.adobe.aem.guides.wknd.core.models.RelatorioDTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = RelatorioDTOService.class)
public class RelatorioDTOServiceImpl implements RelatorioDTOService{

    @Reference
    ProdutoDao produtoDao;
    @Override
    public RelatorioDTO addProdutoRelatorio(int idProduto,RelatorioDTO relatorioDTO) {
        Produto produto = produtoDao.buscaProduto(idProduto);
        relatorioDTO.setProduto(produto);
        return relatorioDTO;
    }

}
