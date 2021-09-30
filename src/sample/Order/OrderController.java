package sample.Order;

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

public class OrderController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<sample.Order.Order> tableField;

    @FXML
    private TableColumn<sample.Order.Order, String> idCol;

    @FXML
    private TableColumn<sample.Order.Order, String> dishCol;

    @FXML
    private TableColumn<sample.Order.Order, String> priceCol;

    @FXML
    private TableColumn<sample.Order.Order, String> editCol;

    @FXML
    private Button backButton;



    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    sample.Order.Order order = null;

    ObservableList<sample.Order.Order> OrderList= FXCollections.observableArrayList();


    @FXML
    void getAddView(MouseEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/Order/addOrder.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch (IOException e) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE,null, e);
        }

    }

    @FXML
    void print(MouseEvent event) {

    }

    @FXML
    private void refreshTable() {

        try {
            OrderList.clear();

            query = "SELECT * FROM restaurant.order";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                OrderList.add(new sample.Order.Order(
                        resultSet.getInt("idorder"),
                        resultSet.getString("dishname"),
                        resultSet.getDouble("price")));

                tableField.setItems(OrderList);
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

        idCol.setCellValueFactory(new PropertyValueFactory<>("idorder"));
        dishCol.setCellValueFactory(new PropertyValueFactory<>("dishname"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        Callback<TableColumn<sample.Order.Order, String>, TableCell<sample.Order.Order, String>> cellFoctory =
                (TableColumn<sample.Order.Order, String> param) -> {

                    final TableCell<sample.Order.Order, String> cell = new TableCell<Order, String>() {
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
                                deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                                    try {
                                        order = tableField.getSelectionModel().getSelectedItem();
                                        query = "DELETE FROM `restaurant`.`order` WHERE idorder  ="+order.getIdorder();
                                        connection = DbConnect.getConnect();
                                        preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.execute();
                                        refreshTable();

                                    } catch (SQLException ex) {
                                        Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                                    }


                                });

                                HBox managebtn = new HBox(deleteIcon);
                                managebtn.setStyle("-fx-alignment:center");
                                HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));

                                setGraphic(managebtn);

                                setText(null);

                            }
                        }

                    };

                    return cell;
                };
        editCol.setCellFactory(cellFoctory);
        tableField.setItems(OrderList);
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

