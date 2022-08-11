<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">

<title>Curso JSP</title>
<style>
	.container {
		margin-top: 20em
	}
	.btn-login, .msg-erro{
		margin-top: 1em
	}

</style>
</head>
<body>

	<div class="container">
		<h1>Bem-vindo ao curso de JSP</h1>

		<form action="ServletLogin" method="post" class="row g-3">
			<input type="hidden" value="<%=request.getParameter("url")%>"
				name="url">
			<div class="col-12">
				<label for="inputAddress" class="form-label">Login</label> <input
					name="login" type="text" class="form-control" id="login">
			</div>
			<div class="col-12">
				<label for="senha" class="form-label">Senha</label> <input
					name="senha" type="password" id="senha" class="form-control">
			</div>
			<div class="col-12 btn-login">
				<button type="submit" class="btn btn-primary">Enviar</button>
			</div>


		</form>

	
		<h4 class="msg-erro">${msg}</h4>
	</div>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
		integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
		crossorigin="anonymous"></script>
</body>
</html>