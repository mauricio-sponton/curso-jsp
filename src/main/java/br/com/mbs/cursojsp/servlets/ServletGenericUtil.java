package br.com.mbs.cursojsp.servlets;

import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.model.Usuario;


public class ServletGenericUtil extends HttpServlet implements Serializable {
	private static final long serialVersionUID = 1L;

	private UsuarioRepository usuarioRepository = new UsuarioRepository();

	public Long getUsuarioLogado(HttpServletRequest request) throws SQLException {

		HttpSession session = request.getSession();
		String usuarioLogado = (String) session.getAttribute("usuario");

		return usuarioRepository.buscarLogado(usuarioLogado).getId();
	}

	public Usuario getUsuarioLogadoObject(HttpServletRequest request) throws SQLException {

		HttpSession session = request.getSession();
		String usuarioLogado = (String) session.getAttribute("usuario");

		return usuarioRepository.buscarLogado(usuarioLogado);
	}

}
