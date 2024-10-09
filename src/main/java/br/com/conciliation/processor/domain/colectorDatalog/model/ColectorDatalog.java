package br.com.conciliation.processor.domain.colectorDatalog.model;

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
public class ColectorDatalog {

	private UUID client_id;

	private String acquirer;

	private String colector;

	private String externalId;

	private LocalDateTime lastColectDatetime;

}
