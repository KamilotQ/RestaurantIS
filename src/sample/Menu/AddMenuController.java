package sample.Menu;

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

public class AddMenuController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField dishField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Menu menu = null;
    private boolean update;
    int menuId;

    @FXML
    private void cleanButton(MouseEvent event) {
        dishField.setText(null);
        descriptionField.setText(null);
        priceField.setText(null);
        quantityField.setText(null);

    }

    @FXML
    private void saveButton(MouseEvent event) {

        connection = DbConnect.getConnect();
        String dishname = dishField.getText();
        String description = descriptionField.getText();
        String price = priceField.getText();
        String quantity = quantityField.getText();

        if (dishname.isEmpty() || description.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
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
            preparedStatement.setString(1,dishField.getText());
            preparedStatement.setString(2,descriptionField.getText());
            preparedStatement.setString(3,priceField.getText());
            preparedStatement.setString(4,quantityField.getText());
            preparedStatement.execute();
        }catch (SQLException e) {
            Logger.getLogger(AddMenuController.class.getName()).log(Level.SEVERE,null, e);
        }
    }

    private void getQuery() {
        if (!update) {

            query = "INSERT INTO `menu`( `dishname`, `description`, `price`, `quantity`) VALUES (?,?,?,?)";

        } else {
            query = "UPDATE `menu` SET "
                    + "`dishname`=?,"
                    + "`description`=?,"
                    + "`price`=?,"
                    + "`quantity`= ? WHERE idmenu = '"+menuId+"'";
        }
    }

    @Override
    public void initialize(URL url,ResourceBundle rx) {

    }

    void setTextField(int idmenu, String dishname, String description, String price, String quantity) {

        menuId = idmenu;
        dishField.setText(dishname);
        descriptionField.setText(description);
        priceField.setText(price);
        quantityField.setText(quantity);

    }

    void setUpdate(boolean b) {
        this.update = b;

    }
}
