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
														<h5>Cadastro de usuário</h5>
													</div>
													<div class="card-block">
														<form class="form-material" id="formUsuario" enctype="multipart/form-data"
															action="<%=request.getContextPath()%>/ServletUsuario"
															method="post">
															<input type="hidden" name="acao" id="acao">
															<div class="form-group row">
																<div class="col-sm-1">
																	<div class="form-group form-default form-static-label">
																		<input type="text" name="id" id="id"
																			value="${usuarioSalvo.id}" class="form-control"
																			readonly> <span class="form-bar"></span> <label
																			class="float-label">ID</label>
																	</div>

																</div>

																<div class="col-sm-10">
																	<input type="radio" name="sexo" id="masculino" value="MASCULINO" ${usuarioSalvo.sexo == 'MASCULINO' ? 'checked' : ''}>
																	<label for="masculino">Masculino</label> <input
																		type="radio" name="sexo" id="feminino" value="FEMININO" ${usuarioSalvo.sexo == 'FEMININO' ? 'checked' : ''}> <label
																		for="feminino">Feminino</label>
																</div>


															</div>
															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="nome" id="nome"
																			value="${usuarioSalvo.nome}" class="form-control"
																			required> <span class="form-bar"></span> <label
																			class="float-label">Nome</label>
																	</div>
																</div>
															</div>


															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="email" name="email" id="email"
																			value="${usuarioSalvo.email}" class="form-control"
																			required> <span class="form-bar"></span> <label
																			class="float-label">Email</label>
																	</div>

																</div>
															</div>

															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="login" id="login"
																			value="${usuarioSalvo.login}" class="form-control"
																			required> <span class="form-bar"></span> <label
																			class="float-label">Login</label>
																	</div>

																</div>
															</div>
															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="senha" id="senha"
																			value="${usuarioSalvo.senha}" class="form-control"
																			required> <span class="form-bar"></span> <label
																			class="float-label">Senha</label>
																	</div>
																</div>
															</div>
															
															<div class="form-group row">
																<div class="col-sm-12">
																
																<div class="form-group form-default input-group">
																
																	<div class="input-group-prepend">
																		<c:if test="${usuarioSalvo.foto != '' && usuarioSalvo.foto != null}">
																			<a href="<%= request.getContextPath() %>/ServletUsuario?acao=downloadFoto&id=${usuarioSalvo.id}">
																				<img alt="Imagem do usuário" src="${usuarioSalvo.foto}" width="60px" id="fotoBase64"></a>
																		</c:if>
																		<c:if test="${usuarioSalvo.foto == '' || usuarioSalvo.foto == null}">
																			<img alt="Imagem do usuário" src="<%= request.getContextPath() %>/assets/images/upload.svg" width="60px" id="fotoBase64">
																		</c:if>
																	</div>
																	<input type="file" class="form-control-file ml-4" accept="image/*" onchange="visualizarImg('fotoBase64', 'fileFoto')" id="fileFoto" name="fileFoto">
																</div>
																</div></div>
															
															<div class="form-group row">
																<div class="col-sm-12">
																	<select class="form-control" name="perfil" id="perfil"
																		aria-label="Default select example">
																		<option disabled="disabled">Selecione o
																			perfil</option>
																		<option value="ADMIN"
																			${usuarioSalvo.perfil == 'ADMIN' ? 'selected' : ''}>Admin</option>
																		<option value="SECRETARIA"
																			${usuarioSalvo.perfil == 'SECRETARIA' ? 'selected' : ''}>Secretária</option>
																		<option value="AUXILIAR"
																			${usuarioSalvo.perfil == 'AUXILIAR' ? 'selected' : ''}>Auxiliar</option>
																	</select>
																</div>
															</div>
															
															

															<button class="btn waves-effect waves-light btn-primary"
																onclick="limparForm();" type="button">Novo</button>
															<button class="btn waves-effect waves-light btn-success">Salvar</button>
															<button class="btn waves-effect waves-light btn-danger"
																type="button" onclick="deletarAjax();">Excluir</button>

															<button type="button" class="btn btn-secondary"
																data-toggle="modal" data-target="#modalPesquisarUsuario">
																Pesquisar</button>

														</form>

													</div>


												</div>

												<div style="overflow-y: scroll; height: 600px">
													<table class="table" id="lista-usuario">
														<thead>
															<tr>
																<th scope="col">ID</th>
																<th scope="col">Nome</th>
																<th scope="col">Email</th>
																<th scope="col">Ver</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${lista}" var="u">
																<tr>
																	<td><c:out value="${u.id}"></c:out></td>
																	<td><c:out value="${u.nome}"></c:out></td>
																	<td><c:out value="${u.email}"></c:out></td>
																	<td><a class="btn btn-success"
																		href="<%= request.getContextPath() %>/ServletUsuario?acao=editar&id=${u.id}">Visualizar</a></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>


												</div>
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


		<div class="modal fade" id="modalPesquisarUsuario" tabindex="-1"
			role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">Pesquisar
							usuário</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="input-group mb-3">
							<input type="text" class="form-control"
								placeholder="Recipient's username"
								aria-label="Digite um nome..." id="nomeBusca"
								aria-describedby="basic-addon2">
							<div class="input-group-append">
								<button class="btn btn-success" type="button"
									onclick="buscarUsuario();">Buscar</button>
							</div>
						</div>
						<div style="overflow-y: scroll; height: 300px">
							<table class="table" id="tabela-usuario">
								<thead>
									<tr>
										<th scope="col">ID</th>
										<th scope="col">Nome</th>
										<th scope="col">Ver</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>

						</div>
					</div>
					<div class="modal-footer"
						style="display: flex; justify-content: space-between;">
						<span id="totalUsuarios"></span>
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Fechar</button>
					</div>
				</div>
			</div>
		</div>

		<jsp:include page="javascript.jsp"></jsp:include>
		<script type="text/javascript">
		
			function visualizarImg(fotoBase64, fileFoto){
				var preview = document.getElementById(fotoBase64);
				var file = document.getElementById(fileFoto).files[0];
				var reader = new FileReader();
				
				reader.onloadend = function (){
					preview.src = reader.result;
				};
				
				if(file){
					reader.readAsDataURL(file);
				}else{
					preview.src = '';
				}
			}
		
			function buscarUsuario() {
				var nomeBusca = document.getElementById('nomeBusca').value;
				var urlAction = document.getElementById('formUsuario').action;

				if (nomeBusca != null && nomeBusca != ''
						&& nomeBusca.trim() != '') {
					$
							.ajax(
									{

										method : "get",
										url : urlAction,
										data : "nomeBusca=" + nomeBusca
												+ "&acao=buscarUsuario",
										success : function(response) {

											var json = JSON.parse(response);

											$('#tabela-usuario > tbody > tr')
													.remove();

											for (var i = 0; i < json.length; i++) {
												$('#tabela-usuario > tbody')
														.append(
																'<tr><td>'
																		+ json[i].id
																		+ '</td><td>'
																		+ json[i].nome
																		+ '</td><td><button type="button" class="btn btn-info" onclick="editar('
																		+ json[i].id
																		+ ');">Visualizar</button></td></tr>');
											}

											$('#totalUsuarios').text(
													'Total de usuários encontrados: '
															+ json.length);
										}

									}).fail(
									function(xhr, status, erroThrow) {
										console.log('Erro ao buscar usuário: '
												+ xhr.responseText);
									});
				}
			}

			function editar(id) {
				var urlAction = document.getElementById('formUsuario').action;

				window.location.href = urlAction + '?acao=editar&id=' + id;

			}

			function deletarAjax() {
				if (confirm('Deseja excluir os dados?')) {
					var urlAction = document.getElementById('formUsuario').action;
					var idUsuario = document.getElementById('id').value;

					$
							.ajax(
									{

										method : "get",
										url : urlAction,
										data : "id=" + idUsuario
												+ "&acao=deletar-ajax",
										success : function(response) {
											limparForm();
											document.getElementById('msg').textContent = response;
										}

									})
							.fail(
									function(xhr, status, erroThrow) {
										console
												.log('Erro ao deletar usuário de ID: '
														+ xhr.responseText);
									});
				}
			}

			function deletar() {

				if (confirm('Deseja excluir os dados?')) {
					document.getElementById('formUsuario').method = 'get';
					document.getElementById('acao').value = 'deletar';
					document.getElementById('formUsuario').submit();
				}

			}

			function limparForm() {
				var form = document.getElementById('formUsuario').elements;

				
				for (var i = 0; i < form.length; i++) {
					form[i].value = '';
				}
				document.getElementById("fotoBase64").src='assets//images/upload.svg';
				document.getElementById("perfil").value = 'ADMIN';
				document.getElementById("masculino").checked = true;
			}
		</script>
</body>

</html>
