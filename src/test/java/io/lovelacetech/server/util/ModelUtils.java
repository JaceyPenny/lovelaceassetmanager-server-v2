package io.lovelacetech.server.util;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ModelUtils {
  public static Company company(int index) {
    Company company = new Company();
    company.setId(new UUID(0, index));
    company.setName("test_company_" + index);
    company.setPhoneNumber(randomPhoneNumber());

    return company;
  }

  public static List<Company> companyList(int size) {
    List<Company> result = new ArrayList<>();
    for (int i = 1; i <= size; i++) {
      result.add(company(i));
    }
    return result;
  }

  private static String randomPhoneNumber() {
    Random random = new Random();
    int a1 = random.nextInt(7) + 1;
    int a2 = random.nextInt(8);
    int a3 = random.nextInt(8);
    int s2 = random.nextInt(643) + 100;
    int s3 = random.nextInt(8999) + 1000;

    return "" + a1 + a2 + a3 + s2 + s3;
  }

  public static User user(int index) {
    User user = new User();
    user.setId(new UUID(0, index));
    user.setEmail(String.format("test_email_%d@test.com", index));
    user.setPassword(PasswordUtils.encode("test_password_" + index));
    user.setAccessLevel(AccessLevel.USER);
    user.setCompanyId(new UUID(index, index));
    user.setFirstName("first_name_" + index);
    user.setLastName("last_name_" + index);
    return user;
  }

  public static List<User> userList(int size) {
    List<User> result = new ArrayList<>();
    for (int i = 1; i <= size; i++) {
      result.add(user(i));
    }
    return result;
  }
}
