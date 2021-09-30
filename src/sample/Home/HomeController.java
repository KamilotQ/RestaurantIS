package sample.Home;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button menuButton;

    @FXML
    private Button employeeButton;

    @FXML
    private Button orderButton;

    @FXML
    private Button reservationButton;

    @FXML
    private Button signUpAdminButton;

    @FXML
    void initialize() {
        signUpAdminButton.setOnAction(event1 -> {
            openNewScene("/sample/SignUp/SignUp.fxml");

        });
        menuButton.setOnAction(event1 -> {
            openNewScene("/sample/Menu/menu.fxml");

        });
        orderButton.setOnAction(event1 -> {
            openNewScene("/sample/Order/order.fxml");

        });
        reservationButton.setOnAction(event1 -> {
            openNewScene("/sample/Reservation/reservation.fxml");

        });
        employeeButton.setOnAction(event1 -> {
            openNewScene("/sample/Employee/employee.fxml");

        });


    }

    public void openNewScene(String window) {
        signUpAdminButton.getScene().getWindow().hide();

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
//        stage.showAndWait();
        stage.show();
    }
}
