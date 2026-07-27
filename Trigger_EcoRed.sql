USE EcoRed_UPA;
DELIMITER //

DROP TRIGGER IF EXISTS mover_a_donacion_after_update //

CREATE TRIGGER mover_a_donacion_after_update
AFTER UPDATE ON Productos
FOR EACH ROW
BEGIN
    IF NEW.estado_venta = 'Expirado' AND OLD.estado_venta != 'Expirado' THEN
        INSERT INTO Donaciones (
            id_donante, 
            id_producto_origen, 
            id_categoria, 
            descripcion_estado, 
            estado_entrega, 
            fecha_donacion,
            cantidad,       -- Nuevo campo añadido
            id_campana,  
            id_transacción-- Nuevo campo añadido
        ) 
        VALUES (
            NEW.id_vendedor, 
            NEW.id_producto, 
            NEW.id_categoria, 
            CONCAT('Artículo expirado del Marketplace: ', NEW.titulo), 
            'En_Acopio_UPA', 
            CURDATE(),
            1,              -- Al ser un artículo del inventario, la cantidad es 1
            NULL,            -- No se asigna a ninguna campaña inicialmente
            NULL			-- No se asigna a ninguna transacción inicialmente
        );
    END IF;
END;
//

DELIMITER ;