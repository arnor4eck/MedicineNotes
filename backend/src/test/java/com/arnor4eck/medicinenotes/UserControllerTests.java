package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.controller.UserController;
import com.arnor4eck.medicinenotes.service.UserService;
import com.arnor4eck.medicinenotes.test_utils.WebMvcTestWithoutFilters;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTestWithoutFilters(controllers = {UserController.class})
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    public void testUserRegistrationOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserRequest request = new CreateUserRequest("test",
                "test@mail.ru", "password");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserRegistration5xx() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserRequest request = new CreateUserRequest("test",
                "test@mailru", "password"); // Валидация email
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }
}
