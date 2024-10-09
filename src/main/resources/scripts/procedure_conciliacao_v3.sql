-- DROP PROCEDURE public.process_conciliation_v3(uuid, varchar, varchar, varchar, varchar, varchar, varchar, numeric, numeric, numeric, numeric, varchar, varchar, int4, int4, varchar, varchar, varchar, varchar, varchar, varchar, varchar, varchar, varchar, varchar, uuid);

CREATE OR REPLACE PROCEDURE public.process_conciliation_v3(IN in_external_transaction_id uuid, IN in_external_id character varying, IN in_acquirer character varying, IN in_colector character varying, IN in_document character varying, IN in_transaction_datetime character varying, IN in_settlement_datetime character varying, IN in_net_value numeric, IN in_gross_value numeric, IN in_fee numeric, IN in_fee_value numeric, IN in_card_flag character varying, IN in_card_number character varying, IN in_total_installments integer, IN in_installment integer, IN in_transaction_code character varying, IN in_authorization_code character varying, IN in_transaction_type character varying, IN in_payment_channel character varying, IN in_terminal_id character varying, IN in_lot_code character varying, IN in_sales_code character varying, IN in_usn character varying, IN in_settlement_type character varying, IN in_event character varying, IN in_external_receivable_id uuid)
 LANGUAGE plpgsql
AS $procedure$
DECLARE

    transaction_record RECORD := null;
    divergence_message TEXT := '';  -- Variável para armazenar divergências
    transaction_datetime_converted TIMESTAMP WITHOUT TIME zone := null;
    settlement_datetime_converted TIMESTAMP WITHOUT TIME zone := null;
    customer_fee_record RECORD := null;
   	has_settlement boolean := null;
    has_fee boolean := null;

