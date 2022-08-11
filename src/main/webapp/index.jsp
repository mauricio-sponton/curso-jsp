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

.btn-login, .msg-erro {
	margin-top: 1em
}
</style>
</head>
<body>

	<div class="container">
		<h3 class="mb-4">Bem-vindo ao curso de JSP</h3>

		<form action="ServletLogin" method="post"
			class="row g-3 needs-validation" novalidate>
			<input type="hidden" value="<%=request.getParameter("url")%>"
				name="url">

			<div class="col-12 mb-2">
				<label for="inputAddress" class="form-label">Login</label> <input
					name="login" type="text" class="form-control" id="login" required>
				<div class="invalid-feedback">Informe seu login</div>
				<div class="valid-feedback">Tudo certo!</div>
			</div>
			<div class="col-12 mb-2">
				<label for="senha" class="form-label">Senha</label> <input
					name="senha" type="password" id="senha" class="form-control"
					required>
				<div class="invalid-feedback">Informe sua senha</div>
				<div class="valid-feedback">Tudo certo!</div>
			</div>
			<div class="col-12 btn-login">
				<button type="submit" class="btn btn-primary">Enviar</button>
			</div>


		</form>


		<h4 class="msg-erro">${msg}</h4>
	</div>

	<script type="text/javascript">
	(function() {
		  'use strict'

		  const forms = document.querySelectorAll('.needs-validation')

		  Array.from(forms).forEach(form => {
		    form.addEventListener('submit', event => {
		      if (!form.checkValidity()) {
		        event.preventDefault()
		        event.stopPropagation()
		      }

		      form.classList.add('was-validated')
		    }, false)
		  })
		})()
	</script>

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