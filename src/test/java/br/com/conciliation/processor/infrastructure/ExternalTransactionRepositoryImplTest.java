package br.com.conciliation.processor.infrastructure;

import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.ExternalTransactionRepositoryImpl;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.entity.ExternalTransactionEntity;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.mapper.ExternalTransactionInfrastructureConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for external transaction repository")
@ExtendWith(MockitoExtension.class)
public class ExternalTransactionRepositoryImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private ExternalTransactionInfrastructureConverter mapper;

	@InjectMocks
	private ExternalTransactionRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("should return list of external transactions successfully")
	void shouldReturnListOfExternalTransactionsSuccessfully() {

		// Arrange
		LocalDate initialDate = LocalDate.of(2024, 9, 7);
		LocalDate finalDate = LocalDate.of(2024, 9, 9);
		String document = "12345678912340";

		ExternalTransactionEntity entity = new ExternalTransactionEntity();
		entity.setDocument("13413753000170");

		ExternalTransaction transaction = new ExternalTransaction();
		transaction.setDocument("13413753000170");

		when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(List.of(entity));

		when(mapper.toModel(entity)).thenReturn(transaction);

		// Act
		List<ExternalTransaction> result = repository.listExternalTransactions(initialDate, finalDate, document);

		// Assert
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("13413753000170", result.get(0).getDocument());

		// Verify
		verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
	}

	@Test
	@DisplayName("should return an empty list if no transactions are found")
	void shouldReturnEmptyListIfNoTransactionsAreFound() {

		// Arrange
		LocalDate initialDate = LocalDate.of(2024, 9, 7);
		LocalDate finalDate = LocalDate.of(2024, 9, 9);
		String document = "12345678912340";

		when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(List.of());

		// Act
		List<ExternalTransaction> result = repository.listExternalTransactions(initialDate, finalDate, document);

		// Assert
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isEmpty());
		verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
	}

	@Test
	@DisplayName("should call stored procedure successfully")
	void shouldCallStoredProcedureSuccessfully() {

		// Arrange
		ExternalTransactionEntity entity = InfrastructureMocks.externalTransactionEntity();

		ExternalTransaction transaction = InfrastructureMocks.externalTransactionModel();

		when(mapper.toEntity(any(ExternalTransaction.class))).thenReturn(entity);

		// Act
		repository.processConciliation(transaction);

		// Assert
		verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
	}

	// @Test
	@DisplayName("should handle exception when calling stored procedure")
	void shouldHandleExceptionWhenCallingStoredProcedure() {

		// Arrange
		ExternalTransactionEntity entity = InfrastructureMocks.externalTransactionEntity();

		ExternalTransaction transaction = InfrastructureMocks.externalTransactionModel();

		when(mapper.toEntity(any(ExternalTransaction.class))).thenReturn(entity);

		doThrow(new RuntimeException("Test exception")).when(jdbcTemplate).update(anyString(), any(Object[].class));

		// Act
		repository.processConciliation(transaction);

		// Assert
		verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

		// Verify
		verify(jdbcTemplate, times(1)).update(eq(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES (?, ?, ?, ?, ?)"),
				any(UUID.class), eq("13413753000170"), eq("Cielo"), any(LocalDateTime.class), eq("Test exception"));
	}

}