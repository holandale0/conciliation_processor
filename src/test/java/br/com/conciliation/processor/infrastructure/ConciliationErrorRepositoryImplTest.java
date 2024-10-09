package br.com.conciliation.processor.infrastructure;

import br.com.conciliation.processor.infrastructure.repository.conciliationError.mapper.ConciliationErrorInfrastructureConverter;
import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.ConciliationErrorRepositoryImpl;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.entity.ConciliationErrorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for conciliation error repository")
@ExtendWith(MockitoExtension.class)
public class ConciliationErrorRepositoryImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private ConciliationErrorInfrastructureConverter mapper;

	@InjectMocks
	private ConciliationErrorRepositoryImpl repository;

	private ConciliationError conciliationError;

	private ConciliationErrorEntity conciliationErrorEntity;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Initialize test data
		conciliationError = InfrastructureMocks.conciliationErrorModel();

		conciliationErrorEntity = InfrastructureMocks.conciliationErrorEntity();
	}

	@Test
	@DisplayName("should save conciliation error successfully")
	void shouldSaveConciliationErrorSuccessfully() {
		// Arrange
		when(mapper.toEntity(conciliationError)).thenReturn(conciliationErrorEntity);

		// Act
		repository.saveConciliationError(conciliationError);

		// Assert
		verify(jdbcTemplate, times(1)).update(anyString(), eq(conciliationErrorEntity.getId()),
				eq(conciliationErrorEntity.getDocument()), eq(conciliationErrorEntity.getAcquirer()),
				eq(conciliationErrorEntity.getProcessDatetime()), eq(conciliationErrorEntity.getErrorMessage()));
	}

	@Test
	@DisplayName("should handle exception when saving conciliation error")
	void shouldHandleExceptionWhenSavingConciliationError() {
		// Arrange
		when(mapper.toEntity(conciliationError)).thenReturn(conciliationErrorEntity);

		doThrow(new RuntimeException("Test Exception")).when(jdbcTemplate).update(eq(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES ?, ?, ?, ?, ?"),
				any(UUID.class), anyString(), anyString(), any(), anyString());

		// Act
		try {
			repository.saveConciliationError(conciliationError);
		}
		catch (RuntimeException e) {
			// Expected exception, do nothing
		}

		// Assert
		verify(jdbcTemplate, times(1)).update(eq(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES (?, ?, ?, ?, ?)"),
				eq(conciliationErrorEntity.getId()), eq(conciliationErrorEntity.getDocument()),
				eq(conciliationErrorEntity.getAcquirer()), eq(conciliationErrorEntity.getProcessDatetime()),
				eq(conciliationErrorEntity.getErrorMessage()));
	}

}