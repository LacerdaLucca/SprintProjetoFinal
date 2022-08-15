package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.models.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> getClientes();
    String strToJson(Object obj);
}