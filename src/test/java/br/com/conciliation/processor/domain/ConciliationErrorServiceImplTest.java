package br.com.conciliation.processor.domain;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.domain.conciliationError.repository.ConciliationErrorRepository;
import br.com.conciliation.processor.domain.conciliationError.service.ConciliationErrorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for conciliation error service")
@ExtendWith(MockitoExtension.class)
public class ConciliationErrorServiceImplTest {

	@Mock
	private ConciliationErrorRepository repository;

	@InjectMocks
	private ConciliationErrorServiceImpl service;

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("should save conciliation error successfully")
	void shouldSaveConciliationErrorSuccessfully() {

		// Arrange
		ConciliationError conciliationError = DomainMocks.conciliationErrorModel();
		conciliationError.setErrorMessage("Test error");

		// Act
		service.saveConciliationError(conciliationError);

		// Assert
		verify(repository, times(1)).saveConciliationError(conciliationError);
	}

	@Test
	@DisplayName("should handle exception when saving conciliation error")
	void shouldHandleExceptionWhenSavingConciliationError() {

		// Arrange
		ConciliationError conciliationError = DomainMocks.conciliationErrorModel();
		conciliationError.setErrorMessage("Test error");

		doThrow(new RuntimeException("Test Exception")).when(repository).saveConciliationError(conciliationError);

		// Act
		service.saveConciliationError(conciliationError);

		// Assert
		verify(repository, times(1)).saveConciliationError(conciliationError);
	}

}