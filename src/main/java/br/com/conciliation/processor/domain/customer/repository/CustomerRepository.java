package br.com.conciliation.processor.domain.customer.repository;

import br.com.conciliation.processor.domain.customer.model.Customer;

import java.util.List;

public interface CustomerRepository {

	List<Customer> getCustomerList();

}
