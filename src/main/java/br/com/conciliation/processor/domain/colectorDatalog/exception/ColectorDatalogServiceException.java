package br.com.conciliation.processor.domain.colectorDatalog.exception;

public class ColectorDatalogServiceException extends RuntimeException {

	public ColectorDatalogServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ColectorDatalogServiceException(String message) {
	}

}
