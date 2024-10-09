package br.com.conciliation.processor.application.configuration;

import br.com.conciliation.processor.domain.externalTransaction.service.ExternalTransactionServiceImpl;
import br.com.conciliation.processor.domain.conciliationError.repository.ConciliationErrorRepository;
import br.com.conciliation.processor.domain.conciliationError.service.ConciliationErrorService;
import br.com.conciliation.processor.domain.conciliationError.service.ConciliationErrorServiceImpl;
import br.com.conciliation.processor.domain.customer.repository.CustomerRepository;
import br.com.conciliation.processor.domain.customer.service.CustomerService;
import br.com.conciliation.processor.domain.customer.service.CustomerServiceImpl;
import br.com.conciliation.processor.domain.externalTransaction.repository.ExternalTransactionRepository;
import br.com.conciliation.processor.domain.externalTransaction.service.ExternalTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServicesBeanConfiguration {

	@Bean
	public CustomerService customerService(CustomerRepository customerRepository) {
		return new CustomerServiceImpl(customerRepository);
	}

	@Bean
	public ExternalTransactionService externalTransactionService(
			ExternalTransactionRepository externalTransactionRepository) {
		return new ExternalTransactionServiceImpl(externalTransactionRepository);
	}

	@Bean
	public ConciliationErrorService conciliationErrorService(ConciliationErrorRepository conciliationErrorRepository) {
		return new ConciliationErrorServiceImpl(conciliationErrorRepository);
	}

}
