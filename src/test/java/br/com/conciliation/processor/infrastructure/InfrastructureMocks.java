package br.com.conciliation.processor.infrastructure;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.entity.ConciliationErrorEntity;
import br.com.conciliation.processor.infrastructure.repository.customer.entity.CustomerEntity;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.entity.ExternalTransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class InfrastructureMocks {

	protected static Customer customerModel() {
		Customer customer = new Customer();
		customer.setId(UUID.randomUUID());
		customer.setDocument("12345678912340");
		customer.setStatus("ATIVO");
		return customer;
	}

	protected static CustomerEntity customerEntity() {
		CustomerEntity customer = new CustomerEntity();
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

	protected static ExternalTransactionEntity externalTransactionEntity() {
		ExternalTransactionEntity transaction = new ExternalTransactionEntity();
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

	protected static ConciliationError conciliationErrorModel() {
		ConciliationError conciliationError = new ConciliationError();
		conciliationError.setId(java.util.UUID.randomUUID());
		conciliationError.setAcquirer("Cielo");
		conciliationError.setErrorMessage("Test error");
		return conciliationError;
	}

	protected static ConciliationErrorEntity conciliationErrorEntity() {
		ConciliationErrorEntity conciliationError = new ConciliationErrorEntity();
		conciliationError.setId(java.util.UUID.randomUUID());
		conciliationError.setAcquirer("Cielo");
		conciliationError.setErrorMessage("Test error");
		return conciliationError;
	}

}
