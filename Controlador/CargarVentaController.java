package Controlador;

import Modelo.Categoria;
import Modelo.CategoriaDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CargarVentaController")
public class CargarVentaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Consultar las categorías a la base de datos
        CategoriaDAO dao = new CategoriaDAO();
        List<Categoria> listaCategorias = dao.obtenerTodas();
        
        // 2. Enviar la lista al JSP
        request.setAttribute("categorias", listaCategorias);
        
        // 3. Redirigir a la vista de ventas
        request.getRequestDispatcher("vender_producto.jsp").forward(request, response);
    }
}