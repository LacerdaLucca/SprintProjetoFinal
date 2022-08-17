package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Produto;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = ProdutoDao.class)
public class ProdutoDaoImpl implements ProdutoDao {
    @Reference
    private DatabaseService databaseService;

    @Override
    public List<Produto> getProdutos() {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM PRODUTO";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    List<Produto> produtoList = new ArrayList<>();
                    while (rs.next()) {
                        Produto produto = new Produto(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4));
                        produtoList.add(produto);
                    }
                    return produtoList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProduto(Produto produto) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO produto (NOME,CATEGORIA,PRECO) VALUES (?,?,?)";
            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

                pstm.setString(1,produto.getNome());
                pstm.setString(2,produto.getCategoria());
                pstm.setDouble(3,produto.getPreco());
                pstm.execute();
                try(ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        produto.setId(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rmvProduto(Produto produto) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM produto WHERE ID = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setInt(1,produto.getId());
                pstm.execute();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void attProduto(Produto produto) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE produto P SET P.NOME = ?, P.CATEGORIA = ?, P.PRECO = ? WHERE ID = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, produto.getNome());
                pstm.setString(2, produto.getCategoria());
                pstm.setDouble(3, produto.getPreco());
                pstm.setInt(4, produto.getId());
                pstm.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Produto buscaProduto(int id) {
        Produto produto = null;
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM produto WHERE ID = ? ";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);
                pstm.execute();
                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        produto = new Produto(id, rst.getString(2),rst.getString(3),rst.getDouble(4));
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produto;
    }
}
