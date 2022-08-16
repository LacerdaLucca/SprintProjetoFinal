package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.NotaFiscal;

import java.util.List;

public interface NotaFiscalDao {
    List<NotaFiscal> getNotasFiscais();
    void addNotaFiscal(NotaFiscal nf);
    void rmvNotaFiscal(NotaFiscal nf);
    void attNotaFiscal(NotaFiscal nf);
    NotaFiscal buscaNotaFiscal(int id);
}
