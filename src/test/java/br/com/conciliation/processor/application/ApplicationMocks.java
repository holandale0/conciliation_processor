package br.com.conciliation.processor.application;

import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ApplicationMocks {

	protected static Customer customerModel() {
		Customer customer = new Customer();
		customer.setId(UUID.randomUUID());
		customer.setDocument("12345678912340");
		customer.setStatus("ATIVO");
		return customer;
	}

	protected static ExternalTransaction externalTransactionModel() {
		ExternalTransaction transaction = new ExternalTransaction();
		transaction.setId(UUID.randomUUID());
		transaction.setExternalId("1802");
		transaction.setAcquirer("Cielo");
		transaction.setColector("VOOO");
		transaction.setDocument("13413753000170");
		transaction.setTransactionDatetime(LocalDateTime.now());
		transaction.setNetValue(BigDecimal.valueOf(10.06));
		transaction.setGrossValue(BigDecimal.valueOf(10.00));
		transaction.setFee(BigDecimal.valueOf(0.01));
		transaction.setFeeValue(BigDecimal.valueOf(0.06));
		transaction.setCardFlag("Mastercard");
		transaction.setCardNumber("514945***3793");
		transaction.setTotalInstallments(1);
		transaction.setInstallment(0);
		transaction.setTransactionCode("425212126647188");
		transaction.setAuthorizationCode("425212126647188");
		transaction.setTransactionType("Cartão de débito");
		transaction.setPaymentChannel("Contactless");
		transaction.setTerminalId("41191108");
		transaction.setLotCode("2409080110330072005");
		transaction.setLotStatus("123");
		transaction.setSalesCode("456");
		transaction.setUsn("12345");

		return transaction;
	}

}
