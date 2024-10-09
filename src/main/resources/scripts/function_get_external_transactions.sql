-- DROP FUNCTION public.get_external_transactions(date, date, varchar);

CREATE OR REPLACE FUNCTION public.get_external_transactions(start_date date, end_date date, customer_document character varying)
 RETURNS TABLE(id uuid, external_receivable_id uuid, external_id character varying, acquirer character varying, colector character varying, document character varying, transaction_datetime timestamp without time zone, enqueue_datetime timestamp without time zone, colect_datetime timestamp without time zone, net_value numeric, gross_value numeric, fee numeric, fee_value numeric, card_flag character varying, card_number character varying, total_installments integer, installment integer, transaction_code character varying, authorization_code character varying, transaction_type character varying, payment_channel character varying, destaxa_transaction character varying, terminal_id character varying, lot_code character varying, lot_status character varying, sales_id character varying, sales_code character varying, usn character varying, concilied boolean, updated_at timestamp without time zone, settlement_datetime timestamp without time zone, settlement_type character varying, event_name character varying)
 LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY

    /*
     *  Retorna todos os registros de external_transaction, left join com external_receivable
     *  Do cliente "customer_document", entre as datas "start_date" e "end_date", que estão como "concilied = null"
     *  E o tipo de transação é 'Transação' ou 'Antecipação'
     */

    SELECT
        et.id,
        er.id AS external_receivable_id,
        et.external_id,
        et.acquirer,
        et.colector,
        et."document",
        et.transaction_datetime,
        et.enqueue_datetime,
        et.colect_datetime,
        et.net_value,
        et.gross_value,
        et.fee,
        et.fee_value,
        et.card_flag,
        et.card_number,
        CAST(et.total_installments AS INTEGER),
        CAST(et.installment AS INTEGER),
        et.transaction_code,
        et.authorization_code,
        et.transaction_type,
        et.payment_channel,
        et.destaxa_transaction,
        et.terminal_id,
        et.lot_code,
        et.lot_status,
        et.sales_id,
        et.sales_code,
        et.usn,
        et.concilied,
        et.updated_at,
        er.settlement_datetime,
        er.settlement_type,
        er.event_name
    FROM
        external_transaction et
    LEFT JOIN
        external_receivable er
    ON
        et.external_id = er.external_id
        AND et.acquirer = er.acquirer
        AND et.usn = er.usn
    WHERE
        et.transaction_datetime::date BETWEEN start_date AND end_date
        AND et."document" = customer_document
        AND et.concilied IS NULL
        AND (er.event_name IN ('Transações', 'Antecipações') OR er.event_name IS NULL)

    union

     /*
     *  Retorna todos os registros de external_receivable
     *  Do cliente "customer_document", entre as datas "start_date" e "end_date", que estão como "concilied = null"
     *  E o tipo de transação é diferente de 'Transação' e 'Antecipação'
     */

    SELECT
        NULL AS id,
        er.id AS external_receivable_id,
        er.external_id,
        er.acquirer,
        er.colector,
        er."document",
        NULL AS transaction_datetime,
        er.enqueue_datetime,
        er.colect_datetime,
        er.net_value,
        er.gross_value,
        er.fee,
        er.fee_value,
        er.card_flag,
        er.card_number,
        CAST(er.total_installments AS INTEGER),
        CAST(er.installment AS INTEGER),
        er.transaction_code,
        er.authorization_code,
        er.transaction_type,
        er.payment_channel,
        er.destaxa_transaction,
        er.terminal_id,
        er.lot_code,
        er.lot_status,
        NULL AS sales_id,
        NULL AS sales_code,
        er.usn,
        NULL AS concilied,
        NULL AS updated_at,
        er.settlement_datetime,
        er.settlement_type,
        er.event_name
    FROM
        external_receivable er
    WHERE
        er.settlement_datetime::date BETWEEN start_date AND end_date
        AND er."document" = customer_document
        AND er.concilied IS NULL
        AND er.event_name IS NOT NULL
        AND er.event_name NOT IN ('Transações', 'Antecipações')

    union

     /*
     *  Retorna todos os registros de external_receivable, inner join com external_transaction
     *  Do cliente "customer_document", onde a data de liquidação é maior que a data atual
     *  E estão como "concilied = false"
     */

    SELECT
        et.id,
        er.id AS external_receivable_id,
        er.external_id,
        er.acquirer,
        er.colector,
        er."document",
        et.transaction_datetime,
        er.enqueue_datetime,
        er.colect_datetime,
        er.net_value,
        er.gross_value,
        er.fee,
        er.fee_value,
        er.card_flag,
        er.card_number,
        CAST(er.total_installments AS INTEGER),
        CAST(er.installment AS INTEGER),
        er.transaction_code,
        er.authorization_code,
        er.transaction_type,
        er.payment_channel,
        er.destaxa_transaction,
        er.terminal_id,
        er.lot_code,
        er.lot_status,
        et.sales_id,
        et.sales_code,
        er.usn,
        er.concilied,
        er.updated_at,
        er.settlement_datetime,
        er.settlement_type,
        er.event_name
    FROM
        external_receivable er
     JOIN
        external_transaction et
        ON
        er.external_id = et.external_id
        AND er.acquirer = et.acquirer
        AND er.usn = et.usn
    WHERE
        er.settlement_datetime::date > now()::date
        AND er."document" = customer_document
        AND er.concilied = false;
END;
$function$
;
