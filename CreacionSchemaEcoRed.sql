-- 1. Crear la base de datos y usarla
CREATE DATABASE IF NOT EXISTS EcoRed_UPA;
USE EcoRed_UPA;

-- 2. Crear tabla Usuarios
CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    tipo_usuario ENUM('Institucional', 'Local') NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    matricula_upa VARCHAR(50) DEFAULT NULL,
    identificacion_local VARCHAR(50) DEFAULT NULL,
    perfil_anonimo BOOLEAN DEFAULT FALSE,
    alias_anonimo VARCHAR(50) DEFAULT NULL,
    estado_cuenta ENUM('Activo', 'Suspendido') DEFAULT 'Activo'
);

-- 3. Crear tabla Categorias
CREATE TABLE Categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

-- 4. Crear tabla Productos (Marketplace)
CREATE TABLE Productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_vendedor INT NOT NULL,
    id_categoria INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    estado_moderacion ENUM('Pendiente', 'Aprobado_IA', 'Rechazado_IA') DEFAULT 'Pendiente',
    estado_venta ENUM('Disponible', 'Reservado', 'Vendido', 'Expirado') DEFAULT 'Disponible',
    fecha_publicacion DATE NOT NULL,
    -- Llaves foráneas
    FOREIGN KEY (id_vendedor) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES Categorias(id_categoria) ON DELETE RESTRICT
);


-- 5. Crear tabla Fundaciones
CREATE TABLE Fundaciones (
    id_fundacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    representante VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT,
    estado ENUM('Activa', 'Inactiva') DEFAULT 'Activa'
);

-- 6. Crear tabla Donaciones 
CREATE TABLE Donaciones (
    id_donacion INT AUTO_INCREMENT PRIMARY KEY,
    id_donante INT NOT NULL,
    id_producto_origen INT DEFAULT NULL, 
    id_categoria INT NOT NULL,
    descripcion_estado TEXT,
    estado_entrega ENUM('En_Acopio_UPA', 'Asignada', 'Entregada_a_Fundacion') DEFAULT 'En_Acopio_UPA',
    fecha_donacion DATE NOT NULL,
    -- Llaves foráneas
    FOREIGN KEY (id_donante) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_producto_origen) REFERENCES Productos(id_producto) ON DELETE SET NULL,
    FOREIGN KEY (id_categoria) REFERENCES Categorias(id_categoria) ON DELETE RESTRICT
);

-- 7. Crear tabla Transacciones de Donaciones
CREATE TABLE Transacciones_Donaciones (
    id_transaccion_d INT AUTO_INCREMENT PRIMARY KEY,
    id_donacion INT NOT NULL,
    id_fundacion INT NOT NULL,
    id_admin_upa INT NOT NULL, -- Relacionado al administrador de la escuela
    fecha_entrega DATETIME DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    -- Llaves foráneas
    FOREIGN KEY (id_donacion) REFERENCES Donaciones(id_donacion) ON DELETE CASCADE,
    FOREIGN KEY (id_fundacion) REFERENCES Fundaciones(id_fundacion) ON DELETE RESTRICT,
    FOREIGN KEY (id_admin_upa) REFERENCES Usuarios(id_usuario) ON DELETE RESTRICT
);

-- 8. Crear tabla Transacciones
CREATE TABLE Transacciones (
    id_transaccion INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_comprador INT NOT NULL,
    id_vendedor INT NOT NULL,
    confirma_comprador ENUM('Pendiente', 'Exitosa', 'Cancelada') DEFAULT 'Pendiente',
    confirma_vendedor ENUM('Pendiente', 'Exitosa', 'Cancelada') DEFAULT 'Pendiente',
    estado_global ENUM('En_Proceso', 'Completada', 'Cancelada', 'Conflicto') DEFAULT 'En_Proceso',
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATETIME DEFAULT NULL,
    
    -- Llaves foráneas
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto) ON DELETE RESTRICT,
    FOREIGN KEY (id_comprador) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_vendedor) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
); 
-- 9. Crear tabla carrito
CREATE TABLE Carrito (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_producto INT NOT NULL,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

-- 10. Crear tabla Campañas
CREATE TABLE Campanas (
    id_campana INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150),
    descripcion TEXT,
    caracteristicas TEXT,
    id_fundacion INT,
    organizadores VARCHAR(200),
    cantidad_a_recaudar INT,
    cantidad_recaudada INT,
    porcentaje_recaudado DECIMAL(3,2),
    ubicacion_ayuda TEXT,
    fecha DATE,
    imagen VARCHAR(255),
    CONSTRAINT fk_fundacion FOREIGN KEY (id_fundacion) 
        REFERENCES fundaciones(id_fundacion)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);