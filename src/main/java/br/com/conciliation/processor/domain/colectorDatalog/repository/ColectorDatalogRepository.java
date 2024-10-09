package br.com.conciliation.processor.domain.colectorDatalog.repository;

import br.com.conciliation.processor.domain.colectorDatalog.model.ColectorDatalog;

public interface ColectorDatalogRepository {

	void saveOrUpdate(ColectorDatalog colectorDatalog);

}
