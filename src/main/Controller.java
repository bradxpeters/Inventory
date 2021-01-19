package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import parts.*;
import products.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button addPartButton;

    @FXML
    private Button deletePartButton;

    @FXML
    private Button modifyPartButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    TableView<Part> partsTable;

    @FXML
    private TableColumn<Part,Integer> partId;

    @FXML
    private TableColumn<Part,String> partName;

    @FXML
    private TableColumn<Part,Integer> inventoryLevel;

    @FXML
    private TableColumn<Part,Double> costPerUnit;

    @FXML
    void handleAddPartButton() throws IOException {
        var fxmlLoader = new FXMLLoader(Part.class.getResource("addPartForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var stage = new Stage();
        stage.setScene(new Scene(root, 800, 900));
        stage.show();
    }

    @FXML
    void handleDeletePartButton() {
       Inventory.getInstance().removeFromList(partsTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void handleModifyPartButton() throws IOException {
        var fxmlLoader = new FXMLLoader(Part.class.getResource("addPartForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var controller = (PartsController) fxmlLoader.getController();
        var stage = new Stage();

        controller.setExistingPart(partsTable.getSelectionModel().getSelectedItem());
        stage.setScene(new Scene(root, 800, 900));
        stage.show();
    }

    @FXML
    void handleAddProductButton() throws IOException {
        var fxmlLoader = new FXMLLoader(Product.class.getResource("addProductForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var stage = new Stage();
        stage.setScene(new Scene(root, 1100, 700));
        stage.show();
    }

    @FXML
    void handleModifyProductButton() throws IOException {
        var fxmlLoader = new FXMLLoader(Product.class.getResource("modifyProductForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var stage = new Stage();
        stage.setScene(new Scene(root, 1100, 700));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize cell factories
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        costPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Link table to observable list singleton
        partsTable.setItems(Inventory.getInstance().getAllParts());

        // Create some starter parts
        var inHousePartNames = Arrays.asList("brake pad", "rim");
        var outsourcePartNames = Arrays.asList("tire", "door", "bolt");
        inHousePartNames.forEach(
            name -> {
                Random rand = new Random();
                var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

                var part = new InHouse(
                    Inventory.getInstance().getCurrentId(),
                    name,
                    price,
                    1 + rand.nextInt((50 - 1) + 1),
                    1 + rand.nextInt((50 - 1) + 1),
                    1 + rand.nextInt((50 - 1) + 1),
                    1 + rand.nextInt((50 - 1) + 1)
                );

                Inventory.getInstance().addToList(part);
            }
        );

        outsourcePartNames.forEach(
                name -> {
                    Random rand = new Random();
                    var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                    var part = new Outsourced(
                        Inventory.getInstance().getCurrentId(),
                        name,
                        price,
                        1 + rand.nextInt((50 - 1) + 1),
                        1 + rand.nextInt((50 - 1) + 1),
                        1 + rand.nextInt((50 - 1) + 1),
                        "Auto Parts Palace"
                    );

                    Inventory.getInstance().addToList(part);
                }
        );
    }
}
