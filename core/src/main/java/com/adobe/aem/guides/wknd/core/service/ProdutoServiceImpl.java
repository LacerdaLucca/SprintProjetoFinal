package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
import com.adobe.aem.guides.wknd.core.models.Produto;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.util.List;

@Component(immediate = true, service = ProdutoService.class)
public class ProdutoServiceImpl implements ProdutoService {
    @Reference
    private ProdutoDao ProdutoDao;

    @Override
    public String doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        if(req.getParameter("id")==null) {
            List<Produto> list = getProdutos();
            if(list.isEmpty()) {
                json = strToJson(getErroDTO("Não há produtos cadastrados"));
            }else {
                json = strToJson(list);
            }
        }else{
            Produto produto = ProdutoDao.buscaProduto(Integer.parseInt(req.getParameter("id")));
            json = strToJson(produto);
            if(produto==null) {
                json = strToJson(getErroDTO("Id não encontrado"));
            }
        }
        return json;
    }

    private ErroDTO getErroDTO(String msg) {
        ErroDTO erroDTO = new ErroDTO(msg);
        return erroDTO;
    }

    @Override
    public String doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String produtoPS = null;
        try {
            produtoPS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Produto produto;
        try {
            produto = new Gson().fromJson(produtoPS, Produto.class);
            ProdutoDao.addProduto(produto);
        }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(produto);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        if(req.getParameter("id")!=null) {
            Produto produto = ProdutoDao.buscaProduto(Integer.parseInt(req.getParameter("id")));
            ProdutoDao.rmvProduto(produto);
            json = strToJson(produto);
            if(produto==null) {
                json = strToJson(getErroDTO("Produto não encontrado para ser deletado"));
            }
        }else{
            json = strToJson(getErroDTO("Você não passou o id como parâmetro para ser deletado"));
        }
        return json;
    }
    @Override
    public String doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp){
        String produtoPS = null;
        try {
            produtoPS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Produto produto;
        try {
            produto = new Gson().fromJson(produtoPS, Produto.class);
            ProdutoDao.attProduto(produto);

        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(produto);
    }

    @Override
    public List<Produto> getProdutos(){
        return ProdutoDao.getProdutos();
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
