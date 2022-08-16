package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.Produto;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.util.List;

public interface ProdutoService {

    String doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doDelete(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    List<Produto> getProdutos();
    String strToJson(Object obj);
}