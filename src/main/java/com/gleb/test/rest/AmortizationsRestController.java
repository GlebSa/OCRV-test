package com.gleb.test.rest;

import com.gleb.test.services.AmortizationsService;
import com.rzd.task.api.AmortizationsApiDelegate;
import com.rzd.task.dto.AmortizationDto;
import com.rzd.task.dto.AmortizationFilterDto;
import com.rzd.task.dto.AmortizationViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;

@RestController
@RequiredArgsConstructor
public class AmortizationsRestController implements AmortizationsApiDelegate {

    private final AmortizationsService amortizationsService;

    @PostMapping("/amortizations")
    @Override
    public ResponseEntity<AmortizationViewDto> addAmortization(@RequestBody AmortizationDto amortizationDto) {
        return ResponseEntity.ok(amortizationsService.addAmortization(amortizationDto));
    }

    @GetMapping("/amortizations")
    @Override
    public ResponseEntity<List<AmortizationViewDto>> getAmortizations(AmortizationFilterDto filter, Pageable page) {
        return ResponseEntity.ok(amortizationsService.getAmortizations(filter, page));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<String> handleConflict(ConstraintViolationException e) {
        //Тестовый пример (не для прода), поскольку нет валидации AmortizationDto
        return ResponseEntity.badRequest().body(e.getConstraintViolations().toString());
    }

}
