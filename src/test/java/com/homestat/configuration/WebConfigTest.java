package com.homestat.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebConfig.class)
class WebConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private final String frontendUrl = "http://localhost:3000";

    @BeforeEach
    void setUp() {
        // Setup can be used for initializing additional mock beans if needed
    }

    @Test
    void testCorsConfiguration() throws Exception {
        // Act & Assert
        mockMvc.perform(options("/some-endpoint") // Use an OPTIONS request to test CORS preflight
                        .header(HttpHeaders.ORIGIN, frontendUrl)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk()) // Verify the preflight request is successful
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, frontendUrl))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*"));
    }

    @Test
    void testCorsWithUnauthorizedOrigin() throws Exception {
        // Act & Assert
        mockMvc.perform(options("/some-endpoint")
                        .header(HttpHeaders.ORIGIN, "http://unauthorized-origin.com")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isForbidden()); // Request should be forbidden for unauthorized origins
    }
}
