package com.example.coursemanagement.bll.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    protected Integer id;
    protected String title;
    protected Integer credits;
    protected String type;
    protected String teacher;
    protected Integer sumOfStudent;
    protected String department;
}
