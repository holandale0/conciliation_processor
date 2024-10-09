-- DROP PROCEDURE public.save_integration_error(uuid, varchar, varchar, varchar, varchar, timestamp, varchar);

CREATE OR REPLACE PROCEDURE public.save_integration_error(IN p_id uuid, IN p_external_id character varying, IN p_document character varying, IN p_acquirer character varying, IN p_colector character varying, IN p_colect_datetime timestamp without time zone, IN p_error_message character varying)
 LANGUAGE plpgsql
AS $procedure$
BEGIN
    INSERT INTO integration_error (
        id,
        external_id,
        document,
        acquirer,
        colector,
        colect_datetime,
        error_message
    ) VALUES (
        p_id,
        p_external_id,
        p_document,
        p_acquirer,
        p_colector,
        p_colect_datetime,
        p_error_message
    );
END;
$procedure$
;
