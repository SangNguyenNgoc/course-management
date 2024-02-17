package com.example.coursemanagement.page;

import lombok.Getter;

@Getter
public enum Component {

    HOME_PAGE("view/home-view.fxml"),

    LIST_COURSE("view/list-course.fxml"),

    COURSE_ITEM("view/course-item.fxml");

    private final String value;

    Component(String value) {
        this.value = value;
    }
}
