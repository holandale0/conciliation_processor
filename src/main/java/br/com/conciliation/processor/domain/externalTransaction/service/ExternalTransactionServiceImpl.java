package br.com.conciliation.processor.domain.externalTransaction.service;

import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.domain.externalTransaction.repository.ExternalTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ExternalTransactionServiceImpl implements ExternalTransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalTransactionServiceImpl.class);

	private final ExternalTransactionRepository repository;

	public ExternalTransactionServiceImpl(ExternalTransactionRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ExternalTransaction> getExternalTransactions(LocalDate initialDate, LocalDate finalDate,
			String acquirer) {
		return repository.listExternalTransactions(initialDate, finalDate, acquirer);
	}

	@Override
	public void processConciliation(ExternalTransaction externalTransaction) {
		try {
			repository.processConciliation(externalTransaction);
		}
		catch (Exception e) {
			LOGGER.error("Erro em ExternalTransactionServiceImpl.processConciliation(): ", e);
		}
	}

}