BEGIN

    -- Conversão dos parâmetros string para timestamp sem fuso horário

    transaction_datetime_converted := to_timestamp(in_transaction_datetime, 'YYYY-MM-DD HH24:MI:SS');
    settlement_datetime_converted := to_timestamp(in_settlement_datetime, 'YYYY-MM-DD HH24:MI:SS');

    -- Regra 1: Se in_transaction_type ILIKE '%PIX%' e in_colector = 'SHIPAY', seguir fluxo específico para PIX

    IF UPPER(in_transaction_type) ILIKE '%PIX%' AND in_colector = 'SHIPAY' THEN  -- Início IF Regra 1
        RAISE NOTICE 'Fluxo específico para PIX';

        -- fluxo para PIX

    ELSE  -- Else Regra 1

        -- Localizar a transação correspondente na tabela "transactions"
        SELECT * INTO transaction_record
        FROM transactions tr
        WHERE tr.usn::varchar = in_usn
          AND tr.customer_id = in_document
          AND tr.gross_amount = in_gross_value
          AND tr."time" BETWEEN transaction_datetime_converted - INTERVAL '1 hour'
                           AND transaction_datetime_converted + INTERVAL '1 hour'
                          limit 1;

        -- Regra 2: Se não for encontrado o registro correspondente

        IF NOT FOUND THEN  -- Início IF Regra 2

            -- Regra 2.1: Verificar o tipo de transação
            IF in_event IS NOT NULL AND UPPER(in_event) NOT IN (UPPER('Transações'), UPPER('Antecipações')) THEN  -- Início IF Regra 2.1

            RAISE NOTICE 'Regra 2.1';

             -- Cria um novo registro na tabela "transactions" e marca ele como "conciliado"

				INSERT INTO transactions (
				    "id",                        -- 1
				    "vspague_id",                -- 2
				    "conciliation_id",           -- 3
				    "time",                      -- 4
				    "end_time",                  -- 5
				    "customer_id",               -- 6
				    "branch",                    -- 7
				    "terminal",                  -- 8
				    "acquirer",                  -- 9
				    "brand",                     -- 10
				    "card_funding",              -- 11
				    "payment_type",              -- 12
				    "transaction_type",          -- 13
				    "product",                   -- 14
				    "type",                      -- 15
				    "gross_amount",              -- 16
				    "fee_rate",                  -- 17
				    "net_amount",                -- 18
				    "bank_account",              -- 19
				    "settlement_date",           -- 20
				    "usn",                       -- 21
				    "acquirer_usn",              -- 22
				    "auth_code",                 -- 23
				    "logical_number",            -- 24
				    "installments",              -- 25
				    "installments_fee_by",       -- 26
				    "status",                    -- 27
				    "input_type",                -- 28
				    "card_bin",                  -- 29
				    "card_last4",                -- 30
				    "card_expiry_date",          -- 31
				    "cardholder",                -- 32
				    "response_code",             -- 33
				    "response_message",          -- 34
				    "receipt_via1",              -- 35
				    "receipt_via2",              -- 36
				    "error",                     -- 37
				    "vspague_usn",               -- 38
				    "updated_at",                -- 39
				    "concilied",                 -- 40
				    "non_conciliation_detail",   -- 41
				    "external_transaction_id",   -- 42
				    "external_receivable_id"	 -- 43
				) VALUES (
				    gen_random_uuid(),           -- id (1)
				    NULL,                        -- vspague_id (2)
				    NULL,                        -- conciliation_id (3)
				    now(), 					     -- time (4)
				    NULL,                        -- end_time (5)
				    in_document,                 -- customer_id (6)
				    '',                          -- branch (7)
				    in_terminal_id,              -- terminal (8)
				    in_acquirer,                 -- acquirer (9)
				    in_card_flag,                -- brand (10)
				    NULL,                        -- card_funding (11)
				    NULL,                        -- payment_type (12)
				    in_event,                    -- transaction_type (13)
				    '',                          -- product (14)
				    in_event,                    -- type (15)
				    in_gross_value,              -- gross_amount (16)
				    in_fee,                      -- fee_rate (17)
				    in_net_value,                -- net_amount (18)
				    NULL,                        -- bank_account (19)
				    settlement_datetime_converted, -- settlement_date (20)
				    case WHEN in_usn ~ '^[0-9]+$' THEN in_usn::int8 ELSE null END, 	-- usn (21)
				    NULL,                        -- acquirer_usn (22)
				    NULL,                        -- auth_code (23)
				    in_terminal_id,              -- logical_number (24)
				    in_total_installments,       -- installments (25)
				    '',                          -- installments_fee_by (26)
				    NULL,                        -- status (27)
				    NULL,                        -- input_type (28)
				    COALESCE(SUBSTRING(in_card_number, 1, 6), ''), -- card_bin (29)
				    COALESCE(RIGHT(in_card_number, 4), ''),        -- card_last4 (30)
				    NULL,                        -- card_expiry_date (31)
				    NULL,                        -- cardholder (32)
				    NULL,                        -- response_code (33)
				    NULL,                        -- response_message (34)
				    NULL,                        -- receipt_via1 (35)
				    NULL,                        -- receipt_via2 (36)
				    NULL,                        -- error (37)
				    NULL,                        -- vspague_usn (38)
				    NOW(),                       -- updated_at (39)
				    true,                        -- concilied (40)
				    NULL,                        -- non_conciliation_detail (41)
				    in_external_transaction_id,  -- external_transaction_id (42)
				    in_external_receivable_id    -- external_transaction_id (43)
				);

                UPDATE external_transaction
                SET concilied = true, updated_at = NOW()
                WHERE id = in_external_transaction_id;

                UPDATE external_receivable
                SET concilied = true, updated_at = NOW()
                WHERE id = in_external_receivable_id;

            ELSE  -- Else Regra 2.1

            RAISE NOTICE 'Regra 2.2';

            -- Cria um novo registro na tabela "transactions" e marca ele como "não conciliado"

				INSERT INTO transactions (
				    "id",                        -- 1
				    "vspague_id",                -- 2
				    "conciliation_id",           -- 3
				    "time",                      -- 4
				    "end_time",                  -- 5
				    "customer_id",               -- 6
				    "branch",                    -- 7
				    "terminal",                  -- 8
				    "acquirer",                  -- 9
				    "brand",                     -- 10
				    "card_funding",              -- 11
				    "payment_type",              -- 12
				    "transaction_type",          -- 13
				    "product",                   -- 14
				    "type",                      -- 15
				    "gross_amount",              -- 16
				    "fee_rate",                  -- 17
				    "net_amount",                -- 18
				    "bank_account",              -- 19
				    "settlement_date",           -- 20
				    "usn",                       -- 21
				    "acquirer_usn",              -- 22
				    "auth_code",                 -- 23
				    "logical_number",            -- 24
				    "installments",              -- 25
				    "installments_fee_by",       -- 26
				    "status",                    -- 27
				    "input_type",                -- 28
				    "card_bin",                  -- 29
				    "card_last4",                -- 30
				    "card_expiry_date",          -- 31
				    "cardholder",                -- 32
				    "response_code",             -- 33
				    "response_message",          -- 34
				    "receipt_via1",              -- 35
				    "receipt_via2",              -- 36
				    "error",                     -- 37
				    "vspague_usn",               -- 38
				    "updated_at",                -- 39
				    "concilied",                 -- 40
				    "non_conciliation_detail",   -- 41
				    "external_transaction_id",   -- 42
				    "external_receivable_id"	 -- 43
				) VALUES (
				    gen_random_uuid(),           -- id (1)
				    NULL,                        -- vspague_id (2)
				    NULL,                        -- conciliation_id (3)
				    transaction_datetime_converted, -- time (4)
				    NULL,                        -- end_time (5)
				    in_document,                 -- customer_id (6)
				    '',                          -- branch (7)
				    in_terminal_id,              -- terminal (8)
				    in_acquirer,                 -- acquirer (9)
				    in_card_flag,                -- brand (10)
				    NULL,                        -- card_funding (11)
				    NULL,                        -- payment_type (12)
				    in_transaction_type,         -- transaction_type (13)
				    '',                          -- product (14)
				    '',                          -- type (15)
				    in_gross_value,              -- gross_amount (16)
				    in_fee,                      -- fee_rate (17)
				    in_net_value,                -- net_amount (18)
				    NULL,                        -- bank_account (19)
				    settlement_datetime_converted, -- settlement_date (20)
				    case WHEN in_usn ~ '^[0-9]+$' THEN in_usn::int8 ELSE null END, 	-- usn (21)
				    NULL,                        -- acquirer_usn (22)
				    NULL,                        -- auth_code (23)
				    in_terminal_id,              -- logical_number (24)
				    in_total_installments,       -- installments (25)
				    '',                          -- installments_fee_by (26)
				    NULL,                        -- status (27)
				    NULL,                        -- input_type (28)
				    COALESCE(SUBSTRING(in_card_number, 1, 6), ''), -- card_bin (29)
				    COALESCE(RIGHT(in_card_number, 4), ''),        -- card_last4 (30)
				    NULL,                        -- card_expiry_date (31)
				    NULL,                        -- cardholder (32)
				    NULL,                        -- response_code (33)
				    NULL,                        -- response_message (34)
				    NULL,                        -- receipt_via1 (35)
				    NULL,                        -- receipt_via2 (36)
				    NULL,                        -- error (37)
				    NULL,                        -- vspague_usn (38)
				    NOW(),                       -- updated_at (39)
				    false,                       -- concilied (40)
				    'TRANSACTION_DIVERGENCE : Transação registrada pela adquirente mas não registrada na destaxa', -- non_conciliation_detail (41)
				    in_external_transaction_id,  -- external_transaction_id (42)
				    in_external_receivable_id    -- external_transaction_id (43)
				);

	            UPDATE external_transaction
	            SET concilied = false, updated_at = NOW()
	            WHERE id = in_external_transaction_id;

	            UPDATE external_receivable
                SET concilied = false, updated_at = NOW()
                WHERE id = in_external_receivable_id;

            END IF;  -- Fim IF Regra 2

		ELSE  -- Else Regra 2 / Início da regra 3

		RAISE NOTICE 'Regra 3';


            SELECT * INTO customer_fee_record
            FROM customer_fee cf
            WHERE cf.customer_document = in_document
              AND UPPER(cf.acquirer) = UPPER(in_acquirer)
              AND UPPER(cf.acquirer_payment_method) = UPPER(in_transaction_type)
              AND UPPER(cf.brand_name) = UPPER(in_card_flag)
              AND cf.installments = in_total_installments
            LIMIT 1;


           has_settlement := (settlement_datetime_converted IS NOT NULL AND settlement_datetime_converted::date <= NOW()::date);
           has_fee := (customer_fee_record IS NOT NULL);


            -- Verifica se existe liquidação e se a liquidação já foi efetuada

            IF has_settlement = true THEN  -- Início IF Regra 3.1

            	-- Verifica se há informações de taxa cadastradas para o cliente

            	IF has_fee = true then  -- Início IF Regra 3.1.1

            		-- Regra 3.1.1.1 : Verifica se as taxas são iguais, se a diferença não excede 0,1% e se o valor da taxa é menor que 1 real;

                	IF (ABS(COALESCE(customer_fee_record.fee, 0) - COALESCE(in_fee, 0)) / NULLIF(COALESCE(customer_fee_record.fee, 1), 0) <= 0.001
    					AND in_fee_value < 1) THEN  -- Início IF Regra 3.1.1.1

	                	RAISE NOTICE 'Regra 3.1.1.1';

	                    -- Atualiza a transação como conciliada, pois as taxas são iguais

	                    UPDATE transactions
	                    SET concilied = TRUE, non_conciliation_detail = NULL, updated_at = NOW(),
	                        fee_rate = in_fee, net_amount = in_net_value,
	                        external_transaction_id = in_external_transaction_id,
	                        external_receivable_id = in_external_receivable_id,
	                        settlement_date = settlement_datetime_converted::date
	                    WHERE id = transaction_record.id;

	                    UPDATE external_transaction
	                    SET concilied = TRUE, updated_at = NOW()
	                    WHERE id = in_external_transaction_id;

	                    UPDATE external_receivable
	                    SET concilied = TRUE, updated_at = NOW()
	                    WHERE id = in_external_receivable_id;

                	else -- else Regra 3.1.1.1

	                	RAISE NOTICE 'Regra 3.1.1.2';

	                    -- Regra 3.1.1.2: Divergência encontrada, atualiza com detalhes de divergência

	                    UPDATE transactions
	                    SET concilied = FALSE, non_conciliation_detail = 'FEE DIVERGENCE - Houve divergência de taxa para este cliente.',
	                        updated_at = NOW(),
	                        fee_rate = in_fee, net_amount = in_net_value,
	                        external_transaction_id = in_external_transaction_id,
	                        external_receivable_id = in_external_receivable_id,
	                        settlement_date = settlement_datetime_converted::date
	                    WHERE id = transaction_record.id;

	                    UPDATE external_transaction
	                    SET concilied = FALSE, updated_at = NOW()
	                    WHERE id = in_external_transaction_id;

	                    UPDATE external_receivable
	                    SET concilied = FALSE, updated_at = NOW()
	                    WHERE id = in_external_receivable_id;

                	end if; --end if Regra 3.1.1.1

            	else -- else Regra 3.1.1

	            	RAISE NOTICE 'Regra 3.1.2';

	                -- Regra 3.1.2: Assume que o cliente é novo e marca como conciliado, pois não há informações de taxa

	                UPDATE transactions
	                SET concilied = TRUE, non_conciliation_detail = null,
	                    updated_at = NOW(),
	                    fee_rate = in_fee, net_amount = in_net_value,
	                    external_transaction_id = in_external_transaction_id,
	                    external_receivable_id = in_external_receivable_id,
	                    settlement_date = settlement_datetime_converted::date
	                WHERE id = transaction_record.id;

	                UPDATE external_transaction
	                SET concilied = TRUE, updated_at = NOW()
	                WHERE id = in_external_transaction_id;

	                UPDATE external_receivable
	                SET concilied = TRUE, updated_at = NOW()
	                WHERE id = in_external_receivable_id;

            	end if; -- end if Regra 3.1.1

          elsif settlement_datetime_converted IS NOT NULL and settlement_datetime_converted::date > NOW()::date then -- elsif Regra 3.2

             	RAISE NOTICE 'Regra 3.2';

             	 -- Marca como não conciliada, pois a data de liquidação é futura

	                    UPDATE transactions
	                    SET concilied = FALSE, non_conciliation_detail = 'FUTURE_SETTLEMENT - A liquidação desta transação será efetuada em uma data futura',
	                        updated_at = NOW(),
	                        fee_rate = in_fee, net_amount = in_net_value,
	                        external_transaction_id = in_external_transaction_id,
	                        external_receivable_id = in_external_receivable_id,
	                        settlement_date = settlement_datetime_converted::date
	                    WHERE id = transaction_record.id;

	                    UPDATE external_transaction
	                    SET concilied = FALSE, updated_at = NOW()
	                    WHERE id = in_external_transaction_id;

	                    UPDATE external_receivable
	                    SET concilied = FALSE, updated_at = NOW()
	                    WHERE id = in_external_receivable_id;

			else -- else regra 3.2

				-- A transação não foi liquidada, então é ignorada e será processada novamente em um próxomo ciclo.

				RAISE NOTICE 'Regra 3.3 - A transação não possui liquidação';

            END IF;  -- Fim IF Regra 3.1

        END IF;  -- Fim IF Regra 3

    END IF;  -- Fim IF Regra 1
END;
$procedure$
;
