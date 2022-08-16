package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = NotaFiscalDao.class)
public class NotaFiscalDaoImpl implements NotaFiscalDao {
    @Reference
    private DatabaseService databaseService;

    @Override
    public List<NotaFiscal> getNotasFiscais() {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM NotaFiscal";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    List<NotaFiscal> NotaFiscalList = new ArrayList<>();
                    while (rs.next()) {
                        NotaFiscal NotaFiscal = new NotaFiscal(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getDouble(4));
                        NotaFiscalList.add(NotaFiscal);
                    }
                    return NotaFiscalList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addNotaFiscal(NotaFiscal NotaFiscal) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO NotaFiscal (IDPRODUTO,IDCLIENTE,VALOR) VALUES (?,?,?)";
            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

                pstm.setInt(1,NotaFiscal.getIdProduto());
                pstm.setInt(2,NotaFiscal.getIdCliente());
                pstm.setDouble(3,NotaFiscal.getValor());
                pstm.execute();
                try(ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        NotaFiscal.setNumero(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rmvNotaFiscal(NotaFiscal NotaFiscal) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM NotaFiscal WHERE NUMERO = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setInt(1,NotaFiscal.getNumero());
                pstm.execute();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void attNotaFiscal(NotaFiscal NotaFiscal) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE NotaFiscal NF SET NF.IDPRODUTO = ?,NF.IDCLIENTE = ?, NF.VALOR = ? WHERE NUMERO = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1,NotaFiscal.getIdProduto());
                pstm.setInt(2,NotaFiscal.getIdCliente());
                pstm.setDouble(3,NotaFiscal.getValor());
                pstm.setInt(4, NotaFiscal.getNumero());
                pstm.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotaFiscal buscaNotaFiscal(int numero) {
        NotaFiscal NotaFiscal = null;
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM NotaFiscal WHERE NUMERO = ? ";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, numero);
                pstm.execute();
                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        NotaFiscal = new NotaFiscal(numero, rst.getInt(2),rst.getInt(3),rst.getDouble(4));
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return NotaFiscal;
    }
}
