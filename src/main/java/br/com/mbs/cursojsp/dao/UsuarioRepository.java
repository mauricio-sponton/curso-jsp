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

	public Usuario salvar(Usuario usuario, Long usuarioLogado) throws SQLException {

		if (usuario.naoExiste()) {

			String sql = "insert into usuario (login, senha, nome, email, usuario_logado_id) values (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());
			statement.setLong(5, usuarioLogado);

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

		return this.buscarPorLoginEUsuarioLogado(usuario.getLogin(), usuarioLogado);
	}

	public List<Usuario> listarUsuarios(Long usuarioLogado) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where adm is false and usuario_logado_id = " + usuarioLogado;
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

	public List<Usuario> conultarUsuariosPorNome(String nome, Long usuarioLogado) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where upper(nome) like upper(?) and adm is false and usuario_logado_id = ? order by id";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, usuarioLogado);

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

		String sql = "select * from usuario where upper(login) = upper('" + login + "') and adm is false";
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
	
	public Usuario buscarLogado(String login) throws SQLException {

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
	

	public Usuario buscarPorLoginEUsuarioLogado(String login, Long usuarioLogado) throws SQLException {

		String sql = "select * from usuario where upper(login) = upper('" + login + "') and adm is false and usuario_logado_id = " + usuarioLogado;
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

	public Usuario buscarPorId(String id, Long usuarioLogado) throws SQLException {

		String sql = "select * from usuario where id = ? and adm is false and usuario_logado_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, usuarioLogado);

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
		String sql = "delete from usuario where id = ? and adm is false";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setLong(1, Long.parseLong(idUsuario));

		statement.executeUpdate();
		connection.commit();

	}

}
