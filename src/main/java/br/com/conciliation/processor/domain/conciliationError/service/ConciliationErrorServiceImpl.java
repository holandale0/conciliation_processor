package br.com.conciliation.processor.domain.conciliationError.service;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.domain.conciliationError.repository.ConciliationErrorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConciliationErrorServiceImpl implements ConciliationErrorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConciliationErrorServiceImpl.class);

	private final ConciliationErrorRepository repository;

	public ConciliationErrorServiceImpl(ConciliationErrorRepository repository) {
		this.repository = repository;
	}

	@Override
	public void saveConciliationError(ConciliationError conciliationError) {
		try {
			repository.saveConciliationError(conciliationError);
		}
		catch (Exception e) {
			LOGGER.error("Erro em ConciliationErrorService.saveConciliationError(): ", e);
		}
	}

}
