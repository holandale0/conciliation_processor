package br.com.conciliation.processor.domain.colectorDatalog.service;

<<<<<<< HEAD:src/main/java/br/com/destaxa/conciliation/processor/domain/colectorDatalog/service/ColectorDatalogServiceImpl.java
import br.com.destaxa.conciliation.processor.domain.colectorDatalog.model.ColectorDatalog;
import br.com.destaxa.conciliation.processor.domain.colectorDatalog.repository.ColectorDatalogRepository;
=======

import br.com.conciliation.processor.domain.colectorDatalog.model.ColectorDatalog;
import br.com.conciliation.processor.domain.colectorDatalog.repository.ColectorDatalogRepository;
>>>>>>> 5fafc6e3808c4efbdfc7a5af8a24b86a6ef379c7:src/main/java/br/com/conciliation/processor/domain/colectorDatalog/service/ColectorDatalogServiceImpl.java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColectorDatalogServiceImpl implements ColectorDatalogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ColectorDatalogServiceImpl.class);

	private ColectorDatalogRepository repository;

	@Override
	public void saveOrUpdateColectorDatalog(ColectorDatalog colectorDatalog) {
		// Assert.notNull(colectorDatalog, "DOMAIN - EXCEPTION - Object to save must not
		// be null");

		repository.saveOrUpdate(colectorDatalog);

	}

}
