package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.response.user.UserListApiResponse;
import io.lovelacetech.server.repository.UserRepository;
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
@WebMvcTest(UserController.class)
public class UserControllerTests extends BaseTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void testGetAllUsers() throws Exception {
    List<User> userList = ModelUtils.userList(3);

    given(userRepository.findAll()).willReturn(userList);

    UserListApiResponse expectedResponse = new UserListApiResponse()
        .setSuccess()
        .setResponse(userList);

    mvc.perform(get("/api/users/").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonString(expectedResponse), true));
  }

}
