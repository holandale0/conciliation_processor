package br.com.conciliation.processor.infrastructure.repository.externalTransaction;

import br.com.conciliation.processor.domain.externalTransaction.repository.ExternalTransactionRepository;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.entity.ConciliationErrorEntity;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.entity.ExternalTransactionEntity;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.mapper.ExternalTransactionInfrastructureConverter;
import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Repository
public class ExternalTransactionRepositoryImpl implements ExternalTransactionRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalTransactionRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ExternalTransactionInfrastructureConverter mapper;

	private static final String CALL_PROCEDURE = "CALL process_conciliation(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	public void processConciliation(ExternalTransaction externalTransaction) {
		if (externalTransaction == null) {
			throw new IllegalArgumentException("ExternalTransaction cannot be null");
		}
		executeFunction(mapper.toEntity(externalTransaction));
	}

	private void executeFunction(ExternalTransactionEntity externalTransaction) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			String transactionDatetime = externalTransaction.getTransactionDatetime().format(formatter);
			String settlementDatetime = externalTransaction.getSettlementDatetime() != null
					? externalTransaction.getSettlementDatetime().format(formatter) : "null";

			// Logando a query formatada no console
			String sql = String.format(Locale.US, "CALL process_conciliation(\n"
					+ "'%s'::uuid, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar,\n"
					+ "%.2f, %.2f, %.2f, %.2f, '%s'::varchar, '%s'::varchar, %d,\n"
					+ "%d, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar,\n"
					+ "'%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar, '%s'::varchar\n);",
					externalTransaction.getId(), externalTransaction.getExternalId(), externalTransaction.getAcquirer(),
					externalTransaction.getColector(), externalTransaction.getDocument(), transactionDatetime,
					settlementDatetime, externalTransaction.getNetValue(), externalTransaction.getGrossValue(),
					externalTransaction.getFee(), externalTransaction.getFeeValue(), externalTransaction.getCardFlag(),
					externalTransaction.getCardNumber(), externalTransaction.getTotalInstallments(),
					externalTransaction.getInstallment(), externalTransaction.getTransactionCode(),
					externalTransaction.getAuthorizationCode(), externalTransaction.getTransactionType(),
					externalTransaction.getPaymentChannel(), externalTransaction.getTerminalId(),
					externalTransaction.getLotCode(), externalTransaction.getSalesCode(), externalTransaction.getUsn(),
					externalTransaction.getSettlementType(), externalTransaction.getEvent(),
					externalTransaction.getExternalReceivableId());

			LOGGER.info("Procedure process_conciliation chamada com SQL:\n{}", sql);

			jdbcTemplate.update(CALL_PROCEDURE, externalTransaction.getId(), externalTransaction.getExternalId(),
					externalTransaction.getAcquirer(), externalTransaction.getColector(),
					externalTransaction.getDocument(), Timestamp.valueOf(transactionDatetime),
					("null".equals(settlementDatetime) ? null : Timestamp.valueOf(settlementDatetime)),
					externalTransaction.getNetValue(), externalTransaction.getGrossValue(),
					externalTransaction.getFee(), externalTransaction.getFeeValue(), externalTransaction.getCardFlag(),
					externalTransaction.getCardNumber(), externalTransaction.getTotalInstallments(),
					externalTransaction.getInstallment(), externalTransaction.getTransactionCode(),
					externalTransaction.getAuthorizationCode(), externalTransaction.getTransactionType(),
					externalTransaction.getPaymentChannel(), externalTransaction.getTerminalId(),
					externalTransaction.getLotCode(), externalTransaction.getSalesCode(), externalTransaction.getUsn(),
					externalTransaction.getSettlementType(), externalTransaction.getEvent(),
					externalTransaction.getExternalReceivableId());

		}
		catch (Exception e) {
			LOGGER.error("Error on process conciliation.");
			ConciliationErrorEntity conciliationError = new ConciliationErrorEntity();
			conciliationError.setId(UUID.randomUUID());
			conciliationError.setDocument(externalTransaction.getDocument());
			conciliationError.setAcquirer(externalTransaction.getAcquirer());
			conciliationError.setProcessDatetime(LocalDateTime.now());
			conciliationError.setErrorMessage(e.getMessage());
			createConciliationError(conciliationError);
		}
	}

	@Override
	public List<ExternalTransaction> listExternalTransactions(LocalDate initialDate, LocalDate finalDate,
			String acquirer) {
		return findAllByParams(initialDate, finalDate, acquirer).stream().map(mapper::toModel).toList();
	}

	private List<ExternalTransactionEntity> findAllByParams(LocalDate initialDate, LocalDate finalDate,
			String customerDocument) {
		try {
			String sql = "SELECT * FROM public.get_external_transactions(?, ?, ?);";
			return jdbcTemplate.query(sql, new Object[] { initialDate, finalDate, customerDocument },
					new ExternalTransactionRowMapper());
		}
		catch (Exception e) {
			LOGGER.error("Error on fetch external transactions.", e);
			ConciliationErrorEntity conciliationError = new ConciliationErrorEntity();
			conciliationError.setId(UUID.randomUUID());
			conciliationError.setAcquirer(customerDocument);
			conciliationError.setProcessDatetime(LocalDateTime.now());
			conciliationError.setErrorMessage(e.getMessage());
			createConciliationError(conciliationError);
			return null;
		}
	}

	private static class ExternalTransactionRowMapper implements RowMapper<ExternalTransactionEntity> {

		@Override
		public ExternalTransactionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

			try {

				ExternalTransactionEntity entity = new ExternalTransactionEntity();
				entity.setId(rs.getObject("id", UUID.class));
				entity.setExternalId(rs.getString("external_id"));
				entity.setAcquirer(rs.getString("acquirer"));
				entity.setColector(rs.getString("colector"));
				entity.setDocument(rs.getString("document"));

				entity.setTransactionDatetime(
						DateTimeUtils.timestampToLocalDateTime(rs.getTimestamp("transaction_datetime")));
				entity.setSettlementDatetime(
						DateTimeUtils.timestampToLocalDateTime(rs.getTimestamp("settlement_datetime")));
				entity.setEnqueueDatetime(DateTimeUtils.timestampToLocalDateTime(rs.getTimestamp("enqueue_datetime")));
				entity.setColectDatetime(DateTimeUtils.timestampToLocalDateTime(rs.getTimestamp("colect_datetime")));

				entity.setNetValue(rs.getBigDecimal("net_value"));
				entity.setGrossValue(rs.getBigDecimal("gross_value"));
				entity.setFee(rs.getBigDecimal("fee"));
				entity.setFeeValue(rs.getBigDecimal("fee_value"));

				entity.setCardFlag(rs.getString("card_flag"));
				entity.setCardNumber(rs.getString("card_number"));
				entity.setTotalInstallments(rs.getInt("total_installments"));
				entity.setInstallment(rs.getInt("installment"));

				entity.setTransactionCode(rs.getString("transaction_code"));
				entity.setAuthorizationCode(rs.getString("authorization_code"));
				entity.setTransactionType(rs.getString("transaction_type"));
				entity.setPaymentChannel(rs.getString("payment_channel"));

				entity.setTerminalId(rs.getString("terminal_id"));
				entity.setLotCode(rs.getString("lot_code"));
				entity.setLotStatus(rs.getString("lot_status"));
				entity.setSalesId(rs.getString("sales_id"));
				entity.setSalesCode(rs.getString("sales_code"));
				entity.setUsn(rs.getString("usn"));
				entity.setSettlementType(rs.getString("settlement_type"));
				entity.setEvent(rs.getString("event_name"));
				entity.setExternalReceivableId(rs.getObject("external_receivable_id", UUID.class));

				return entity;

			}
			catch (Exception e) {
				LOGGER.error("Error external_transaction row mapper");
				return null;
			}
		}

	}

	private void createConciliationError(ConciliationErrorEntity entity) {
		jdbcTemplate.update(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES (?, ?, ?, ?, ?)",
				entity.getId(), entity.getDocument(), entity.getAcquirer(), entity.getProcessDatetime(),
				entity.getErrorMessage());
	}

}
