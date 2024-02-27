package com.example.coursemanagement.gui.button;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ButtonModel {

    private String key;

    private String text;
}
