package br.com.mbs.cursojsp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.dto.GraficoSalarioUsuarioDTO;
import br.com.mbs.cursojsp.model.Telefone;
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

	public List<Usuario> findAllUsuarios(Long usuarioLogado) throws SQLException {

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
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));

			usuario.setTelefones(this.listarTelefones(usuario.getId()));

			lista.add(usuario);
		}

		return lista;
	}

	public List<Usuario> findAllUsuariosByDatas(Long usuarioLogado, Date dataInicialConvertida,
			Date dataFinalConvertida) throws SQLException {

		List<Usuario> lista = new ArrayList<Usuario>();

		String sql = "select * from usuario where adm is false and usuario_logado_id = " + usuarioLogado
				+ " and data_nascimento >= ? and data_nascimento <= ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDate(1, dataInicialConvertida);
		statement.setDate(2, dataFinalConvertida);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Usuario usuario = new Usuario();
			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setEmail(resultado.getString("email"));
			usuario.setLogin(resultado.getString("login"));
			usuario.setPerfil(resultado.getString("perfil"));
			usuario.setSexo(resultado.getString("sexo"));
			usuario.setDataNascimento(resultado.getDate("data_nascimento"));

			usuario.setTelefones(this.listarTelefones(usuario.getId()));

			lista.add(usuario);
		}

		return lista;
	}

	public List<Telefone> listarTelefones(Long idDono) throws SQLException {

		List<Telefone> lista = new ArrayList<>();
		String sql = "select * from telefone where usuario_dono =?";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setLong(1, idDono);

		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {

			Telefone telefone = new Telefone();
			telefone.setId(resultSet.getLong("id"));
			telefone.setNumero(resultSet.getString("numero"));
			telefone.setUsuarioDono(this.buscarPorId(resultSet.getLong("usuario_dono")));
			telefone.setUsuarioLogado(this.buscarPorId(resultSet.getLong("usuario_logado")));
			lista.add(telefone);
		}

		return lista;
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

	public GraficoSalarioUsuarioDTO graficoMediaSalarial(Long usuarioLogado) throws SQLException {

		String sql = "select avg(renda_mensal) as media_salarial, perfil from usuario where usuario_logado_id = ? group by perfil";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, usuarioLogado);

		ResultSet resultado = statement.executeQuery();

		List<String> perfis = new ArrayList<>();
		List<Double> salarios = new ArrayList<>();

		GraficoSalarioUsuarioDTO dto = new GraficoSalarioUsuarioDTO();

		while (resultado.next()) {
			Double medialSalarial = resultado.getDouble("media_salarial");
			String perfil = resultado.getString("perfil");

			perfis.add(perfil);
			salarios.add(medialSalarial);
		}

		dto.setPerfis(perfis);
		dto.setSalarios(salarios);

		return dto;

	}

	public GraficoSalarioUsuarioDTO graficoMediaSalarial(Long usuarioLogado, Date dataInicialConvertida,
			Date dataFinalConvertida) throws SQLException {

		String sql = "select avg(renda_mensal) as media_salarial, perfil from usuario where usuario_logado_id = ? and data_nascimento >= ? and data_nascimento <= ? group by perfil";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, usuarioLogado);
		statement.setDate(2, dataInicialConvertida);
		statement.setDate(3, dataFinalConvertida);

		ResultSet resultado = statement.executeQuery();

		List<String> perfis = new ArrayList<>();
		List<Double> salarios = new ArrayList<>();

		GraficoSalarioUsuarioDTO dto = new GraficoSalarioUsuarioDTO();

		while (resultado.next()) {
			Double medialSalarial = resultado.getDouble("media_salarial");
			String perfil = resultado.getString("perfil");

			perfis.add(perfil);
			salarios.add(medialSalarial);
		}

		dto.setPerfis(perfis);
		dto.setSalarios(salarios);

		return dto;

	}

}
