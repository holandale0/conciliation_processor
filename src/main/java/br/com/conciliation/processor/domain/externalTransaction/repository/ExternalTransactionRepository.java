package br.com.conciliation.processor.domain.externalTransaction.repository;

import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;

import java.time.LocalDate;
import java.util.List;

public interface ExternalTransactionRepository {

	List<ExternalTransaction> listExternalTransactions(LocalDate initialDate, LocalDate finalDate, String acquirer);

	void processConciliation(ExternalTransaction externalTransaction);

}
