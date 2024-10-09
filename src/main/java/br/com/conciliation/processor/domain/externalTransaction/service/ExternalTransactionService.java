package br.com.conciliation.processor.domain.externalTransaction.service;

import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;

import java.time.LocalDate;
import java.util.List;

public interface ExternalTransactionService {

	List<ExternalTransaction> getExternalTransactions(LocalDate initialDate, LocalDate finalDate, String acquirer);

	void processConciliation(ExternalTransaction externalTransaction);

}
