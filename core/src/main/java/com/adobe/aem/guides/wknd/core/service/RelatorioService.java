package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.util.List;

public interface RelatorioService {

    String doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doDelete(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp);
    String strToJson(Object obj);
}