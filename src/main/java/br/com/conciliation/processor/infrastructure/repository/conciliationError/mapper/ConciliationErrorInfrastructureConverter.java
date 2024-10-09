package br.com.conciliation.processor.infrastructure.repository.conciliationError.mapper;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.entity.ConciliationErrorEntity;
import org.springframework.stereotype.Component;

@Component
public class ConciliationErrorInfrastructureConverter {

	public ConciliationErrorEntity toEntity(ConciliationError model) {
		ConciliationErrorEntity entity = new ConciliationErrorEntity();
		entity.setId(model.getId());
		entity.setDocument(model.getDocument());
		entity.setAcquirer(model.getAcquirer());
		entity.setProcessDatetime(model.getProcessDatetime());
		entity.setErrorMessage(model.getErrorMessage());
		return entity;
	}

}
