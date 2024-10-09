package br.com.conciliation.processor.domain.conciliationError.repository;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;

public interface ConciliationErrorRepository {

	void saveConciliationError(ConciliationError conciliationError);

}
