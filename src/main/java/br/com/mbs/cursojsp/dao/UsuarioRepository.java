package br.com.mbs.cursojsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.model.Usuario;

public class UsuarioRepository {

	private Connection connection;

	public UsuarioRepository() {
		connection = SingleConnection.getConnection();
	}

	public Usuario salvar(Usuario usuario) throws SQLException {

		String sql = "insert into usuario (login, senha, nome, email) values (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, usuario.getLogin());
		statement.setString(2, usuario.getSenha());
		statement.setString(3, usuario.getNome());
		statement.setString(4, usuario.getEmail());

		statement.execute();

		connection.commit();

		return this.buscarPorLogin(usuario.getLogin());
	}

	public Usuario buscarPorLogin(String login) throws SQLException {

		String sql = "select * from usuario where upper(login) = upper('" + login + "')";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		Usuario usuario = new Usuario();

		while (resultado.next()) {

			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
		}

		return usuario;
	}

	public boolean validarLogin(String login) throws SQLException {
		String sql = "select count(1) > 0 as existe from usuario where upper(login) = upper('" + login + "')";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();
		resultado.next();

		return resultado.getBoolean("existe");

	}

}
