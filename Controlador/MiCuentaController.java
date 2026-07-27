package Controlador;

import Modelo.Usuario;
import Modelo.UsuarioDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "MiCuentaController", urlPatterns = {"/MiCuentaController"})
// Configuraciones requeridas para procesar archivos "multipart/form-data"
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class MiCuentaController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");

        if (usuarioActivo == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Recuperar datos de texto
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String correo = request.getParameter("correo");
        String aliasAnonimo = request.getParameter("aliasAnonimo");
        
        // Manejo de la subida de imagen
        Part filePart = request.getPart("imagen");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Conservamos la ruta actual en caso de que el usuario no suba una foto nueva
        String rutaImagen = usuarioActivo.getImagen(); 
        
       if (fileName != null && !fileName.isEmpty()) {
            // Modificado para apuntar a la carpeta assets/img/Usuarios
            String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img" + File.separator + "Usuarios";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Usamos mkdirs() para crear toda la ruta si no existe
            }
            
            // Guardamos el archivo en el servidor
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);
            
            // Guardamos la ruta relativa exacta para la base de datos
            rutaImagen = "assets/img/Usuarios/" + fileName; 
        }

        // Actualizar el objeto Usuario actual
        usuarioActivo.setNombres(nombres);
        usuarioActivo.setApellidos(apellidos);
        usuarioActivo.setCorreo(correo);
        usuarioActivo.setAliasAnonimo(aliasAnonimo);
        usuarioActivo.setImagen(rutaImagen);

        // Guardar cambios en la Base de Datos
        UsuarioDAO dao = new UsuarioDAO();
        if (dao.actualizarPerfil(usuarioActivo)) {
            // Actualizar la sesión para que la Navbar y otras pantallas detecten los cambios
            session.setAttribute("usuario", usuarioActivo);
            response.sendRedirect("mi_cuenta.jsp?msg=exito");
        } else {
            response.sendRedirect("mi_cuenta.jsp?error=db");
        }
    }
}