package sample.Reservation;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.DbConnect;


public class ReservationController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private TableView<sample.Reservation.Reservation> tableField;

    @FXML
    private TableColumn<sample.Reservation.Reservation, String> idCol;

    @FXML
    private TableColumn<sample.Reservation.Reservation, String> tableCol;

    @FXML
    private TableColumn<sample.Reservation.Reservation, String> reservationCol;

    @FXML
    private TableColumn<sample.Reservation.Reservation, String> TimeCol;

    @FXML
    private TableColumn<sample.Reservation.Reservation, String> editCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    sample.Reservation.Reservation reservation = null;


    ObservableList<sample.Reservation.Reservation> ReservationList= FXCollections.observableArrayList();

    @FXML
    void getAddView(MouseEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/Reservation/addReservation.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch (IOException e) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE,null, e);
        }

    }

    @FXML
    void print(MouseEvent event) {

    }

    @FXML
    private void refreshTable() {

        try {
            ReservationList.clear();

            query = "SELECT * FROM restaurant.table_reservation";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                ReservationList.add(new sample.Reservation.Reservation(
                        resultSet.getInt("idtable_reservation"),
                        resultSet.getInt("table_number"),
                        resultSet.getString("reservator_name"),
                        resultSet.getDate("time")));

                tableField.setItems(ReservationList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        backButton.setOnAction(event -> {
            openNewScene("/sample/Home/app.fxml/");
        });
        loadData();
    }

    private void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("idtable_reservation"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("table_number"));
        reservationCol.setCellValueFactory(new PropertyValueFactory<>("reservator_name"));
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));


        Callback<TableColumn<sample.Reservation.Reservation, String>, TableCell<sample.Reservation.Reservation, String>> cellFoctory =
                (TableColumn<sample.Reservation.Reservation, String> param) -> {

                    final TableCell<sample.Reservation.Reservation, String> cell = new TableCell<Reservation, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty) {
                                setGraphic(null);
                                setText(null);

                            } else {

                                FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                                FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                                deleteIcon.setStyle(
                                        " -fx-cursor: hand ;"
                                                + "-glyph-size:28px;"
                                                + "-fx-fill:#ff1744;"
                                );
                                editIcon.setStyle(
                                        " -fx-cursor: hand ;"
                                                + "-glyph-size:28px;"
                                                + "-fx-fill:#00E676;"
                                );
                                deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                                    try {
                                        reservation = tableField.getSelectionModel().getSelectedItem();
                                        query = "DELETE FROM `restaurant`.`table_reservation` WHERE idtable_reservation  ="+reservation.getIdtable_reservation();
                                        connection = DbConnect.getConnect();
                                        preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.execute();
                                        refreshTable();

                                    } catch (SQLException ex) {
                                        Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
                                    }





                                });
                                editIcon.setOnMouseClicked((MouseEvent event) -> {

                                    reservation = tableField.getSelectionModel().getSelectedItem();
                                    FXMLLoader loader = new FXMLLoader ();
                                    loader.setLocation(getClass().getResource("/sample/Reservation/addReservation.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    AddReservationController addReservationController = loader.getController();
                                    addReservationController.setUpdate(true);
                                    addReservationController.setTextField(
                                            reservation.getIdtable_reservation(),
                                            reservation.getTable_number(),
                                            reservation.getReservator_name(),
                                            reservation.time.toLocalDate());

                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();




                                });

                                HBox managebtn = new HBox(editIcon, deleteIcon);
                                managebtn.setStyle("-fx-alignment:center");
                                HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                                HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                                setGraphic(managebtn);

                                setText(null);

                            }
                        }

                    };

                    return cell;
                };
        editCol.setCellFactory(cellFoctory);
        tableField.setItems(ReservationList);
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
    }
}
