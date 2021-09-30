package sample.SignUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLogin;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpPhone;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private Button registrationButton;

    @FXML
    private CheckBox signUpMale;

    @FXML
    private CheckBox signUpFemale;

    @FXML
    private Label userShowLabel;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {

        registrationButton.setOnAction(event -> {
            SignUpNewUser();

        });
        backButton.setOnAction(event -> {
            openNewScene("/sample/SignUp/sample.fxml");
        });


    }

    private void SignUpNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        String firstName = signUpFirstName.getText();
        String lastName = signUpLastName.getText();
        String userName = signUpLogin.getText();
        String password = signUpPassword.getText();
        String phone = signUpPhone.getText();
        String gender = "";
        if(signUpMale.isSelected()){
            gender = "Male";
        }
        else
            gender = "Female";

        User user = new User(firstName,lastName,userName, password,phone, gender);

        dbHandler.signUpNewUser(user);

        System.out.println("New User");
    }

    public void openNewScene(String window) {
        backButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
//        stage.show();
    }

}
