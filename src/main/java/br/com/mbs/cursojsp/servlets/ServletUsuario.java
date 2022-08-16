package br.com.mbs.cursojsp.servlets;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.jakartaee.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


@MultipartConfig
public class ServletUsuario extends ServletGenericUtil {
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
				List<Usuario> lista = usuarioRepository.conultarUsuariosPorNome(nomeBusca, super.getUsuarioLogado(request));
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(lista);
				response.getWriter().write(json);
				
			
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("editar")) {
				String id = request.getParameter("id");
				
				Usuario usuario = usuarioRepository.buscarPorId(id, super.getUsuarioLogado(request));
				
				request.setAttribute("msg", "Atualize as informações clicando em salvar");
				request.setAttribute("usuarioSalvo", usuario);

				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
				
			
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listar")) {
				
				List<Usuario> lista = usuarioRepository.listarUsuarios(super.getUsuarioLogado(request));
				
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
			String perfil = request.getParameter("perfil");
			String sexo = request.getParameter("sexo");

			Usuario usuario = new Usuario();

			usuario.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setPerfil(perfil);
			usuario.setSexo(sexo);
			
			if(ServletFileUpload.isMultipartContent(request)) {
				Part part = request.getPart("fileFoto");
				
				if(part.getSize() > 0) {
					
					byte[] foto = IOUtils.toByteArray(part.getInputStream());
					new Base64();
					String imagemBase64 = "data:image/" + part.getContentType().split("/")[1] + ";base64," + Base64.encodeBase64String(foto);
					
					usuario.setFoto(imagemBase64);
					usuario.setExtensaoFoto(part.getContentType().split("/")[1]);
				}
				
			}

			if (usuarioRepository.validarLogin(usuario.getLogin()) && usuario.getId() == null) {
				msg = "Já existe usuário com esse login!";
			} else {
				usuario = usuarioRepository.salvar(usuario, super.getUsuarioLogado(request));
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
