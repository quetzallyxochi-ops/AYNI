<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuario" %>
<%@ page import="Modelo.Producto" %>
<%@ page import="Modelo.Transaccion" %>
<%
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
    if (usuarioActivo == null) {
        response.sendRedirect("login.html?error=no_sesion");
        return;
    }
    
    // Recuperar las listas enviadas desde MisProductosController
    List<Producto> misProductosActivos = (List<Producto>) request.getAttribute("misProductosActivos");
    List<Transaccion> misVentas = (List<Transaccion>) request.getAttribute("misVentas");
    List<Transaccion> misCompras = (List<Transaccion>) request.getAttribute("misCompras");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>AYNI - Mis Productos</title>
    <link rel="stylesheet" href="dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .seccion-productos {
            background: white;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            margin-top: 20px;
            margin-bottom: 30px;
        }
        .header-seccion {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #eee;
            padding-bottom: 12px;
            margin-bottom: 20px;
        }
        .btn-nuevo {
            background-color: #27ae60;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            transition: background 0.3s;
        }
        .btn-nuevo:hover { background-color: #219150; }
        
        .item-lista {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #f5f5f5;
        }
        .item-lista:last-child { border-bottom: none; }
        
        /* ESTANDARIZACIÓN DE IMAGEN  */
        .item-lista img { 
            width: 90px; 
            height: 90px; 
            border-radius: 8px; 
            object-fit: cover; 
            border: 1px solid #ddd;
        }
        
        .item-info { flex: 1; margin-left: 20px; }
        .item-info h4 { margin: 0 0 5px 0; font-size: 1.2rem; color: #333; }
        .item-info p { margin: 0; font-size: 0.95rem; }
        
        .acciones-btn a, .acciones-btn button {
            padding: 8px 14px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            color: white;
            margin-left: 5px;
            text-decoration: none;
            font-size: 0.9rem;
            display: inline-block;
            transition: opacity 0.3s;
        }
        .acciones-btn a:hover, .acciones-btn button:hover { opacity: 0.85; }
        
        .btn-modificar { background-color: #f39c12; }
        .btn-eliminar { background-color: #e74c3c; }
        .btn-donar { background-color: #8e44ad; }
        .btn-exito { background-color: #27ae60; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo"><img src="logo2.png" alt="AYNI Logo"></div>
        <div class="nav-icons">
            <i class="fa-solid fa-arrow-left" onclick="window.location.href='DashboardController'" style="cursor: pointer; font-size: 1.5rem;" title="Regresar al inicio"></i>
        </div>
    </nav>

    <div class="container" style="display: block; max-width: 1100px; margin: 0 auto;">
        
        <!-- SECCIÓN 1: MIS PRODUCTOS OFERTADOS Y VENTAS EN PROCESO -->
        <div class="seccion-productos">
            <div class="header-seccion">
                <h2><i class="fa-solid fa-box-open"></i> Lo que estoy vendiendo</h2>
                <a href="CargarVentaController" class="btn-nuevo"><i class="fa-solid fa-plus"></i> Vender un producto</a>
            </div>
            
            <!-- A. PRODUCTOS ACTIVOS (Disponibles) -->
            <% 
                if(misProductosActivos != null && !misProductosActivos.isEmpty()) {
                    for(Producto p : misProductosActivos) {
            %>
                <div class="item-lista">
                    <img src="<%= (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : "default_product.jpg" %>" alt="Producto">
                    <div class="item-info">
                        <h4><%= p.getTitulo() %></h4>
                        <p style="color: #777;">Estado: <%= p.getEstadoVenta() %> | Precio: $<%= String.format("%.2f", p.getPrecio()) %> MXN</p>
                    </div>
                    <div class="acciones-btn">
                        <!-- Enlaces listos para conectar con controladores futuros de modificar/eliminar/donar -->
                        <a href="ModificarProductoController?id=<%= p.getIdProducto() %>" class="btn-modificar"><i class="fa-solid fa-pen"></i> Modificar</a>
                        <a href="ProductoController?action=eliminar&id=<%= p.getIdProducto() %>" class="btn-eliminar"><i class="fa-solid fa-trash"></i> Eliminar</a>
                        <a href="ProductoController?action=donar&id=<%= p.getIdProducto() %>" class="btn-donar"><i class="fa-solid fa-hand-holding-heart"></i> Donar</a>
                    </div>
                </div>
            <% 
                    }
                } 
            %>

            <!-- B. VENTAS EN PROCESO (Comprados por alguien más, esperando entrega) -->
            <% 
                if(misVentas != null && !misVentas.isEmpty()) {
                    for(Transaccion v : misVentas) {
            %>
                <div class="item-lista">
                    <img src="<%= (v.getProductoImagen() != null && !v.getProductoImagen().isEmpty()) ? v.getProductoImagen() : "default_product.jpg" %>" alt="Producto">
                    <div class="item-info">
                        <h4><%= v.getProductoTitulo() %></h4>
                        <p style="color: #e67e22; font-weight: bold;">Estado: En proceso de entrega (Comprador: <%= v.getNombreComprador() %>)</p>
                    </div>
                    <div class="acciones-btn">
                        <% if ("Conflicto".equals(v.getEstadoGlobal())) { %>
                            <div class="alerta-conflicto" style="color: #d9534f; background-color: #f2dede; padding: 10px; border-radius: 5px;">
                                <p style="margin: 0;">⚠️ <strong>Atención:</strong> Por el momento espera a que el administrador se comunique para solucionar el proceso de transacción.</p>
                            </div>
                        <% } else { %>
                            <button type="button" onclick="confirmarAccionTransaccion(<%= v.getIdTransaccion() %>, 'venta_exitosa')" class="btn-exito">
                                <i class="fa-solid fa-check"></i> Venta Exitosa
                            </button>
                            <button type="button" onclick="confirmarAccionTransaccion(<%= v.getIdTransaccion() %>, 'venta_cancelada')" class="btn-eliminar">
                                <i class="fa-solid fa-xmark"></i> Cancelar Venta
                            </button>
                        <% } %>
                    </div>
                </div>
            <% 
                    }
                } 
            %>
            
            <% if((misProductosActivos == null || misProductosActivos.isEmpty()) && (misVentas == null || misVentas.isEmpty())) { %>
                <p style="text-align: center; color: #777; padding: 20px 0;">No tienes productos en oferta ni ventas pendientes actualmente.</p>
            <% } %>
        </div>

        <!-- SECCIÓN 2: MIS COMPRAS POR ENTREGAR -->
        <div class="seccion-productos">
            <div class="header-seccion">
                <h2><i class="fa-solid fa-bag-shopping"></i> Mis compras por recoger</h2>
            </div>
            
            <% 
                if(misCompras != null && !misCompras.isEmpty()) {
                    for(Transaccion c : misCompras) {
            %>
                <div class="item-lista">
                    <img src="<%= (c.getProductoImagen() != null && !c.getProductoImagen().isEmpty()) ? c.getProductoImagen() : "default_product.jpg" %>" alt="Producto">
                    <div class="item-info">
                        <h4><%= c.getProductoTitulo() %></h4>
                        <p style="color: #e67e22; font-weight: bold;">Estado: Esperando entrega (Vendedor: <%= c.getNombreVendedor() %>)</p>
                    </div>
                   <div class="acciones-btn">
                        <% if ("Conflicto".equals(c.getEstadoGlobal())) { %>
                            <div class="alerta-conflicto" style="color: #d9534f; background-color: #f2dede; padding: 10px; border-radius: 5px;">
                                <p style="margin: 0;">⚠️ <strong>Atención:</strong> Por el momento espera a que el administrador se comunique para solucionar el proceso de transacción.</p>
                            </div>
                        <% } else { %>
                            <button type="button" onclick="confirmarAccionTransaccion(<%= c.getIdTransaccion() %>, 'compra_exitosa')" class="btn-exito">
                                <i class="fa-solid fa-check"></i> Compra Exitosa
                            </button>
                            <button type="button" onclick="confirmarAccionTransaccion(<%= c.getIdTransaccion() %>, 'compra_cancelada')" class="btn-eliminar">
                                <i class="fa-solid fa-xmark"></i> Cancelar Compra
                            </button>
                        <% } %>
                    </div>
                </div>
            <% 
                    }
                } else {
            %>
                <p style="text-align: center; color: #777; padding: 20px 0;">No tienes compras pendientes por recoger en este momento.</p>
            <% } %>
        </div>
    </div>
        
        <!-- Modal para confirmar transacción con contraseña -->
    <dialog id="modalConfirmarPass" class="modal-ayni">
        <div class="modal-body centrado">
            <button type="button" onclick="document.getElementById('modalConfirmarPass').close()" class="btn-cerrar-modal" style="float: right; border: none; background: none; font-size: 1.2rem; cursor: pointer;">&times;</button>
            <i class="fa-solid fa-shield-halved icon-exito" style="color: #2c3e50;"></i>
            <h3>Confirmación de Seguridad</h3>
            <p>Ingresa tu contraseña para autorizar esta acción sobre la transacción.</p>
            
            <!-- El formulario enviará los datos por POST a un Servlet unificado -->
            <form action="TransaccionController" method="POST">
                <input type="hidden" id="transaccion_id" name="id">
                <input type="hidden" id="transaccion_action" name="action">
                
                <div class="grupo-input" style="margin: 15px 0;">
                    <input type="password" name="password" placeholder="Tu contraseña actual" required style="width: 100%; padding: 10px; border-radius: 5px; border: 1px solid #ccc;">
                </div>
                <button type="submit" class="btn-aceptar">Confirmar Acción</button>
            </form>
        </div>
    </dialog>

    <script>
        // Función para inyectar los datos en el modal y abrirlo
        function confirmarAccionTransaccion(idTransaccion, accion) {
            document.getElementById('transaccion_id').value = idTransaccion;
            document.getElementById('transaccion_action').value = accion;
            document.getElementById('modalConfirmarPass').showModal();
        }
    </script>
</body>
</html>