package br.com.mbs.cursojsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.model.Usuario;

public class UsuarioRepository {

	private Connection connection;

	public UsuarioRepository() {
		connection = SingleConnection.getConnection();
	}

	public Usuario salvar(Usuario usuario) throws SQLException {

		if (usuario.naoExiste()) {

			String sql = "insert into usuario (login, senha, nome, email) values (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());

			statement.execute();

			connection.commit();
		} else {

			String sql = "update usuario set login=?, senha=?, nome=?, email=? where id= " + usuario.getId() + "";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());

			statement.executeUpdate();

			connection.commit();
		}

		return this.buscarPorLogin(usuario.getLogin());
	}

	public List<Usuario> listarUsuarios() throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();
			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));

			lista.add(usuario);
		}

		return lista;
	}

	public List<Usuario> conultarUsuariosPorNome(String nome) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where upper(nome) like upper(?) order by id";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, "%" + nome + "%");

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();
			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));

			lista.add(usuario);
		}

		return lista;
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

	public Usuario buscarPorId(String id) throws SQLException {

		String sql = "select * from usuario where id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));

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

	public void deletar(String idUsuario) throws SQLException {
		String sql = "delete from usuario where id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setLong(1, Long.parseLong(idUsuario));

		statement.executeUpdate();
		connection.commit();

	}

}
