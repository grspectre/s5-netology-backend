package com.utmn.shanaurin.supercomputers.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "supercomputer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supercomputer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @org.hibernate.annotations.UuidGenerator
    private String id;

    @Column
    private Integer rank;

    @Column(name = "previous_rank")
    private Integer previousRank;

    @Column(length = 255)
    private String name;

    @Column(name = "system_model", length = 500)
    private String systemModel;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String country;

    @Column
    private Integer year;

    @Column(length = 50)
    private String segment;

    @Column(name = "total_cores")
    private Integer totalCores;

    @Column(name = "accelerator_cores")
    private Integer acceleratorCores;

    @Column(name = "rmax_tflops")
    private Double rmaxTflops;

    @Column(name = "rpeak_tflops")
    private Double rpeakTflops;

    @Column(name = "power_kw")
    private Double powerKw;

    @Column(name = "power_efficiency")
    private Double powerEfficiency;

    @Column(length = 50)
    private String architecture;

    @Column(name = "processor_technology", length = 100)
    private String processorTechnology;

    @Column(name = "processor_speed_mhz")
    private Integer processorSpeedMhz;

    @Column(name = "operating_system", length = 100)
    private String operatingSystem;

    @Column(name = "os_family", length = 50)
    private String osFamily;

    @Column(length = 100)
    private String accelerator;

    @Column(name = "cores_per_socket")
    private Integer coresPerSocket;

    @Column(name = "system_family", length = 100)
    private String systemFamily;

    @Column(length = 50)
    private String continent;
}
