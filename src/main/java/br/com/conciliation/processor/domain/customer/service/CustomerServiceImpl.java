package br.com.conciliation.processor.domain.customer.service;

import br.com.conciliation.processor.domain.customer.repository.CustomerRepository;
import br.com.conciliation.processor.domain.customer.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private final CustomerRepository repository;

	public CustomerServiceImpl(CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Customer> listCustomers() {
		return repository.getCustomerList();
	}

}
