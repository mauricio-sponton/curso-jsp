package br.com.mbs.cursojsp.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import br.com.mbs.cursojsp.connection.SingleConnection;
import br.com.mbs.cursojsp.dao.VersionadorBDRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/principal/*" })
public class AutenticacaoFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;

	private static Connection connection;

	public AutenticacaoFilter() {
	}

	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();

			String usuarioLogado = (String) session.getAttribute("usuario");
			String urlParaAutenticar = req.getServletPath();

			if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {

				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg", "Por favor, realize o login!");
				redireciona.forward(request, response);
				return;

			} else {
				chain.doFilter(request, response);
			}

			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();

			RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);

			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnection.getConnection();
		VersionadorBDRepository versionadorBDRepository = new VersionadorBDRepository();

		String pastaSQL = fConfig.getServletContext().getRealPath("versionador-sql") + File.separator;

		File[] files = new File(pastaSQL).listFiles();

		try {
			for (File file : files) {
				boolean isArquivoExecutado = versionadorBDRepository.arquivoSQLExecutado(file.getName());
				if(!isArquivoExecutado) {
					
					FileInputStream entrada = new FileInputStream(file);
					Scanner lerArquivo = new Scanner(entrada, "UTF-8");
					
					StringBuilder sql = new StringBuilder();
					
					while(lerArquivo.hasNext()) {
						sql.append(lerArquivo.nextLine());
						sql.append("\n");
					}
					connection.prepareStatement(sql.toString()).execute();
					versionadorBDRepository.gravar(file.getName());
					connection.commit();
					lerArquivo.close();
				}
			}
		} catch (SQLException | FileNotFoundException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
