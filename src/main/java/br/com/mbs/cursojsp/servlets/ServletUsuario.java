package br.com.mbs.cursojsp.servlets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		try {
			String acao = request.getParameter("acao");

			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				String idUsuario = request.getParameter("id");

				usuarioRepository.deletar(idUsuario);
				request.setAttribute("msg", "Usuário excluido com sucesso!");
				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar-ajax")) {
				String idUsuario = request.getParameter("id");

				usuarioRepository.deletar(idUsuario);
				response.getWriter().write("Excluído com sucesso");
			
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUsuario")) {
				String nomeBusca = request.getParameter("nomeBusca");
				List<Usuario> lista = usuarioRepository.conultarUsuariosPorNome(nomeBusca);
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(lista);
				response.getWriter().write(json);
				
			
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("editar")) {
				String id = request.getParameter("id");
				
				Usuario usuario = usuarioRepository.buscarPorId(id);
				
				request.setAttribute("msg", "Atualize as informações clicando em salvar");
				request.setAttribute("usuarioSalvo", usuario);

				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
				
			
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listar")) {
				
				List<Usuario> lista = usuarioRepository.listarUsuarios();
				
				request.setAttribute("lista", lista);
				
				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
				
			
			}
			
			else {
				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
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

			if (usuarioRepository.validarLogin(usuario.getLogin()) && usuario.getId() == null) {
				msg = "Já existe usuário com esse login!";
			} else {
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
