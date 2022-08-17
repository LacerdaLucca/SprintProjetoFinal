package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.models.RelatorioDTO;
import com.adobe.aem.guides.wknd.core.service.RelatorioService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
@ExtendWith(AemContextExtension.class)
public class RelatorioServletTest
{

    @Mock
    private RelatorioService relatorioServiceMock;

    private MockSlingHttpServletRequest req;

    private MockSlingHttpServletResponse resp;

    private RelatorioServlet relatorioServlet;

    @BeforeEach
    void setup(AemContext context) {
        MockitoAnnotations.openMocks(this);
        relatorioServlet = new RelatorioServlet(relatorioServiceMock);

        req = context.request();
        resp = context.response();
    }

    @Test
    void TesteComParametros(){

        RelatorioDTO relatorioDTO = new RelatorioDTO(1);
        req.addRequestParameter("cliente", "1");
        Mockito.when(relatorioServiceMock.doGet(req,resp)).thenReturn(String.valueOf(relatorioDTO.getIdCliente()));
        try {
            relatorioServlet.doGet(req, resp);
        }
        catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertEquals(HttpServletResponse.SC_OK, resp.getStatus());
        Assertions.assertEquals("text/html", resp.getContentType());
//        Assertions.assertEquals("application/json;charset=UTF-8", resp.getContentType());
    }
}
