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
    <title>Mi Cuenta - AYNI</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f7f6;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #333;
        }
        .header-profile {
            background-color: #ffffff;
            padding: 15px 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        .header-profile img.logo {
            height: 40px;
        }
        .btn-back {
            background: none;
            border: none;
            color: #2c3e50;
            font-size: 1rem;
            cursor: pointer;
            font-weight: bold;
        }
        .btn-back:hover {
            color: #4a90e2;
        }
        .profile-container {
            max-width: 700px;
            margin: 0 auto;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.08);
            padding: 40px;
        }
        .profile-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .profile-header h2 {
            margin: 0 0 10px 0;
            font-size: 1.8rem;
        }
        .avatar-wrapper {
            position: relative;
            display: inline-block;
            margin-bottom: 20px;
        }
        .avatar-wrapper img {
            width: 130px;
            height: 130px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #f0f2f5;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        .avatar-upload {
            position: absolute;
            bottom: 5px;
            right: 5px;
            background: #2c3e50;
            color: white;
            border-radius: 50%;
            width: 35px;
            height: 35px;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            border: 2px solid #fff;
        }
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .input-group {
            display: flex;
            flex-direction: column;
        }
        .input-group.full-width {
            grid-column: 1 / -1;
        }
        .input-group label {
            font-size: 0.9rem;
            font-weight: 600;
            margin-bottom: 8px;
            color: #555;
        }
        .input-group input[type="text"],
        .input-group input[type="email"] {
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            transition: border-color 0.3s;
        }
        .input-group input:focus {
            border-color: #4a90e2;
            outline: none;
        }
        .file-input-real {
            display: none;
        }
        .btn-save {
            width: 100%;
            padding: 14px;
            background-color: #27ae60;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 1.1rem;
            font-weight: bold;
            cursor: pointer;
            margin-top: 25px;
            transition: background-color 0.3s;
        }
        .btn-save:hover {
            background-color: #219653;
        }
        .alert-success {
            background-color: #e8f8f5;
            color: #27ae60;
            padding: 12px;
            border-radius: 6px;
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    
    <header class="header-profile">
        <img src="logo.png" alt="AYNI Logo" class="logo">
        <button class="btn-back" onclick="window.location.href='DashboardController'">
            <i class="fa-solid fa-arrow-left"></i> Volver al Inicio
        </button>
    </header>

    <div class="profile-container">
        <div class="profile-header">
            <h2>Configuración de Perfil</h2>
            <p style="color: #777;">Actualiza tu información personal y foto de perfil</p>
        </div>

        <% if("exito".equals(request.getParameter("msg"))) { %>
            <div class="alert-success">
                <i class="fa-solid fa-circle-check"></i> ¡Tus datos se actualizaron correctamente!
            </div>
        <% } %>

        <form action="MiCuentaController" method="POST" enctype="multipart/form-data">
            
            <div style="text-align: center;">
                <div class="avatar-wrapper">
                    <img id="previewImg" src="<%= (usuarioActivo.getImagen() != null && !usuarioActivo.getImagen().isEmpty()) ? usuarioActivo.getImagen() : "default_avatar.png" %>" alt="Foto de perfil">
                    <label for="imagenUpload" class="avatar-upload" title="Cambiar foto">
                        <i class="fa-solid fa-camera"></i>
                    </label>
                    <input type="file" id="imagenUpload" name="imagen" class="file-input-real" accept="image/png, image/jpeg" onchange="previewFile()">
                </div>
                <p style="font-size: 0.8rem; color: #888; margin-top: -10px; margin-bottom: 25px;">Sube una imagen cuadrada (JPG, PNG)</p>
            </div>

            <div class="form-grid">
                <div class="input-group">
                    <label>Nombres</label>
                    <input type="text" name="nombres" value="<%= usuarioActivo.getNombres() %>" required>
                </div>
                
                <div class="input-group">
                    <label>Apellidos</label>
                    <input type="text" name="apellidos" value="<%= usuarioActivo.getApellidos() %>" required>
                </div>
                
                <div class="input-group full-width">
                    <label>Correo Electrónico</label>
                    <input type="email" name="correo" value="<%= usuarioActivo.getCorreo() %>" required>
                </div>
                
                <div class="input-group full-width">
                    <label>Alias Anónimo <span style="font-weight: normal; color: #888; font-size: 0.8rem;">(Se mostrará cuando actives el modo anónimo)</span></label>
                    <input type="text" name="aliasAnonimo" value="<%= usuarioActivo.getAliasAnonimo() != null ? usuarioActivo.getAliasAnonimo() : "" %>">
                </div>
            </div>
            
            <button type="submit" class="btn-save"><i class="fa-solid fa-floppy-disk"></i> Guardar Cambios</button>
        </form>
    </div>

    <script>
        // Para que al seleccionar una nueva foto, se actualice la previsualización al instante
        function previewFile() {
            const preview = document.getElementById('previewImg');
            const file = document.querySelector('input[type=file]').files[0];
            const reader = new FileReader();

            reader.onloadend = function () {
                preview.src = reader.result;
            }

            if (file) {
                reader.readAsDataURL(file);
            } else {
                preview.src = "<%= (usuarioActivo.getImagen() != null && !usuarioActivo.getImagen().isEmpty()) ? usuarioActivo.getImagen() : "default_avatar.png" %>";
            }
        }
    </script>
</body>
</html>