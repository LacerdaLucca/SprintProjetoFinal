package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.dao.RelatorioDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.adobe.aem.guides.wknd.core.models.RelatorioDTO;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import java.io.IOException;
import java.util.List;

@Component(immediate = true, service = RelatorioService.class)
public class RelatorioServiceImpl implements RelatorioService {
    @Reference
    private ClienteDao clienteDao;
    @Reference
    private RelatorioDao relatorioDao;

    @Override
    public String doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String clienteStr = req.getParameter("cliente");
        RelatorioDTO relatorios = null;
        int cliente = 0;
        if(clienteStr !=null){
            cliente = Integer.parseInt(clienteStr);
            if(clienteDao.buscaCliente(cliente)!=null){
               relatorios = relatorioDao.buscaComprasCliente(cliente);
            }
        }
        if(cliente == 0){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(relatorios);
    }

    private ErroDTO getErroDTO(String msg) {
        ErroDTO erroDTO = new ErroDTO(msg);
        return erroDTO;
    }

    @Override
    public String doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String ClientePS = null;
        try {
            ClientePS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Cliente cliente;
        try {
            cliente = new Gson().fromJson(ClientePS, Cliente.class);
            clienteDao.addCliente(cliente);
        }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(cliente);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        if(req.getParameter("id")!=null) {
            Cliente cliente = clienteDao.buscaCliente(Integer.parseInt(req.getParameter("id")));
            clienteDao.rmvCliente(cliente);
            json = strToJson(cliente);
            if(cliente==null) {
                json = strToJson(getErroDTO("Cliente não encontrado para ser deletado"));
            }
        }else{
            json = strToJson(getErroDTO("Você não passou o id como parâmetro para ser deletado"));
        }
        return json;
    }
    @Override
    public String doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp){
        String ClientePS = null;
        try {
            ClientePS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Cliente cliente;
        try {
            cliente = new Gson().fromJson(ClientePS, Cliente.class);
            clienteDao.attCliente(cliente);

        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(cliente);
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
