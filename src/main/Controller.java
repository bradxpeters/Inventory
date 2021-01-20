package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import parts.InHouse;
import parts.Outsourced;
import parts.Part;
import parts.PartsController;
import products.Product;
import products.ProductsController;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private Button exitButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    private TextField searchPartField;

    @FXML
    TableView<Part> partsTable;

    @FXML
    TableView<Product> productsTable;

    @FXML
    private TableColumn<Part,Integer> partId;

    @FXML
    private TableColumn<Part,String> partName;

    @FXML
    private TableColumn<Part,Integer> partStock;

    @FXML
    private TableColumn<Part,Double> partPrice;

    @FXML
    private TableColumn<Product,Integer> productId;

    @FXML
    private TableColumn<Product,String> productName;

    @FXML
    private TableColumn<Product,Integer> productStock;

    @FXML
    private TableColumn<Product,Double> productPrice;

    @FXML
    void handleExitButton() {
        var stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

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
       Inventory.getInstance().deletePart(partsTable.getSelectionModel().getSelectedItem());
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
        var fxmlLoader = new FXMLLoader(Product.class.getResource("addProductForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var controller = (ProductsController) fxmlLoader.getController();
        var stage = new Stage();

        controller.setExistingProduct(productsTable.getSelectionModel().getSelectedItem());
        stage.setScene(new Scene(root, 1100, 700));
        stage.show();
    }

    public void handlePartSelectionChange(Part newValue) {
        if (newValue != null) {
            modifyPartButton.setDisable(false);
        }
    }

    public void handleProductSelectionChange(Product newValue) {
        if (newValue != null) {
            modifyProductButton.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize cell factories
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Link table to observable list singleton
        partsTable.setItems(Inventory.getInstance().getAllParts());
        productsTable.setItems(Inventory.getInstance().getAllProducts());

        // Listen to search field
        searchPartField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            int partId = -1;
            try {
                partId = Integer.parseInt(newValue);
            } catch (Exception ignored) {} // Not an parsable integer

            if (partId != -1) {
                var tempList = FXCollections.observableList(new ArrayList<Part>());
                tempList.add(Inventory.getInstance().lookupPart(partId));
                partsTable.setItems(tempList);
            } else {
                partsTable.setItems(Inventory.getInstance().lookupPart(newValue));
            }
        });

        // Modify buttons should be disabled until a selection is made
        modifyPartButton.setDisable(true);
        modifyProductButton.setDisable(true);
        partsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            handlePartSelectionChange(newValue);
        });
        productsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            handleProductSelectionChange(newValue);
        });

        // Create some starter parts
        var inHousePartNames = Arrays.asList("brake pad", "rim");
        var outsourcePartNames = Arrays.asList("tire", "door", "bolt");
        var productNames = Arrays.asList("Gian Bike", "Trek Bike", "Specialized Bike", "Scott Bike");

        inHousePartNames.forEach(
            name -> {
                Random rand = new Random();
                var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

                var part = new InHouse(
                    Inventory.getInstance().getCurrentPartId(),
                    name,
                    price,
                    1 + rand.nextInt((50 - 1) + 1),
                    1 + rand.nextInt((50 - 1) + 1),
                    1 + rand.nextInt((50 - 1) + 1),
                    1 + rand.nextInt((50 - 1) + 1)
                );

                Inventory.getInstance().addPart(part);
            }
        );

        outsourcePartNames.forEach(
                name -> {
                    Random rand = new Random();
                    var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                    var part = new Outsourced(
                        Inventory.getInstance().getCurrentPartId(),
                        name,
                        price,
                        1 + rand.nextInt((50 - 1) + 1),
                        1 + rand.nextInt((50 - 1) + 1),
                        1 + rand.nextInt((50 - 1) + 1),
                        "Auto Parts Palace"
                    );

                    Inventory.getInstance().addPart(part);
                }
        );

        productNames.forEach(
                name -> {
                    Random rand = new Random();
                    var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                    var product = new Product(
                            null,
                            Inventory.getInstance().getCurrentProductId(),
                            name,
                            price,
                            1 + rand.nextInt((50 - 1) + 1),
                            1 + rand.nextInt((50 - 1) + 1),
                            1 + rand.nextInt((50 - 1) + 1)
                    );

                    Inventory.getInstance().addProduct(product);
                }
        );
    }
}
