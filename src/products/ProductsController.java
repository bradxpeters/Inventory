package products;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Inventory;
import parts.Part;

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
    private TableColumn<Part,Integer> partId;

    @FXML
    private TableColumn<Part,String> partName;

    @FXML
    private TableColumn<Part,Integer> partStock;

    @FXML
    private TableColumn<Part,Double> partPrice;

    public Product getExistingProduct() {
        return existingProduct;
    }

    public void setExistingProduct(Product existingProduct) {
        this.existingProduct = existingProduct;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize cell factories
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        Platform.runLater(() -> {

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
            System.out.println("SETTING SMALL PARTS TABLE");
            allPartsTable.setItems(Inventory.getInstance().getAllParts());
        });
    }
}
