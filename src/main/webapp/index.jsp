<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Curso JSP</title>
</head>
<body>

	<h1>Bem-vindo ao curso de JSP</h1>

	<form action="ServletLogin" method="post">
	<input type="hidden" value="<%= request.getParameter("url") %>" name="url">
		<table>
			<tr>
				<td><input name="login" type="text"></td>
			</tr>
			<tr>
				<td><input name="senha" type="password"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Enviar"></td>
			</tr>
		</table>
	</form>
	
	<h4>${msg}</h4>

</body>
</html>