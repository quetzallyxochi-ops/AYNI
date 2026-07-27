package Controlador;

import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Transaccion;
import Modelo.TransaccionDAO;
import Modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MisProductosController", urlPatterns = {"/MisProductosController"})
public class MisProductosController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Dentro del doGet en MisProductosController.java
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");

        if (usuarioActivo != null) {
            int idUsuario = usuarioActivo.getIdUsuario(); // Asumiendo que tu clase Usuario tiene este método

            // 1. Instanciar los DAO
            ProductoDAO productoDAO = new ProductoDAO();
            TransaccionDAO transaccionDAO = new TransaccionDAO();

            // 2. Obtener las listas
            List<Producto> activos = productoDAO.obtenerMisProductosActivos(idUsuario);
            List<Transaccion> ventas = transaccionDAO.obtenerVentasEnProceso(idUsuario);
            List<Transaccion> compras = transaccionDAO.obtenerComprasEnProceso(idUsuario);

            // 3. Guardar las listas en el request EXACTAMENTE con los nombres que espera el JSP
            request.setAttribute("misProductosActivos", activos);
            request.setAttribute("misVentas", ventas);
            request.setAttribute("misCompras", compras);

            // 4. Mandar a la vista usando FORWARD, ¡NUNCA sendRedirect para pasar datos!
            request.getRequestDispatcher("mis_productos.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.html");
        }
    }
}