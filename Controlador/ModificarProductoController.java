package Controlador;

import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Usuario;
import Modelo.Categoria;    
import Modelo.CategoriaDAO; 

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

// Añadimos la configuración para permitir la subida de archivos (imágenes)
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
@WebServlet(name = "ModificarProductoController", urlPatterns = {"/ModificarProductoController"})
public class ModificarProductoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
        
        if (usuarioActivo == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Recibimos el ID del producto que viene en el enlace <a>
        String idParam = request.getParameter("id");
        
        if (idParam != null) {
            int idProducto = Integer.parseInt(idParam);
            
            // Buscamos el producto en la BD
            ProductoDAO productoDAO = new ProductoDAO();
            Producto producto = productoDAO.obtenerProductoPorId(idProducto);
            
            // Validación de seguridad: Asegurarnos que el producto le pertenece al usuario logueado
            if (producto != null && producto.getIdVendedor() == usuarioActivo.getIdUsuario()) {
                
                // Guardamos el producto para que actualizar_producto.jsp lo pueda leer
                request.setAttribute("producto", producto);
                
                // Consultamos las categorías para mandarlas a la vista
                CategoriaDAO catDAO = new CategoriaDAO();
                List<Categoria> categorias = catDAO.obtenerTodas();
                request.setAttribute("categorias", categorias);
                
                // Redirigimos a la vista enviando los datos
                request.getRequestDispatcher("actualizar_producto.jsp").forward(request, response);
                
            } else {
                response.sendRedirect("MisProductosController");
            }
        } else {
            response.sendRedirect("MisProductosController");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Aseguramos que los caracteres especiales (como acentos) se lean bien
        request.setCharacterEncoding("UTF-8");
        
        // Validación de sesión
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
        if (usuarioActivo == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            // 1. Recibir los parámetros de texto del formulario
            int idProducto = Integer.parseInt(request.getParameter("id_producto"));
            String titulo = request.getParameter("titulo");
            int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            String descripcion = request.getParameter("descripcion");
            String imagenActual = request.getParameter("imagen_actual");

            // 2. Procesar la imagen opcional
            Part filePart = request.getPart("imagen");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Por defecto, mantenemos la imagen que ya tenía el producto
            String rutaFinalImagen = imagenActual; 

            // Si el nombre del archivo no está vacío, significa que subieron una nueva foto
            if (fileName != null && !fileName.isEmpty()) {
                // Generamos la ruta donde se guardará (ajusta "img" si tu carpeta se llama distinto)
                String uploadPath = getServletContext().getRealPath("") + File.separator + "img";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir(); // Crea la carpeta si no existe
                }
                
                // Guardamos el archivo físicamente en el servidor
                filePart.write(uploadPath + File.separator + fileName);
                
                // Actualizamos la ruta que se guardará en la base de datos
                rutaFinalImagen = "img/" + fileName; 
            }

            // 3. Crear y llenar el objeto Producto con los nuevos datos
            Producto productoEditado = new Producto();
            productoEditado.setIdProducto(idProducto);
            productoEditado.setTitulo(titulo);
            productoEditado.setIdCategoria(idCategoria);
            productoEditado.setPrecio(precio);
            productoEditado.setDescripcion(descripcion);
            productoEditado.setImagen(rutaFinalImagen);

            // 4. Mandar a actualizar a la base de datos
            ProductoDAO dao = new ProductoDAO();
            boolean exito = dao.actualizarProducto(productoEditado);

            // 5. Redirigir según el resultado
            if (exito) {
                response.sendRedirect("MisProductosController?mensaje=actualizado");
            } else {
                response.sendRedirect("MisProductosController?mensaje=error_bd");
            }

        } catch (Exception e) {
            System.err.println("❌ Error en ModificarProductoController (POST): " + e.getMessage());
            response.sendRedirect("MisProductosController?mensaje=error_servidor");
        }
    }
}