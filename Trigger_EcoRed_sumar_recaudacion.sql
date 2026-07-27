USE EcoRed_UPA;

DELIMITER //

DROP TRIGGER IF EXISTS sumar_recaudacion_campana //

CREATE TRIGGER sumar_recaudacion_campana
AFTER UPDATE ON Donaciones
FOR EACH ROW
BEGIN
    -- Verificamos si el estado cambió a 'Asignada' y si la donación está ligada a una campaña
    IF NEW.estado_entrega = 'Asignada' AND OLD.estado_entrega != 'Asignada' AND NEW.id_campana IS NOT NULL THEN
        
        -- Actualizamos la campaña vinculada
        UPDATE Campanas 
        SET 
            cantidad_recaudada = cantidad_recaudada + NEW.cantidad,
            -- Calculamos el porcentaje, asegurando que cantidad_a_recaudar > 0 para evitar error de división por cero
            porcentaje_recaudado = IF(cantidad_a_recaudar > 0, 
                                     ((cantidad_recaudada + NEW.cantidad) / cantidad_a_recaudar) * 100, 
                                     0)
        WHERE id_campana = NEW.id_campana;
        
    END IF;
END;
//

DELIMITER ;