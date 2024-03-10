package com.example.coursemanagement.bll.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends Person {
    protected LocalDateTime hireDate;
}
