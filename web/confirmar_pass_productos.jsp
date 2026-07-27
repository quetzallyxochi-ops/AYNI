<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Modelo.Usuario" %>
<%
    // Validar que la sesión exista
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
    if (usuarioActivo == null) {
        response.sendRedirect("login.html?error=no_sesion");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>AYNI - Confirmar Identidad</title>
    <link rel="stylesheet" href="estilos.css"> 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f7f6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .confirm-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            text-align: center;
            width: 100%;
            max-width: 400px;
        }
        .confirm-container h2 {
            color: #333;
            margin-bottom: 10px;
        }
        .confirm-container p {
            color: #666;
            margin-bottom: 25px;
            font-size: 0.9rem;
        }
        .input-group {
            position: relative;
            margin-bottom: 20px;
            text-align: left;
        }
        .input-group i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #888;
        }
        .input-group input {
            width: 100%;
            padding: 12px 15px 12px 40px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 1rem;
        }
        .btn-confirmar {
            background-color: #27ae60;
            color: white;
            border: none;
            padding: 12px;
            width: 100%;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            font-weight: bold;
            transition: background 0.3s;
        }
        .btn-confirmar:hover {
            background-color: #219150;
        }
        .btn-cancelar {
            display: inline-block;
            margin-top: 15px;
            color: #e74c3c;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .error-msg {
            color: #e74c3c;
            font-size: 0.85rem;
            margin-bottom: 15px;
            display: none;
        }
        <% if (request.getParameter("error") != null) { %>
        .error-msg { display: block; }
        <% } %>
    </style>
</head>
<body>

    <div class="confirm-container">
        <i class="fa-solid fa-shield-halved" style="font-size: 3rem; color: #27ae60; margin-bottom: 15px;"></i>
        <h2>Confirmar Identidad</h2>
        <p>Por seguridad, ingresa tu contraseña para acceder a tus productos y transacciones.</p>
        
        <div class="error-msg">
            <i class="fa-solid fa-circle-exclamation"></i> Contraseña incorrecta. Inténtalo de nuevo.
        </div>

        <form action="ConfirmarPassController" method="POST">
            <div class="input-group">
                <i class="fa-solid fa-lock"></i>
                <input type="password" name="password" placeholder="Tu contraseña" required>
            </div>
            <button type="submit" class="btn-confirmar">Ver mis productos</button>
        </form>
        
        <a href="DashboardController" class="btn-cancelar">Cancelar y volver al inicio</a>
    </div>

</body>
</html>