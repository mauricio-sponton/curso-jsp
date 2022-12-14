package br.com.mbs.cursojsp.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.mbs.cursojsp.dao.LoginRepository;
import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.model.Usuario;


@WebServlet(urlPatterns = { "/principal/ServletLogin" })
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PAGINA_LOGIN = "/index.jsp";
	private static final String ERRO_LOGIN = "Informe o login e senha correto!";

	private LoginRepository loginRepository = new LoginRepository();
	private UsuarioRepository usuarioRepository = new UsuarioRepository();

	public ServletLogin() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String acao = request.getParameter("acao");
		
		
		if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
			request.getSession().invalidate();
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);
		}else {
			
			doPost(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");

		try {

			if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {

				Usuario usuario = new Usuario();
				usuario.setLogin(login);
				usuario.setSenha(senha);

				if (loginRepository.validarAutenticacao(usuario)) {
					
					usuario = usuarioRepository.buscarLogado(login);
					
					request.getSession().setAttribute("usuario", usuario.getLogin());
					request.getSession().setAttribute("perfil", usuario.getPerfil());
					request.getSession().setAttribute("foto", usuario.getFoto());

					if (url == null || url.equals("null")) {
						url = "principal/principal.jsp";
					}

					RequestDispatcher redirecionar = request.getRequestDispatcher(url);
					redirecionar.forward(request, response);
				} else {
					redirecionarPagina(request, response, PAGINA_LOGIN, ERRO_LOGIN);
				}
			} else {
				redirecionarPagina(request, response, PAGINA_LOGIN, ERRO_LOGIN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirecionarPagina(request, response, "/erro.jsp", e.getMessage());
		}

	}
	
	

	private void redirecionarPagina(HttpServletRequest request, HttpServletResponse response, String pagina, String erro)
			throws ServletException, IOException {
		RequestDispatcher redirecionar = request.getRequestDispatcher(pagina);
		request.setAttribute("msg", erro);
		redirecionar.forward(request, response);
	}
}
