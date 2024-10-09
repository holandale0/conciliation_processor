package br.com.conciliation.processor.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

	public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		ZonedDateTime zonedDateTime = timestamp.toInstant().atZone(ZoneId.of("America/Sao_Paulo"));
		return zonedDateTime.toLocalDateTime();
	}

	public static Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return Timestamp.from(localDateTime.atZone(ZoneId.of("America/Sao_Paulo")).toInstant());
	}

}
