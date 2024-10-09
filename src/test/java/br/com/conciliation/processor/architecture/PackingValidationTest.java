package br.com.conciliation.processor.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class PackingValidationTest {

	@Test
	void domainCannotAccessApplicationOrInfrastructure() {
		JavaClasses domain = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
				.importPackages("br.com.conciliation.processor.domain");

		noClasses().should().dependOnClassesThat()
				.resideInAnyPackage("br.com.conciliation.processor.application..",
						"br.com.conciliation.processor.infrastructure..")
				.check(domain);
	}

	@Test
	void applicationCannotAccessInfrastructure() {
		JavaClasses domain = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
				.importPackages("br.com.conciliation.processor.application");

		noClasses().should().dependOnClassesThat()
				.resideInAnyPackage("br.com.conciliation.processor.infrastructure..").check(domain);
	}

	@Test
	void domainCannotAccessSpring() {
		JavaClasses domain = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
				.importPackages("br.com.conciliation.processor.domain");

		noClasses().should().dependOnClassesThat().resideInAnyPackage("org.springframework..").check(domain);
	}

}
