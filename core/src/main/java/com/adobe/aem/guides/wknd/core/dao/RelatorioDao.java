package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.RelatorioDTO;

import java.util.List;

public interface RelatorioDao {
    RelatorioDTO buscaComprasCliente(int idCliente);
}
