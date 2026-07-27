<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Producto" %>
<%@ page import="Modelo.Usuario" %>
<%
    // Validar sesión
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
    if (usuarioActivo == null) {
        response.sendRedirect("login.html?error=no_sesion");
        return;
    }
    
    // Recuperar lista de productos del carrito enviada por el controlador
    List<Producto> productosCarrito = (List<Producto>) request.getAttribute("productosCarrito");
    double total = 0.0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>AYNI - Mi Carrito</title>
    <link rel="stylesheet" href="dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .carrito-wrapper { display: flex; gap: 20px; margin-top: 30px; flex-wrap: wrap; }
        .carrito-lista { flex: 2; min-width: 60%; background: #fff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
        .carrito-resumen { flex: 1; min-width: 30%; background: #fff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); height: fit-content; }
        
        .header-carrito { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 20px; }
        
        .carrito-item { display: flex; align-items: center; justify-content: space-between; padding: 15px 0; border-bottom: 1px solid #f5f5f5; }
        .carrito-item:last-child { border-bottom: none; }
        
        /* ESTANDARIZACIÓN DE IMAGEN REQUERIDA */
        .carrito-item img { width: 90px; height: 90px; object-fit: cover; border-radius: 8px; border: 1px solid #ddd; }
        
        .carrito-info { flex: 1; margin-left: 20px; }
        .carrito-info h4 { margin: 0 0 8px 0; font-size: 1.3rem; color: #333; }
        .carrito-info p { margin: 0; color: #d35400; font-weight: bold; font-size: 1.1rem; }
        
        .btn-eliminar { background: #e74c3c; color: white; border: none; padding: 10px 15px; border-radius: 6px; cursor: pointer; text-decoration: none; font-size: 0.9rem; transition: background 0.3s;}
        .btn-eliminar:hover { background: #c0392b; }
        
        .btn-comprar { background: #27ae60; color: white; border: none; padding: 15px; width: 100%; border-radius: 8px; font-size: 1.2rem; cursor: pointer; margin-top: 25px; font-weight: bold; text-align: center; display: block; text-decoration: none; transition: background 0.3s;}
        .btn-comprar:hover { background: #219150; }
        
        .nav-regresar { font-size: 1.5rem; color: #333; cursor: pointer; text-decoration: none; }
    </style>
</head>
<body>

    <!-- BARRA DE NAVEGACIÓN SIMPLE -->
    <nav class="navbar">
        <div class="logo">
            <img src="logo2.png" alt="AYNI Logo">
        </div>
        <div class="nav-icons">
            <!-- Botón para regresar como solicitaste -->
            <a href="DashboardController" class="nav-regresar" title="Regresar al inicio"><i class="fa-solid fa-arrow-left"></i></a>
        </div>
    </nav>

    <!-- CONTENIDO DEL CARRITO -->
    <div class="container" style="max-width: 1100px; margin: 0 auto; display: block;">
        <div class="carrito-wrapper">
            
            <!-- LISTA DE PRODUCTOS RESERVADOS -->
            <div class="carrito-lista">
                <div class="header-carrito">
                    <h2 style="margin:0;"><i class="fa-solid fa-cart-shopping"></i> Mis Artículos Reservados</h2>
                </div>
                
                <% 
                    if(productosCarrito != null && !productosCarrito.isEmpty()) { 
                        for(Producto p : productosCarrito) { 
                            total += p.getPrecio(); // Sumatoria dinámica
                %>
                    <div class="carrito-item">
                        <img src="<%= (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : "default_product.jpg" %>" alt="Producto">
                        <div class="carrito-info">
                            <h4><%= p.getTitulo() %></h4>
                            <p>$<%= String.format("%.2f", p.getPrecio()) %> MXN</p>
                        </div>
                        <!-- Conecta con la acción eliminar del Controlador -->
                        <a href="CarritoController?action=eliminar&idProducto=<%= p.getIdProducto() %>" class="btn-eliminar">
                            <i class="fa-solid fa-trash"></i> Eliminar
                        </a>
                    </div>
                <%      
                        } 
                    } else { 
                %>
                    <div style="text-align: center; padding: 40px 0; color: #777;">
                        <i class="fa-solid fa-box-open" style="font-size: 3rem; margin-bottom: 15px; color: #ccc;"></i>
                        <p style="font-size: 1.2rem;">Aún no tienes artículos reservados.</p>
                    </div>
                <% } %>
            </div>
            
            <!-- PANEL DE RESUMEN Y COMPRA -->
            <div class="carrito-resumen">
                <h3 style="margin-top:0; color: #333;">Resumen de Transacción</h3>
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                
                <div style="display: flex; justify-content: space-between; font-size: 1.4rem; font-weight: bold; color: #2c3e50;">
                    <span>Total a pagar:</span>
                    <span>$<%= String.format("%.2f", total) %> MXN</span>
                </div>
                
                <% if(total > 0) { %>
                    <!-- Conecta con la acción comprar del Controlador -->
                    <a href="CarritoController?action=comprar" class="btn-comprar">
                        <i class="fa-solid fa-check-to-slot"></i> Confirmar Compra
                    </a>
                <% } else { %>
                    <button class="btn-comprar" style="background: #bdc3c7; cursor: not-allowed;" disabled>
                        Confirmar Compra
                    </button>
                <% } %>
                
                <p style="font-size: 0.85rem; color: #777; margin-top: 15px; text-align: center;">
                    <i class="fa-solid fa-shield-halved"></i> Al confirmar, los artículos pasarán a estado "Vendido" y se iniciará el proceso de transacción.
                </p>
            </div>

        </div>
    </div>
</body>
</html>