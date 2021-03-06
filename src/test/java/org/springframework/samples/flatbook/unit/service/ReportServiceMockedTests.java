package org.springframework.samples.flatbook.unit.service;

import static org.mockito.Mockito.when;
import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.ReportRepository;
import org.springframework.samples.flatbook.service.ReportService;

@ExtendWith(MockitoExtension.class)
class ReportServiceMockedTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	FIRSTNAME_2	= "Dani";
	private static final String	LASTNAME_2	= "Sanchez";
	private static final String	DNI_2		= "23330000B";
	private static final String	EMAIL_2		= "a@a.com";
	private static final String	USERNAME_2	= "asasa";
	private static final String	TELEPHONE_2	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private static final String	REASON		= "reason";
	private static final Integer ID		= 1;
	private static final Integer ID2		= 2;

	@Mock
	private ReportRepository	reportRepositoryMocked;

	private Collection<Report>  reports;
	private Report				report;
	private Report				report2;
	private Tenant				sender;
	private Tenant				receiver;

	private ReportService		reportServiceMocked;


	@BeforeEach
	void setupMock() {
		this.sender = new Tenant();
		this.sender.setPassword(PASSWORD);
		this.sender.setUsername(USERNAME_1);
		this.sender.setDni(DNI_1);
		this.sender.setEmail(EMAIL_1);
		this.sender.setEnabled(true);
		this.sender.setFirstName(FIRSTNAME_1);
		this.sender.setLastName(LASTNAME_1);
		this.sender.setPhoneNumber(TELEPHONE_1);

		this.receiver = new Tenant();
		this.receiver.setPassword(PASSWORD);
		this.receiver.setUsername(USERNAME_2);
		this.receiver.setDni(DNI_2);
		this.receiver.setEmail(EMAIL_2);
		this.receiver.setEnabled(true);
		this.receiver.setFirstName(FIRSTNAME_2);
		this.receiver.setLastName(LASTNAME_2);
		this.receiver.setPhoneNumber(TELEPHONE_2);

		this.report = new Report();
		this.report.setSender(this.sender);
		this.report.setCreationDate(LocalDate.now());
		this.report.setId(ID);
		this.report.setReason(REASON);
		this.report.setReceiver(this.receiver);

		this.report2 = new Report();
		this.report2.setSender(this.sender);
		this.report2.setCreationDate(LocalDate.now());
		this.report2.setId(ID2);
		this.report2.setReason(REASON);
		this.report2.setReceiver(this.receiver);

		this.reports = Arrays.asList(report, report2);

		this.reportServiceMocked = new ReportService(this.reportRepositoryMocked);
	}

	@Test
	void shouldFindReportById() {
		when(this.reportRepositoryMocked.findById(ID)).thenReturn(this.report);
		Report reportById = this.reportServiceMocked.findReportById(ID);
		assertThat(reportById).hasNoNullFieldsOrProperties();
		assertThat(reportById).hasId(1);
		assertThat(reportById).hasSender(this.sender);
		assertThat(reportById).hasReceiver(this.receiver);
		assertThat(reportById).hasReason(REASON);
	}

	@Test
	void shouldNotFindReport() {
		Report reportById = this.reportServiceMocked.findReportById(2);
		assertThat(reportById).isNull();
	}

	@Test
	void shouldSaveReport() {
		Mockito.lenient().doNothing().when(this.reportRepositoryMocked).save(ArgumentMatchers.isA(Report.class));
		this.reportServiceMocked.saveReport(this.report2);
		Mockito.verify(this.reportRepositoryMocked).save(this.report2);
	}

	@Test
	void shouldDeleteReport() {
        Mockito.lenient().doNothing().when(this.reportRepositoryMocked).deleteById(ID);
		this.reportServiceMocked.deleteReportById(ID);

		Mockito.verify(this.reportRepositoryMocked).deleteById(ID);
	}

	@Test
    void shouldFindReportsByReceiver() {
	    when(this.reportRepositoryMocked.findByReceiver(receiver)).thenReturn(reports);

        Collection<Report> reports = this.reportServiceMocked.findReportsByReceiver(receiver);
        Assertions.assertThat(reports).hasSize(2);

        Report report = reports.iterator().next();
        assertThat(report).hasId(1);
        assertThat(report).hasReceiver(receiver);
        assertThat(report).hasCreationDate(LocalDate.now());

    }

    @Test
    void shouldNotFindReportsByReceiver() {
        when(this.reportRepositoryMocked.findByReceiver(null)).thenReturn(new ArrayList<>());
        Collection<Report> reports = this.reportServiceMocked.findReportsByReceiver(null);
        Assertions.assertThat(reports).isEmpty();
    }

    @Test
    void shouldFindAllReports() {
	    Mockito.when(this.reportRepositoryMocked.findAll()).thenReturn(reports);

	    Collection<Report> reports = this.reportServiceMocked.findAllReports();
	    Assertions.assertThat(reports).hasSize(2);
	    Assertions.assertThat(reports).extracting(Report::getReason)
            .containsExactlyInAnyOrder(REASON, REASON);
    }
}
