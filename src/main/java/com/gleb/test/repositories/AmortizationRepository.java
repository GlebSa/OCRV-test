package com.gleb.test.repositories;

import com.gleb.test.model.AmortizationEntityDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AmortizationRepository extends JpaRepository<AmortizationEntityDto, UUID> {
}
