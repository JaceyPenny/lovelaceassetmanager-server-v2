package io.lovelacetech.lovelaceassetmanager.controllers;

import io.lovelacetech.lovelaceassetmanager.commands.companies.CompaniesQuery;
import io.lovelacetech.lovelaceassetmanager.commands.companies.CompanyByCredentialsQuery;
import io.lovelacetech.lovelaceassetmanager.commands.companies.CompanySaveCommand;
import io.lovelacetech.lovelaceassetmanager.models.api.Company;
import io.lovelacetech.lovelaceassetmanager.models.api.CompanyListing;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "apiv2/company")
public class CompanyRestController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public CompanyListing getCompanies() {
        return new CompaniesQuery().execute();
    }

    @RequestMapping(value = "/byCredentials/{email}/{password}", method = RequestMethod.GET)
    public Company getCompanyByCredentials(@PathVariable String email, @PathVariable String password) {
        return new CompanyByCredentialsQuery()
                .setEmail(email)
                .setPassword(password)
                .execute();

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Company putCompany(@RequestBody Company company) {
        return new CompanySaveCommand()
                .setApiCompany(company)
                .execute();
    }

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "Successful test. (CompanyRestController)";
    }
}
