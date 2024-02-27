package com.example.coursemanagement.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OnsiteCourse extends Course{
    private String location;
    private String days;
    private LocalTime time;
}
