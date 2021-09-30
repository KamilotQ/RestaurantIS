package sample.Menu;

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

public class MenuController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

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
    private TableColumn<sample.Menu.Menu, String> editCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    sample.Menu.Menu menu = null;

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

        backButton.setOnAction(event -> {
            openNewScene("/sample/Home/app.fxml/");
        });
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


        Callback<TableColumn<sample.Menu.Menu, String>, TableCell<sample.Menu.Menu, String>> cellFoctory =
                (TableColumn<sample.Menu.Menu, String> param) -> {

            final TableCell<sample.Menu.Menu, String> cell = new TableCell<Menu, String>() {
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
                                menu = tableField.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `restaurant`.`menu` WHERE idmenu  ="+menu.getIdmenu();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();

                            } catch (SQLException ex) {
                                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                            }





                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            menu = tableField.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/sample/Menu/addMenu.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            AddMenuController addMenuController = loader.getController();
                            addMenuController.setUpdate(true);
                            addMenuController.setTextField(
                                    menu.getIdmenu(),
                                    menu.getDishname(),
                                    menu.getDescription(),
                                    menu.getPrice().toString(),
                                    menu.getQuantity());
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
        tableField.setItems(MenuList);
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

