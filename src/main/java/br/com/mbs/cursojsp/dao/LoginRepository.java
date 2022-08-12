package br.com.mbs.cursojsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.model.Usuario;

public class LoginRepository {

	private Connection connection;
	
	public LoginRepository() {
		connection = SingleConnection.getConnection();
	}
	
	public boolean validarAutenticacao(Usuario login) throws SQLException {
		
		String sql = "select * from usuario where login = ? and senha = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, login.getLogin());
		statement.setString(2, login.getSenha());
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			return true;
		}
		
		
		return false;
	}
}
