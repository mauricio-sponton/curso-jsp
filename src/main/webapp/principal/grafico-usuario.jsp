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
														<h5>Relatório de usuário</h5>
													</div>
													<div class="card-block">
														<form class="form-material" id="formUsuario"
															action="<%=request.getContextPath()%>/ServletUsuario"
															method="get">

															<input type="hidden" name="acao" id="acaoRelatorio"
																value="imprimirRelatorio">

															<div class="form-group row">
																<div class="col-auto">
																	<label class="sr-only" for="dataInicial">Data
																		Inicial</label> <input type="text" class="form-control"
																		value="${dataInicial}" id="dataInicial"
																		name="dataInicial">
																</div>
																<div class="col-auto">
																	<label class="sr-only" for="dataFinal">Data
																		Final</label> <input type="text" class="form-control"
																		value="${dataFinal}" id="dataFinal" name="dataFinal">

																</div>
															</div>

															<button type="button" onclick="gerarGrafico()"
																class="btn btn-primary mb-2">Gerar gráfico</button>
														</form>


														<div style="overflow-y: scroll; height: 600px">
															<div>
																<canvas id="myChart"></canvas>
															</div>

														</div>

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
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script type="text/javascript">
		

		function gerarGrafico() {
			const myChart = new Chart(document.getElementById('myChart'),
					{
				type : 'line',
				data :{
					labels : [ 'January', 'February', 'March', 'April', 'May',
						'June', ],
					datasets : [ {
						label : 'Média salarial por tipo',
						backgroundColor : 'rgb(255, 99, 132)',
						borderColor : 'rgb(255, 99, 132)',
						data : [ 0, 10, 5, 2, 20, 30, 45 ],
					} ]
				},
				options : {}
			});
		}

		$(function() {

			$("#dataInicial, #dataFinal")
					.datepicker(
							{
								dateFormat : 'dd/mm/yy',
								dayNames : [ 'Domingo', 'Segunda', 'Terça',
										'Quarta', 'Quinta', 'Sexta', 'Sábado' ],
								dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S',
										'S', 'D' ],
								dayNamesShort : [ 'Dom', 'Seg', 'Ter', 'Qua',
										'Qui', 'Sex', 'Sáb', 'Dom' ],
								monthNames : [ 'Janeiro', 'Fevereiro', 'Março',
										'Abril', 'Maio', 'Junho', 'Julho',
										'Agosto', 'Setembro', 'Outubro',
										'Novembro', 'Dezembro' ],
								monthNamesShort : [ 'Jan', 'Fev', 'Mar', 'Abr',
										'Mai', 'Jun', 'Jul', 'Ago', 'Set',
										'Out', 'Nov', 'Dez' ],
								nextText : 'Próximo',
								prevText : 'Anterior'
							});
		});
	</script>
</body>

</html>
