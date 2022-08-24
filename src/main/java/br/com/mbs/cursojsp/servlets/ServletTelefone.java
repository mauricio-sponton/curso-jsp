package br.com.mbs.cursojsp.servlets;

import java.io.IOException;
import java.util.List;

import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletTelefone extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

	private UsuarioRepository usuarioRepository = new UsuarioRepository();

	public ServletTelefone() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			String idUsuario = request.getParameter("idUsuario");

			if (idUsuario != null && !idUsuario.isEmpty()) {

				Usuario usuario = usuarioRepository.buscarPorId(Long.parseLong(idUsuario));
				request.setAttribute("usuario", usuario);
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);

			} else {
				List<Usuario> lista = usuarioRepository.listarUsuarios(super.getUsuarioLogado(request));

				request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));
				request.setAttribute("lista", lista);

				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
