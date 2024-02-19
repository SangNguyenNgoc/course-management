package com.example.coursemanagement.page;

import java.io.IOException;

public interface Route {
    void changeView(Component component) throws IOException;
}
