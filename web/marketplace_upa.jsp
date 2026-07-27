<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Modelo.Usuario" %>
<%
    // 1. Validar que la sesión exista
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
    if (usuarioActivo == null) {
        response.sendRedirect("login.html?error=no_sesion");
        return; // Detenemos la carga de la página
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>AYNI - Dashboard</title>
    <link rel="stylesheet" href="dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <!-- BARRA DE NAVEGACIÓN SUPERIOR -->
    <nav class="navbar">
        <div class="logo">
            <img src="logo2.png" alt="AYNI Logo">
        </div>
        
        <!-- Buscador -->
        <div class="search-bar">
            <i class="fa-solid fa-magnifying-glass"></i>
            <form action="DashboardController" method="GET" style="width: 100%; margin: 0;">
                 <input type="text" name="query" placeholder="Buscar libros, muebles, donaciones...">
            </form>
        </div>

        <div class="nav-icons">
            <!-- Notificaciones (Actualizado) -->
            <i class="fa-regular fa-bell" onclick="abrirModal('modalNotificaciones')" style="cursor: pointer;" title="Notificaciones"></i>
            
            <!-- Mensajes (Actualizado) -->
            <i class="fa-regular fa-envelope" onclick="abrirModal('modalMensajes')" style="cursor: pointer;" title="Mensajes"></i>
            
            <!-- Carrito -->
            <i class="fa-solid fa-cart-shopping" style="cursor: pointer;" onclick="window.location.href='CarritoController?action=ver'"> </i>
            
            <!-- Perfil del Usuario -->
            <div class="user-profile">
                <img src="<%= (usuarioActivo.getImagen() != null && !usuarioActivo.getImagen().isEmpty()) ? usuarioActivo.getImagen() : "default_avatar.png" %>" alt="Avatar">
                <div>
                    <span style="font-size: 0.75rem; display: block;"><%= usuarioActivo.getTipoUsuario() %> UPA</span>
                    <strong><%= usuarioActivo.getNombres() %> <i class="fa-solid fa-chevron-down"></i></strong>
                </div>

               <!-- Menú desplegable del perfil -->
                    <div class="dropdown-menu" style="top: 45px;">
                        <a href="mi_cuenta.jsp"><i class="fa-solid fa-user"></i> Mi Cuenta</a>
                        <a href="confirmar_pass_productos.jsp"><i class="fa-solid fa-box-open"></i> Mis Productos</a>
                        <a href="cambiar_contraseña.jsp"><i class="fa-solid fa-key"></i> Cambiar Contraseña</a>
                        <a href="#"><i class="fa-solid fa-user-ninja"></i> Perfil Anónimo: <%= usuarioActivo.isPerfilAnonimo() ? "ON" : "OFF" %></a>
                        <hr style="margin: 5px 0; border-top: 1px solid #ccc;">
                        <a href="LogoutController"><i class="fa-solid fa-right-from-bracket"></i> Cerrar Sesión</a>
                    </div>
            </div>
        </div>
    </nav>

    <!-- CONTENEDOR PRINCIPAL -->
    <div class="container">
        
        <!-- BARRA LATERAL -->
        <aside class="sidebar">
            <%@ page import="Modelo.Categoria" %>


                <div class="card categorias">
                    <h3>Categorías</h3>
                    <ul>
                        
                        <li><a href="DashboardController" style="text-decoration: none; color: inherit;"><i class="fa-solid fa-border-all"></i> Todos los productos</a></li>

                        <%
                            // Rescatamos la lista de categorías enviada por el controlador
                            List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
                            if (categorias != null && !categorias.isEmpty()) {
                                for (Categoria c : categorias) {
                        %>
                                    <li>
                                        <a href="DashboardController?categoria=<%= c.getNombre() %>" style="text-decoration: none; color: inherit;">
                                            <i class="fa-solid fa-tag"></i> <%= c.getNombre() %>
                                        </a>
                                    </li>
                        <%
                                }
                            }
                        %>
                        <!-- Enlace especial para las donaciones -->
                        <li><a href="DashboardController?categoria=Donaciones" style="text-decoration: none; color: inherit;"><i class="fa-solid fa-hand-holding-heart"></i> Donaciones activas</a></li>
                    </ul>
                </div>

            <div class="card impacto">
                <h3>Impacto Comunitario</h3>
                <div class="impacto-stats">
                    <div>
                        <h4>163</h4>
                        <p>Artículos<br>Reutilizados</p>
                    </div>
                    <div>
                        <h4>200</h4>
                        <p>Donaciones<br>Recibidas</p>
                    </div>
                </div>
                <div style="text-align: left; font-size: 0.8rem; color: #555;">
                    Campaña: Kits Escolares - 75%
                    <div class="progress-bar-container">
                        <div class="progress-bar"></div>
                    </div>
                </div>
            </div>
        </aside>

        <!-- CONTENIDO CENTRAL -->
        <main class="main-content">
            
            <!-- BANNER -->
            <div class="banner">
                <div>
                    <h1>¡Hola, <%= usuarioActivo.getNombres() %>!</h1>
                    <p>Bienvenido a tu comunidad AYNI</p>
                </div>
                <div class="banner-btns">
                    <button onclick="window.location.href='CargarVentaController'">Vender un Objeto</button>
                    <button onclick="window.location.href='hacer_donacion.jsp'">Hacer una Donación</button>
                </div>
            </div>

            <!-- FILTROS -->
            <div class="filtros-bar">
                <form action="DashboardController" method="GET" class="filtros-bar">
                    <span>Rango de Precio:</span>
                    <input type="number" name="min" placeholder="Min" min="0">
                    <span>—</span>
                    <input type="number" name="max" placeholder="Max" min="0">

                    <!-- Guardamos la búsqueda actual en secreto por si el usuario está buscando y filtrando al mismo tiempo -->
                    <input type="hidden" name="query" value="<%= request.getParameter("query") != null ? request.getParameter("query") : "" %>">

                    <button type="submit" style="padding: 5px 10px; border-radius: 5px; border: none; cursor:pointer;">Filtrar</button>
                </form>
            </div>

                        <!-- PRODUCTOS DINÁMICOS -->
             <div class="productos-grid">
                 <%@ page import="java.util.List" %>
                 <%@ page import="Modelo.Producto" %>
                 <%
                     // Recuperamos la lista que nos mandó el DashboardController
                     List<Producto> productos = (List<Producto>) request.getAttribute("productos");

                     if(productos != null && !productos.isEmpty()) {
                         for(Producto p : productos) {
                 %>
                            <div class="product-card">
                       
                        <img src="<%= (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : "default_product.png" %>" alt="Producto">
                        <h4><%= p.getTitulo() %></h4>
                        <span class="price">$<%= p.getPrecio() %></span>

                       
                        <span class="info"><i class="fa-solid fa-tag"></i> <%= p.getNombreCategoria() %></span>
                          
                        <div class="card-btns">
                            <% 
                                // Si el ID del vendedor es igual al ID del usuario activo, es su propio producto
                                if (p.getIdVendedor() == usuarioActivo.getIdUsuario()) { 
                            %>
                                <p style="color: #8e44ad; font-weight: bold; font-size: 0.9rem; text-align: center; width: 100%; margin: 10px 0;">
                                    <i class="fa-solid fa-star"></i> Este es un artículo tuyo
                                </p>
                            <% } else { %>
                                <!-- Botones normales para los productos de otras personas -->
                                <button class="btn-ver" onclick="window.location.href='ver_articulo.jsp?id=<%= p.getIdProducto() %>'">Ver Artículo</button>
                                <button class="btn-comprar" onclick="window.location.href='CarritoController?action=agregar&idProducto=<%= p.getIdProducto() %>'">Me interesa</button>
                            <% } %>
                        </div>
                    </div>
                 <%
                         }
                     } else {
                 %>
                         <p style="grid-column: 1 / -1; text-align: center; color: #555;">No se encontraron productos disponibles en este momento.</p>
                 <%  } %>
             </div>
        </main>
    </div>
             
             <!-- ================= VENTANAS MODALES ================= -->
    
    <!-- Modal de Notificaciones -->
    <dialog id="modalNotificaciones" class="modal-ayni">
        <div class="modal-header">
            <h3><i class="fa-solid fa-bell"></i> Mis Notificaciones</h3>
            <button class="btn-cerrar" onclick="cerrarModal('modalNotificaciones')"><i class="fa-solid fa-xmark"></i></button>
        </div>
        <div class="modal-body">
            <div class="item-vacio">
                <i class="fa-regular fa-face-smile-wink" style="font-size: 2rem; color: #ccc; margin-bottom: 10px;"></i>
                <p>¡Todo al día! No tienes notificaciones nuevas.</p>
            </div>
            
        </div>
    </dialog>

    <!-- Modal de Mensajes -->
    <dialog id="modalMensajes" class="modal-ayni">
        <div class="modal-header">
            <h3><i class="fa-solid fa-envelope"></i> Bandeja de Entrada</h3>
            <button class="btn-cerrar" onclick="cerrarModal('modalMensajes')"><i class="fa-solid fa-xmark"></i></button>
        </div>
        <div class="modal-body">
            <div class="item-vacio">
                <i class="fa-regular fa-comments" style="font-size: 2rem; color: #ccc; margin-bottom: 10px;"></i>
                <p>Aún no tienes mensajes sobre tus transacciones.</p>
            </div>
        </div>
    </dialog>

    <!-- Script para controlar abrir/cerrar -->
    <script>
        function abrirModal(id) {
            document.getElementById(id).showModal(); // showModal oscurece el fondo automáticamente
        }

        function cerrarModal(id) {
            document.getElementById(id).close();
        }
    </script>
</body>
</html>