package com.example.coursemanagement.gui.page;

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

    REGISTER("view/register.fxml"),

    LIST_DEPARTMENT("view/list-department.fxml"),

    DEPARTMENT_ITEM("view/department-item.fxml"),

    ADD_DEPARTMENT("view/add-department.fxml"),

    LIST_PERSON("view/list-person.fxml"),

    PERSON_ITEM("view/person-item.fxml"),

    ADD_PERSON("view/add-person.fxml"),

    COURSE_FILTER("view/course-filter.fxml");


    private final String value;

    Component(String value) {
        this.value = value;
    }
}
