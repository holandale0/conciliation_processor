package br.com.conciliation.processor.infrastructure.repository.conciliationError;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.domain.conciliationError.repository.ConciliationErrorRepository;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.entity.ConciliationErrorEntity;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.mapper.ConciliationErrorInfrastructureConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConciliationErrorRepositoryImpl implements ConciliationErrorRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ConciliationErrorInfrastructureConverter mapper;

	@Override
	public void saveConciliationError(ConciliationError conciliationError) {
		createConciliationError(mapper.toEntity(conciliationError));
	}

	private void createConciliationError(ConciliationErrorEntity entity) {
		jdbcTemplate.update(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES (?, ?, ?, ?, ?)",
				entity.getId(), entity.getDocument(), entity.getAcquirer(), entity.getProcessDatetime(),
				entity.getErrorMessage());
	}

}
