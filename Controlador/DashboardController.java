package Controlador;

import Modelo.Categoria;
import Modelo.CategoriaDAO;
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

@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Validar seguridad (que la sesión exista)
        HttpSession sesion = request.getSession();
        Usuario usuarioActivo = (Usuario) sesion.getAttribute("usuario");
        
        if (usuarioActivo == null) {
            response.sendRedirect("login.html?error=no_sesion");
            return;
        }

        // 2. Atrapar parámetros de búsqueda o categoría (si los hay)
       
        String busqueda = request.getParameter("query");
        String categoria = request.getParameter("categoria");
        String minStr = request.getParameter("min");
        String maxStr = request.getParameter("max");

        Double min = (minStr != null && !minStr.isEmpty()) ? Double.parseDouble(minStr) : null;
        Double max = (maxStr != null && !maxStr.isEmpty()) ? Double.parseDouble(maxStr) : null;

        // 3. Consultar la base de datos
        ProductoDAO dao = new ProductoDAO();
        List<Producto> listaProductos = dao.obtenerProductosDisponibles(busqueda, categoria, min, max);

        // 4. Enviar los datos a la pantalla (JSP)
        request.setAttribute("productos", listaProductos);
        
        CategoriaDAO catDAO = new CategoriaDAO();
        List<Categoria> listaCategorias = catDAO.obtenerTodas();
        request.setAttribute("categorias", listaCategorias);
        
        // Redirigir conservando los datos
        request.getRequestDispatcher("marketplace_upa.jsp").forward(request, response);
    }
}