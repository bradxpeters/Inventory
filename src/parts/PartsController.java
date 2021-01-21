package parts;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Helpers;
import main.Inventory;
import products.Product;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The type Parts controller.
 */
public class PartsController implements Initializable {

    private final String IN_HOUSE_RADIO = "inHouseRadioButton";

    private Part existingPart;

    @FXML
    private Label machineIdLabel;

    @FXML
    private Label companyNameLabel;

    @FXML
    private TextField partIdField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partStockField;

    @FXML
    private TextField partPriceField;

    @FXML
    private TextField machineIdField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField partMaxField;

    @FXML
    private TextField partMinField;

    @FXML
    private ToggleGroup radioButtonGroup;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    /**
     * Handle cancel button.
     *
     * @param e the e
     */
    @FXML
    void handleCancelButton(ActionEvent e) {
        // Close the window
        var stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    };

    private boolean validateSave() {
        return partNameField.getText() != null && !partNameField.getText().equalsIgnoreCase("")
            && partPriceField.getText() != null && !partPriceField.getText().equalsIgnoreCase("")
            && partStockField.getText() != null && !partStockField.getText().equalsIgnoreCase("")
            && partMaxField.getText() != null && !partMaxField.getText().equalsIgnoreCase("")
            && partMinField.getText() != null && !partMinField.getText().equalsIgnoreCase("");
    }

    private boolean validateSaveInHouse() {
        return validateSave()
            && machineIdField.getText() != null
            && !machineIdField.getText().equalsIgnoreCase("");
    }

    private boolean validateSaveOutsourced() {
        return validateSave()
            && companyNameField.getText() != null
            && !companyNameField.getText().equalsIgnoreCase("");
    }

    /**
     * Handle submit add part form.
     *
     * @param e the e
     */
    @FXML
    void handleSubmitAddPartForm(ActionEvent e) {
        // handle existing part
        if (this.existingPart != null) {
            if (inHouseRadioButton.isSelected()) {

                if (!validateSaveInHouse()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                var updatedPart = new InHouse(
                    this.existingPart.getId(),
                    partNameField.getText(),
                    Double.parseDouble(partPriceField.getText()),
                    Integer.parseInt(partStockField.getText()),
                    Integer.parseInt(partMinField.getText()),
                    Integer.parseInt(partMaxField.getText()),
                    Integer.parseInt(machineIdField.getText())
                );

                var index = Inventory.getInstance().getAllParts().indexOf(this.existingPart);
                Inventory.getInstance().updatePart(index, updatedPart);
            } else {

                if (!validateSaveOutsourced()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                var updatedPart = new Outsourced(
                    this.existingPart.getId(),
                    partNameField.getText(),
                    Double.parseDouble(partPriceField.getText()),
                    Integer.parseInt(partStockField.getText()),
                    Integer.parseInt(partMinField.getText()),
                    Integer.parseInt(partMaxField.getText()),
                    companyNameField.getText()
                );

                var index = Inventory.getInstance().getAllParts().indexOf(this.existingPart);
                Inventory.getInstance().updatePart(index, updatedPart);
            }
        } else {
            // Handle new part
            if (inHouseRadioButton.isSelected()) {

                if (!validateSaveInHouse()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                Inventory.getInstance().addPart(
                    new InHouse(
                        Integer.parseInt(partIdField.getText()),
                        partNameField.getText(),
                        Integer.parseInt(partPriceField.getText()),
                        Integer.parseInt(partStockField.getText()),
                        Integer.parseInt(partMinField.getText()),
                        Integer.parseInt(partMaxField.getText()),
                        Integer.parseInt(machineIdField.getText())
                    )
                );
            } else {
                if (!validateSaveOutsourced()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                Inventory.getInstance().addPart(
                    new Outsourced(
                        Integer.parseInt(partIdField.getText()),
                        partNameField.getText(),
                        Integer.parseInt(partPriceField.getText()),
                        Integer.parseInt(partStockField.getText()),
                        Integer.parseInt(partMinField.getText()),
                        Integer.parseInt(partMaxField.getText()),
                        companyNameField.getText()
                    )
                );
            }
        }

        // Close the window
        var stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handle toggle radio buttons.
     *
     * @param e the e
     */
    @FXML
    void handleToggleRadioButtons(ActionEvent e) {
        var selectedButton = (RadioButton) e.getSource();
        handleCurrentView(selectedButton);
    }

    /**
     * Handle current view.
     *
     * @param button the button
     */
    void handleCurrentView(RadioButton button) {
        if (button.getId().equals(IN_HOUSE_RADIO)) {
            machineIdLabel.setVisible(true);
            machineIdField.setVisible(true);
            companyNameLabel.setVisible(false);
            companyNameField.setVisible(false);
        } else {
            companyNameLabel.setVisible(true);
            companyNameField.setVisible(true);
            machineIdLabel.setVisible(false);
            machineIdField.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

            // Handle editing existing part
            if (this.getExistingPart() != null) {
                var existingPart = this.getExistingPart();
                partIdField.setText(String.valueOf(existingPart.getId()));
                partNameField.setText(existingPart.getName());
                partStockField.setText(String.valueOf(existingPart.getStock()));
                partPriceField.setText(String.valueOf(existingPart.getPrice()));
                partMaxField.setText(String.valueOf(existingPart.getMax()));
                partMinField.setText(String.valueOf(existingPart.getMin()));

                if (existingPart.getClass() == InHouse.class) {
                    inHouseRadioButton.setSelected(true);
                    handleCurrentView(inHouseRadioButton);
                    machineIdField.setText(String.valueOf(((InHouse) existingPart).getMachineId()));
                } else {
                    outsourcedRadioButton.setSelected(true);
                    handleCurrentView(outsourcedRadioButton);
                    companyNameField.setText(String.valueOf(((Outsourced) existingPart).getCompanyName()));
                }
            } else {
                // New part
                partIdField.setText(String.valueOf(Inventory.getInstance().getCurrentPartId()));
            }

            // Handle enforce types
            var helpers = new Helpers();
            partIdField.textProperty().addListener(helpers.integerFilter);
            partStockField.textProperty().addListener(helpers.integerFilter);
            partMaxField.textProperty().addListener(helpers.integerFilter);
            partMinField.textProperty().addListener(helpers.integerFilter);
            partPriceField.textProperty().addListener(helpers.decimalFilter);
        });
    }

    /**
     * Gets existing part.
     *
     * @return the existing part
     */
    public Part getExistingPart() {
        return existingPart;
    }

    /**
     * Sets existing part.
     *
     * @param existingPart the existing part
     */
    public void setExistingPart(Part existingPart) {
        this.existingPart = existingPart;
    }
}
