package br.com.mbs.cursojsp.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.mbs.cursojsp.connection.SingleConnection;

public class VersionadorBDRepository implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	
	public VersionadorBDRepository() {
		connection = SingleConnection.getConnection();
	}
	
	public void gravar(String nomeArquivo) throws SQLException {
		String sql = "insert into versionador_bd (arquivo_sql) values (?)";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, nomeArquivo);
		statement.execute();
	}
	
	public boolean arquivoSQLExecutado(String nomeArquivo) throws SQLException {
		String sql = "select count(1) > 0 as executado from versionador_bd where arquivo_sql = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, nomeArquivo);
		
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		
		return resultSet.getBoolean("executado");
	}

}
