package products;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Inventory;
import parts.Part;

import javafx.event.ActionEvent;
import java.net.URL;
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
                this.existingProduct = new Product();
            }

            // Fill all parts table
            allPartsTable.setItems(Inventory.getInstance().getAllParts());
            associatedPartsTable.setItems(this.getExistingProduct().getAllAssociatedParts());

            // Add part button should be disabled until a selection is made
            addPartButton.setDisable(true);

            allPartsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    addPartButton.setDisable(false);
                }
            });


        });
    }
}
