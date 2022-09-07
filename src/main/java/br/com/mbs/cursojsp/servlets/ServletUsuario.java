package br.com.mbs.cursojsp.servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.jakartaee.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.dto.GraficoSalarioUsuarioDTO;
import br.com.mbs.cursojsp.model.Usuario;
import br.com.mbs.cursojsp.util.ReportUtils;
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
				List<Usuario> lista = usuarioRepository.conultarUsuariosPorNome(nomeBusca,
						super.getUsuarioLogado(request));

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(lista);

				response.addHeader("totalPagina", "" + usuarioRepository.conultarUsuariosPorNomePaginado(nomeBusca,
						super.getUsuarioLogado(request)));
				response.getWriter().write(json);

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUsuarioPaginado")) {
				String nomeBusca = request.getParameter("nomeBusca");
				String pagina = request.getParameter("pagina");
				List<Usuario> lista = usuarioRepository.conultarUsuariosPorNomeOffset(nomeBusca,
						super.getUsuarioLogado(request), Integer.parseInt(pagina));

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(lista);

				response.addHeader("totalPagina", "" + usuarioRepository.conultarUsuariosPorNomePaginado(nomeBusca,
						super.getUsuarioLogado(request)));
				response.getWriter().write(json);
			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("editar")) {
				String id = request.getParameter("id");

				Usuario usuario = usuarioRepository.buscarPorId(id, super.getUsuarioLogado(request));

				request.setAttribute("msg", "Atualize as informações clicando em salvar");
				request.setAttribute("usuarioSalvo", usuario);
				request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));

				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listar")) {

				List<Usuario> lista = usuarioRepository.listarUsuarios(super.getUsuarioLogado(request));

				request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));
				request.setAttribute("lista", lista);

				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {

				String idUsuario = request.getParameter("id");

				Usuario usuario = usuarioRepository.buscarPorId(idUsuario, super.getUsuarioLogado(request));

				if (usuario.getFoto() != null && !usuario.getFoto().isEmpty()) {

					response.setHeader("Content-Disposition",
							"attachment;filename=arquivo." + usuario.getExtensaoFoto());
					new Base64();
					response.getOutputStream().write(Base64.decodeBase64(usuario.getFoto().split(",")[1]));
				}

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("paginar")) {
				Integer offset = Integer.parseInt(request.getParameter("pagina"));
				List<Usuario> lista = usuarioRepository.listarUsuariosPaginado(getUsuarioLogado(request), offset);

				request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));
				request.setAttribute("lista", lista);

				request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorio")) {

				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");

				request.setAttribute("listaUsuario",
						usuarioRepository.findAllUsuarios(super.getUsuarioLogado(request)));

				if (dataInicial != null && !dataInicial.isEmpty() && dataFinal != null && !dataFinal.isEmpty()) {

					Date dataInicialConvertida = converterData(dataInicial);
					Date dataFinalConvertida = converterData(dataFinal);
					request.setAttribute("listaUsuario", usuarioRepository.findAllUsuariosByDatas(
							super.getUsuarioLogado(request), dataInicialConvertida, dataFinalConvertida));

				}

				request.setAttribute("dataInicial", dataInicial);
				request.setAttribute("dataFinal", dataFinal);
				request.getRequestDispatcher("principal/relatorio-usuario.jsp").forward(request, response);
			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirPdf") || acao.equalsIgnoreCase("imprimirExcel")) {

				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");

				List<Usuario> lista = usuarioRepository.findAllUsuarios(super.getUsuarioLogado(request));

				if (dataInicial != null && !dataInicial.isEmpty() && dataFinal != null && !dataFinal.isEmpty()) {

					Date dataInicialConvertida = converterData(dataInicial);
					Date dataFinalConvertida = converterData(dataFinal);

					lista = usuarioRepository.findAllUsuariosByDatas(super.getUsuarioLogado(request),
							dataInicialConvertida, dataFinalConvertida);

				}
				
				HashMap<String, Object> params = new HashMap<>();
				params.put("PARAM_SUBREPORT", request.getServletContext().getRealPath("relatorios") + File.separator);
				
				
				byte[] relatorio = null;
				String extensao = "";
				
				if(acao.equalsIgnoreCase("imprimirPdf")) {
					
					relatorio = new ReportUtils().geraRelatorioPDF(lista, "relatorio-usuario", params,
							request.getServletContext());
					extensao = ".pdf";
				}else if(acao.equalsIgnoreCase("imprimirExcel")) {
					relatorio = new ReportUtils().geraRelatorioExcel(lista, "relatorio-usuario", params,
							request.getServletContext());
					extensao = ".xls";
				}
				
				
				response.setHeader("Content-Disposition", "attachment;filename=arquivo" + extensao);
				new Base64();
				response.getOutputStream().write(relatorio);

			}
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("graficoMediaSalarial")) {
				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");
				System.out.println(dataInicial);
				
				GraficoSalarioUsuarioDTO dto = usuarioRepository.graficoMediaSalarial(getUsuarioLogado(request));
				
				if (dataInicial != null && !dataInicial.isEmpty() && dataFinal != null && !dataFinal.isEmpty()) {
					System.out.println("AQUI");
					Date dataInicialConvertida = converterData(dataInicial);
					Date dataFinalConvertida = converterData(dataFinal);
					dto = usuarioRepository.graficoMediaSalarial(getUsuarioLogado(request), dataInicialConvertida ,dataFinalConvertida);

				}
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dto);
				response.getWriter().write(json);
				
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
			String cep = request.getParameter("cep");
			String logradouro = request.getParameter("logradouro");
			String bairro = request.getParameter("bairro");
			String localidade = request.getParameter("localidade");
			String uf = request.getParameter("uf");
			String numero = request.getParameter("numero");
			String dataNascimento = request.getParameter("dataNascimento");
			String rendaMensal = request.getParameter("rendaMensal");

			rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.", "").replaceAll("\\,", ".");
			Date dataConvertida = converterData(dataNascimento);

			Usuario usuario = new Usuario();

			usuario.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setPerfil(perfil);
			usuario.setSexo(sexo);
			usuario.setCep(cep);
			usuario.setLogradouro(logradouro);
			usuario.setBairro(bairro);
			usuario.setLocalidade(localidade);
			usuario.setUf(uf);
			usuario.setNumero(numero);
			usuario.setDataNascimento(dataConvertida);
			usuario.setRendaMensal(Double.parseDouble(rendaMensal));

			if (ServletFileUpload.isMultipartContent(request)) {
				Part part = request.getPart("fileFoto");

				if (part.getSize() > 0) {

					byte[] foto = IOUtils.toByteArray(part.getInputStream());
					new Base64();
					String imagemBase64 = "data:image/" + part.getContentType().split("/")[1] + ";base64,"
							+ Base64.encodeBase64String(foto);

					usuario.setFoto(imagemBase64);
					usuario.setExtensaoFoto(part.getContentType().split("/")[1]);
				}

			}

			if (usuarioRepository.validarLogin(usuario.getLogin()) && usuario.getId() == null) {
				msg = "Já existe usuário com esse login!";
			} else {
				usuario = usuarioRepository.salvar(usuario, super.getUsuarioLogado(request));
			}

			List<Usuario> lista = usuarioRepository.listarUsuarios(getUsuarioLogado(request));

			request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));
			request.setAttribute("lista", lista);

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

	private Date converterData(String dataNascimento) throws ParseException {
		return Date.valueOf(
				new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataNascimento)));
	}

}
