package br.com.conciliation.processor.domain.conciliationError.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConciliationError {

	@Builder.Default
	private UUID id = UUID.randomUUID();

	private String acquirer;

	private String document;

	@Builder.Default
	private LocalDateTime processDatetime = LocalDateTime.now();

	private String errorMessage;

}