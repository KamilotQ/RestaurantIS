package sample.Employee;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.DbConnect;



public class AddEmployeeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField firstField;

    @FXML
    private TextField lastField;

    @FXML
    private TextField vacancyField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField phoneField;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Employee employee = null;
    private boolean update;
    int employeeId;

    @FXML
    private void cleanButton(MouseEvent event) {
        firstField.setText(null);
        lastField.setText(null);
        vacancyField.setText(null);
        genderField.setText(null);
        ageField.setText(null);
        salaryField.setText(null);
        phoneField.setText(null);

    }

    @FXML
    private void saveButton(MouseEvent event) {

        connection = DbConnect.getConnect();
        String firstname = firstField.getText();
        String lastname = lastField.getText();
        String vacancy = vacancyField.getText();
        String gender = genderField.getText();
        String age = ageField.getText();
        String salary = salaryField.getText();
        String phone = phoneField.getText();

        if (firstname.isEmpty() || lastname.isEmpty() || vacancy.isEmpty() || gender.isEmpty() ||
                age.isEmpty() || salary.isEmpty() || phone.isEmpty() ) {
            Alert alert = new Alert( Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill All Fields");
            alert.showAndWait();
        }else {
            getQuery();
            insert();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();

        }

    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,firstField.getText());
            preparedStatement.setString(2,lastField.getText());
            preparedStatement.setString(3,vacancyField.getText());
            preparedStatement.setString(4,genderField.getText());
            preparedStatement.setString(5,ageField.getText());
            preparedStatement.setString(6,salaryField.getText());
            preparedStatement.setString(7,phoneField.getText());
            preparedStatement.execute();
        }catch (SQLException e) {
            Logger.getLogger(AddEmployeeController.class.getName()).log(Level.SEVERE,null, e);
        }
    }

    private void getQuery() {
        if (!update) {

            query = "INSERT INTO `employee`( `firstname`, `lastname`, `vacancy`, `gender`, `age`, `salary`, `phone`) VALUES (?,?,?,?,?,?,?)";

        } else {
            query = "UPDATE `employee` SET "
                    + "`firstname`=?,"
                    + "`lastname`=?,"
                    + "`vacancy`=?,"
                    + "`gender`=?,"
                    + "`age`=?,"
                    + "`salary`=?,"
                    + "`phone`= ? WHERE idemployee = '"+employeeId+"'";
        }
    }

    @Override
    public void initialize(URL url,ResourceBundle rx) {

    }

    void setTextField(int idemployee, String firstname,String lastname,String vacancy,String gender,
                      int age,int salary,int phone) {

        employeeId = idemployee;
        firstField.setText(firstname);
        lastField.setText(lastname);
        vacancyField.setText(vacancy);
        genderField.setText(gender);
        ageField.setText(String.valueOf(age));
        salaryField.setText(String.valueOf(salary));
        phoneField.setText(String.valueOf(phone));


    }

    void setUpdate(boolean b) {
        this.update = b;

    }
}
