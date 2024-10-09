package br.com.conciliation.processor.domain.externalTransaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTransaction {

	private UUID id;

	private String externalId;

	private String acquirer;

	private String colector;

	private String document;

	private LocalDateTime transactionDatetime;

	private LocalDateTime settlementDatetime;

	private LocalDateTime enqueueDatetime;

	private LocalDateTime colectDatetime;

	private BigDecimal netValue;

	private BigDecimal grossValue;

	private BigDecimal fee;

	private BigDecimal feeValue;

	private String cardFlag;

	private String cardNumber;

	private Integer totalInstallments;

	private Integer installment;

	private String transactionCode;

	private String authorizationCode;

	private String transactionType;

	private String paymentChannel;

	private String terminalId;

	private String lotCode;

	private String lotStatus;

	private String salesId;

	private String salesCode;

	private String usn;

	private String settlementType;

	private String event;

	private UUID externalReceivableId;

}