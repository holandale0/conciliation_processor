--Colunas novas na tabela transactions para lidar com conciliação
ALTER TABLE transactions
ADD COLUMN concilied boolean NULL,
ADD COLUMN non_conciliation_detail varchar(2000) NULL,
ADD COLUMN external_transaction_id uuid NULL;
ADD COLUMN external_receivable_id uuid NULL;

--Coluna usn deve permitir valores null, pois PIX vindos da vooo não tem usn
ALTER TABLE transactions
ALTER COLUMN usn DROP NOT NULL;