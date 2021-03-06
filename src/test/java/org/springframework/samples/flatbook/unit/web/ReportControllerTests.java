package org.springframework.samples.flatbook.unit.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.ReportService;
import org.springframework.samples.flatbook.web.ReportController;
import org.springframework.samples.flatbook.web.formatters.PersonFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ReportController.class,
includeFilters = {@ComponentScan.Filter(value = PersonFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class ReportControllerTests {

	private static final Integer TEST_REPORT_ID = 1;
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_REPORTED_USERNAME = "asignee";
    private static final String TEST_BAD_REPORTED_USERNAME = "bad";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private PersonService personService;

    @BeforeEach
    void setup() {

        Person creator = new Person();
        creator.setUsername(TEST_CREATOR_USERNAME);

        Person reported = new Person();
        reported.setUsername(TEST_REPORTED_USERNAME);

        LocalDate creationDate = LocalDate.now();

        Report report = new Report();
        report.setId(TEST_REPORT_ID);
        report.setCreationDate(creationDate);
        report.setReason("reason");
        report.setReceiver(reported);
        report.setSender(creator);

        given(this.personService.findUserById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.personService.findUserById(TEST_REPORTED_USERNAME)).willReturn(reported);
        given(this.reportService.findReportById(TEST_REPORT_ID)).willReturn(report);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/reports/{userId}/new", TEST_REPORTED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("reports/createOrUpdateReportForm"))
            .andExpect(model().attributeExists("report"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testInitCreationFormThrowExceptionBadReportedUsername() throws Exception {
        mockMvc.perform(get("/reports/{userId}/new", TEST_BAD_REPORTED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }


    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/reports/{userId}/new", TEST_REPORTED_USERNAME)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("reason", "reason")
        	.param("receiver", TEST_REPORTED_USERNAME)
        	.param("sender", TEST_CREATOR_USERNAME))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessCreationFormThrowExceptionBadReportedUsername() throws Exception {
        mockMvc.perform(post("/reports/{userId}/new", TEST_BAD_REPORTED_USERNAME)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("reason", "reason")
        	.param("receiver", TEST_REPORTED_USERNAME)
        	.param("sender", TEST_CREATOR_USERNAME))
        	.andExpect(status().isOk())
        	.andExpect(view().name("exception"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessCreationFormHasErrors() throws Exception {

        mockMvc.perform(post("/reports/{userId}/new", TEST_REPORTED_USERNAME)
            .with(csrf())
            .param("creationDate", "01/01/2005")
        	.param("reason", "")
        	.param("sender", TEST_CREATOR_USERNAME))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("report"))
            .andExpect(model().attributeHasFieldErrors("report", "reason"))
            .andExpect(model().attributeHasFieldErrors("report", "receiver"))
            .andExpect(view().name("reports/createOrUpdateReportForm"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testInitList() throws Exception {
        mockMvc.perform(get("/reports/list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("reports"))
            .andExpect(view().name("reports/reportsList"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testInitUserList() throws Exception {
        mockMvc.perform(get("/reports/{userId}/list", TEST_REPORTED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("username"))
            .andExpect(model().attributeExists("reports"))
            .andExpect(view().name("reports/reportsList"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testInitUserListThrowExceptionBadUserId() throws Exception {
        mockMvc.perform(get("/reports/{userId}/list", "baduser"))
            .andExpect(status().isOk())
            .andExpect(status().is2xxSuccessful());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessReportRemovalSucess() throws Exception {
        mockMvc.perform(get("/reports/{reportId}/delete", TEST_REPORT_ID))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessReportRemovalThrowExceptionBadReportId() throws Exception {
        mockMvc.perform(get("/reports/{reportId}/delete", 000))
        	.andExpect(status().isOk())
        	.andExpect(status().is2xxSuccessful());
    }
}
