package controllers;

import dboperations.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.Course;
import objects.dbinterfaces.*;
import users.Student;
import users.Teacher;
import users.dbinterfaces.TeacherDatabaseOperation;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class TeacherDashboardController implements Initializable {

    public TextField ccode;
    public TextField cname;
    public TextField section;
    public TextField cid;
    private Teacher teacher;
    // currently logged in Teacher

    @FXML
    private Label teacherNameLabel;
    @FXML
    private Label teacherIdLabel;

    // anchor panes
    @FXML
    private AnchorPane teacherDashboardHomePane;
    @FXML
    private AnchorPane teacherInformationPane;
    @FXML
    private AnchorPane adminAddCourse;

    private LocalDate evaluationDeadline;

    // teacher information page variables
    @FXML
    private Label evaluationDeadlineLabel;
    @FXML
    private Label teacherInformationNameLabel;
    @FXML
    private Label teacherInformationIdLabel;
    @FXML
    private TextField teacherInformationEmailField;
    @FXML
    private TextField teacherInformationBloodGroupField;
    @FXML
    private TextField teacherInformationContactNumberField;
    @FXML
    private TextField teacherInformationAddressField;
    @FXML
    private Label teacherInformationUpdationStatusLabel;
    @FXML
    private Label teacherInformationOfficeLabel;
    @FXML
    private AnchorPane studentRegistrationPane;
    private Student student;
    // Course picker table
    private int registrationId;
    @FXML
    private ObservableList <Course> courseList;
    @FXML
    private TableView <Course> registrationTableView;
    @FXML
    private Label registrationStatusLabel;

    // Registered courses table
    @FXML
    private ObservableList < Course > registeredCourseList;
    @FXML
    private TableView <Course> registeredCoursesTableView;
    @FXML
    private Label removeCourseStatusLabel;


    @FXML
    private void handleDashboardSignout(ActionEvent actionEvent) throws IOException {
        /*
         * Signing out will take user to login page
         * */
        Parent LoginUIParent = FXMLLoader.load(getClass().getResource("/gui/LoginUI.fxml"));
        Scene LoginUIScene = new Scene(LoginUIParent);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(LoginUIScene);
        window.show();
    }

    private void fetchTeacherInformationFromDatabase() throws SQLException {
        String teacherId = LoginUIController.getUsername();
        TeacherDatabaseOperation teacherOp = new TeacherDatabaseOperationImplementation();
        teacher = teacherOp.getTeacher(teacherId);

        teacherInformationNameLabel.setText(teacher.getTeacherName());
        teacherInformationIdLabel.setText(teacher.getTeacherInitial());
        teacherInformationOfficeLabel.setText("Department of CSE, QV University");

        teacherInformationEmailField.setText(teacher.getTeacherEmail());
        teacherInformationBloodGroupField.setText(teacher.getTeacherBloodGroup());
        teacherInformationContactNumberField.setText(teacher.getTeacherContactNumber());
        teacherInformationAddressField.setText(teacher.getTeacherAddress());
    }

    @FXML
    private void populateTeacherInformationBar() {
        teacherIdLabel.setText(teacher.getTeacherInitial());
        teacherNameLabel.setText(teacher.getTeacherName());
    }


    /***************************************
     *  Navigation related method are below
     ****************************************/
    @FXML
    private void navigateToTeacherDashboard(ActionEvent actionEvent) {
        teacherInformationPane.setVisible(false);
        adminAddCourse.setVisible(false);

        teacherDashboardHomePane.setVisible(true);
    }

    @FXML
    private void navigateToTeacherInformation(ActionEvent actionEvent) {
        teacherInformationUpdationStatusLabel.setText(null);

        teacherDashboardHomePane.setVisible(false);

        teacherInformationPane.setVisible(true);
    }

    /******************************************************
     *  "Teacher Information" page related method are below
     ******************************************************/


    private void fetchCourseInformationFromDatabase() throws SQLException{
        courseList = FXCollections.observableArrayList();
        CourseDatabaseOperation courseOp = new CourseDatabaseOperationImplementation();
        courseList = courseOp.getAllCourses();

//        for (Object course : courseList)
//            System.out.println(course.toString());
    }

    private void fetchRegistrationInformationFromDatabase() throws SQLException{
        RegistrationDatabaseOperation regOp = new RegistrationDatabaseOperationImplementation();
        registrationId = regOp.getLastPrimaryKey();
        System.out.println(registrationId);
        registrationId = registrationId + 1; // next primary key

        registeredCourseList = regOp.getAllRegisteredCourses(student);
    }

    private void populateRegistrationTableView(){
        TableColumn<Course, Integer> courseId = new TableColumn<>("ID");
        courseId.setCellValueFactory(new PropertyValueFactory("courseId"));
        // Course Title Column
        TableColumn<Course, String> courseCode = new TableColumn<>("Course Code");
        courseCode.setCellValueFactory(new PropertyValueFactory("courseCode"));

        TableColumn<Course, String> courseTitle = new TableColumn<>("Course Title");
        courseTitle.setCellValueFactory(new PropertyValueFactory("courseTitle"));

        TableColumn<Course, Integer> courseSection = new TableColumn<>("Section");
        courseSection.setCellValueFactory(new PropertyValueFactory("courseSection"));

        registrationTableView.setItems(courseList);
        registrationTableView.getColumns().addAll(courseId, courseCode, courseTitle, courseSection);
    }

    private void populateRegisteredCoursesTableView(){
        TableColumn<Course, Integer> courseId = new TableColumn<>("ID");
        courseId.setCellValueFactory(new PropertyValueFactory("courseId"));
        // Course Title Column
        TableColumn<Course, String> courseCode = new TableColumn<>("Course Code");
        courseCode.setCellValueFactory(new PropertyValueFactory("courseCode"));

        TableColumn<Course, String> courseTitle = new TableColumn<>("Course Title");
        courseTitle.setCellValueFactory(new PropertyValueFactory("courseTitle"));

        TableColumn<Course, Integer> courseSection = new TableColumn<>("Section");
        courseSection.setCellValueFactory(new PropertyValueFactory("courseSection"));

        registeredCoursesTableView.setItems(registeredCourseList);
        registeredCoursesTableView.getColumns().addAll(courseId, courseCode, courseTitle, courseSection);
    }



    @FXML
    private void handleCourseRemoval(ActionEvent actionEvent) {
        registrationStatusLabel.setText(null); // clearing other fields

        Course pickedCourse = registeredCoursesTableView.getSelectionModel().getSelectedItem();
        if (pickedCourse == null){
            removeCourseStatusLabel.setText("Select a course first!");
            return;
        }

        if (pickedCourse.getCourseId() == 1 ){
            removeCourseStatusLabel.setText("You can not remove CSE4000 after joining a Research Team.");
            return;
        }

        RegistrationDatabaseOperation regOp = new RegistrationDatabaseOperationImplementation();
        boolean removeStatus = regOp.removeRegistration(pickedCourse, student);
        if (removeStatus){
            registeredCourseList.remove(pickedCourse);
            removeCourseStatusLabel.setText("Removed " + pickedCourse.getCourseCode() + " successfully.");
        } else{
            removeCourseStatusLabel.setText("Failed to remove " + pickedCourse.getCourseCode() + "!");
        }
    }

        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addCourse(ActionEvent actionEvent) {
        String result=TeacherDatabaseOperationImplementation.addCourse(cid.getText(),ccode.getText(), cname.getText(), section.getText());
        String [] rs=result.split(",");
        showMessage(rs[1], JOptionPane.INFORMATION_MESSAGE,rs[0]);
    }

    private void showMessage(String message, int type,String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title,type);
            }
        });
    }
    public void handleCourseRegistration(ActionEvent actionEvent) {
    }
}
