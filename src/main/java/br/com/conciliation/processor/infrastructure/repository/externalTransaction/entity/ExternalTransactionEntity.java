package br.com.conciliation.processor.infrastructure.repository.externalTransaction.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("external_transaction")
@Data
@Getter
@Setter
@EqualsAndHashCode
public class ExternalTransactionEntity {

	@Id
	@Column("id")
	private UUID id;

	@Column("external_id")
	private String externalId;

	@Column("acquirer")
	private String acquirer;

	@Column("colector")
	private String colector;

	@Column("document")
	private String document;

	@Column("transaction_datetime")
	private LocalDateTime transactionDatetime;

	@Column("settlement_datetime")
	private LocalDateTime settlementDatetime;

	@Column("enqueue_datetime")
	private LocalDateTime enqueueDatetime;

	@Column("colect_datetime")
	private LocalDateTime colectDatetime;

	@Column("net_value")
	private BigDecimal netValue;

	@Column("gross_value")
	private BigDecimal grossValue;

	@Column("fee")
	private BigDecimal fee;

	@Column("fee_value")
	private BigDecimal feeValue;

	@Column("card_flag")
	private String cardFlag;

	@Column("card_number")
	private String cardNumber;

	@Column("total_installments")
	private Integer totalInstallments;

	@Column("installment")
	private Integer installment;

	@Column("transaction_code")
	private String transactionCode;

	@Column("authorization_code")
	private String authorizationCode;

	@Column("transaction_type")
	private String transactionType;

	@Column("payment_channel")
	private String paymentChannel;

	@Column("terminal_id")
	private String terminalId;

	@Column("lot_code")
	private String lotCode;

	@Column("lot_status")
	private String lotStatus;

	@Column("sales_id")
	private String salesId;

	@Column("sales_code")
	private String salesCode;

	@Column("usn")
	private String usn;

	@Column("settlement_type")
	private String settlementType;

	@Column("event_name")
	private String event;

	@Column("external_receivable_id")
	private UUID externalReceivableId;

}
