package com.gleb.test.services;

import com.gleb.test.mapper.AmortizationMapper;
import com.gleb.test.model.AmortizationEntityDto;
import com.gleb.test.repositories.AmortizationRepository;
import com.rzd.task.dto.AmortizationDto;
import com.rzd.task.dto.AmortizationFilterDto;
import com.rzd.task.dto.AmortizationViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmortizationsService {

    private final AmortizationRepository amortizationRepository;
    private final AmortizationMapper amortizationMapper;

    public AmortizationViewDto addAmortization(AmortizationDto amortizationDto) {
        AmortizationEntityDto entity = amortizationRepository.save(amortizationMapper.toEntity(amortizationDto));
        return amortizationMapper.toView(entity);
    }

    public List<AmortizationViewDto> getAmortizations(AmortizationFilterDto filter, Pageable page) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<AmortizationEntityDto> exampleQuery = Example.of(amortizationMapper.toEntity(filter), matcher);
        return amortizationRepository.findAll(exampleQuery, page).stream()
            .map(amortizationMapper::toView)
            .collect(Collectors.toList());
    }

}
