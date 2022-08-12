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

											<div class="col-sm-12">
												<div class="card">
													<div class="card-header">
														<h5>Cadastro de usuário</h5>
													</div>
													<div class="card-block">
														<h4 class="sub-title">Basic Inputs</h4>
														<form class="form-material"
															action="<%= request.getContextPath() %>/ServletUsuario"
															method="post">
															<div class="form-group row">
																<div class="col-sm-1">
																	<div class="form-group form-default">
																		<input type="text" name="id" id="id" value="${usuarioSalvo.id}"
																			class="form-control" disabled> <span
																			class="form-bar"></span> <label class="float-label">ID</label>
																	</div>

																</div>
															</div>
															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="nome" id="nome" value="${usuarioSalvo.nome}"
																			class="form-control" required> <span
																			class="form-bar"></span> <label class="float-label">Nome</label>
																	</div>
																</div>
															</div>


															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="email" name="email" id="email" value="${usuarioSalvo.email}"
																			class="form-control" required> <span
																			class="form-bar"></span> <label class="float-label">Email</label>
																	</div>

																</div>
															</div>

															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="login" id="login" value="${usuarioSalvo.login}"
																			class="form-control" required> <span
																			class="form-bar"></span> <label class="float-label">Login</label>
																	</div>

																</div>
															</div>
															<div class="form-group row">
																<div class="col-sm-12">
																	<div class="form-group form-default">
																		<input type="text" name="senha" id="senha" value="${usuarioSalvo.senha}"
																			class="form-control" required> <span
																			class="form-bar"></span> <label class="float-label">Senha</label>
																	</div>
																</div>
															</div>


															<button class="btn waves-effect waves-light btn-primary">Cadastrar</button>
															<button class="btn waves-effect waves-light btn-success">Editar</button>
															<button class="btn waves-effect waves-light btn-danger">Excluir</button>

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
</body>

</html>
