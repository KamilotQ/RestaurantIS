package sample.Employee;

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


public class EmployeeController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private TableView<sample.Employee.Employee> tableField;

    @FXML
    private TableColumn<sample.Employee.Employee, String> idCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> firstCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> lastCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> vacancyCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> genderCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> ageCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> phoneCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> salaryCol;

    @FXML
    private TableColumn<sample.Employee.Employee, String> editCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    sample.Employee.Employee employee = null;

    ObservableList<sample.Employee.Employee> EmployeeList= FXCollections.observableArrayList();

    @FXML
    void getAddView(MouseEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/Employee/addEmployee.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch (IOException e) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE,null, e);
        }

    }

    @FXML
    void print(MouseEvent event) {

    }

    @FXML
    private void refreshTable() {

        try {
            EmployeeList.clear();

            query = "SELECT * FROM restaurant.employee";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                EmployeeList.add(new sample.Employee.Employee(
                        resultSet.getInt("idemployee"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("vacancy"),
                        resultSet.getString("gender"),
                        resultSet.getInt("age"),
                        resultSet.getInt("salary"),
                        resultSet.getInt("phone")));

                tableField.setItems(EmployeeList);
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

        idCol.setCellValueFactory(new PropertyValueFactory<>("idemployee"));
        firstCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        vacancyCol.setCellValueFactory(new PropertyValueFactory<>("vacancy"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));


        Callback<TableColumn<sample.Employee.Employee, String>, TableCell<sample.Employee.Employee, String>> cellFoctory =
                (TableColumn<sample.Employee.Employee, String> param) -> {

            final TableCell<sample.Employee.Employee, String> cell = new TableCell<Employee, String>() {
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
                                employee = tableField.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `restaurant`.`employee` WHERE idemployee  ="+employee.getIdemployee();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();

                            } catch (SQLException ex) {
                                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                            }





                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            employee = tableField.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/sample/Employee/addEmployee.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            AddEmployeeController addEmployeeController = loader.getController();
                            addEmployeeController.setUpdate(true);
                            addEmployeeController.setTextField(
                                    employee.getIdemployee(),
                                    employee.getFirstname(),
                                    employee.getLastname(),
                                    employee.getVacancy(),
                                    employee.getGender(),
                                    employee.getAge(),
                                    employee.getSalary(),
                                    employee.getPhone());
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
        tableField.setItems(EmployeeList);
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
