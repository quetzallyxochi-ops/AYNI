package Controlador;

import Modelo.ProductoDAO;
import Modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ProductoController", urlPatterns = {"/ProductoController"})
public class ProductoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Validación de sesión
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
        
        if (usuarioActivo == null) {
            response.sendRedirect("login.html?error=no_sesion");
            return;
        }

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        
        if (action != null && idParam != null) {
            int idProducto = Integer.parseInt(idParam);
            ProductoDAO dao = new ProductoDAO();
            
            switch (action) {
                case "eliminar":
                    dao.eliminarProducto(idProducto);
                    break;
                case "donar":
                    dao.donarProducto(idProducto);
                    break;
                // El caso "modificar" generalmente redirige a un formulario de edición (vender_producto.jsp modificado)
            }
        }
        
        // Al terminar la acción, recargamos la página de Mis Productos
        response.sendRedirect("MisProductosController");
    }
}