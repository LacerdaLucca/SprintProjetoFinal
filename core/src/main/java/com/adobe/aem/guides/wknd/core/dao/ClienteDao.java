package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Cliente;

import java.util.List;

public interface ClienteDao {
    List<Cliente> getClientes();
}
