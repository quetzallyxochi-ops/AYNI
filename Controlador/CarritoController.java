package Controlador;

import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CarritoController", urlPatterns = {"/CarritoController"})
public class CarritoController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Validar la sesión
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
        
        if (usuarioActivo == null) {
            response.sendRedirect("login.html?error=no_sesion");
            return;
        }

        // 2. Leer la acción a realizar
        String action = request.getParameter("action");
        int idUsuario = usuarioActivo.getIdUsuario();
        ProductoDAO dao = new ProductoDAO();
        
        if ("agregar".equals(action)) {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            boolean exito = dao.agregarAlCarrito(idUsuario, idProducto);
            
            if (exito) {
                response.sendRedirect("DashboardController?mensaje=reservado");
            } else {
                response.sendRedirect("DashboardController?error=reserva_fallida");
            }
            
        } else if ("eliminar".equals(action)) {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            boolean exito = dao.eliminarDelCarrito(idUsuario, idProducto);
            
            if (exito) {
                // CORRECCIÓN: Redirigir al controlador en lugar del JSP directo
                response.sendRedirect("CarritoController?action=ver&mensaje=eliminado");
            } else {
                response.sendRedirect("CarritoController?action=ver&error=error_eliminar");
            }
            
        } else if ("comprar".equals(action)) {
            boolean exito = dao.procesarCompraCarrito(idUsuario);
            
            if (exito) {
                // Redirigimos al inicio indicando que la compra fue exitosa (transacciones en proceso)
                response.sendRedirect("DashboardController?mensaje=compra_exitosa");
            } else {
                // CORRECCIÓN: Redirigir al controlador
                response.sendRedirect("CarritoController?action=ver&error=error_compra");
            }
            
        } else {
            // NUEVO: Acción por defecto (Ver el carrito)
            // Extrae los productos y los manda al JSP
            List<Producto> productosCarrito = dao.obtenerProductosDelCarrito(idUsuario);
            request.setAttribute("productosCarrito", productosCarrito);
            request.getRequestDispatcher("carrito.jsp").forward(request, response);
        }
    }
}