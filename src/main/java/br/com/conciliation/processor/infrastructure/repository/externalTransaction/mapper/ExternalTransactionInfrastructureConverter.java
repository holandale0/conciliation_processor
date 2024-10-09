package br.com.conciliation.processor.infrastructure.repository.externalTransaction.mapper;

import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.infrastructure.repository.externalTransaction.entity.ExternalTransactionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExternalTransactionInfrastructureConverter {

	public ExternalTransactionEntity toEntity(ExternalTransaction model) {
		ExternalTransactionEntity entity = new ExternalTransactionEntity();
		entity.setId(model.getId());
		entity.setExternalId(model.getExternalId());
		entity.setAcquirer(model.getAcquirer());
		entity.setColector(model.getColector());
		entity.setDocument(model.getDocument());
		entity.setTransactionDatetime(model.getTransactionDatetime());
		entity.setSettlementDatetime(model.getSettlementDatetime());
		entity.setNetValue(model.getNetValue());
		entity.setGrossValue(model.getGrossValue());
		entity.setFee(model.getFee());
		entity.setFeeValue(model.getFeeValue());
		entity.setCardFlag(model.getCardFlag());
		entity.setTotalInstallments(model.getTotalInstallments());
		entity.setInstallment(model.getInstallment());
		entity.setTransactionCode(model.getTransactionCode());
		entity.setAuthorizationCode(model.getAuthorizationCode());
		entity.setCardNumber(model.getCardNumber());
		entity.setTransactionType(model.getTransactionType());
		entity.setPaymentChannel(model.getPaymentChannel());
		entity.setTerminalId(model.getTerminalId());
		entity.setLotCode(model.getLotCode());
		entity.setLotStatus(model.getLotStatus());
		entity.setEnqueueDatetime(model.getEnqueueDatetime());
		entity.setColectDatetime(model.getColectDatetime());
		entity.setSalesCode(model.getSalesCode());
		entity.setUsn(model.getUsn());
		entity.setSettlementType(model.getSettlementType());
		entity.setEvent(model.getEvent());
		entity.setExternalReceivableId(model.getExternalReceivableId());
		return entity;
	}

	public ExternalTransaction toModel(ExternalTransactionEntity entity) {
		ExternalTransaction model = new ExternalTransaction();
		model.setId(entity.getId());
		model.setExternalId(entity.getExternalId());
		model.setAcquirer(entity.getAcquirer());
		model.setColector(entity.getColector());
		model.setDocument(entity.getDocument());
		model.setTransactionDatetime(entity.getTransactionDatetime());
		model.setSettlementDatetime(entity.getSettlementDatetime());
		model.setNetValue(entity.getNetValue());
		model.setGrossValue(entity.getGrossValue());
		model.setFee(entity.getFee());
		model.setFeeValue(entity.getFeeValue());
		model.setCardFlag(entity.getCardFlag());
		model.setTotalInstallments(entity.getTotalInstallments());
		model.setInstallment(entity.getInstallment());
		model.setTransactionCode(entity.getTransactionCode());
		model.setAuthorizationCode(entity.getAuthorizationCode());
		model.setCardNumber(entity.getCardNumber());
		model.setTransactionType(entity.getTransactionType());
		model.setPaymentChannel(entity.getPaymentChannel());
		model.setTerminalId(entity.getTerminalId());
		model.setLotCode(entity.getLotCode());
		model.setLotStatus(entity.getLotStatus());
		model.setEnqueueDatetime(entity.getEnqueueDatetime());
		model.setColectDatetime(entity.getColectDatetime());
		model.setSalesCode(entity.getSalesCode());
		model.setUsn(entity.getUsn());
		model.setSettlementType(entity.getSettlementType());
		model.setEvent(entity.getEvent());
		model.setExternalReceivableId(entity.getExternalReceivableId());
		return model;
	}

	public List<ExternalTransactionEntity> toEntities(List<ExternalTransaction> models) {
		return models.stream().map(this::toEntity).collect(Collectors.toList());
	}

	public List<ExternalTransaction> toModels(List<ExternalTransactionEntity> entities) {
		return entities.stream().map(this::toModel).collect(Collectors.toList());
	}

}
