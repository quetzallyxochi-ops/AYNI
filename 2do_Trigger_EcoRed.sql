USE EcoRed_UPA;
DELIMITER //

DROP TRIGGER IF EXISTS evaluar_confirmaciones_transaccion //

CREATE TRIGGER evaluar_confirmaciones_transaccion
BEFORE UPDATE ON Transacciones
FOR EACH ROW
BEGIN
    -- ESCENARIO 1: Ambos confirman compra/venta exitosa
    IF NEW.confirma_comprador = 'Exitosa' AND NEW.confirma_vendedor = 'Exitosa' THEN
        -- 1. Actualizamos el estado global de la transacción
        SET NEW.estado_global = 'Completada';
        SET NEW.fecha_cierre = NOW();
        
        -- 2. Actualizamos el producto a 'Vendido'
        UPDATE Productos 
        SET estado_venta = 'Vendido' 
        WHERE id_producto = NEW.id_producto;

    -- ESCENARIO 2: Ambos cancelan la transacción (no les gustó el artículo, no se vieron, etc.)
    ELSEIF NEW.confirma_comprador = 'Cancelada' AND NEW.confirma_vendedor = 'Cancelada' THEN
        -- 1. Cancelamos la transacción
        SET NEW.estado_global = 'Cancelada';
        SET NEW.fecha_cierre = NOW();
        
        -- 2. El producto regresa al catálogo para que alguien más lo compre
        UPDATE Productos 
        SET estado_venta = 'Disponible' 
        WHERE id_producto = NEW.id_producto;

    -- ESCENARIO 3: Conflicto (Uno dice que fue exitosa y el otro que se canceló)
    ELSEIF (NEW.confirma_comprador = 'Exitosa' AND NEW.confirma_vendedor = 'Cancelada') OR 
           (NEW.confirma_comprador = 'Cancelada' AND NEW.confirma_vendedor = 'Exitosa') THEN
        -- 1. Se marca como conflicto para revisión de un administrador
        SET NEW.estado_global = 'Conflicto';
        
        -- Nota: No actualizamos el producto aún, se queda como 'Reservado' 
        -- hasta que el administrador de la UPA resuelva el problema.
    END IF;
END;
//

DELIMITER ;