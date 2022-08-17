package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.adobe.aem.guides.wknd.core.models.Produto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.JsonException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
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
        List<Produto> listaProduto = null;
        try {
            produtoPS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(produtoPS.charAt(0) == '['){
            Type listType = new TypeToken<ArrayList<Produto>>(){}.getType();
            listaProduto= new Gson().fromJson(produtoPS, listType);;
            try {
                for(Produto produto: listaProduto){
                    produtoDao.addProduto(produto);
                }

            }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidos"));
            }
        }else{
            Produto produto;
            try {
                produto = new Gson().fromJson(produtoPS, Produto.class);
                produtoDao.addProduto(produto);
                return strToJson(produto);
            }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidas"));
            }
        }
        return strToJson(listaProduto);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        try{
            String json = "";
            String produtoPS = null;
            List<Produto> listaProduto = null;
            try {
                produtoPS = IOUtils.toString(req.getReader());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (req.getParameter("id") != null || (produtoPS != null && (produtoPS.charAt(0) != '['))) {

                Produto produto = null;
                if (req.getParameter("id") == null) {
                    produto = new Gson().fromJson(produtoPS, Produto.class);
                } else {
                    produto = produtoDao.buscaProduto(Integer.parseInt(req.getParameter("id")));
                }
                try {
                    produtoDao.rmvProduto(produto);
                } catch (Exception e) {
                    return strToJson(getErroDTO("Produto não encontrado para ser deletado"));
                }
                json = strToJson(produto);
            } else {
                if (produtoPS != null) {
                    Type listType = new TypeToken<ArrayList<Produto>>() {
                    }.getType();
                    List<Produto> produtoList = new Gson().fromJson(produtoPS, listType);
                    try {
                        for (Produto produto : produtoList) {
                            if (produtoDao.buscaProduto(produto.getId()).getNome().equals(produto.getNome())) {
                                produtoDao.rmvProduto(produto);
                            } else {
                                return strToJson(getErroDTO("Nome diferente do registrado, remoção interrompida"));
                            }
                        }
                    } catch (Exception e) {
                        return strToJson(getErroDTO("Entrada de dados invalidos"));
                    }
                    json = strToJson(produtoList);
                } else {
                    return strToJson(getErroDTO("Informações invalidas para a remoção"));
                }
            }
            return json;
        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
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
        try {
            return new Gson().toJson(obj);
        }catch (Exception e){
            throw new JsonException("Erro no Json");
        }
    }
}
