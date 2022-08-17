package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Cliente;

import java.util.List;

public interface ClienteDao {
    List<Cliente> getClientes();
    void addCliente(Cliente cliente);
    void rmvCliente(Cliente cliente);
    void attCliente(Cliente cliente);
    Cliente buscaCliente(int id);
}
