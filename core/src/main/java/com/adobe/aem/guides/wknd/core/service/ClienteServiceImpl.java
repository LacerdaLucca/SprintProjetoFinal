package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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

@Component(immediate = true, service = ClienteService.class)
public class ClienteServiceImpl implements ClienteService {
    @Reference
    private ClienteDao clienteDao;

    @Override
    public String doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        if(req.getParameter("id")==null) {
            List<Cliente> list = getClientes();
            if(list.isEmpty()) {
                json = strToJson(getErroDTO("Não há clientes cadastrados"));
            }else {
                json = strToJson(list);
            }
        }else{
            Cliente cliente = clienteDao.buscaCliente(Integer.parseInt(req.getParameter("id")));
            json = strToJson(cliente);
            if(cliente==null) {
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
        String clientePS = null;
        try {
            clientePS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Type listType = new TypeToken<ArrayList<Cliente>>(){}.getType();
        List<Cliente> clienteList= new Gson().fromJson(clientePS, listType);;
        try {
            for(Cliente cliente: clienteList){
                clienteDao.addCliente(cliente);
            }

        }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidos"));
        }
        return strToJson(clienteList);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        String clientePS = null;
        if(req.getParameter("id")!=null) {
            Cliente cliente = clienteDao.buscaCliente(Integer.parseInt(req.getParameter("id")));
            clienteDao.rmvCliente(cliente);
            json = strToJson(cliente);
            if(cliente==null) {
                json = strToJson(getErroDTO("Cliente não encontrado para ser deletado"));
            }
        }else{
            try {
                if(req.getReader()!=null) {
                    clientePS = IOUtils.toString(req.getReader());
                    Type listType = new TypeToken<ArrayList<Cliente>>() {}.getType();
                    List<Cliente> clienteList = new Gson().fromJson(clientePS, listType);
                    try {
                        for (Cliente cliente : clienteList) {
                            if(clienteDao.buscaCliente(cliente.getId()).getNome().equals(cliente.getNome())) {
                                clienteDao.rmvCliente(cliente);
                            }else{
                                return strToJson(getErroDTO("Nome diferente do registrado, remoção interrompida"));
                            }
                        }
                    } catch (Exception e) {
                        return strToJson(getErroDTO("Entrada de dados invalidos"));
                    }
                    json = strToJson(clienteList);
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
        String clientePS = null;
        try {
            clientePS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Cliente cliente;
        try {
            cliente = new Gson().fromJson(clientePS, Cliente.class);
            clienteDao.attCliente(cliente);

        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(cliente);
    }

    @Override
    public List<Cliente> getClientes(){
        return clienteDao.getClientes();
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
