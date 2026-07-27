<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Categoria" %>
<%@ page import="Modelo.Producto" %> <!-- Importamos el modelo Producto -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actualizar Producto | AYNI UPA</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="vender.css">
   
    <style>
        body {
           
            background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
            min-height: 100vh;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .header-vender {
            background-color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .btn-regresar {
            color: #e74c3c;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s;
        }
        
        .btn-regresar:hover { color: #c0392b; }

        .contenedor-formulario {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
        }

        .tarjeta-vender {
            background: white;
            width: 100%;
            max-width: 650px;
            border-radius: 15px; 
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            overflow: hidden; 
        }

        .tarjeta-header {
            background-color: #1a252f; 
            color: white; 
            padding: 25px;
            text-align: center;
        }

        .tarjeta-header h2 {
            margin: 0 0 10px 0;
            color: #ffffff; 
            font-size: 1.8rem;
        }

        .tarjeta-header p {
            margin: 0;
            color: #f1c40f !important; 
            font-weight: 500;
        }

        form#formActualizar {
            padding: 30px;
        }

        .grupo-input { margin-bottom: 20px; }
        
        .grupo-input label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #444;
        }

        .grupo-input input, .grupo-input select, .grupo-input textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #dcdcdc;
            border-radius: 8px;
            font-size: 1rem;
            box-sizing: border-box;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        .grupo-input input:focus, .grupo-input select:focus, .grupo-input textarea:focus {
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
            outline: none;
        }

        .grupo-input-doble {
            display: flex;
            gap: 20px;
        }

        .grupo-input-doble .grupo-input { flex: 1; }

        .upload-area {
            border: 2px dashed #bdc3c7;
            padding: 20px;
            text-align: center;
            border-radius: 8px;
            background: #f8f9fa;
        }

        .btn-submit-vender {
            width: 100%;
            padding: 15px;
            background-color: #e67e22;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.1s;
            margin-top: 10px;
        }

        .btn-submit-vender:hover { background-color: #d35400; }
        .btn-submit-vender:active { transform: scale(0.98); }
        
        /* Ajuste responsivo para celulares */
        @media (max-width: 600px) {
            .grupo-input-doble { flex-direction: column; gap: 0; }
        }
    </style>
</head>
<body>

    <%
        
        Producto producto = (Producto) request.getAttribute("producto");
        
        
        if(producto == null) {
            response.sendRedirect("MisProductosController");
            return;
        }
    %>

    <header class="header-vender">
        <div class="logo-container">
            <img src="logo.png" alt="Logo AYNI" class="logo" style="height: 40px;">
        </div>
        <a href="MisProductosController" class="btn-regresar"><i class="fa-solid fa-arrow-left"></i> Cancelar y Regresar</a>
    </header>

    <main class="contenedor-formulario">
        <div class="tarjeta-vender">
            <div class="tarjeta-header">
                <h2>Actualizar datos del artículo</h2>
               
                
                <p>
                    <i class="fa-solid fa-triangle-exclamation"></i> Nota: Al guardar, tu producto volverá a estado "Pendiente" de moderación.
                </p>
            </div>

           
            <form action="ModificarProductoController" method="POST" enctype="multipart/form-data" id="formActualizar">
                
                <!-- ID Oculto para que el controlador sepa qué producto actualizar -->
                <input type="hidden" name="id_producto" value="<%= producto.getIdProducto() %>">
                <input type="hidden" name="imagen_actual" value="<%= producto.getImagen() %>">

                <div class="grupo-input">
                    <label for="titulo"><i class="fa-solid fa-tag"></i> Título del Producto</label>
                    <input type="text" id="titulo" name="titulo" value="<%= producto.getTitulo() %>" required maxlength="100">
                </div>

                <div class="grupo-input-doble">
                    <div class="grupo-input">
                        <label for="categoria"><i class="fa-solid fa-layer-group"></i> Categoría</label>
                        <select id="categoria" name="id_categoria" required>
                            <option value="" disabled>Selecciona una...</option>
                            <%
                                List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
                                if(categorias != null) {
                                    for(Categoria cat : categorias) {
                                        // Validamos para dejar pre-seleccionada la categoría actual del producto
                                        String selected = (cat.getIdCategoria() == producto.getIdCategoria()) ? "selected" : "";
                            %>
                                        <option value="<%= cat.getIdCategoria() %>" <%= selected %> ><%= cat.getNombre() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="grupo-input">
                        <label for="precio"><i class="fa-solid fa-money-bill-wave"></i> Precio ($ MXN)</label>
                        <input type="number" id="precio" name="precio" value="<%= producto.getPrecio() %>" step="0.50" min="0" required>
                    </div>
                </div>

                <div class="grupo-input">
                    <label for="descripcion"><i class="fa-solid fa-align-left"></i> Descripción</label>
                
                    <textarea id="descripcion" name="descripcion" rows="4" required><%= producto.getDescripcion() %></textarea>
                </div>

                <div class="grupo-input">
                    <label for="imagen"><i class="fa-solid fa-camera"></i> Actualizar Fotografía (Opcional)</label>
                    <div class="upload-area">
                        <!-- Quitamos el 'required' porque el usuario podría no querer cambiar la foto -->
                        <input type="file" id="imagen" name="imagen" accept="image/png, image/jpeg, image/jpg">
                        <p style="font-size: 0.85rem; margin-top: 8px; color: #7f8c8d;">Si no seleccionas un archivo, se mantendrá la foto actual.</p>
                    </div>
                </div>

                <button type="submit" class="btn-submit-vender">Guardar Cambios <i class="fa-solid fa-floppy-disk"></i></button>
            </form>
        </div>
    </main>
</body>
</html>