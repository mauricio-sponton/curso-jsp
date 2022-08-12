package br.com.mbs.cursojsp.servlets;

import java.io.IOException;

import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UsuarioRepository usuarioRepository = new UsuarioRepository();

	public ServletUsuario() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			
			String msg = "Operação realizada com sucesso!";
			
			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");

			Usuario usuario = new Usuario();

			usuario.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			
			if(usuarioRepository.validarLogin(usuario.getLogin()) && usuario.getId() == null) {
				msg = "Já existe usuário com esse login!";
			}else {
				usuario = usuarioRepository.salvar(usuario);
			}


			request.setAttribute("msg", msg);
			request.setAttribute("usuarioSalvo", usuario);

			request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}
