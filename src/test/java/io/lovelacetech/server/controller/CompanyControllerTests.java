package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.model.api.response.company.CompanyListApiResponse;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.test.BaseTests;
import io.lovelacetech.server.util.ModelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CompanyController.class)
public class CompanyControllerTests extends BaseTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private CompanyRepository companyRepository;

  @Test
  public void testGetCompanies() throws Exception {
    List<Company> companyList = ModelUtils.companyList(3);

    given(companyRepository.findAll()).willReturn(companyList);

    CompanyListApiResponse expectedResponse = new CompanyListApiResponse()
        .setSuccess()
        .setResponse(companyList);

    this.mvc.perform(get("/api/companies/").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonString(expectedResponse), true));
  }

  @Test
  public void testFindByName() throws Exception {
    Company company1 = ModelUtils.company(1);
    Company company2 = ModelUtils.company(2);

    given(companyRepository.findByName(company1.getName())).willReturn(company1);
    given(companyRepository.findByName(company2.getName())).willReturn(company2);

    CompanyApiResponse expectedResponse = new CompanyApiResponse()
        .setSuccess()
        .setResponse(company1.toApi());

    this.mvc.perform(get("/api/companies/byName/" + company1.getName())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonString(expectedResponse), true));
  }

  @Test
  public void testFindByName_no_companies() throws Exception {
    String searchName = "test_name";

    CompanyApiResponse expectedResponse = new CompanyApiResponse()
        .setNotFoundByName(searchName)
        .setResponse(null);

    this.mvc.perform(get("/api/companies/byName/" + searchName).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonString(expectedResponse), true));
  }

  @Test
  public void testFindByPhoneNumber() throws Exception {
    Company company1 = ModelUtils.company(1);
    Company company2 = ModelUtils.company(2);

    given(companyRepository.findByPhoneNumber(company1.getPhoneNumber())).willReturn(company1);
    given(companyRepository.findByPhoneNumber(company2.getPhoneNumber())).willReturn(company2);

    CompanyApiResponse expectedResponse = new CompanyApiResponse()
        .setSuccess()
        .setResponse(company1.toApi());

    this.mvc.perform(get("/api/companies/byPhoneNumber/" + company1.getPhoneNumber())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonString(expectedResponse), true));
  }
}
