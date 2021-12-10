package com.htec.gateway;

import com.htec.gateway.controller.GatewayController;
import com.htec.gateway.service.GatewayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GatewayController controller;

    @MockBean
    private GatewayService gatewayService;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void test_Ok_AccountMicroservice() throws Exception {

        when(gatewayService.sendRequest(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        this.mockMvc.perform(get("/account/auth/authenticate"))
                .andDo(print()).andExpect(status().is(HttpStatus.OK.value()));

    }

    @Test
    public void test_Forbidden_AccountMicroservice() throws Exception {

        when(gatewayService.sendRequest(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));

        this.mockMvc.perform(get("/account/auth/authenticate"))
                .andDo(print()).andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }

    @Test
    public void test_BadRequest_AccountMicroservice() throws Exception {

        when(gatewayService.sendRequest(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        this.mockMvc.perform(post("/account/login"))
                .andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    }

}
