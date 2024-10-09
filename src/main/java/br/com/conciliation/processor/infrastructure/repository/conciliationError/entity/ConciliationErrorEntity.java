package br.com.conciliation.processor.infrastructure.repository.conciliationError.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("conciliation_error")
@Data
@Getter
@Setter
@EqualsAndHashCode
public class ConciliationErrorEntity {

	@Id
	@Column("id")
	private UUID id;

	@Column("acquirer")
	private String acquirer;

	@Column("document")
	private String document;

	@Column("process_datetime")
	private LocalDateTime processDatetime;

	@Column("error_message")
	private String errorMessage;

}
