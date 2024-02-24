package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.ICourseBll;
import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CourseBll implements ICourseBll {

    private static class CourseBllHolder {
        private static final CourseBll INSTANCE = new CourseBll();
    }

    private CourseBll() {
    }

    public static CourseBll getInstance() {
        return CourseBllHolder.INSTANCE;
    }

    @Override
    public List<Course> getAllCourse() {
        List<Course> courses = CourseDal.getInstance().getAll();
        if (courses == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return courses;
        }
    }

    @Override
    public Course getById(Integer courseId) {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return null;
        } else {
            return course;
        }
    }

    @Override
    public int registerStudentForCourse(Integer personId, Integer courseId) {
        return 0;
    }

    @Override
    public Optional<Course> createCourse(String title, String credits, Integer departmentId, String url, Integer teacherId) {
        if(validateCourseOnline(title, credits, departmentId, url, teacherId)){
            Course newCourse = Course.builder()
                    .credits(validateCredits(credits))
                    .title(title)
                    .build();
            Integer courseId = CourseDal.getInstance().createCourse(newCourse, departmentId, teacherId)
                    .orElseThrow()
                    .getId();
            if(CourseDal.getInstance().createCourseOnline(courseId, url)){
                return Optional.of(newCourse);
            }

        }
        return Optional.empty();
    }

    @Override
    public Optional<Course> createCourse(String title, String credits, Integer departmentId, String location, LocalDate days, String time, Integer teacherId) {
        if(validateCourseOnsite(title, credits, departmentId, location, days, teacherId)){
            Course newCourse = Course.builder()
                    .credits(validateCredits(credits))
                    .title(title)
                    .build();
            Integer courseId = CourseDal.getInstance().createCourse(newCourse, departmentId, teacherId)
                    .orElseThrow()
                    .getId();
            if(CourseDal.getInstance().createCourseOnsite(courseId, location, days, Objects.requireNonNull(convertStringToTime(time)))){
                return Optional.of(newCourse);
            }

        }
        return Optional.empty();
    }

    @Override
    public int deleteCourse(Integer courseId) {
        return 0;
    }

    @Override
    public int updateCourse(Course course) {
        return 0;
    }

    public static LocalTime convertStringToTime(String timeString) {
        try {
            // Định dạng của chuỗi thời gian
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            // Chuyển đổi chuỗi thành đối tượng LocalTime
            return LocalTime.parse(timeString, formatter);
        } catch (DateTimeParseException e) {
            // Xử lý trường hợp ngoại lệ khi chuỗi không đúng định dạng
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return null; // hoặc giá trị mặc định khác tùy vào yêu cầu của bạn
        }
    }

    public static boolean validateCourseOnline(String title, String credits, Integer departmentId, String url, Integer teacherId) {
        // Kiểm tra mỗi thuộc tính và trả về false nếu có bất kỳ lỗi nào được phát hiện
        return validateTitle(title) &&
                validateDepartmentId(departmentId) &&
                validateUrl(url) &&
                validateTeacherId(teacherId);
    }

    public static boolean validateCourseOnsite(String title, String credits, Integer departmentId, String location, LocalDate days, Integer teacherId) {
        // Kiểm tra mỗi thuộc tính và trả về false nếu có bất kỳ lỗi nào được phát hiện
        return validateTitle(title) &&
                validateDepartmentId(departmentId) &&
                validateLocation(location) &&
                validateTeacherId(teacherId)
                ;
    }

    private static boolean validateTitle(String title) {
        // Kiểm tra xem title có tồn tại không
        if (title != null && !title.isEmpty()){
            return true;
        }
        DialogUtil.getInstance().showAlert("Lỗi", "Title không được để trống ", Alert.AlertType.ERROR);
        return false;
    }

    private static Integer validateCredits(String credits) {
        // Kiểm tra xem credits có tồn tại và có thể chuyển đổi thành số không
        try {
            Integer.parseInt(credits);
            return Integer.parseInt(credits);
        } catch (NumberFormatException e) {
            DialogUtil.getInstance().showAlert("Lỗi", "Tín chỉ phải là số", Alert.AlertType.ERROR);
            return -1;
        }
    }

    private static boolean validateDepartmentId(Integer departmentId) {
        // Kiểm tra xem departmentId có giá trị không
        return departmentId != null;
    }

    private static boolean validateUrl(String url) {
        // Kiểm tra xem url có tồn tại không và có định dạng URL hợp lệ không (điều này chỉ là ví dụ đơn giản)
        if(url != null){
            return true;
        }
        DialogUtil.getInstance().showAlert("Lỗi", "Link không được trống", Alert.AlertType.ERROR);
        return  false;
    }

    private static boolean validateTeacherId(Integer teacherId) {
        // Kiểm tra xem teacherId có giá trị không
        return teacherId != null;
    }

    private static boolean validateLocation(String location) {
        if(location != null && !location.trim().isEmpty()){
            return true;
        }
        DialogUtil.getInstance().showAlert("Lỗi", "Link không được trống", Alert.AlertType.ERROR);
        return false;
    }

    private static boolean validateDays(LocalDate days) {
        return days != null; // Có thể thêm các kiểm tra khác tùy vào yêu cầu
    }
}
