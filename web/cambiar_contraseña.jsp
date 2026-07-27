<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Modelo.Usuario" %>
<%
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
    if (usuarioActivo == null) {
        response.sendRedirect("login.html");
        return; 
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Cambiar Contraseña - AYNI</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f7f6;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .modern-card {
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.1);
            padding: 40px;
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        .modern-card img.logo {
            width: 120px;
            margin-bottom: 20px;
        }
        .modern-card h2 {
            color: #333;
            font-size: 1.5rem;
            margin-bottom: 25px;
        }
        .input-group {
            position: relative;
            margin-bottom: 20px;
            text-align: left;
        }
        .input-group label {
            display: block;
            font-size: 0.9rem;
            color: #555;
            margin-bottom: 8px;
            font-weight: 600;
        }
        .input-group i {
            position: absolute;
            bottom: 12px;
            left: 12px;
            color: #888;
        }
        .input-group input {
            width: 100%;
            padding: 10px 10px 10px 35px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        .input-group input:focus {
            border-color: #4a90e2;
            outline: none;
        }
        .btn-modern {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 6px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
            margin-bottom: 10px;
        }
        .btn-primary {
            background-color: #2c3e50;
            color: white;
        }
        .btn-primary:hover {
            background-color: #1a252f;
        }
        .btn-secondary {
            background-color: #e0e0e0;
            color: #333;
        }
        .btn-secondary:hover {
            background-color: #d5d5d5;
        }
        .alert-error {
            background-color: #ffeaea;
            color: #e74c3c;
            padding: 10px;
            border-radius: 6px;
            font-size: 0.85rem;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="modern-card">
        <img src="logo.png" alt="AYNI Logo" class="logo">
        <h2>Seguridad de la Cuenta</h2>
        
        <% if(request.getParameter("error") != null) { %>
            <div class="alert-error">
                <i class="fa-solid fa-triangle-exclamation"></i> Error: Verifica que tu contraseña actual sea correcta y las nuevas coincidan.
            </div>
        <% } %>

        <form action="CambiarContrasenaController" method="POST">
            <div class="input-group">
                <label>Contraseña Actual</label>
                <i class="fa-solid fa-lock"></i>
                <input type="password" name="passActual" placeholder="Ingresa tu contraseña actual" required>
            </div>
            
            <div class="input-group">
                <label>Nueva Contraseña</label>
                <i class="fa-solid fa-key"></i>
                <input type="password" name="passNueva" placeholder="Crea una nueva contraseña" required>
            </div>
            
            <div class="input-group">
                <label>Confirmar Nueva Contraseña</label>
                <i class="fa-solid fa-check-double"></i>
                <input type="password" name="passConfirmar" placeholder="Repite la nueva contraseña" required>
            </div>
            
            <button type="submit" class="btn-modern btn-primary">Actualizar Contraseña</button>
            <button type="button" class="btn-modern btn-secondary" onclick="window.location.href='DashboardController'">Cancelar y Regresar</button>
        </form>
    </div>
</body>
</html>