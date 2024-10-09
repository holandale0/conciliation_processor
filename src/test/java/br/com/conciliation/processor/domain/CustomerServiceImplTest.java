package br.com.conciliation.processor.domain;

import br.com.conciliation.processor.domain.customer.repository.CustomerRepository;
import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.domain.customer.service.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for customer service")
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

	@Mock
	private CustomerRepository repository;

	@InjectMocks
	private CustomerServiceImpl service;

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("should return list of processor datalogs successfully")
	void shouldReturnListOfCustomersSuccessfully() {
		// Arrange
		Customer customer = DomainMocks.customerModel();

		when(repository.getCustomerList()).thenReturn(List.of(customer));

		// Act
		List<Customer> result = service.listCustomers();

		// Assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("12345678912340", result.get(0).getDocument());
		Assertions.assertEquals("ATIVO", result.get(0).getStatus());
		verify(repository, times(1)).getCustomerList();

	}

	@Test
	@DisplayName("should return empty list")
	void shouldReturnEmptyList() {
		// Arrange
		when(repository.getCustomerList()).thenReturn(List.of());

		// Act
		List<Customer> result = service.listCustomers();

		// Assert
		Assertions.assertTrue(result.isEmpty());
		verify(repository, times(1)).getCustomerList();

	}

}
