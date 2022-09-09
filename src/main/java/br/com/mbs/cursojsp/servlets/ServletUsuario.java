package br.com.mbs.cursojsp.servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.util.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mbs.cursojsp.dao.UsuarioRepository;
import br.com.mbs.cursojsp.dto.GraficoSalarioUsuarioDTO;
import br.com.mbs.cursojsp.model.Usuario;
import br.com.mbs.cursojsp.util.ReportUtils;
import net.sf.jasperreports.engine.JRException;

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

			if (acao != null && !acao.isEmpty()) {

				switch (acao) {
				case "deletar":
					deletar(request, response);
					break;
				case "deletar-ajax":
					deletarViaAjax(request, response);
					break;
				case "buscarUsuario":
					buscarUsuario(request, response);
					break;
				case "buscarUsuarioPaginado":
					buscarUsuarioPaginado(request, response);
					break;
				case "editar":
					editar(request, response);
					break;
				case "listar":
					listar(request, response);
					break;
				case "downloadFoto":
					downloadFoto(request, response);
					break;
				case "paginar":
					paginar(request, response);
					break;
				case "imprimirRelatorio":
					imprimirRelatorio(request, response);
					break;
				case "imprimirPdf":
				case "imprimirExcel":
					imprimirPdfOuExcel(request, response, acao);
					break;
				case "graficoMediaSalarial":
					graficoMediaSalarial(request, response);
					break;
				default:
					request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
					break;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

	private void graficoMediaSalarial(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ParseException, JsonProcessingException, IOException {
		String dataInicial = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");

		GraficoSalarioUsuarioDTO dto = usuarioRepository.graficoMediaSalarial(getUsuarioLogado(request));

		if (dataInicial != null && !dataInicial.isEmpty() && dataFinal != null && !dataFinal.isEmpty()) {
			Date dataInicialConvertida = converterData(dataInicial);
			Date dataFinalConvertida = converterData(dataFinal);
			dto = usuarioRepository.graficoMediaSalarial(getUsuarioLogado(request), dataInicialConvertida,
					dataFinalConvertida);

		}

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dto);
		response.getWriter().write(json);
	}

	private void imprimirPdfOuExcel(HttpServletRequest request, HttpServletResponse response, String acao)
			throws SQLException, ParseException, JRException, IOException {
		String dataInicial = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");

		List<Usuario> lista = usuarioRepository.findAllUsuarios(super.getUsuarioLogado(request));

		if (dataInicial != null && !dataInicial.isEmpty() && dataFinal != null && !dataFinal.isEmpty()) {

			Date dataInicialConvertida = converterData(dataInicial);
			Date dataFinalConvertida = converterData(dataFinal);

			lista = usuarioRepository.findAllUsuariosByDatas(super.getUsuarioLogado(request), dataInicialConvertida,
					dataFinalConvertida);

		}

		HashMap<String, Object> params = new HashMap<>();
		params.put("PARAM_SUBREPORT", request.getServletContext().getRealPath("relatorios") + File.separator);

		byte[] relatorio = null;
		String extensao = "";

		if (acao.equalsIgnoreCase("imprimirPdf")) {

			relatorio = new ReportUtils().geraRelatorioPDF(lista, "relatorio-usuario", params,
					request.getServletContext());
			extensao = ".pdf";
		} else if (acao.equalsIgnoreCase("imprimirExcel")) {
			relatorio = new ReportUtils().geraRelatorioExcel(lista, "relatorio-usuario", params,
					request.getServletContext());
			extensao = ".xls";
		}

		response.setHeader("Content-Disposition", "attachment;filename=arquivo" + extensao);
		new Base64();
		response.getOutputStream().write(relatorio);
	}

	private void imprimirRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ParseException, ServletException, IOException {
		String dataInicial = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");

		request.setAttribute("listaUsuario", usuarioRepository.findAllUsuarios(super.getUsuarioLogado(request)));

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

	private void paginar(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		Integer offset = Integer.parseInt(request.getParameter("pagina"));
		List<Usuario> lista = usuarioRepository.listarUsuariosPaginado(getUsuarioLogado(request), offset);

		request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));
		request.setAttribute("lista", lista);

		request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
	}

	private void downloadFoto(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String idUsuario = request.getParameter("id");

		Usuario usuario = usuarioRepository.buscarPorId(idUsuario, super.getUsuarioLogado(request));

		if (usuario.getFoto() != null && !usuario.getFoto().isEmpty()) {

			response.setHeader("Content-Disposition", "attachment;filename=arquivo." + usuario.getExtensaoFoto());
			new Base64();
			response.getOutputStream().write(Base64.decodeBase64(usuario.getFoto().split(",")[1]));
		}
	}

	private void listar(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		List<Usuario> lista = usuarioRepository.listarUsuarios(super.getUsuarioLogado(request));

		request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));
		request.setAttribute("lista", lista);

		request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
	}

	private void editar(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		String id = request.getParameter("id");

		Usuario usuario = usuarioRepository.buscarPorId(id, super.getUsuarioLogado(request));

		request.setAttribute("msg", "Atualize as informações clicando em salvar");
		request.setAttribute("usuarioSalvo", usuario);
		request.setAttribute("totalPagina", usuarioRepository.totalPaginas(getUsuarioLogado(request)));

		request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
	}

	private void buscarUsuarioPaginado(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, JsonProcessingException, IOException {
		String nomeBusca = request.getParameter("nomeBusca");
		String pagina = request.getParameter("pagina");
		List<Usuario> lista = usuarioRepository.conultarUsuariosPorNomeOffset(nomeBusca,
				super.getUsuarioLogado(request), Integer.parseInt(pagina));

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(lista);

		response.addHeader("totalPagina",
				"" + usuarioRepository.conultarUsuariosPorNomePaginado(nomeBusca, super.getUsuarioLogado(request)));
		response.getWriter().write(json);
	}

	private void buscarUsuario(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, JsonProcessingException, IOException {
		String nomeBusca = request.getParameter("nomeBusca");
		List<Usuario> lista = usuarioRepository.conultarUsuariosPorNome(nomeBusca, super.getUsuarioLogado(request));

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(lista);

		response.addHeader("totalPagina",
				"" + usuarioRepository.conultarUsuariosPorNomePaginado(nomeBusca, super.getUsuarioLogado(request)));
		response.getWriter().write(json);
	}

	private void deletarViaAjax(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String idUsuario = request.getParameter("id");

		usuarioRepository.deletar(idUsuario);
		response.getWriter().write("Excluído com sucesso");
	}

	private void deletar(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		String idUsuario = request.getParameter("id");

		usuarioRepository.deletar(idUsuario);
		request.setAttribute("msg", "Usuário excluido com sucesso!");
		request.getRequestDispatcher("principal/cadastro-usuario.jsp").forward(request, response);
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
