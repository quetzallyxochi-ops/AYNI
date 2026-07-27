<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Modelo.Usuario" %>
<%@ page import="Modelo.Producto" %>
<%@ page import="Modelo.ProductoDAO" %>
<%
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
    if (usuarioActivo == null) {
        response.sendRedirect("login.html?error=no_sesion");
        return;
    }
    
    // Rescatamos el ID y buscamos el producto en la BD
    String idProductoStr = request.getParameter("id");
    Producto p = null;
    if(idProductoStr != null && !idProductoStr.isEmpty()){
        int idProducto = Integer.parseInt(idProductoStr);
        ProductoDAO dao = new ProductoDAO();
        p = dao.obtenerProductoPorId(idProducto);
    }
    
    // Si alguien altera la URL y pone un ID que no existe, lo regresamos
    if(p == null){
        response.sendRedirect("DashboardController");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>AYNI - <%= p.getTitulo() %></title>
    <link rel="stylesheet" href="dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
            .producto-detalle-card {
                background: #ffffff;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                display: flex;
                flex-direction: row;
                overflow: hidden;
                margin-top: 30px;
            }
            .producto-imagen-container {
                flex: 1;
                max-width: 50%;
            }
            .producto-imagen-container img {
                width: 100%;
                height: 100%;
                min-height: 400px;
                max-height: 450px;
                object-fit: cover; 
            }
            .producto-info-container {
                flex: 1;
                padding: 40px;
                display: flex;
                flex-direction: column;
            }
            .categoria-badge {
                background-color: #0f7a73; 
                color: white;
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 0.9rem;
                display: inline-flex;
                align-items: center;
                width: fit-content;
                margin-bottom: 15px;
            }
            .producto-titulo {
                font-size: 2.2rem;
                margin: 0 0 10px 0;
                color: #2c3e50;
            }
            .producto-precio {
                font-size: 2rem;
                color: #d35400;
                font-weight: bold;
                margin: 0 0 20px 0;
            }
            .producto-descripcion {
                font-size: 1.1rem;
                color: #555;
                line-height: 1.6;
                margin-bottom: 40px;
                flex-grow: 1;
            }
            .btn-agregar-carrito {
                background-color: #d35400;
                color: white;
                border: none;
                padding: 15px 20px;
                font-size: 1.2rem;
                border-radius: 8px;
                cursor: pointer;
                font-weight: bold;
                transition: background 0.3s;
                text-align: center;
                text-decoration: none;
                width: 100%;
                display: inline-block;
            }
            .btn-agregar-carrito:hover {
                background-color: #e67e22;
            }

            /* Diseño responsivo para pantallas pequeñas */
            @media (max-width: 768px) {
                .producto-detalle-card { flex-direction: column; }
                .producto-imagen-container { max-width: 100%; }
                .producto-imagen-container img { min-height: 300px; }
            }
        </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo"><img src="logo2.png" alt="AYNI Logo"></div>
        <div class="nav-icons">
            <i class="fa-solid fa-arrow-left" onclick="window.location.href='DashboardController'" style="cursor: pointer; font-size: 1.5rem;" title="Regresar al inicio"></i>
        </div>
    </nav>

    <div class="container" style="display: block; max-width: 900px; margin: 0 auto;">
    
    <div class="producto-detalle-card">
        <!-- Lado izquierdo: Imagen -->
        <div class="producto-imagen-container">
            <img src="<%= (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : "default_product.jpg" %>" alt="Imagen del producto">
        </div>
        
        <!-- Lado derecho: Información -->
        <div class="producto-info-container">
           
            <div class="categoria-badge">
                <i class="fa-solid fa-layer-group" style="margin-right: 5px;"></i> Artículo Disponible
            </div>
            
            <h2 class="producto-titulo"><%= p.getTitulo() %></h2> 
            <p class="producto-precio">$<%= String.format("%.2f", p.getPrecio()) %> MXN</p>
            
            <p style="font-weight: bold; margin-bottom: 5px; color: #333;">Descripción</p>
            <p class="producto-descripcion">
                <%= p.getDescripcion() != null ? p.getDescripcion() : "El vendedor no ha proporcionado una descripción detallada para este artículo." %>
            </p>
            
          
            <a href="CarritoController?action=agregar&idProducto=<%= p.getIdProducto() %>" class="btn-agregar-carrito">
                <i class="fa-solid fa-cart-plus"></i> Agregar al Carrito
            </a>
        </div>
    </div>
</div>
</body>
</html>