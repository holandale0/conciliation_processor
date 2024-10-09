package br.com.conciliation.processor.application;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.domain.conciliationError.service.ConciliationErrorService;
import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.domain.customer.service.CustomerService;
import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.domain.externalTransaction.service.ExternalTransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for conciliation processor application service")
@ExtendWith(MockitoExtension.class)
public class ConciliationProcessorApplicationServiceTest {

	@Mock
	private ExternalTransactionService externalTransactionService;

	@Mock
	private CustomerService customerService;

	@Mock
	private ConciliationErrorService conciliationErrorService;

	@Mock
	private ExecutorService executorService;

	@InjectMocks
	private ConciliationProcessorApplicationService service;

	@BeforeEach
	void setUp() {
	}

	@Test
	void shouldProcessConciliationForEachAcquirer() throws Exception {

		// Arrange
		Customer customer = ApplicationMocks.customerModel();

		ExternalTransaction transaction = ApplicationMocks.externalTransactionModel();

		when(customerService.listCustomers()).thenReturn(List.of(customer));
		when(externalTransactionService.getExternalTransactions(any(LocalDate.class), any(LocalDate.class),
				anyString())).thenReturn(List.of(transaction));

		when(executorService.invokeAll(anyList())).thenAnswer(invocation -> {
			List<Callable<Void>> tasks = invocation.getArgument(0);
			for (Callable<Void> task : tasks) {
				task.call(); // Simulate task execution
			}
			return List.of(mock(Future.class)); // Return a dummy future to simulate
												// success
		});

		// Act
		service.startConciliationProcessor();

		// Assert
		verify(externalTransactionService, times(1)).processConciliation(transaction);

	}

	@Test
	void shouldHandleConciliationError() throws Exception {

		// Arrange
		Customer customer = ApplicationMocks.customerModel();

		ExternalTransaction externalTransaction = new ExternalTransaction();
		externalTransaction.setDocument("13413753000170");
		externalTransaction.setUsn("188715");

		when(customerService.listCustomers()).thenReturn(List.of(customer));
		when(externalTransactionService.getExternalTransactions(any(LocalDate.class), any(LocalDate.class),
				anyString())).thenReturn(List.of(externalTransaction));

		doThrow(new InterruptedException("Test error")).when(executorService).invokeAll(anyList());

		// Act
		service.startConciliationProcessor();

		// Assert
		verify(conciliationErrorService, times(1)).saveConciliationError(any(ConciliationError.class));
		verify(executorService).shutdown();
	}

}
