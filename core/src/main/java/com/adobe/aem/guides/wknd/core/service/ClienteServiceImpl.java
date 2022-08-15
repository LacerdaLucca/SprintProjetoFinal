package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.google.gson.Gson;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

@Component(immediate = true, service = ClienteService.class)
public class ClienteServiceImpl implements ClienteService {
    @Reference
    private ClienteDao clienteDao;
    @Override
    public List<Cliente> getClientes(){
        return clienteDao.getClientes();
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
