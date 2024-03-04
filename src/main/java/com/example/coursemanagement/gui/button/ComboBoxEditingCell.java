package com.example.coursemanagement.gui.button;

import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.bll.dtos.Teacher;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;

import java.util.List;

public class ComboBoxEditingCell extends TableCell<Department, String> {

    private ComboBox<String> comboBox;

    public ComboBoxEditingCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getTyp());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp());
                setGraphic(comboBox);
            } else {
                setText(getTyp());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        List<Teacher> teachers = TeacherBll.getInstance().getAllTeacher();
        comboBox = new ComboBox<String>();
        teachers.forEach(item -> comboBox.getItems().add(item.getId() + " - " + item.getLastName() + " " + item.getFirstName()));
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getTyp());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
            System.out.println("Committed: " + teachers.get(comboBox.getSelectionModel().getSelectedIndex()).getId());
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
    }

    private void comboBoxConverter(ComboBox<String> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });
    }

    private String getTyp() {
        return getItem() == null ? " " : getItem();
    }
}