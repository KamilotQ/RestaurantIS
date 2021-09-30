package sample.Reservation;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.DbConnect;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;



public class AddReservationController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private TextField tableNumField;

    @FXML
    private TextField reservatorField;

    @FXML
    private DatePicker dateField;



    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Reservation reservation = null;
    private boolean update;
    int reservId;

    @FXML
    private void cleanButton(MouseEvent event) {
        tableNumField.setText(null);
        reservatorField.setText(null);
        dateField.setValue(null);


    }

    @FXML
    private void saveButton(MouseEvent event) {

        connection = DbConnect.getConnect();
        String table_number = tableNumField.getText();
        String reservator_name = reservatorField.getText();
        String time = String.valueOf(dateField.getValue());


        if (table_number.isEmpty() || reservator_name.isEmpty() || time.equals("") ) {
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
            preparedStatement.setString(1,tableNumField.getText());
            preparedStatement.setString(2,reservatorField.getText());
            preparedStatement.setDate(3, Date.valueOf(dateField.getValue()));

            preparedStatement.execute();
        }catch (SQLException e) {
            Logger.getLogger(AddReservationController.class.getName()).log(Level.SEVERE,null, e);
        }
    }

    private void getQuery() {
        if (!update) {

            query = "INSERT INTO `table_reservation` ( `table_number`, `reservator_name`, `time`) VALUES (?,?,?)";

        } else {
            query = "UPDATE `table_reservation` SET "
                    + "`table_number`=?,"
                    + "`reservator_name`=?,"
                    + "`time`= ? WHERE idtable_reservation = '"+reservId+"'";
        }
    }

    @Override
    public void initialize(URL url,ResourceBundle rx) {

    }

    void setTextField(int idtable_reservation, int table_number, String reservator_name, LocalDate time) {

        reservId = idtable_reservation;
        tableNumField.setText(String.valueOf(table_number));
        reservatorField.setText(reservator_name);
        dateField.setValue(time);



    }

    void setUpdate(boolean b) {
        this.update = b;

    }
}
