package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.Produto;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = ProdutoService.class)
public class ProdutoServiceImpl implements ProdutoService {
    @Reference
    private ProdutoDao produtoDao;

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
            Produto produto = produtoDao.buscaProduto(Integer.parseInt(req.getParameter("id")));
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
        Type listType = new TypeToken<ArrayList<Cliente>>(){}.getType();
        List<Produto> produtoList= new Gson().fromJson(produtoPS, listType);;
        try {
            for(Produto produto: produtoList){
                produtoDao.addProduto(produto);
            }

        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidos"));
        }
        return strToJson(produtoList);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        String produtoPS = null;
        if(req.getParameter("id")!=null) {
            Produto produto = produtoDao.buscaProduto(Integer.parseInt(req.getParameter("id")));
            produtoDao.rmvProduto(produto);
            json = strToJson(produto);
            if(produto==null) {
                json = strToJson(getErroDTO("Cliente não encontrado para ser deletado"));
            }
        }else{
            try {
                if(req.getReader()!=null) {
                    produtoPS = IOUtils.toString(req.getReader());
                    Type listType = new TypeToken<ArrayList<Cliente>>() {}.getType();
                    List<Produto> produtoList = new Gson().fromJson(produtoPS, listType);
                    try {
                        for (Produto produto : produtoList) {
                            if(produtoDao.buscaProduto(produto.getId()).getNome().equals(produto.getNome())) {
                                produtoDao.rmvProduto(produto);
                            }else{
                                return strToJson(getErroDTO("Nome diferente do registrado, remoção interrompida"));
                            }
                        }
                    } catch (Exception e) {
                        return strToJson(getErroDTO("Entrada de dados invalidos"));
                    }
                    json = strToJson(produtoList);
                }else{
                    json = strToJson(getErroDTO("Informações invalidas para a remoção"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            produtoDao.attProduto(produto);

        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(produto);
    }

    @Override
    public List<Produto> getProdutos(){
        return produtoDao.getProdutos();
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
