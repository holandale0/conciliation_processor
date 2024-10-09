package br.com.conciliation.processor.infrastructure;

import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.infrastructure.repository.customer.CustomerRepositoryImpl;
import br.com.conciliation.processor.infrastructure.repository.customer.entity.CustomerEntity;
import br.com.conciliation.processor.infrastructure.repository.customer.mapper.CustomerInfrastructureConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for processor data log repository")
@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private CustomerInfrastructureConverter mapper;

	@InjectMocks
	private CustomerRepositoryImpl repository;

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("should return list of customers successfully")
	void shouldReturnListOfCustomersSuccessfully() {

		// Arrange
		CustomerEntity customerEntity;
		Customer customerModel;

		customerEntity = InfrastructureMocks.customerEntity();

		customerModel = InfrastructureMocks.customerModel();

		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(customerEntity));
		when(mapper.toModel(customerEntity)).thenReturn(customerModel);

		// Act
		List<Customer> result = repository.getCustomerList();

		// Assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals("12345678912340", result.get(0).getDocument());
		Assertions.assertEquals("ATIVO", result.get(0).getStatus());
		verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
	}

	@Test
	@DisplayName("should handle exception when fetching customer list")
	void shouldHandleExceptionWhenFetchingCustomerList() {
		// Arrange
		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(new RuntimeException("Test Exception"));

		// Act
		List<Customer> result = repository.getCustomerList();

		// Assert
		Assertions.assertTrue(result.isEmpty());
		verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));

		// Verify
		verify(jdbcTemplate, times(1)).update(eq(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES (?, ?, ?, ?, ?)"),
				any(UUID.class), eq(null), eq(null), any(LocalDateTime.class), eq("Test Exception"));
	}

}
