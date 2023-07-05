package com.gleb.test.mapper;

import com.gleb.test.model.AmortizationEntityDto;
import com.rzd.task.dto.AmortizationDto;
import com.rzd.task.dto.AmortizationFilterDto;
import com.rzd.task.dto.AmortizationViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AmortizationMapper {

    public abstract AmortizationEntityDto toEntity(AmortizationDto amortizationDto);

    public abstract AmortizationEntityDto toEntity(AmortizationFilterDto filter);

    @Mapping(target = "totalPrice", expression = "java(getTotalPrice(entity))")
    public abstract AmortizationViewDto toView(AmortizationEntityDto entity);

    protected Double getTotalPrice(AmortizationEntityDto entity) {
        return entity.getUsePeriod() * entity.getAvgPrice();
    }

}
