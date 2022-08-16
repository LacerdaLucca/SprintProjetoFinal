package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = ClienteDao.class)
public class ClienteDaoImpl implements ClienteDao {
    @Reference
    private DatabaseService databaseService;

    @Override
    public List<Cliente> getClientes() {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM CLIENTE";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    List<Cliente> clienteList = new ArrayList<>();
                    while (rs.next()) {
                        Cliente cliente = new Cliente();
                        cliente.setId(rs.getInt(1));
                        cliente.setNome(rs.getString(2));
                        clienteList.add(cliente);
                    }
                    return clienteList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCliente(Cliente cliente) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO CLIENTE (NOME) VALUES (?)";
            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

                pstm.setString(1,cliente.getNome());
                pstm.execute();
                try(ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        cliente.setId(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rmvCliente(Cliente cliente) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM CLIENTE WHERE ID = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setInt(1,cliente.getId());
                pstm.execute();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void attCliente(Cliente cliente) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE CLIENTE C SET C.NOME = ? WHERE ID = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, cliente.getNome());
                pstm.setInt(2, cliente.getId());
                pstm.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente buscaCliente(int id) {
        Cliente cliente = null;
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM CLIENTE WHERE ID = ? ";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);
                pstm.execute();
                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        cliente = new Cliente(id, rst.getString(2));
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cliente;
    }
}
