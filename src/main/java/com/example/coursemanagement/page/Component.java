package com.example.coursemanagement.page;

import lombok.Getter;

@Getter
public enum Component {

    HOME_PAGE("view/home-view.fxml"),

    DASHBOARD("view/dashboard.fxml"),

    COURSE_ITEM("view/course-item.fxml"),

    COURSE_DETAIL("view/course-detail.fxml"),

    ADD_COURSE("view/add-course.fxml"),

    LIST_COURSE("view/list-course.fxml"),

    EMPTY("view/empty.fxml"),

    ONLINE_COURSE_FORM("view/online-course-form.fxml"),

    ONSITE_COURSE_FORM("view/onsite-course-form.fxml"),

    UPDATE_GRADE("view/update-grade.fxml");

    private final String value;

    Component(String value) {
        this.value = value;
    }
}
