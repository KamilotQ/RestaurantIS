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
import sample.Menu.MenuController;

public class AddOrderController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<sample.Menu.Menu> tableField;

    @FXML
    private TableColumn<sample.Menu.Menu, String> idCol;
    @FXML
    private TableColumn<sample.Menu.Menu, String> dishCol;

    @FXML
    private TableColumn<sample.Menu.Menu, String> descriptionCol;

    @FXML
    private TableColumn<sample.Menu.Menu, String> priceCol;

    @FXML
    private TableColumn<sample.Menu.Menu, String> quantityCol;

    @FXML
    private TableColumn<Order, String> editCol;

    String query = null;
    String addOrder = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private boolean update;
    sample.Menu.Menu menu = null;
    int orderId;

    ObservableList<sample.Menu.Menu> MenuList= FXCollections.observableArrayList();


    @FXML
    void getAddView(MouseEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/Menu/addMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch (IOException e) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE,null, e);
        }

    }

    @FXML
    void print(MouseEvent event) {

    }

    @FXML
    private void refreshTable() {

        try {
            MenuList.clear();

            query = "SELECT * FROM restaurant.menu";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                MenuList.add(new sample.Menu.Menu(
                        resultSet.getInt("idmenu"),
                        resultSet.getString("dishname"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("quantity")));

                tableField.setItems(MenuList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }

    private void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("idmenu"));
        dishCol.setCellValueFactory(new PropertyValueFactory<>("dishname"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
                                FontAwesomeIconView addIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);

                                addIcon.setStyle(
                                        " -fx-cursor: hand ;"
                                                + "-glyph-size:28px;"
                                                + "-fx-fill:#00E676;"
                                );

                                addIcon.setOnMouseClicked((MouseEvent event) -> {


                                    try {
                                        menu = tableField.getSelectionModel().getSelectedItem();
                                        addOrder = "INSERT INTO `order`( `dishname`,`price`) VALUES (?,?)";
                                        connection = DbConnect.getConnect();
                                        preparedStatement = connection.prepareStatement(addOrder);
                                        preparedStatement.setString(1, menu.getDishname());
                                        preparedStatement.setString(2,String.valueOf(menu.getPrice()));
                                        preparedStatement.execute();

                                        refreshTable();

                                    } catch (SQLException ex) {
                                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });

                                HBox managebtn = new HBox(addIcon);
                                managebtn.setStyle("-fx-alignment:center");
                                HBox.setMargin(addIcon, new Insets(2, 3, 0, 2));

                                setGraphic(managebtn);

                                setText(null);
                            }
                        }
                    };
                    return cell;
                };
        editCol.setCellFactory(cellFoctory);
        tableField.setItems(MenuList);
    }

    private void getQuery() {
        if (!update) {

            query = "INSERT INTO `order`( `dishname`,`price`) VALUES (?,?)";

        } else {
            query = "UPDATE `order` SET "
                    + "`dishname`=?,"
                    + "`price`= ? WHERE idorder = '"+orderId+"'";
        }
    }
}

