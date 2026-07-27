package Controlador;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import Modelo.Producto;
import Modelo.ProductoDAO;

// ¡IMPORTANTE! Esta etiqueta permite recibir archivos (imágenes) en el Servlet
@MultipartConfig
@WebServlet(name = "VenderController", urlPatterns = {"/VenderController"})
public class VenderController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
                // 1. Obtener la sesión activa para sacar el ID del vendedor
         HttpSession session = request.getSession();
         Modelo.Usuario usuarioActivo = (Modelo.Usuario) session.getAttribute("usuario");

         // Validación de seguridad por si la sesión expiró justo antes de enviar el formulario
         if (usuarioActivo == null) {
             response.sendRedirect("login.html?error=no_sesion");
             return; // Detiene la ejecución
         }

         // Extraemos el ID dinámico del usuario que está publicando
         int idVendedor = usuarioActivo.getIdUsuario();

        // 2. Obtener los datos de texto del formulario
        String titulo = request.getParameter("titulo");
       int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
        
        String descripcion = request.getParameter("descripcion");
        double precio = Double.parseDouble(request.getParameter("precio"));
        
        // 3. Procesamiento de la IMAGEN
        Part filePart = request.getPart("imagen"); // Obtiene el archivo subido
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
        
        // Definir la ruta donde se guardará (Debe existir esta carpeta en tu proyecto web)
        String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img" + File.separator + "productos";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs(); // Crea la carpeta si no existe
        
        // Guardar el archivo en el servidor
        File file = new File(uploadPath, fileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
        // La ruta que guardaremos en la base de datos (Ej. "assets/img/productos/foto.png")
        String rutaBaseDatos = "assets/img/productos/" + fileName;

        // 4. Crear el objeto Producto (omitiendo los automáticos)
        Producto p = new Producto();
        p.setIdVendedor(idVendedor);
        p.setTitulo(titulo);
        p.setIdCategoria(idCategoria);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setImagen(rutaBaseDatos);
        
        // Los estados y la fecha se asignarán directamente en el ProductoDAO (Pendiente, Disponible, CURDATE())
        
        // 5. Enviar a la Base de Datos
        ProductoDAO dao = new ProductoDAO();
        boolean insertado = dao.insertarProducto(p);
        
        if(insertado) {
            // Regresamos a la vista activando el modal de éxito
            request.setAttribute("mensaje", "exito");
            request.getRequestDispatcher("vender_producto.jsp").forward(request, response);
        } else {
            // Manejo de error básico
            response.sendRedirect("vender_producto.jsp?error=true");
        }
    }
}