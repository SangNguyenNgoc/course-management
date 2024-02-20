package com.example.coursemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    protected Integer id;
    protected String name;
    protected Double budget;
    protected LocalDateTime startDate;
    protected Integer administrator;
}
