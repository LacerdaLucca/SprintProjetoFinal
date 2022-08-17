/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.adobe.aem.guides.wknd.core.service.RelatorioService;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingAllMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(immediate = true, service = Servlet.class, property = {
        SLING_SERVLET_METHODS + "=" + "GET",
        SLING_SERVLET_METHODS + "=" + "POST",
        SLING_SERVLET_PATHS + "=" + "/bin/RelatorioServlet",
        SLING_SERVLET_EXTENSIONS + "=" + "txt", SLING_SERVLET_EXTENSIONS + "=" + "json"})

@ServiceDescription("Servlet do Relatorio")
public class RelatorioServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private final RelatorioService relatorioService;
    @Activate
    public RelatorioServlet(@Reference RelatorioService relatorioService){
        this.relatorioService=relatorioService;
    }
    @Override
    protected void doGet(SlingHttpServletRequest req,
                         SlingHttpServletResponse resp) throws ServletException, IOException {
        int statusCode = HttpServletResponse.SC_OK;
        resp.setContentType("application/json");
        String json = "";
        try {
             json = relatorioService.doGet(req, resp);
        }catch (Exception e){
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            json = new Gson().toJson(new ErroDTO("Erro no get"));
        }
        resp.setStatus(statusCode);
        resp.getWriter().println(json);


    }
    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
    }
    @Override
    protected void doDelete(final SlingHttpServletRequest req,
                            final SlingHttpServletResponse resp) throws ServletException, IOException {

    }
    @Override
    protected void doPut(final SlingHttpServletRequest req,
                            final SlingHttpServletResponse resp) throws ServletException, IOException {

    }


}