package br.com.conciliation.processor.infrastructure.repository.customer.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("client")
@Data
@Getter
@Setter
@EqualsAndHashCode
public class CustomerEntity {

	private UUID id;

	private String document;

	private String status;

}
