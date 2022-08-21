package br.com.mbs.cursojsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.model.Telefone;

public class TelefoneRepository {
	
	private Connection connection;
	private UsuarioRepository usuarioRepository = new UsuarioRepository();
	
	public TelefoneRepository() {
		connection = SingleConnection.getConnection();
	}
	
	public void salvar(Telefone telefone) throws SQLException {
		String sql = "insert into telefone (numero, usuario_dono, usuario_logado) values (?, ?, ?)";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, telefone.getNumero());
		statement.setLong(2, telefone.getUsuarioDono().getId());
		statement.setLong(2, telefone.getUsuarioLogado().getId());
		
		statement.execute();
		connection.commit();
	}
	
	public void deletar(Long id) throws SQLException {
		
		String sql = "delete from telefone where id =?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
		
		statement.executeUpdate();
		connection.commit();
		
	}
	
	public List<Telefone> listar(Long idDono) throws SQLException{
		
		List<Telefone> lista = new ArrayList<>();
		String sql = "select * from telefone where usuario_dono =?";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultSet = statement.executeQuery();
		
		while(resultSet.next()) {
			
			Telefone telefone = new Telefone();
			telefone.setId(resultSet.getLong("id"));
			telefone.setNumero(resultSet.getString("numero"));
			telefone.setUsuarioDono(usuarioRepository.buscarPorId(resultSet.getLong("usuario_dono")));
			telefone.setUsuarioLogado(usuarioRepository.buscarPorId(resultSet.getLong("usuario_logado")));
			lista.add(telefone);
		}
		
		return lista;
	}

}
