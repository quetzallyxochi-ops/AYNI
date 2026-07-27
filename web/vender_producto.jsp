<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vender Producto | AYNI UPA</title>
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
   <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="vender.css">
</head>
<body>

    <!-- Encabezado simple -->
    <header class="header-vender">
        <div class="logo-container">
            
            <img src="logo2.png" alt="Logo AYNI" class="logo">
        </div>
        <a href="DashboardController" class="btn-regresar"><i class="fa-solid fa-arrow-left"></i> Regresar al Inicio</a>
    </header>

    <main class="contenedor-formulario">
        <div class="tarjeta-vender">
            <div class="tarjeta-header">
                <h2>Publicar un nuevo artículo</h2>
                <p>Únete a la economía circular de la UPA</p>
            </div>

          
            <form action="VenderController" method="POST" enctype="multipart/form-data" id="formVender">
                
                <div class="grupo-input">
                    <label for="titulo"><i class="fa-solid fa-tag"></i> Título del Producto</label>
                    <input type="text" id="titulo" name="titulo" placeholder="Ej. Calculadora Científica Casio" required maxlength="100">
                </div>

                <div class="grupo-input-doble">
                    <div class="grupo-input">
                        <label for="categoria"><i class="fa-solid fa-layer-group"></i> Categoría</label>
                                                <%@ page import="java.util.List" %>
                    <%@ page import="Modelo.Categoria" %>

                   
                        <select id="categoria" name="id_categoria" required>
                            <option value="" disabled selected>Selecciona una...</option>
                            <%
                                List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
                                if(categorias != null) {
                                    for(Categoria cat : categorias) {
                            %>
                                       
                                        <option value="<%= cat.getIdCategoria() %>"><%= cat.getNombre() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="grupo-input">
                        <label for="precio"><i class="fa-solid fa-money-bill-wave"></i> Precio ($ MXN)</label>
                        <input type="number" id="precio" name="precio" placeholder="0.00" step="0.50" min="0" required>
                    </div>
                </div>

                <div class="grupo-input">
                    <label for="descripcion"><i class="fa-solid fa-align-left"></i> Descripción</label>
                    <textarea id="descripcion" name="descripcion" rows="4" placeholder="Describe el estado de tu producto, detalles, tiempo de uso..." required></textarea>
                </div>

                <div class="grupo-input">
                    <label for="imagen"><i class="fa-solid fa-camera"></i> Fotografía del Producto</label>
                    <div class="upload-area">
                        <input type="file" id="imagen" name="imagen" accept="image/png, image/jpeg, image/jpg" required>
                    </div>
                </div>

                <button type="submit" class="btn-submit-vender">Subir al market <i class="fa-solid fa-cloud-arrow-up"></i></button>
            </form>
        </div>
    </main>

    <!-- Modal de Éxito (Oculto por defecto) -->
    <dialog id="modalExito" class="modal-ayni">
        <div class="modal-body centrado">
            <i class="fa-solid fa-circle-check icon-exito"></i>
            <h3>¡Gracias por subir al market!</h3>
            <p>Espere la confirmación de moderación para ver su producto disponible en la plataforma.</p>
            <a href="DashboardController" class="btn-aceptar">Entendido</a>
        </div>
    </dialog>

    <!-- Para mostrar el modal si el registro fue exitoso -->
    <% if(request.getAttribute("mensaje") != null && request.getAttribute("mensaje").equals("exito")) { %>
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                document.getElementById('modalExito').showModal();
            });
        </script>
    <% } %>

</body>
</html>