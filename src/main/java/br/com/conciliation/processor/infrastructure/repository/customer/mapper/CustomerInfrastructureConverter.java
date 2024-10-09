package br.com.conciliation.processor.infrastructure.repository.customer.mapper;

import br.com.conciliation.processor.infrastructure.repository.customer.entity.CustomerEntity;
import br.com.conciliation.processor.domain.customer.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerInfrastructureConverter {

	public CustomerEntity toEntity(Customer customer) {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setId(customer.getId());
		customerEntity.setDocument(customer.getDocument());
		customerEntity.setStatus(customer.getStatus());

		return customerEntity;
	}

	public Customer toModel(CustomerEntity customer) {
		Customer customerModel = new Customer();
		customerModel.setId(customer.getId());
		customerModel.setDocument(customer.getDocument());
		customerModel.setStatus(customer.getStatus());

		return customerModel;
	}

}
