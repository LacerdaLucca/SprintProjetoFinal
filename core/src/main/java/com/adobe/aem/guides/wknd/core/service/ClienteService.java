package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public interface ClienteService {

    String doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doDelete(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    List<Cliente> getClientes();
    String strToJson(Object obj);
}