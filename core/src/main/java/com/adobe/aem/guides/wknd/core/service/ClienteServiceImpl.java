package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
        List<Cliente> listaCliente = null;
        try {
            clientePS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(clientePS.charAt(0) == '['){
            Type tipoLista = new TypeToken<ArrayList<Cliente>>(){}.getType();
            listaCliente = new Gson().fromJson(clientePS, tipoLista);;
            try {
                for(Cliente cliente: listaCliente){
                    clienteDao.addCliente(cliente);
                }

            }catch (Exception e){
                    return strToJson(getErroDTO("Entrada de dados invalidos"));
            }
        }else{
            Cliente cliente;
            try {
                cliente = new Gson().fromJson(clientePS, Cliente.class);
                clienteDao.addCliente(cliente);
                return strToJson(cliente);
            }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidas"));
            }
        }
        return strToJson(listaCliente);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        try {
            String json = "";
            String clientePS = null;
            List<Cliente> listaCliente = null;
            try {
                clientePS = IOUtils.toString(req.getReader());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (req.getParameter("id") != null || (clientePS != null && (clientePS.charAt(0) != '['))) {
                Cliente cliente = null;
                if (req.getParameter("id") == null) {
                    cliente = new Gson().fromJson(clientePS, Cliente.class);
                } else {
                    cliente = clienteDao.buscaCliente(Integer.parseInt(req.getParameter("id")));
                }
                try {
                    if (clienteDao.buscaCliente(cliente.getId()).getNome().equals(cliente.getNome())) {
                        clienteDao.rmvCliente(cliente);
                    } else {
                        return strToJson(getErroDTO("Nome diferente do registrado, remoção interrompida"));
                    }
                } catch (Exception ex) {
                    return strToJson(getErroDTO("Cliente não encontrado para ser deletado"));
                }
                json = strToJson(cliente);
            } else {
                if (clientePS != null && (clientePS.charAt(0) == '[')) {
                    Type tipoLista = new TypeToken<ArrayList<Cliente>>() {
                    }.getType();
                    listaCliente = new Gson().fromJson(clientePS, tipoLista);
                    try {
                        for (Cliente cliente : listaCliente) {
                            if (clienteDao.buscaCliente(cliente.getId()).getNome().equals(cliente.getNome())) {
                                clienteDao.rmvCliente(cliente);
                            } else {
                                return strToJson(getErroDTO("Nome diferente do registrado, remoção interrompida"));
                            }
                        }
                        json = strToJson(listaCliente);
                    } catch (Exception e) {
                        return strToJson(getErroDTO("Entrada de dados invalidos"));
                    }
                } else {
                    json = strToJson(getErroDTO("Informações invalidas para a remoção"));
                }
            }
            return json;
        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
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
        try {
            return new Gson().toJson(obj);
        }catch (Exception e){
            throw new JsonException("Erro no Json");
        }
    }
}
