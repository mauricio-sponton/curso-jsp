<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="head.jsp"></jsp:include>

<body>

	<jsp:include page="theme-loader.jsp"></jsp:include>

	<div id="pcoded" class="pcoded">
		<div class="pcoded-overlay-box"></div>
		<div class="pcoded-container navbar-wrapper">

			<jsp:include page="nav.jsp"></jsp:include>

			<div class="pcoded-main-container">
				<div class="pcoded-wrapper">
					<jsp:include page="menu-lateral.jsp"></jsp:include>

					<div class="pcoded-content">
						<jsp:include page="breadcrumb.jsp"></jsp:include>
						<div class="pcoded-inner-content">
							<div class="main-body">
								<div class="page-wrapper">
									<div class="page-body">
										<div class="row">
											<span id="msg">${msg}</span>
											<div class="col-sm-12">
												<div class="card">
													<div class="card-header">
														<h5>Cadastro de telefone</h5>
													</div>
													<div class="card-block">
														<form class="form-material" id="formTelefone"
															action="<%=request.getContextPath()%>/ServletTelefone"
															method="post">

															<div class="form-group row">
																<div class="col-sm-1">
																	<div class="form-group form-default form-static-label">
																		<input type="text" name="id" id="id"
																			value="${usuario.id}" class="form-control" readonly>
																		<span class="form-bar"></span> <label
																			class="float-label">ID do usuário</label>
																	</div>

																</div>
															</div>

															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default form-static-label">
																		<input readonly="readonly" type="text" name="nome"
																			id="nome" value="${usuario.nome}"
																			class="form-control" required> <span
																			class="form-bar"></span> <label class="float-label">Nome
																			do usuário</label>
																	</div>
																</div>
															</div>

															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="numero" id="numero"
																			class="form-control" required> <span
																			class="form-bar"></span> <label class="float-label">Número</label>
																	</div>
																</div>
															</div>

															<button class="btn waves-effect waves-light btn-success">Salvar</button>

														</form>

													</div>
												</div>
											</div>

										</div>

										<div style="overflow-y: scroll; height: 600px">
											<table class="table" id="lista-usuario">
												<thead>
													<tr>
														<th scope="col">ID</th>
														<th scope="col">Número</th>
														<th scope="col">Excluir</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${lista}" var="t">
														<tr>
															<td><c:out value="${t.id}"></c:out></td>
															<td><c:out value="${t.numero}"></c:out></td>
															<td><a class="btn btn-success"
																href="<%= request.getContextPath() %>/ServletTelefone?acao=excluir&id=${t.id}&idDono=${usuario.id}">Excluir</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>


										</div>
									</div>
								</div>
								<div id="styleSelector"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="javascript.jsp"></jsp:include>
	<script type="text/javascript">
		
		$('#numero').keypress(function(event){
			return /\d/.test(String.fromCharCode(event.keyCode));
		});
	
	</script>
</body>

</html>
