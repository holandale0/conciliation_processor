package br.com.conciliation.processor.infrastructure.repository.customer;

import br.com.conciliation.processor.infrastructure.repository.customer.entity.CustomerEntity;
import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.domain.customer.repository.CustomerRepository;
import br.com.conciliation.processor.infrastructure.repository.conciliationError.entity.ConciliationErrorEntity;
import br.com.conciliation.processor.infrastructure.repository.customer.mapper.CustomerInfrastructureConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CustomerInfrastructureConverter mapper;

	private static final String BASE_QUERY = "SELECT * FROM client WHERE UPPER(status) = 'ATIVO' ";

	@Override
	public List<Customer> getCustomerList() {
		return findAll().stream().map(mapper::toModel).toList();
	}

	private List<CustomerEntity> findAll() {
		try {
			return jdbcTemplate.query(BASE_QUERY, new CustomerRepositoryImpl.CustomerRowMapper());
		}
		catch (Exception e) {
			LOGGER.error("Error on fetch customer.");
			ConciliationErrorEntity conciliationError = new ConciliationErrorEntity();
			conciliationError.setId(UUID.randomUUID());
			conciliationError.setProcessDatetime(LocalDateTime.now());
			conciliationError.setErrorMessage(e.getMessage());
			createConciliationError(conciliationError);
			return List.of();
		}
	}

	private static class CustomerRowMapper implements RowMapper<CustomerEntity> {

		@Override
		public CustomerEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				CustomerEntity entity = new CustomerEntity();
				entity.setId(rs.getObject("id", UUID.class));
				entity.setDocument(rs.getString("document"));
				entity.setStatus(rs.getString("status"));
				return entity;

			}
			catch (Exception e) {
				LOGGER.error("Error on customer row mapper.");
				return null;
			}
		}

	}

	private void createConciliationError(ConciliationErrorEntity entity) {
		jdbcTemplate.update(
				"INSERT INTO conciliation_error(id, document, acquirer, process_datetime, error_message) VALUES (?, ?, ?, ?, ?)",
				entity.getId(), entity.getDocument(), entity.getAcquirer(), entity.getProcessDatetime(),
				entity.getErrorMessage());
	}

}
