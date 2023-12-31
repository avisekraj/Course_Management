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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import objects.*;
import objects.dbinterfaces.*;
import users.*;
import users.dbinterfaces.StudentDatabaseOperation;
import users.dbinterfaces.TeacherDatabaseOperation;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentDashboardController implements Initializable {

    @FXML
    private Label studentNameLabel;
    @FXML
    private Label studentIdLabel;
    @FXML
    private AnchorPane studentDashboardHomePane;
    @FXML
    private AnchorPane studentInformationPane;
    @FXML
    private TextField studentInformationEmailField;
    @FXML
    private TextField studentInformationBloodGroupField;
    @FXML
    private TextField studentInformationContactNumberField;
    @FXML
    private TextField studentInformationAddressField;

    /*
    * Following are the data of the student fetched from database
    * */

    private Student student;


    @FXML
    private Label studentInformationUpdationStatusLabel;
    @FXML
    private Label studentInformationNameLabel;
    @FXML
    private Label studentInformationIdLabel;
    @FXML
    private Label studentInformationProgramLabel;

    /*
    * This variables are used throughout
    * */
    private String studentId;
    private int registrationId;





    @FXML
    private AnchorPane studentRegistrationPane;

    // Course picker table
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navigateToStudentDashboard(null); // setting "Dashboard" as default home page

        try{

            // fetching necessary information from database
            fetchStudentInformationFromDatabase();
            fetchCourseInformationFromDatabase();
            fetchRegistrationInformationFromDatabase();
            fetchApplicationInformation();

            // initializing all pages using fetched information
            populateStudentInformationBar();
            populateRegistrationTableView();
            populateRegisteredCoursesTableView();
            populateApplicationTableView();
            //populateResearchTeamInformation();

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    private void fetchApplicationInformation() {
    }

    private void populateApplicationTableView() {
    }

    /*******************************************
     *  Page Navigation related methods
     * ******************************************/
    @FXML
    private void navigateToStudentDashboard(ActionEvent actionEvent) {
        studentInformationPane.setVisible(false);
        studentRegistrationPane.setVisible(false);
        studentDashboardHomePane.setVisible(true);
    }

    @FXML
    private void navigateToStudentInformation(ActionEvent actionEvent) {
        studentDashboardHomePane.setVisible(false);
        studentRegistrationPane.setVisible(false);
        studentInformationPane.setVisible(true);
        studentInformationUpdationStatusLabel.setText(null);
    }

    @FXML
    private void navigateToRegistration(ActionEvent actionEvent) {
        studentDashboardHomePane.setVisible(false);
        studentInformationPane.setVisible(false);
        registrationStatusLabel.setText(null);
        removeCourseStatusLabel.setText(null);
        studentRegistrationPane.setVisible(true);
    }

    @FXML
    private void navigateToStudentTeamPane(ActionEvent actionEvent){

        studentDashboardHomePane.setVisible(false);
        studentInformationPane.setVisible(false);
        studentRegistrationPane.setVisible(false);
    }

    @FXML
    private void navigateToResearchApplicationPane(ActionEvent actionEvent) throws SQLException {
        clearApplicationStats(null);
        studentDashboardHomePane.setVisible(false);
        studentInformationPane.setVisible(false);
        studentRegistrationPane.setVisible(false);
    }

    private void clearApplicationStats(Object o) {
    }

    // Method to get all information about currently logged in Student from database
    private void fetchStudentInformationFromDatabase() throws SQLException {

        // Getting username passed from LoginUI
        studentId = LoginUIController.getUsername();
        System.out.println("User is " + studentId);
        StudentDatabaseOperation studentOp = new StudentDatabaseOperationImplementation();
        student = studentOp.getStudent(studentId);

        studentInformationNameLabel.setText(student.getStudentName());
        studentInformationIdLabel.setText(student.getStudentId());
        studentInformationProgramLabel.setText("BSc in CSE");

        studentInformationEmailField.setText(student.getStudentEmail());
        studentInformationBloodGroupField.setText(student.getStudentBloodGroup());
        studentInformationContactNumberField.setText(student.getStudentContactNumber());
        studentInformationAddressField.setText(student.getStudentAddress());
    }

    @FXML
    private void populateStudentInformationBar(){
        studentNameLabel.setText(student.getStudentName());
        studentIdLabel.setText(student.getStudentId());
    }

    @FXML
    private void handleDashboardSignout(ActionEvent actionEvent) throws IOException {
        /*
        * Signing out will take user to login page
        * */
        Parent LoginUIParent = FXMLLoader.load(getClass().getResource("/gui/LoginUI.fxml"));
        Scene LoginUIScene = new Scene(LoginUIParent);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(LoginUIScene);
        window.show();
    }

    /*******************************************
    *  Student Information Page related methods
    * ******************************************/
    @FXML
    private void handleStudentInformationUpdate(ActionEvent actionEvent) {
        String newEmail = studentInformationEmailField.getText();
        String newBloodGroup = studentInformationBloodGroupField.getText();
        String newContactNumber = studentInformationContactNumberField.getText();
        String newAddress = studentInformationAddressField.getText();

        StudentDatabaseOperation studentOp = new StudentDatabaseOperationImplementation();

        // flag variables to check if update operations were successful
        boolean emailStatus ;
        boolean bloodGroupStatus;
        boolean contactNumberStatus;
        boolean addressStatus;

        emailStatus = studentOp.updateStudentEmail(studentId, newEmail);
        // If the database operation is performed, we update the student object
        if (emailStatus)
            this.student.setStudentEmail(newEmail);

        bloodGroupStatus = studentOp.updateStudentBloodGroup(studentId, newBloodGroup);
        // If the database operation is performed, we update the student object
        if (bloodGroupStatus)
            this.student.setStudentBloodGroup(newBloodGroup);

        contactNumberStatus = studentOp.updateStudentContactNumber(studentId, newContactNumber);
        // If the database operation is performed, we update the student object
        if (contactNumberStatus)
            this.student.setStudentContactNumber(newContactNumber);


        addressStatus = studentOp.updateStudentAddress(studentId, newAddress);
        // If the database operation is performed, we update the student object
        if (addressStatus)
            this.student.setStudentAddress(newAddress);


        // if all status flags are true then we conclude that the update operations are successful
        if (emailStatus && bloodGroupStatus && contactNumberStatus && addressStatus) {
            //System.out.println("Email updated successfully.");
            studentInformationUpdationStatusLabel.setText("Information updated successfully.");
        } else {
            //System.out.println("Updation error!");
            studentInformationUpdationStatusLabel.setText("Error updating information. Please recheck!");
        }
    }


    /*******************************************
     *  Registration Page related methods
     * ******************************************/

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
    private void handleCourseRegistration(ActionEvent actionEvent) throws SQLException {
        removeCourseStatusLabel.setText(null); // clearing other fields

        Course pickedCourse = registrationTableView.getSelectionModel().getSelectedItem();
        if (pickedCourse == null){
            registrationStatusLabel.setText("Select a course first!");
            return;
        }

        //System.out.println("Register for " + pickedCourse.toString());

        Registration registration = new Registration(registrationId, studentId, pickedCourse.getCourseId());
        //System.out.println(registration.toString());
        RegistrationDatabaseOperation regOp = new RegistrationDatabaseOperationImplementation();
        boolean exists = regOp.exists(pickedCourse, student);

        if (exists){
            registrationStatusLabel.setText("Already registered for " + pickedCourse.getCourseCode());
            return;
        }

        boolean regStatus = regOp.insertRegistrationEntry(registration);
        if (regStatus){
            registrationId++; // increment registrationId to be primary key for the next entry
            registeredCourseList.add(pickedCourse);
            //System.out.println(registration.toString() + " is done!");
            registrationStatusLabel.setText("Registration for " + pickedCourse.getCourseCode() + " successful.");
        } else{
            registrationStatusLabel.setText("Failed to add " + pickedCourse.getCourseCode() + "!");
        }
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
            removeCourseStatusLabel.setText("You can not remove CSE1007 after Choosing its compulsory course.");
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

    private void fetchTeacherInformationFromDatabase() throws SQLException {
        TeacherDatabaseOperation teacherOp = new TeacherDatabaseOperationImplementation();
        ObservableList<Teacher> teacherList = teacherOp.getAllTeachers();
    }

}
