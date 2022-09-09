package br.com.mbs.cursojsp.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.mbs.cursojsp.dao.TelefoneRepository;
import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.model.Telefone;
import br.com.mbs.cursojsp.model.Usuario;


public class ServletTelefone extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

	private UsuarioRepository usuarioRepository = new UsuarioRepository();
	private TelefoneRepository telefoneRepository = new TelefoneRepository();

	public ServletTelefone() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			String acao = request.getParameter("acao");

			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("excluir")) {

				String id = request.getParameter("id");
				String idDono = request.getParameter("idDono");

				telefoneRepository.deletar(Long.parseLong(id));
				Usuario usuario = usuarioRepository.buscarPorId(Long.parseLong(idDono));
				request.setAttribute("usuario", usuario);

				List<Telefone> lista = telefoneRepository.listar(usuario.getId());
				request.setAttribute("lista", lista);

				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
				return;
			}

			String idUsuario = request.getParameter("idUsuario");

			if (idUsuario != null && !idUsuario.isEmpty()) {

				Usuario usuario = usuarioRepository.buscarPorId(Long.parseLong(idUsuario));
				request.setAttribute("usuario", usuario);

				List<Telefone> lista = telefoneRepository.listar(usuario.getId());
				request.setAttribute("lista", lista);

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

		try {

			String idDono = request.getParameter("id");
			String numero = request.getParameter("numero");
			Usuario usuario = usuarioRepository.buscarPorId(Long.parseLong(idDono));

			if (!telefoneRepository.existeTelefone(numero, Long.parseLong(idDono))) {

				Telefone telefone = new Telefone();
				telefone.setNumero(numero);

				telefone.setUsuarioDono(usuario);
				telefone.setUsuarioLogado(super.getUsuarioLogadoObject(request));

				telefoneRepository.salvar(telefone);
				request.setAttribute("msg", "Telefone cadastrado com sucesso!");
			
			} else {
				request.setAttribute("msg", "Telefone já está cadastrado!");
			}

			List<Telefone> lista = telefoneRepository.listar(Long.parseLong(idDono));
			request.setAttribute("lista", lista);
			request.setAttribute("usuario", usuario);
			request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);

		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

}
