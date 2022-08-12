package br.com.mbs.cursojsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.model.Usuario;

public class UsuarioRepository {
	
	private Connection connection;
	
	public UsuarioRepository() {
		connection = SingleConnection.getConnection();
	}
	
	public void salvar(Usuario usuario) throws SQLException {
		
		String sql = "insert into usuario (login, senha, nome, email) values (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, usuario.getLogin());
		statement.setString(2, usuario.getSenha());
		statement.setString(3, usuario.getNome());
		statement.setString(4, usuario.getEmail());
		
		statement.execute();
		
		connection.commit();
	}

}
