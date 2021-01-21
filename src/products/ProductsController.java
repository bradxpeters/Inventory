package products;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 * The type Products controller.
 */
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

    private ObservableList<Part> tmpParts;

    /**
     * The All parts table.
     */
    @FXML
    TableView<Part> allPartsTable;

    /**
     * The Associated parts table.
     */
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
        return this.getProductNameField().getText() != null && !this.getProductNameField().getText().equalsIgnoreCase("")
            && this.getProductPriceField().getText() != null && !this.getProductPriceField().getText().equalsIgnoreCase("")
            && this.getProductStockField().getText() != null && !this.getProductStockField().getText().equalsIgnoreCase("")
            && this.getProductMaxField().getText() != null && !this.getProductMaxField().getText().equalsIgnoreCase("")
            && this.getProductMinField().getText() != null && !this.getProductMinField().getText().equalsIgnoreCase("");
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
                this.getAssociatedPartsTable().getItems(),
                this.getExistingProduct().getId(),
                this.getProductNameField().getText(),
                Double.parseDouble(this.getProductPriceField().getText()),
                Integer.parseInt(this.getProductStockField().getText()),
                Integer.parseInt(this.getProductMinField().getText()),
                Integer.parseInt(this.getProductMaxField().getText())
            );

            var index = Inventory.getInstance().getAllProducts().indexOf(this.getExistingProduct());
            Inventory.getInstance().updateProduct(index, updatedProduct);
        } else {
            var newProduct = new Product(
                this.getAssociatedPartsTable().getItems(),
                Integer.parseInt(this.getProductIdField().getText()),
                this.getProductNameField().getText(),
                Double.parseDouble(this.getProductPriceField().getText()),
                Integer.parseInt(this.getProductStockField().getText()),
                Integer.parseInt(this.getProductMinField().getText()),
                Integer.parseInt(this.getProductMaxField().getText())
            );

            Inventory.getInstance().addProduct(newProduct);
        }

        var stage = (Stage) this.getSaveButton().getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancelButton() {
        var scene = (Stage) this.getCancelButton().getScene().getWindow();
        scene.close();
    }

    /**
     * Gets existing product.
     *
     * @return the existing product
     */
    public Product getExistingProduct() {
        return existingProduct;
    }

    /**
     * Sets existing product.
     *
     * @param existingProduct the existing product
     */
    public void setExistingProduct(Product existingProduct) {
        this.existingProduct = existingProduct;
    }

    /**
     * Handle add part.
     *
     * @param e the ActionEvent
     */
    @FXML
    public void handleAddPart(ActionEvent e){
        if (this.getExistingProduct() != null) {
            this.getExistingProduct().addAssociatedPart(
                this.getAllPartsTable().getSelectionModel().getSelectedItem()
            );
        } else {
            this.getTmpParts().add(this.getAllPartsTable().getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Handle remove part.
     *
     * @param e the ActionEvent
     */
    @FXML
    public void handleRemovePart(ActionEvent e){
        if (this.getExistingProduct() != null) {
            this.getExistingProduct().deleteAssociatedPart(this.getAssociatedPartsTable().getSelectionModel().getSelectedItem());
        } else {
            this.getTmpParts().removeIf(p -> p.getId() == this.getAssociatedPartsTable().getSelectionModel().getSelectedItem().getId());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            // Initialize cell factories
            this.getPartId().setCellValueFactory(new PropertyValueFactory<>("id"));
            this.getPartName().setCellValueFactory(new PropertyValueFactory<>("name"));
            this.getPartStock().setCellValueFactory(new PropertyValueFactory<>("stock"));
            this.getPartPrice().setCellValueFactory(new PropertyValueFactory<>("price"));
            this.getAssociatedPartId().setCellValueFactory(new PropertyValueFactory<>("id"));
            this.getAssociatedPartName().setCellValueFactory(new PropertyValueFactory<>("name"));
            this.getAssociatedPartStock().setCellValueFactory(new PropertyValueFactory<>("stock"));
            this.getAssociatedPartPrice().setCellValueFactory(new PropertyValueFactory<>("price"));

            // Enforce max > min

            // Handle editing existing product
            if (this.getExistingProduct() != null) {
                var existingProduct = this.getExistingProduct();
                this.getProductIdField().setText(String.valueOf(existingProduct.getId()));
                this.getProductNameField().setText(existingProduct.getName());
                this.getProductStockField().setText(String.valueOf(existingProduct.getStock()));
                this.getProductPriceField().setText(String.valueOf(existingProduct.getPrice()));
                this.getProductMaxField().setText(String.valueOf(existingProduct.getMax()));
                this.getProductMinField().setText(String.valueOf(existingProduct.getMin()));
            } else {
                // New product
                this.getProductIdField().setText(String.valueOf(Inventory.getInstance().getCurrentProductId()));
                this.setTmpParts(FXCollections.observableList(new ArrayList<>()));
            }

            // Fill all parts table
            this.getAllPartsTable().setItems(Inventory.getInstance().getAllParts());
            this.getAssociatedPartsTable().setItems(
                this.getExistingProduct() != null
                    ? this.getExistingProduct().getAllAssociatedParts()
                    : this.getTmpParts()
            );

            // Add part button should be disabled until a selection is made
            this.getAddPartButton().setDisable(true);
            this.getAllPartsTable().getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    this.getAddPartButton().setDisable(false);
                }
            });

            // Handle search
            this.getSearchField().textProperty().addListener((observableValue, oldValue, newValue) -> {
                int partId = -1;
                try {
                    partId = Integer.parseInt(newValue);
                } catch (Exception ignored) {} // Not an parsable integer

                if (partId != -1) {
                    var tempList = FXCollections.observableList(new ArrayList<Part>());
                    tempList.add(Inventory.getInstance().lookupPart(partId));
                    this.getAllPartsTable().setItems(tempList);
                } else {
                    this.getAllPartsTable().setItems(Inventory.getInstance().lookupPart(newValue));
                }

                if (this.getAllPartsTable().getItems().size() == 0) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No result found!");
                    alert.setHeaderText("Search error");
                    alert.showAndWait();
                }
            });

            // Handle enforce types
            var helpers = new Helpers();
            this.getProductIdField().textProperty().addListener(helpers.integerFilter);
            this.getProductStockField().textProperty().addListener(helpers.integerFilter);
            this.getProductMaxField().textProperty().addListener(helpers.integerFilter);
            this.getProductMinField().textProperty().addListener(helpers.integerFilter);
            this.getProductPriceField().textProperty().addListener(helpers.decimalFilter);
        });
    }

    /**
     * Gets product id field.
     *
     * @return the product id field
     */
    public TextField getProductIdField() {
        return productIdField;
    }

    /**
     * Sets product id field.
     *
     * @param productIdField the product id field
     */
    public void setProductIdField(TextField productIdField) {
        this.productIdField = productIdField;
    }

    /**
     * Gets product name field.
     *
     * @return the product name field
     */
    public TextField getProductNameField() {
        return productNameField;
    }

    /**
     * Sets product name field.
     *
     * @param productNameField the product name field
     */
    public void setProductNameField(TextField productNameField) {
        this.productNameField = productNameField;
    }

    /**
     * Gets product stock field.
     *
     * @return the product stock field
     */
    public TextField getProductStockField() {
        return productStockField;
    }

    /**
     * Sets product stock field.
     *
     * @param productStockField the product stock field
     */
    public void setProductStockField(TextField productStockField) {
        this.productStockField = productStockField;
    }

    /**
     * Gets product price field.
     *
     * @return the product price field
     */
    public TextField getProductPriceField() {
        return productPriceField;
    }

    /**
     * Sets product price field.
     *
     * @param productPriceField the product price field
     */
    public void setProductPriceField(TextField productPriceField) {
        this.productPriceField = productPriceField;
    }

    /**
     * Gets product max field.
     *
     * @return the product max field
     */
    public TextField getProductMaxField() {
        return productMaxField;
    }

    /**
     * Sets product max field.
     *
     * @param productMaxField the product max field
     */
    public void setProductMaxField(TextField productMaxField) {
        this.productMaxField = productMaxField;
    }

    /**
     * Gets product min field.
     *
     * @return the product min field
     */
    public TextField getProductMinField() {
        return productMinField;
    }

    /**
     * Sets product min field.
     *
     * @param productMinField the product min field
     */
    public void setProductMinField(TextField productMinField) {
        this.productMinField = productMinField;
    }

    /**
     * Gets save button.
     *
     * @return the save button
     */
    public Button getSaveButton() {
        return saveButton;
    }

    /**
     * Sets save button.
     *
     * @param saveButton the save button
     */
    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    /**
     * Gets cancel button.
     *
     * @return the cancel button
     */
    public Button getCancelButton() {
        return cancelButton;
    }

    /**
     * Sets cancel button.
     *
     * @param cancelButton the cancel button
     */
    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    /**
     * Gets tmp parts.
     *
     * @return the tmp parts
     */
    public ObservableList<Part> getTmpParts() {
        return tmpParts;
    }

    /**
     * Sets tmp parts.
     *
     * @param tmpParts the tmp parts
     */
    public void setTmpParts(ObservableList<Part> tmpParts) {
        this.tmpParts = tmpParts;
    }

    /**
     * Gets all parts table.
     *
     * @return the all parts table
     */
    public TableView<Part> getAllPartsTable() {
        return allPartsTable;
    }

    /**
     * Sets all parts table.
     *
     * @param allPartsTable the all parts table
     */
    public void setAllPartsTable(TableView<Part> allPartsTable) {
        this.allPartsTable = allPartsTable;
    }

    /**
     * Gets associated parts table.
     *
     * @return the associated parts table
     */
    public TableView<Part> getAssociatedPartsTable() {
        return associatedPartsTable;
    }

    /**
     * Sets associated parts table.
     *
     * @param associatedPartsTable the associated parts table
     */
    public void setAssociatedPartsTable(TableView<Part> associatedPartsTable) {
        this.associatedPartsTable = associatedPartsTable;
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
     * Gets associated part id.
     *
     * @return the associated part id
     */
    public TableColumn<Part, Integer> getAssociatedPartId() {
        return associatedPartId;
    }

    /**
     * Sets associated part id.
     *
     * @param associatedPartId the associated part id
     */
    public void setAssociatedPartId(TableColumn<Part, Integer> associatedPartId) {
        this.associatedPartId = associatedPartId;
    }

    /**
     * Gets associated part name.
     *
     * @return the associated part name
     */
    public TableColumn<Part, String> getAssociatedPartName() {
        return associatedPartName;
    }

    /**
     * Sets associated part name.
     *
     * @param associatedPartName the associated part name
     */
    public void setAssociatedPartName(TableColumn<Part, String> associatedPartName) {
        this.associatedPartName = associatedPartName;
    }

    /**
     * Gets associated part stock.
     *
     * @return the associated part stock
     */
    public TableColumn<Part, Integer> getAssociatedPartStock() {
        return associatedPartStock;
    }

    /**
     * Sets associated part stock.
     *
     * @param associatedPartStock the associated part stock
     */
    public void setAssociatedPartStock(TableColumn<Part, Integer> associatedPartStock) {
        this.associatedPartStock = associatedPartStock;
    }

    /**
     * Gets associated part price.
     *
     * @return the associated part price
     */
    public TableColumn<Part, Double> getAssociatedPartPrice() {
        return associatedPartPrice;
    }

    /**
     * Sets associated part price.
     *
     * @param associatedPartPrice the associated part price
     */
    public void setAssociatedPartPrice(TableColumn<Part, Double> associatedPartPrice) {
        this.associatedPartPrice = associatedPartPrice;
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
     * Gets search field.
     *
     * @return the search field
     */
    public TextField getSearchField() {
        return searchField;
    }

    /**
     * Sets search field.
     *
     * @param searchField the search field
     */
    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }
}
