package main;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import parts.InHouse;
import parts.Outsourced;
import parts.Part;
import parts.PartsController;
import products.Product;
import products.ProductsController;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * The Main Controller.
 */
public class Controller implements Initializable {

    @FXML
    private Button addPartButton;

    @FXML
    private Button modifyPartButton;

    @FXML
    private Button deletePartButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField searchPartField;

    @FXML
    private TextField searchProductField;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableView<Product> productsTable;

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
    private Hyperlink documentationLink;

    /**
     * Handle exit button.
     */
    @FXML
    private void handleExitButton() {
        var stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handle clicking documentation link.
     */
    @FXML
    private void handleClickDocumentationLink() throws IOException {
        Desktop.getDesktop().browse(Paths.get("javadocs/index.html").toUri());
    }

    /**
     * Handle add part button.
     *
     * @throws IOException the io exception
     */
    @FXML
    private void handleAddPartButton() throws IOException {
        var fxmlLoader = new FXMLLoader(Part.class.getResource("addPartForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var stage = new Stage();
        stage.setScene(new Scene(root, 800, 900));
        stage.show();
    }

    /**
     * Handle delete part button.
     */
    @FXML
    private void handleDeletePartButton() {
        Inventory.deletePart(partsTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Handle delete product button.
     */
    @FXML
    private void handleDeleteProductButton() {
        Inventory.deleteProduct(productsTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Handle modify part button.
     *
     * @throws IOException the io exception
     */
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

    /**
     * Handle add product button.
     *
     * @throws IOException the io exception
     */
    @FXML
    void handleAddProductButton() throws IOException {
        var fxmlLoader = new FXMLLoader(Product.class.getResource("addProductForm.fxml"));
        var root = (Parent) fxmlLoader.load();
        var stage = new Stage();
        stage.setScene(new Scene(root, 1100, 700));
        stage.show();
    }

    /**
     * Handle modify product button.
     *
     * @throws IOException the io exception
     */
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

    /**
     * Handle part selection change.
     *
     * @param newValue the new value
     */
    public void handlePartSelectionChange(Part newValue) {
        if (newValue != null) {
            this.getModifyPartButton().setDisable(false);
        }
    }

    /**
     * Handle product selection change.
     *
     * @param newValue the new value
     */
    public void handleProductSelectionChange(Product newValue) {
        if (newValue != null) {
            this.getModifyProductButton().setDisable(false);
        }
    }

    /**
     * Handle the initialization of the main stage
     *
     * @param url the url
     * @param resourceBundle the ResourceBundle
     *
     *
     * I ran into several issues during development that lead me to implementing the Initializable pattern.
     * This allowed me to populate some initial parts and products and speed up the development feedback loop.
     * I also leveraged this method to apply listeners to simplify things like search.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize cell factories
        this.getPartId().setCellValueFactory(new PropertyValueFactory<>("id"));
        this.getPartName().setCellValueFactory(new PropertyValueFactory<>("name"));
        this.getPartStock().setCellValueFactory(new PropertyValueFactory<>("stock"));
        this.getPartPrice().setCellValueFactory(new PropertyValueFactory<>("price"));

        this.getProductId().setCellValueFactory(new PropertyValueFactory<>("id"));
        this.getProductName().setCellValueFactory(new PropertyValueFactory<>("name"));
        this.getProductStock().setCellValueFactory(new PropertyValueFactory<>("stock"));
        this.getProductPrice().setCellValueFactory(new PropertyValueFactory<>("price"));

        // Link table to observable list singleton
        this.getPartsTable().setItems(Inventory.getAllParts());
        this.getProductsTable().setItems(Inventory.getAllProducts());

        // Listen to search fields
        this.getSearchPartField().textProperty().addListener((observableValue, oldValue, newValue) -> {
            int partId = -1;
            try {
                partId = Integer.parseInt(newValue);
            } catch (Exception ignored) {} // Not an parsable integer

            if (partId != -1) {
                var tempList = FXCollections.observableList(new ArrayList<Part>());
                tempList.add(Inventory.lookupPart(partId));
                this.getPartsTable().setItems(tempList);
            } else {
                this.getPartsTable().setItems(Inventory.lookupPart(newValue));
            }

            // Ensure that if an exact match is found that it is selected for modification
            if (this.getPartsTable().getItems().size() == 1) {
                this.getPartsTable().getSelectionModel().select(0);
                this.getPartsTable().getFocusModel().focus(0);
            }

            if (this.getPartsTable().getItems().size() == 0) {
                var alert = new Alert(Alert.AlertType.ERROR, "No result found!");
                alert.setHeaderText("Search error");
                alert.showAndWait();
            }
        });

        this.getSearchProductField().textProperty().addListener((observableValue, oldValue, newValue) -> {
            int productId = -1;
            try {
                productId = Integer.parseInt(newValue);
            } catch (Exception ignored) {} // Not an parsable integer

            if (productId != -1) {
                var tempList = FXCollections.observableList(new ArrayList<Product>());
                tempList.add(Inventory.lookupProduct(productId));
                this.getProductsTable().setItems(tempList);
            } else {
                this.getProductsTable().setItems(Inventory.lookupProduct(newValue));
            }

            if (this.getProductsTable().getItems().size() == 0) {
                var alert = new Alert(Alert.AlertType.ERROR, "No result found!");
                alert.setHeaderText("Search error");
                alert.showAndWait();
            }
        });


        // Modify buttons should be disabled until a selection is made
        this.getModifyPartButton().setDisable(true);
        this.getModifyProductButton().setDisable(true);
        this.getPartsTable()
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((observableValue, oldValue, newValue) -> handlePartSelectionChange(newValue));
        this.getProductsTable()
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((observableValue, oldValue, newValue) -> handleProductSelectionChange(newValue));

        // Create some starter parts and products
        var inHousePartNames = Arrays.asList("brake pad", "rim");
        var outsourcePartNames = Arrays.asList("tire", "door", "bolt");
        var productNames = Arrays.asList("Giant Bike", "Trek Bike", "Specialized Bike", "Scott Bike");

        inHousePartNames.forEach(
            name -> {
                Random rand = new Random();
                var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

                var part = new InHouse(
                    Inventory.currentPartId,
                    name,
                    price,
                        30 + rand.nextInt((30 - 20) + 1),
                    28 + rand.nextInt((29 - 20) + 1),
                    50 + rand.nextInt((50 - 35) + 1),
                    50 + rand.nextInt((50 - 1) + 1)
                );

                Inventory.addPart(part);
            }
        );

        outsourcePartNames.forEach(
                name -> {
                    Random rand = new Random();
                    var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                    var part = new Outsourced(
                        Inventory.currentPartId,
                        name,
                        price,
                        30 + rand.nextInt((30 - 20) + 1),
                        28 + rand.nextInt((29 - 20) + 1),
                        50 + rand.nextInt((50 - 35) + 1),
                        "Auto Parts Palace"
                    );

                    Inventory.addPart(part);
                }
        );

        productNames.forEach(
                name -> {
                    Random rand = new Random();
                    var price = BigDecimal.valueOf(1.0 + rand.nextDouble() * 1 + rand.nextInt((50 - 1) + 1))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                    var product = new Product(
                            FXCollections.observableList(new ArrayList<>()),
                            Inventory.currentProductId,
                            name,
                            price,
                            30 + rand.nextInt((30 - 20) + 1),
                            28 + rand.nextInt((29 - 20) + 1),
                            50 + rand.nextInt((50 - 35) + 1)
                    );

                    Inventory.addProduct(product);
                }
        );
    }

    /**
     * Gets add part button.
     *
     * @return the add part button
     */
    public Button getAddPartButton() {
        return addPartButton;
    }

    /**
     * Sets add part button.
     *
     * @param addPartButton the add part button
     */
    public void setAddPartButton(Button addPartButton) {
        this.addPartButton = addPartButton;
    }

    /**
     * Gets modify part button.
     *
     * @return the modify part button
     */
    public Button getModifyPartButton() {
        return modifyPartButton;
    }

    /**
     * Sets modify part button.
     *
     * @param modifyPartButton the modify part button
     */
    public void setModifyPartButton(Button modifyPartButton) {
        this.modifyPartButton = modifyPartButton;
    }

    /**
     * Gets delete part button.
     *
     * @return the delete part button
     */
    public Button getDeletePartButton() {
        return deletePartButton;
    }

    /**
     * Sets delete part button.
     *
     * @param deletePartButton the delete part button
     */
    public void setDeletePartButton(Button deletePartButton) {
        this.deletePartButton = deletePartButton;
    }

    /**
     * Gets add product button.
     *
     * @return the add product button
     */
    public Button getAddProductButton() {
        return addProductButton;
    }

    /**
     * Sets add product button.
     *
     * @param addProductButton the add product button
     */
    public void setAddProductButton(Button addProductButton) {
        this.addProductButton = addProductButton;
    }

    /**
     * Gets modify product button.
     *
     * @return the modify product button
     */
    public Button getModifyProductButton() {
        return modifyProductButton;
    }

    /**
     * Sets modify product button.
     *
     * @param modifyProductButton the modify product button
     */
    public void setModifyProductButton(Button modifyProductButton) {
        this.modifyProductButton = modifyProductButton;
    }

    /**
     * Gets delete product button.
     *
     * @return the delete product button
     */
    public Button getDeleteProductButton() {
        return deleteProductButton;
    }

    /**
     * Sets delete product button.
     *
     * @param deleteProductButton the delete product button
     */
    public void setDeleteProductButton(Button deleteProductButton) {
        this.deleteProductButton = deleteProductButton;
    }

    /**
     * Gets exit button.
     *
     * @return the exit button
     */
    public Button getExitButton() {
        return exitButton;
    }

    /**
     * Sets exit button.
     *
     * @param exitButton the exit button
     */
    public void setExitButton(Button exitButton) {
        this.exitButton = exitButton;
    }

    /**
     * Gets search part field.
     *
     * @return the search part field
     */
    public TextField getSearchPartField() {
        return searchPartField;
    }

    /**
     * Sets search part field.
     *
     * @param searchPartField the search part field
     */
    public void setSearchPartField(TextField searchPartField) {
        this.searchPartField = searchPartField;
    }

    /**
     * Gets search product field.
     *
     * @return the search product field
     */
    public TextField getSearchProductField() {
        return searchProductField;
    }

    /**
     * Sets search product field.
     *
     * @param searchProductField the search product field
     */
    public void setSearchProductField(TextField searchProductField) {
        this.searchProductField = searchProductField;
    }

    /**
     * Gets parts table.
     *
     * @return the parts table
     */
    public TableView<Part> getPartsTable() {
        return partsTable;
    }

    /**
     * Sets parts table.
     *
     * @param partsTable the parts table
     */
    public void setPartsTable(TableView<Part> partsTable) {
        this.partsTable = partsTable;
    }

    /**
     * Gets products table.
     *
     * @return the products table
     */
    public TableView<Product> getProductsTable() {
        return productsTable;
    }

    /**
     * Sets products table.
     *
     * @param productsTable the products table
     */
    public void setProductsTable(TableView<Product> productsTable) {
        this.productsTable = productsTable;
    }

    /**
     * Gets part id.
     *
     * @return the part id
     */
    public TableColumn<Part, Integer> getPartId() {
        return partId;
    }

    /**
     * Sets part id.
     *
     * @param partId the part id
     */
    public void setPartId(TableColumn<Part, Integer> partId) {
        this.partId = partId;
    }

    /**
     * Gets part name.
     *
     * @return the part name
     */
    public TableColumn<Part, String> getPartName() {
        return partName;
    }

    /**
     * Sets part name.
     *
     * @param partName the part name
     */
    public void setPartName(TableColumn<Part, String> partName) {
        this.partName = partName;
    }

    /**
     * Gets part stock.
     *
     * @return the part stock
     */
    public TableColumn<Part, Integer> getPartStock() {
        return partStock;
    }

    /**
     * Sets part stock.
     *
     * @param partStock the part stock
     */
    public void setPartStock(TableColumn<Part, Integer> partStock) {
        this.partStock = partStock;
    }

    /**
     * Gets part price.
     *
     * @return the part price
     */
    public TableColumn<Part, Double> getPartPrice() {
        return partPrice;
    }

    /**
     * Sets part price.
     *
     * @param partPrice the part price
     */
    public void setPartPrice(TableColumn<Part, Double> partPrice) {
        this.partPrice = partPrice;
    }

    /**
     * Gets product id.
     *
     * @return the product id
     */
    public TableColumn<Product, Integer> getProductId() {
        return productId;
    }

    /**
     * Sets product id.
     *
     * @param productId the product id
     */
    public void setProductId(TableColumn<Product, Integer> productId) {
        this.productId = productId;
    }

    /**
     * Gets product name.
     *
     * @return the product name
     */
    public TableColumn<Product, String> getProductName() {
        return productName;
    }

    /**
     * Sets product name.
     *
     * @param productName the product name
     */
    public void setProductName(TableColumn<Product, String> productName) {
        this.productName = productName;
    }

    /**
     * Gets product stock.
     *
     * @return the product stock
     */
    public TableColumn<Product, Integer> getProductStock() {
        return productStock;
    }

    /**
     * Sets product stock.
     *
     * @param productStock the product stock
     */
    public void setProductStock(TableColumn<Product, Integer> productStock) {
        this.productStock = productStock;
    }

    /**
     * Gets product price.
     *
     * @return the product price
     */
    public TableColumn<Product, Double> getProductPrice() {
        return productPrice;
    }

    /**
     * Sets product price.
     *
     * @param productPrice the product price
     */
    public void setProductPrice(TableColumn<Product, Double> productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Gets documentation link.
     *
     * @return the documentation link
     */
    public Hyperlink getDocumentationLink() {
        return documentationLink;
    }

    /**
     * Sets documentation link.
     *
     * @param documentationLink the documentation link
     */
    public void setDocumentationLink(Hyperlink documentationLink) {
        this.documentationLink = documentationLink;
    }
}
