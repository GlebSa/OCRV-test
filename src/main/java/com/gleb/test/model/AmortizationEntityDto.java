package com.gleb.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AMORTIZATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmortizationEntityDto implements Serializable {

    @Id
    @Column(name = "UUID", nullable = false, unique = true, columnDefinition = "uuid")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Column(name = "VEHICLE_KIND_UUID", columnDefinition = "uuid")
    @NotNull
    private UUID vehicleKindUuid;

    @Column(name = "USE_PERIOD")
    @NotNull
    private Integer usePeriod;

    @Column(name = "AVG_PRICE")
    @NotNull
    private Double avgPrice;

}
