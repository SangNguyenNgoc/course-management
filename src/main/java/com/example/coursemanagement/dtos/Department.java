package com.example.coursemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    protected Integer id;
    protected String name;
    protected Double budget;
    protected Date startDate;
    protected Integer administratorId;
    protected String administrator;
}
