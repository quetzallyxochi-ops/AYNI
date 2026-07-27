USE EcoRed_UPA;

-- -----------------------------------------------------
-- 1. CREAR TABLA CAMPAÑAS
-- -----------------------------------------------------
CREATE TABLE Campanas (
    id_campana INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    caracteristicas TEXT,
    id_fundacion INT NOT NULL,
    organizadores VARCHAR(200),
    cantidad_a_recaudar DECIMAL(10, 2) NOT NULL,
    cantidad_recaudada DECIMAL(10, 2) DEFAULT 0.00,
    porcentaje_recaudado DECIMAL(5, 2) DEFAULT 0.00, -- Máximo 999.99%
    ubicacion_ayuda TEXT,
    fecha DATE,
    imagen VARCHAR(255), -- Ruta o URL de la imagen de la campaña
    
    -- Relación con la tabla Fundaciones existente
    FOREIGN KEY (id_fundacion) REFERENCES Fundaciones(id_fundacion) ON DELETE RESTRICT
);

-- -----------------------------------------------------
-- 2. MODIFICAR TABLA DONACIONES
-- -----------------------------------------------------
-- Agregamos los campos id_campana y cantidad
ALTER TABLE Donaciones
ADD COLUMN id_campana INT DEFAULT NULL,
ADD COLUMN cantidad DECIMAL(10, 2) NOT NULL DEFAULT 1.00,
ADD CONSTRAINT fk_donaciones_campana FOREIGN KEY (id_campana) REFERENCES Campanas(id_campana) ON DELETE SET NULL;