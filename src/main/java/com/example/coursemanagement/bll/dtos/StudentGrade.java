package com.example.coursemanagement.bll.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGrade extends Student {
    protected Double grade;
}
