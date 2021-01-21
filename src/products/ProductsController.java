package products;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Helpers;
import main.Inventory;
import parts.Part;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {

    @FXML
    private TextField productIdField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productStockField;

    @FXML
    private TextField productPriceField;


    @FXML
    private TextField productMaxField;

    @FXML
    private TextField productMinField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Product existingProduct;

    private Product newProduct;

    @FXML
    TableView<Part> allPartsTable;

    @FXML
    TableView<Part> associatedPartsTable;

    @FXML
    private TableColumn<Part,Integer> partId;

    @FXML
    private TableColumn<Part,String> partName;

    @FXML
    private TableColumn<Part,Integer> partStock;

    @FXML
    private TableColumn<Part,Double> partPrice;

    @FXML
    private TableColumn<Part,Integer> associatedPartId;

    @FXML
    private TableColumn<Part,String> associatedPartName;

    @FXML
    private TableColumn<Part,Integer> associatedPartStock;

    @FXML
    private TableColumn<Part,Double> associatedPartPrice;

    @FXML
    private Button addPartButton;

    @FXML
    private Button deletePartButton;

    @FXML
    private TextField searchField;

    private boolean validateSave() {
        return productNameField.getText() != null && !productNameField.getText().equalsIgnoreCase("")
            && productPriceField.getText() != null && !productPriceField.getText().equalsIgnoreCase("")
            && productStockField.getText() != null && !productStockField.getText().equalsIgnoreCase("")
            && productMaxField.getText() != null && !productMaxField.getText().equalsIgnoreCase("")
            && productMinField.getText() != null && !productMinField.getText().equalsIgnoreCase("");
    }

    @FXML
    private void handleSaveProduct() {

        if (!validateSave()) {
            var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
            alert.setHeaderText("Missing data error");
            alert.showAndWait();
            return;
        }

        if (this.getExistingProduct() != null) {
            var updatedProduct = new Product(
                associatedPartsTable.getItems(),
                this.getExistingProduct().getId(),
                productNameField.getText(),
                Double.parseDouble(productPriceField.getText()),
                Integer.parseInt(productStockField.getText()),
                Integer.parseInt(productMinField.getText()),
                Integer.parseInt(productMaxField.getText())
            );

            var index = Inventory.getInstance().getAllProducts().indexOf(this.getExistingProduct());
            Inventory.getInstance().updateProduct(index, updatedProduct);
        } else {
            var newProduct = new Product(
                associatedPartsTable.getItems(),
                Integer.parseInt(productIdField.getText()),
                productNameField.getText(),
                Double.parseDouble(productPriceField.getText()),
                Integer.parseInt(productStockField.getText()),
                Integer.parseInt(productMinField.getText()),
                Integer.parseInt(productMaxField.getText())
            );

            Inventory.getInstance().addProduct(newProduct);
        }

        var stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancelButton() {
        var scene = (Stage) cancelButton.getScene().getWindow();
        scene.close();
    }

    public Product getExistingProduct() {
        return existingProduct;
    }

    public void setExistingProduct(Product existingProduct) {
        this.existingProduct = existingProduct;
    }

    @FXML
    public void handleAddPart(ActionEvent e){
        this.getExistingProduct().addAssociatedPart(
            allPartsTable.getSelectionModel().getSelectedItem()
        );
    }

    @FXML
    public void handleRemovePart(ActionEvent e){
        this.getExistingProduct().deleteAssociatedPart(associatedPartsTable.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            // Initialize cell factories
            partId.setCellValueFactory(new PropertyValueFactory<>("id"));
            partName.setCellValueFactory(new PropertyValueFactory<>("name"));
            partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
            associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            associatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

            // Enforce max > min

            // Handle editing existing product
            if (this.getExistingProduct() != null) {
                var existingProduct = this.getExistingProduct();
                productIdField.setText(String.valueOf(existingProduct.getId()));
                productNameField.setText(existingProduct.getName());
                productStockField.setText(String.valueOf(existingProduct.getStock()));
                productPriceField.setText(String.valueOf(existingProduct.getPrice()));
                productMaxField.setText(String.valueOf(existingProduct.getMax()));
                productMinField.setText(String.valueOf(existingProduct.getMin()));
            } else {
                // New product
                productIdField.setText(String.valueOf(Inventory.getInstance().getCurrentProductId()));
            }

            // Fill all parts table
            allPartsTable.setItems(Inventory.getInstance().getAllParts());
            associatedPartsTable.setItems(
                this.getExistingProduct() != null
                    ? this.getExistingProduct().getAllAssociatedParts()
                    : FXCollections.observableList(new ArrayList<>())
            );

            // Add part button should be disabled until a selection is made
            addPartButton.setDisable(true);
            allPartsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    addPartButton.setDisable(false);
                }
            });

            // Handle search
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                int partId = -1;
                try {
                    partId = Integer.parseInt(newValue);
                } catch (Exception ignored) {} // Not an parsable integer

                if (partId != -1) {
                    var tempList = FXCollections.observableList(new ArrayList<Part>());
                    tempList.add(Inventory.getInstance().lookupPart(partId));
                    allPartsTable.setItems(tempList);
                } else {
                    allPartsTable.setItems(Inventory.getInstance().lookupPart(newValue));
                }

                if (allPartsTable.getItems().size() == 0) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No result found!");
                    alert.setHeaderText("Search error");
                    alert.showAndWait();
                }
            });

            // Handle enforce types
            var helpers = new Helpers();
            productIdField.textProperty().addListener(helpers.integerFilter);
            productStockField.textProperty().addListener(helpers.integerFilter);
            productMaxField.textProperty().addListener(helpers.integerFilter);
            productMinField.textProperty().addListener(helpers.integerFilter);
            productPriceField.textProperty().addListener(helpers.decimalFilter);
        });
    }
}
