<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
														<form class="form-material" id="formUsuario"
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


															<button class="btn waves-effect waves-light btn-primary" onclick="limparForm();" type="button">Novo</button>
															<button class="btn waves-effect waves-light btn-success">Salvar</button>
															<button class="btn waves-effect waves-light btn-danger" type="button" onclick="deletarAjax();">Excluir</button>

														</form>
													</div>
												</div>
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


	<jsp:include page="javascript.jsp"></jsp:include>
	<script type="text/javascript">
	
		function deletarAjax(){
			if(confirm('Deseja excluir os dados?')){
				var urlAction = document.getElementById('formUsuario').action;
				var idUsuario = document.getElementById('id').value;
				
				$.ajax({
					
					method: "get",
					url: urlAction,
					data: "id=" + idUsuario + "&acao=deletar-ajax",
					success: function(response){
						limparForm();
						document.getElementById('msg').textContent = response;
					}
					
				}).fail(function(xhr, status, erroThrow){
					console.log('Erro ao deletar usuário de ID: ' + xhr.responseText);
				});
			}
		}
	
		function deletar(){
			
			if(confirm('Deseja excluir os dados?')){
				document.getElementById('formUsuario').method = 'get';
				document.getElementById('acao').value = 'deletar';
				document.getElementById('formUsuario').submit();
			}
			
		}
		
		function limparForm() {
			var form = document.getElementById('formUsuario').elements;
			
			for(var i=0; i < form.length; i++){
				form[i].value = '';
			}
		}
	
	</script>
</body>

</html>
