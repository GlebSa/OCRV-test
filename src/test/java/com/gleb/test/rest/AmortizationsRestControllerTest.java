package com.gleb.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gleb.test.repositories.AmortizationRepository;
import com.rzd.task.dto.AmortizationDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Поскольку база и так h2 не стал делать отдельную конфигурацию для тестов
 *
 * testContainers пока не взлетели, если успею, переделаю
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AmortizationsRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AmortizationRepository amortizationRepository;

    @Test
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addAmortization() throws Exception {
        UUID uuid = UUID.randomUUID();
        AmortizationDto dto = createAmortizationDto(uuid, 2, 1.0);
        mockMvc.perform(MockMvcRequestBuilders.post("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleKindUuid", Matchers.is(uuid.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.usePeriod", Matchers.is(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.avgPrice", Matchers.is(1.0)));

        assertEquals(1, amortizationRepository.findAll().size());
    }

    @Test
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addAmortization_fail() throws Exception {
        AmortizationDto dto = createAmortizationDto(null, 2, 1.0);
        mockMvc.perform(MockMvcRequestBuilders.post("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertTrue(amortizationRepository.findAll().isEmpty());
    }

    @Test
    @Sql(scripts = "classpath:scripts/insertAmortisations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAmortizations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/amortizations")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    @Sql(scripts = "classpath:scripts/insertAmortisations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAmortizations_page() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "2"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @Sql(scripts = "classpath:scripts/insertAmortisations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAmortizations_filterUsePeriod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "3")
                .param("usePeriod", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Sql(scripts = "classpath:scripts/insertAmortisations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAmortizations_filterAvgPrice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "3")
                .param("avgPrice", "2.0"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Sql(scripts = "classpath:scripts/insertAmortisations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAmortizations_filterVehicleKindUuid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "3")
                .param("vehicleKindUuid", "cdd4cab0-bf21-4fe9-b851-0d577ab80555"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @Sql(scripts = "classpath:scripts/insertAmortisations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/clearAmortisations.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAmortizations_empty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/amortizations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "3")
                .param("vehicleKindUuid", UUID.randomUUID().toString()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));
    }

    private static AmortizationDto createAmortizationDto(UUID uuid, int usePeriod, double avgPrice) {
        AmortizationDto dto = new AmortizationDto();
        dto.setVehicleKindUuid(uuid);
        dto.setUsePeriod(2);
        dto.setAvgPrice(1.0);
        return dto;
    }
}