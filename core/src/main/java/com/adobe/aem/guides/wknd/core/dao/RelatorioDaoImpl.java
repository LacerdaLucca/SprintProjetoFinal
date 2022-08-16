package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.RelatorioDTO;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component(immediate = true, service = RelatorioDao.class)
public class RelatorioDaoImpl implements RelatorioDao{
    @Reference
    private DatabaseService databaseService;

    public RelatorioDTO buscaComprasCliente(int idCliente) {
        RelatorioDTO relatorio = new RelatorioDTO(idCliente);
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT IDPRODUTO FROM NotaFiscal WHERE IDCLIENTE = ? ";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idCliente);
                pstm.execute();
                try (ResultSet rst = pstm.getResultSet()) {

                    while (rst.next()) {
                        relatorio.addProdutoRelatorio(rst.getInt(1));
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return relatorio;
    }
}

