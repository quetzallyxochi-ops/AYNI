
package Controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// La anotación debe coincidir exactamente con el "action" de tu formulario HTML
@WebServlet("/RegistroController")
public class RegistroController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener los datos del formulario (los nombres de los parámetros deben coincidir con los atributos 'name' del HTML)
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String tipoUsuario = request.getParameter("tipo_usuario"); // "Institucional" o "Local"
        String identificacion = request.getParameter("identificacion"); // El usuario captura esto en un solo campo
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        // 2. Instanciar el objeto Usuario y rellenarlo con los datos
        Modelo.Usuario nuevoUsuario = new Modelo.Usuario();
        nuevoUsuario.setNombres(nombres);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setTipoUsuario(tipoUsuario);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setPasswordHash(password); // Nota: En un futuro, aquí podrías agregar la encriptación de la contraseña

        // 3. Lógica para acomodar la identificación
        // Si es estudiante/personal, se va a matricula_upa. Si es externo, a identificacion_local.
        if ("Institucional".equals(tipoUsuario)) {
            nuevoUsuario.setMatriculaUpa(identificacion);
            nuevoUsuario.setIdentificacionLocal(null);
        } else {
            nuevoUsuario.setMatriculaUpa(null);
            nuevoUsuario.setIdentificacionLocal(identificacion);
        }

        // Configurar valores por defecto (opcional, aunque la BD y el DAO ya lo manejan)
        nuevoUsuario.setPerfilAnonimo(false);
        nuevoUsuario.setAliasAnonimo(null);
        nuevoUsuario.setEstadoCuenta("Activo");

        // 4. Llamar al Modelo (DAO) para guardar en la Base de Datos
        Modelo.UsuarioDAO dao = new Modelo.UsuarioDAO();
        boolean registroExitoso = dao.registrarUsuario(nuevoUsuario);

        // 5. Evaluar el resultado y redirigir
        if (registroExitoso) {
            // Si salió bien, lo mandamos al login para que inicie sesión, pasando un parámetro de éxito
            response.sendRedirect("login.html?registro=exitoso");
        } else {
            // Si hubo un error (ej. correo duplicado), lo regresamos al formulario con un mensaje de error
            response.sendRedirect("registro.html?error=correo_duplicado");
        }
    }
}