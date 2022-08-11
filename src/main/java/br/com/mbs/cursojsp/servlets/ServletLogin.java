package br.com.mbs.cursojsp.servlets;

import java.io.IOException;

import br.com.mbs.cursojsp.model.Login;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletLogin() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");

		if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {

			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			request.setAttribute("msg", "Informe o login e senha corretos!");
			redirecionar.forward(request, response);
			
		}
		
		var loginModel = new Login();
		loginModel.setLogin(login);
		loginModel.setSenha(senha);

	}

}
