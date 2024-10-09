-- DROP PROCEDURE public.save_update_colector_data_log(uuid, varchar, varchar, varchar, timestamp);

CREATE OR REPLACE PROCEDURE public.save_update_colector_data_log(IN p_client_id uuid, IN p_external_id character varying, IN p_acquirer character varying, IN p_colector character varying, IN p_last_colect_datetime timestamp without time zone)
 LANGUAGE plpgsql
AS $procedure$
BEGIN
    BEGIN
        -- Atualizar a tabela colector_data_log
        UPDATE colector_data_log
        SET
            last_colect_datetime = p_last_colect_datetime,
            colect_now = false
        WHERE
            external_id = p_external_id
            AND LOWER(acquirer) = LOWER(p_acquirer)
            AND LOWER(colector) = LOWER(p_colector);

        -- Atualizar a tabela task_colector
        UPDATE task_colector
        SET
            last_colect_datetime = p_last_colect_datetime,
            colect_now = false
        WHERE
            external_id = p_external_id
            AND LOWER(acquirer) = LOWER(p_acquirer)
            AND LOWER(colector) = LOWER(p_colector);
    END;
END;
$procedure$
;
