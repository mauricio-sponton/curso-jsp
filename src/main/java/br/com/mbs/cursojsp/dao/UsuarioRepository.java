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

			String sql = "insert into usuario (login, senha, nome, email, usuario_logado_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, data_nascimento, renda_mensal) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());
			statement.setLong(5, usuarioLogado);
			statement.setString(6, usuario.getPerfil());
			statement.setString(7, usuario.getSexo());
			statement.setString(8, usuario.getCep());
			statement.setString(9, usuario.getLogradouro());
			statement.setString(10, usuario.getBairro());
			statement.setString(11, usuario.getLocalidade());
			statement.setString(12, usuario.getUf());
			statement.setString(13, usuario.getNumero());
			statement.setDate(14, usuario.getDataNascimento());
			statement.setDouble(15, usuario.getRendaMensal());

			statement.execute();

			connection.commit();

			if (usuario.getFoto() != null && !usuario.getFoto().isEmpty()) {
				sql = "update usuario set foto=?, extensao_foto=? where login=?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, usuario.getFoto());
				statement.setString(2, usuario.getExtensaoFoto());
				statement.setString(3, usuario.getLogin());

				statement.execute();

				connection.commit();
			}

		} else {

			String sql = "update usuario set login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, data_nascimento=?, renda_mensal=? where id= "
					+ usuario.getId() + "";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());
			statement.setString(5, usuario.getPerfil());
			statement.setString(6, usuario.getSexo());
			statement.setString(7, usuario.getCep());
			statement.setString(8, usuario.getLogradouro());
			statement.setString(9, usuario.getBairro());
			statement.setString(10, usuario.getLocalidade());
			statement.setString(11, usuario.getUf());
			statement.setString(12, usuario.getNumero());
			statement.setDate(13, usuario.getDataNascimento());
			statement.setDouble(14, usuario.getRendaMensal());

			statement.executeUpdate();

			connection.commit();

			if (usuario.getFoto() != null && !usuario.getFoto().isEmpty()) {
				sql = "update usuario set foto=?, extensao_foto=? where id=?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, usuario.getFoto());
				statement.setString(2, usuario.getExtensaoFoto());
				statement.setLong(3, usuario.getId());

				statement.execute();

				connection.commit();
			}
		}

		return this.buscarPorLoginEUsuarioLogado(usuario.getLogin(), usuarioLogado);
	}

	public List<Usuario> listarUsuariosPaginado(Long usuarioLogado, Integer offset) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where adm is false and usuario_logado_id = " + usuarioLogado
				+ " order by nome offset " + offset + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();
			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));

			lista.add(usuario);
		}

		return lista;
	}

	public int totalPaginas(Long usuarioLogado) throws SQLException {

		String sql = "select count(1) as total from usuario where usuario_logado_id = " + usuarioLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();
		resultado.next();

		Double totalRegistros = resultado.getDouble("total");
		Double quantidadePorPagina = 5.0;
		Double pagina = totalRegistros / quantidadePorPagina;
		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();
	}

	public List<Usuario> listarUsuarios(Long usuarioLogado) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where adm is false and usuario_logado_id = " + usuarioLogado + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();
			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));

			lista.add(usuario);
		}

		return lista;
	}

	public List<Usuario> conultarUsuariosPorNome(String nome, Long usuarioLogado) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where upper(nome) like upper(?) and adm is false and usuario_logado_id = ? order by id  limit 5";
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
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));

			lista.add(usuario);
		}

		return lista;
	}

	public List<Usuario> conultarUsuariosPorNomeOffset(String nome, Long usuarioLogado, Integer offset)
			throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where upper(nome) like upper(?) and adm is false and usuario_logado_id = ? order by id offset "
				+ offset + " limit 5";
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
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));

			lista.add(usuario);
		}

		return lista;
	}

	public int conultarUsuariosPorNomePaginado(String nome, Long usuarioLogado) throws SQLException {

		String sql = "select count(1) as total from usuario where upper(nome) like upper(?) and adm is false and usuario_logado_id = ? ";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, usuarioLogado);

		ResultSet resultado = statement.executeQuery();
		resultado.next();

		Double totalRegistros = resultado.getDouble("total");
		Double quantidadePorPagina = 5.0;
		Double pagina = totalRegistros / quantidadePorPagina;
		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();
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
			usuario.setAdm(resultado.getBoolean("adm"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setFoto(resultado.getString("foto"));
			usuario.setCep(resultado.getString("cep"));
			usuario.setLogradouro(resultado.getString("logradouro"));
			usuario.setBairro(resultado.getString("bairro"));
			usuario.setLocalidade(resultado.getString("localidade"));
			usuario.setUf(resultado.getString("uf"));
			usuario.setNumero(resultado.getString("numero"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));
			usuario.setRendaMensal(resultado.getDouble("renda_mensal"));
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
			usuario.setAdm(resultado.getBoolean("adm"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setFoto(resultado.getString("foto"));
			usuario.setCep(resultado.getString("cep"));
			usuario.setLogradouro(resultado.getString("logradouro"));
			usuario.setBairro(resultado.getString("bairro"));
			usuario.setLocalidade(resultado.getString("localidade"));
			usuario.setUf(resultado.getString("uf"));
			usuario.setNumero(resultado.getString("numero"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));
			usuario.setRendaMensal(resultado.getDouble("renda_mensal"));
		}

		return usuario;
	}

	public Usuario buscarPorLoginEUsuarioLogado(String login, Long usuarioLogado) throws SQLException {

		String sql = "select * from usuario where upper(login) = upper('" + login
				+ "') and adm is false and usuario_logado_id = " + usuarioLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		Usuario usuario = new Usuario();

		while (resultado.next()) {

			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setFoto(resultado.getString("foto"));
			usuario.setCep(resultado.getString("cep"));
			usuario.setLogradouro(resultado.getString("logradouro"));
			usuario.setBairro(resultado.getString("bairro"));
			usuario.setLocalidade(resultado.getString("localidade"));
			usuario.setUf(resultado.getString("uf"));
			usuario.setNumero(resultado.getString("numero"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));
			usuario.setRendaMensal(resultado.getDouble("renda_mensal"));
		}

		return usuario;
	}

	public Usuario buscarPorId(Long id) throws SQLException {

		String sql = "select * from usuario where id = ? and adm is false";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);

		ResultSet resultado = statement.executeQuery();

		Usuario usuario = new Usuario();

		while (resultado.next()) {

			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setFoto(resultado.getString("foto"));
			usuario.setExtensaoFoto(resultado.getString("extensao_foto"));
			usuario.setCep(resultado.getString("cep"));
			usuario.setLogradouro(resultado.getString("logradouro"));
			usuario.setBairro(resultado.getString("bairro"));
			usuario.setLocalidade(resultado.getString("localidade"));
			usuario.setUf(resultado.getString("uf"));
			usuario.setNumero(resultado.getString("numero"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));
			usuario.setRendaMensal(resultado.getDouble("renda_mensal"));
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
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setFoto(resultado.getString("foto"));
			usuario.setExtensaoFoto(resultado.getString("extensao_foto"));
			usuario.setCep(resultado.getString("cep"));
			usuario.setLogradouro(resultado.getString("logradouro"));
			usuario.setBairro(resultado.getString("bairro"));
			usuario.setLocalidade(resultado.getString("localidade"));
			usuario.setUf(resultado.getString("uf"));
			usuario.setNumero(resultado.getString("numero"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));
			usuario.setRendaMensal(resultado.getDouble("renda_mensal"));
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
