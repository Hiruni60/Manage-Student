package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class mainFormController {
    public TextField txtStId;
    public TextField txtStName;
    public TextField txtEmail;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtNic;
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        saveStudent();
        loadAllStudent();
        clearOnAction();

    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student student = new Student(
                txtStId.getText(),
                txtStName.getText(),
                txtEmail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNic.getText()
        );
        boolean isStUpdated = CrudUtil.execute("UPDATE student SET student_name=?, email=?, contact=?, address=?, NIC=? WHERE student_id=?",
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getContact(),
                student.getAddress(),
                student.getNic();
        if (isStUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

        loadAllStudent();
        clearOnAction();
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student emp = (Student) tblStudent.getSelectionModel().getSelectedItem();
        tblStudent.getItems().remove(emp);

        boolean isEmDeleted=CrudUtil.execute("DELETE FROM student WHERE student_id=? ",
                emp.getId()

        );
        if (isEmDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }

    private void saveStudent() {
        Student student = new Student(
                txtStId.getText(),
                txtStName.getText(),
                txtEmail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNic.getText()
        );
        try {
            if (CrudUtil.execute("INSERT INTO student VALUES (?,?,?,?,?,?)",
                    student.getId(), student.getName(), student.getEmail(), student.getContact(), student.getAddress(), student.getNic())) {
                new Alert(Alert.AlertType.CONFIRMATION, "SAVE").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Empty Result").show();
        }
    }

    private void loadAllStudent() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM student");
        ObservableList<Student> oblist = FXCollections.observableArrayList();
        while (resultSet.next()) {
            oblist.add(
                    new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6)
                    )
            );
        }
        tblStudent.setItems(oblist);
        tblStudent.refresh();
    }
    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));

        loadAllStudent();
    }

    private void clearOnAction() {
        txtStId.clear();
        txtStName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNic.clear();
    }
    public void stIdOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM student WHERE student_id=? ", txtStId.getText());
            if (result.next()) {
                txtStName.setText(result.getString(2));
                txtEmail.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtAddress.setText(result.getString(5));
                txtNic.setText(result.getString(6));


            } else {
                new Alert(Alert.AlertType.WARNING, "Empty set").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
