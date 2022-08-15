package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
