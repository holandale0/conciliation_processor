package br.com.conciliation.processor.application;

import br.com.conciliation.processor.domain.conciliationError.model.ConciliationError;
import br.com.conciliation.processor.domain.conciliationError.service.ConciliationErrorService;
import br.com.conciliation.processor.domain.customer.model.Customer;
import br.com.conciliation.processor.domain.customer.service.CustomerService;
import br.com.conciliation.processor.domain.externalTransaction.model.ExternalTransaction;
import br.com.conciliation.processor.domain.externalTransaction.service.ExternalTransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ConciliationProcessorApplicationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConciliationProcessorApplicationService.class);

	// @Value("${scheduler.initial-date}")
	private final LocalDate initialDate = LocalDate.of(2024, 9, 7);

	// @Value("${scheduler.final-date}")
	private final LocalDate finalDate = LocalDate.of(2024, 9, 9);

	private final ExternalTransactionService externalTransactionService;

	private final CustomerService customerService;

	private final ConciliationErrorService conciliationErrorService;

	private final ExecutorService executorService;

	public ConciliationProcessorApplicationService(CustomerService customerService,
			ExternalTransactionService externalTransactionService, ConciliationErrorService conciliationErrorService,
			ExecutorService executorService) {
		this.customerService = customerService;
		this.externalTransactionService = externalTransactionService;
		this.conciliationErrorService = conciliationErrorService;
		this.executorService = executorService;
	}

	public void startConciliationProcessor() {
		AtomicInteger transactionCount = new AtomicInteger();

		AtomicInteger totalTransactions = new AtomicInteger();

		Instant startTime = Instant.now();

		// Recupera a lista de clientes para realizar o processamento
		List<Customer> customers = customerService.listCustomers();

		customers.forEach(customer -> {
			LOGGER.info("Iniciando o processo de conciliação para o cliente: {}", customer.getDocument());

			// Busca as transações externas do cliente dentro do intervalo de datas
			List<ExternalTransaction> externalTransactions = externalTransactionService
					.getExternalTransactions(initialDate, finalDate, customer.getDocument());

			transactionCount.set(externalTransactions.size());
			totalTransactions.addAndGet(transactionCount.get());

			// Processa as transações em paralelo usando ExecutorService
			List<Callable<Void>> tasks = externalTransactions.stream()
					.map(externalTransaction -> (Callable<Void>) () -> {
						LOGGER.info("Processando conciliação do cliente: {} NSU: {}", externalTransaction.getDocument(),
								externalTransaction.getUsn());
						externalTransactionService.processConciliation(externalTransaction);
						return null;
					}).toList();

			try {
				// Executa todas as tarefas em paralelo e espera pela conclusão
				List<Future<Void>> futures = executorService.invokeAll(tasks);

				// Verifica se todas as tarefas foram concluídas com sucesso
				for (Future<Void> future : futures) {
					future.get();
				}
			}
			catch (InterruptedException | ExecutionException e) {
				LOGGER.error("Erro durante o processamento paralelo de conciliação: ", e);

				ConciliationError conciliationError = new ConciliationError();
				conciliationError.setId(UUID.randomUUID());
				conciliationError.setDocument(customer.getDocument());
				conciliationError.setProcessDatetime(LocalDateTime.now());
				conciliationError.setErrorMessage(e.getMessage());
				conciliationErrorService.saveConciliationError(conciliationError);

				Thread.currentThread().interrupt();
			}

			// Atualiza o log de processamento
			// if (transactionCount.get() > 0) {
			// LOGGER.info("..:: Processo de conciliação concluído para o cliente: {}
			// ::..", customer.getDocument());
			// transactionCount.set(0);
			// }

		});

		// Finaliza o executor service
		executorService.shutdown();

		// Calcula o tempo total de execução
		Instant endTime = Instant.now();
		Duration duration = Duration.between(startTime, endTime);

		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();

		LOGGER.info("...::: Processo de conciliação concluído ! :::...");
		LOGGER.info("...::: Foram processadas {} transações em: {} horas, {} minutos, {} segundos :::...",
				totalTransactions.get(), hours, minutes, seconds);
	}

}
