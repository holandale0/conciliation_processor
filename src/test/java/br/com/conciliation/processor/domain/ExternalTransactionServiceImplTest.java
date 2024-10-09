package br.com.conciliation.processor.domain;

import br.com.conciliation.processor.domain.externalTransaction.repository.ExternalTransactionRepository;
import br.com.conciliation.processor.domain.externalTransaction.service.ExternalTransactionServiceImpl;
import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for external transaction service")
@ExtendWith(MockitoExtension.class)
class ExternalTransactionServiceImplTest {

	@Mock
	private ExternalTransactionRepository repository;

	@InjectMocks
	private ExternalTransactionServiceImpl service;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("should return list of external transactions successfully")
	void shouldReturnListOfExternalTransactionsSuccessfully() {

		// Arrange
		LocalDate initialDate = LocalDate.of(2024, 9, 7);
		LocalDate finalDate = LocalDate.of(2024, 9, 9);
		String document = "12345678912340";

		ExternalTransaction transaction = DomainMocks.externalTransactionModel();

		when(repository.listExternalTransactions(any(LocalDate.class), any(LocalDate.class), anyString()))
				.thenReturn(List.of(transaction));

		// Act
		List<ExternalTransaction> result = service.getExternalTransactions(initialDate, finalDate, document);

		// Assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("13413753000170", result.get(0).getDocument());

		// Verify
		verify(repository, times(1)).listExternalTransactions(any(LocalDate.class), any(LocalDate.class), anyString());
	}

	@Test
	@DisplayName("should return empty list when getExternalTransactions")
	void shouldReturnEmptyListWhenGetExternalTransactions() {

		// Arrange
		LocalDate initialDate = LocalDate.of(2024, 9, 7);
		LocalDate finalDate = LocalDate.of(2024, 9, 9);
		String document = "12345678912340";

		when(repository.listExternalTransactions(initialDate, finalDate, document)).thenReturn(List.of());

		// Act
		List<ExternalTransaction> result = service.getExternalTransactions(initialDate, finalDate, document);

		// Assert
		Assertions.assertTrue(result.isEmpty());

		// Verify
		verify(repository, times(1)).listExternalTransactions(initialDate, finalDate, document);
	}

	@Test
	@DisplayName("should process conciliation successfully")
	void shouldProcessConciliationSuccessfully() {

		// Arrange
		ExternalTransaction transaction = new ExternalTransaction();
		transaction.setDocument("13413753000170");
		transaction.setUsn("188715");

		// Act
		service.processConciliation(transaction);

		// Verify
		verify(repository, times(1)).processConciliation(transaction);
	}

	@Test
	@DisplayName("should handle exception in processConciliation")
	void shouldHandleExceptionInProcessConciliation() {

		// Arrange
		ExternalTransaction transaction = new ExternalTransaction();
		transaction.setDocument("13413753000170");
		transaction.setUsn("188715");

		doThrow(new RuntimeException("Test exception")).when(repository).processConciliation(transaction);

		// Act
		service.processConciliation(transaction);

		// Assert
		verify(repository, times(1)).processConciliation(transaction);
	}

}