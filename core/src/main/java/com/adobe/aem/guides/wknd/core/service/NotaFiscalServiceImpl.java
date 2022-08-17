package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.dao.NotaFiscalDao;
import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
import com.adobe.aem.guides.wknd.core.models.ErroDTO;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.JsonException;
import java.io.IOException;
import java.util.List;

@Component(immediate = true, service = NotaFiscalService.class)
public class NotaFiscalServiceImpl implements NotaFiscalService {
    @Reference
    private NotaFiscalDao notaFiscalDao;

    @Override
    public String doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String json = "";
        if(req.getParameter("numero")==null) {
            List<NotaFiscal> list = getNotasFiscais();
            if(list.isEmpty()) {
                json = strToJson(getErroDTO("Não há notaFiscals cadastrados"));
            }else {
                json = strToJson(list);
            }
        }else{
            NotaFiscal notaFiscal = notaFiscalDao.buscaNotaFiscal(Integer.parseInt(req.getParameter("numero")));
            json = strToJson(notaFiscal);
            if(notaFiscal==null) {
                json = strToJson(getErroDTO("Numero não encontrado"));
            }
        }
        return json;
    }

    private ErroDTO getErroDTO(String msg) {
        ErroDTO erroDTO = new ErroDTO(msg);
        return erroDTO;
    }

    @Override
    public String doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        String NotaFiscalPS = null;
        try {
            NotaFiscalPS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NotaFiscal notaFiscal;
        try {
            notaFiscal = new Gson().fromJson(NotaFiscalPS, NotaFiscal.class);
            notaFiscalDao.addNotaFiscal(notaFiscal);
        }catch (Exception e){
                return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(notaFiscal);
    }

    @Override
    public String doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        try {
            String json = "";
            if (req.getParameter("numero") != null) {
                NotaFiscal notaFiscal = notaFiscalDao.buscaNotaFiscal(Integer.parseInt(req.getParameter("numero")));
                notaFiscalDao.rmvNotaFiscal(notaFiscal);
                json = strToJson(notaFiscal);
                if (notaFiscal == null) {
                    json = strToJson(getErroDTO("NotaFiscal não encontrado para ser deletado"));
                }
            } else {
                json = strToJson(getErroDTO("Você não passou o id como parâmetro para ser deletado"));
            }
            return json;
        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
    }
    @Override
    public String doPut(final SlingHttpServletRequest req, final SlingHttpServletResponse resp){
        String NotaFiscalPS = null;
        try {
            NotaFiscalPS = IOUtils.toString(req.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NotaFiscal notaFiscal;
        try {
            notaFiscal = new Gson().fromJson(NotaFiscalPS, NotaFiscal.class);
            notaFiscalDao.attNotaFiscal(notaFiscal);

        }catch (Exception e){
            return strToJson(getErroDTO("Entrada de dados invalidas"));
        }
        return strToJson(notaFiscal);
    }

    @Override
    public List<NotaFiscal> getNotasFiscais(){
        return notaFiscalDao.getNotasFiscais();
    }

    @Override
    public String strToJson(Object obj) {
        try {
            return new Gson().toJson(obj);
        }catch (Exception e){
            throw new JsonException("Erro no Json");
        }
    }
}
