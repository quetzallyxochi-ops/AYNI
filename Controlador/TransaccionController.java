package Controlador;

import Modelo.TransaccionDAO;
import Modelo.Usuario;
// import Modelo.TransaccionDAO; // Descomenta esto cuando tengas tus métodos en el DAO

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "TransaccionController", urlPatterns = {"/TransaccionController"})
public class TransaccionController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
        
        // Verificamos que la sesión siga activa
        if (usuarioActivo == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Recibimos los datos enviados desde el modal oculto
        String idTransaccionStr = request.getParameter("id");
        String action = request.getParameter("action");
        String password = request.getParameter("password");
        
        if (idTransaccionStr != null && action != null && password != null) {
            int idTransaccion = Integer.parseInt(idTransaccionStr);
            
            // 1. Aquí validas la contraseña (ejemplo asumiendo que tu objeto Usuario tiene getPassword/getContrasena)
            if (usuarioActivo.getPasswordHash().equals(password)) {
                
                 TransaccionDAO dao = new TransaccionDAO();
                
                // 2. Evaluamos qué botón presionó el usuario y ejecutamos el DAO correspondiente
                switch (action) {
                    case "venta_exitosa":
                        dao.confirmarVentaExitosa(idTransaccion);
                        break;
                    case "venta_cancelada":
                         dao.cancelarVenta(idTransaccion);
                        break;
                    case "compra_exitosa":
                         dao.confirmarCompraExitosa(idTransaccion);
                        break;
                    case "compra_cancelada":
                         dao.cancelarCompra(idTransaccion);
                        break;
                }
            } else {
            //     Opcional: Mandar un mensaje de error de contraseña incorrecta
             }
        }
        
        // 3. Finalmente, redirigimos a la vista de mis productos para ver los cambios
        response.sendRedirect("MisProductosController");
    }
    
    // Mantenemos el doGet por si el usuario intenta acceder a la URL directamente,
    // simplemente lo regresamos a sus productos.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("MisProductosController");
    }
}