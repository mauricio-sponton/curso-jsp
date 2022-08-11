package br.com.mbs.cursojsp.servlets;

import java.io.IOException;

import br.com.mbs.cursojsp.dao.LoginRepository;
import br.com.mbs.cursojsp.model.Login;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/principal/ServletLogin" })
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LoginRepository loginRepository = new LoginRepository();

	public ServletLogin() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");

		try {

			if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {

				var loginModel = new Login();
				loginModel.setLogin(login);
				loginModel.setSenha(senha);

				if (loginRepository.validarAutenticacao(loginModel)) {
					request.getSession().setAttribute("usuario", loginModel.getLogin());

					if (url == null || url.equals("null")) {
						url = "principal/principal.jsp";
					}

					RequestDispatcher redirecionar = request.getRequestDispatcher(url);
					redirecionar.forward(request, response);
				} else {
					redirecionarPaginaLogin(request, response);
				}
			} else {
				redirecionarPaginaLogin(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void redirecionarPaginaLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
		request.setAttribute("msg", "Informe o login e senha corretos!");
		redirecionar.forward(request, response);
	}
}
