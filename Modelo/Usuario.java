
package Modelo;


public class Usuario {
     
    // Atributos privados (Encapsulamiento)
    private int idUsuario;
    private String tipoUsuario; // 'Institucional' o 'Local'
    private String nombres;
    private String apellidos;
    private String correo;
    private String passwordHash;
    private String matriculaUpa;
    private String identificacionLocal;
    private boolean perfilAnonimo;
    private String aliasAnonimo;
    private String estadoCuenta; // 'Activo' o 'Suspendido'
    private String imagen;

    // Constructor vacío (necesario para instanciar el objeto antes de llenarlo)
    public Usuario() {
    }

    // Constructor con todos los campos (excepto ID, que es autoincrementable)
    public Usuario(String tipoUsuario, String nombres, String apellidos, String correo, 
                   String passwordHash, String matriculaUpa, String identificacionLocal, 
                   boolean perfilAnonimo, String aliasAnonimo, String estadoCuenta, String imagen) {
        this.tipoUsuario = tipoUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.passwordHash = passwordHash;
        this.matriculaUpa = matriculaUpa;
        this.identificacionLocal = identificacionLocal;
        this.perfilAnonimo = perfilAnonimo;
        this.aliasAnonimo = aliasAnonimo;
        this.estadoCuenta = estadoCuenta;
        this.imagen =imagen;
    }

    // --- GETTERS Y SETTERS ---
    // (Estos métodos permiten leer y modificar los datos de forma segura)

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getMatriculaUpa() { return matriculaUpa; }
    public void setMatriculaUpa(String matriculaUpa) { this.matriculaUpa = matriculaUpa; }

    public String getIdentificacionLocal() { return identificacionLocal; }
    public void setIdentificacionLocal(String identificacionLocal) { this.identificacionLocal = identificacionLocal; }

    public boolean isPerfilAnonimo() { return perfilAnonimo; }
    public void setPerfilAnonimo(boolean perfilAnonimo) { this.perfilAnonimo = perfilAnonimo; }

    public String getAliasAnonimo() { return aliasAnonimo; }
    public void setAliasAnonimo(String aliasAnonimo) { this.aliasAnonimo = aliasAnonimo; }

    public String getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
    
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
 
}
