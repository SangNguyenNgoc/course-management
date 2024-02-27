package com.example.coursemanagement.bll;

import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dal.StudentDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.utils.AppUtil;
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

public class CourseBll {

    private static class CourseBllHolder {
        private static final CourseBll INSTANCE = new CourseBll();
    }

    private CourseBll() {
    }

    public static CourseBll getInstance() {
        return CourseBllHolder.INSTANCE;
    }


    public List<Course> getAllCourse() {
        List<Course> courses = CourseDal.getInstance().getAll();
        if (courses == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return courses;
        }
    }

    public Course getById(Integer courseId) {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return null;
        } else {
            return course;
        }
    }

    public void registerStudentForCourse(String studentId, Integer courseId) throws Exception {
        int id = AppUtil.getInstance().validateInteger(studentId, "Mã sinh viên");
        Student student = StudentDal.getInstance().getById(id).orElse(null);
        if (student == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy sinh viên.", Alert.AlertType.ERROR);
            throw new Exception();
        }

        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }


        if (CourseDal.getInstance().isStudentInCourse(id, courseId)) {
            DialogUtil.getInstance().showAlert("Lỗi", "Sinh viên đã đăng ký khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }

        if (CourseDal.getInstance().isStudentInCourse(id, courseId) == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
            throw new Exception();
        }

        int result = CourseDal.getInstance().registerStudentForCourse(id, courseId);
        if (result == 0) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
            throw new Exception();
        }

    }

    public Optional<Course> createCourse(String title, String credits, Integer departmentId, String url, Integer teacherId) throws Exception {
        if (validateCourseOnline(title, credits, departmentId, url, teacherId)) {
            Course newCourse = Course.builder()
                    .credits(validateCredits(credits))
                    .title(title)
                    .build();
            Integer courseId = CourseDal.getInstance().createCourse(newCourse, departmentId, teacherId)
                    .orElseThrow()
                    .getId();
            if (CourseDal.getInstance().createCourseOnline(courseId, url)) {
                return Optional.of(newCourse);
            }

        }
        return Optional.empty();
    }

    public Optional<Course> createCourse(String title, String credits, Integer departmentId, String location, String days, String time, Integer teacherId) throws Exception {
        if (validateCourseOnsite(title, credits, departmentId, location, days, teacherId)) {
            Course newCourse = Course.builder()
                    .credits(validateCredits(credits))
                    .title(title)
                    .build();
            Integer courseId = CourseDal.getInstance().createCourse(newCourse, departmentId, teacherId)
                    .orElseThrow()
                    .getId();
            if (CourseDal.getInstance().createCourseOnsite(courseId, location, days, Objects.requireNonNull(convertStringToTime(time)))) {
                return Optional.of(newCourse);
            }

        }
        return Optional.empty();
    }

    public void deleteCourse(Integer courseId) throws Exception {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        int amount = StudentBll.getInstance().getStudentsInCourse(courseId).size();
        if (amount != 0) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không thể xóa do đã có học sinh đăng ký.", Alert.AlertType.WARNING);
            throw new Exception();
        }
        CourseDal.getInstance().deleteCourse(courseId);
    }


    public static LocalTime convertStringToTime(String timeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(timeString, formatter);
        } catch (DateTimeParseException e) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return null;
        }
    }

    public int updateCourse(Integer courseId, String title, String credits, Integer departmentId, String url, Integer teacherId) throws Exception {
        if (validateCourseOnline(title, credits, departmentId, url, teacherId)) {
            Course course = Course.builder()
                    .id(courseId)
                    .title(title)
                    .credits(validateCredits(credits))
                    .build();
            if (CourseDal.getInstance().updateCourse(course, departmentId, teacherId) != 0) {
                return CourseDal.getInstance().updateCourseOnline(courseId, url);
            }
        }
        return 0;
    }

    public int updateCourse(Integer courseId, String title, String credits, Integer departmentId, String location, String days, String time, Integer teacherId) throws Exception {
        if (validateCourseOnsite(title, credits, departmentId, location, days, teacherId)) {
            Course newCourse = Course.builder()
                    .id(courseId)
                    .credits(validateCredits(credits))
                    .title(title)
                    .build();
            if (CourseDal.getInstance().updateCourse(newCourse, departmentId, teacherId) != 0) {
                return CourseDal.getInstance().updateCourseOnsite(courseId, location, days, Objects.requireNonNull(convertStringToTime(time)));
            }
        }
        return 0;
    }

    public static boolean validateCourseOnline(String title, String credits, Integer departmentId, String url, Integer teacherId) {
        return validateTitle(title) &&
                validateDepartmentId(departmentId) &&
                validateUrl(url) &&
                validateTeacherId(teacherId);
    }

    public static boolean validateCourseOnsite(String title, String credits, Integer departmentId, String location, String days, Integer teacherId) {
        return validateTitle(title) &&
                validateDepartmentId(departmentId) &&
                validateLocation(location) &&
                validateTeacherId(teacherId)
                ;
    }

    private static boolean validateTitle(String title) {
        // Kiểm tra xem title có tồn tại không
        if (title != null && !title.isEmpty()) {
            return true;
        }
        DialogUtil.getInstance().showAlert("Lỗi", "Title không được để trống ", Alert.AlertType.ERROR);
        return false;
    }

    private static Integer validateCredits(String credits) throws Exception {
        try {
            Integer.parseInt(credits);
            return Integer.parseInt(credits);
        } catch (NumberFormatException e) {
            DialogUtil.getInstance().showAlert("Lỗi", "Tín chỉ phải là số", Alert.AlertType.ERROR);
            throw new Exception();
        }
    }

    private static boolean validateDepartmentId(Integer departmentId) {
        // Kiểm tra xem departmentId có giá trị không
        return departmentId != null;
    }

    private static boolean validateUrl(String url) {
        // Kiểm tra xem url có tồn tại không và có định dạng URL hợp lệ không (điều này chỉ là ví dụ đơn giản)
        if (url != null) {
            return true;
        }
        DialogUtil.getInstance().showAlert("Lỗi", "Link không được trống", Alert.AlertType.ERROR);
        return false;
    }

    private static boolean validateTeacherId(Integer teacherId) {
        // Kiểm tra xem teacherId có giá trị không
        return teacherId != null;
    }

    private static boolean validateLocation(String location) {
        if (location != null && !location.trim().isEmpty()) {
            return true;
        }
        DialogUtil.getInstance().showAlert("Lỗi", "Link không được trống", Alert.AlertType.ERROR);
        return false;
    }

    private static boolean validateDays(LocalDate days) {
        return days != null; // Có thể thêm các kiểm tra khác tùy vào yêu cầu
    }
}
