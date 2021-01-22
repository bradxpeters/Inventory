package parts;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Helpers;
import main.Inventory;

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
     * @param e the ActionEvent
     */
    @FXML
    public void handleCancelButton(ActionEvent e) {
        // Close the window
        var stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    };

    /**
     * Validate save boolean.
     *
     * @return the boolean
     */
    public boolean validateSave() {
        return this.getPartNameField().getText() != null && !this.getPartNameField().getText().equalsIgnoreCase("")
            && this.getPartPriceField().getText() != null && !this.getPartPriceField().getText().equalsIgnoreCase("")
            && this.getPartStockField().getText() != null && !this.getPartStockField().getText().equalsIgnoreCase("")
            && this.getPartMaxField().getText() != null && !this.getPartMaxField().getText().equalsIgnoreCase("")
            && this.getPartMinField().getText() != null && !this.getPartMinField().getText().equalsIgnoreCase("");
    }

    /**
     * Validate save in house boolean.
     *
     * @return the boolean
     */
    public boolean validateSaveInHouse() {
        return validateSave()
            && this.getMachineIdField().getText() != null
            && !this.getMachineIdField().getText().equalsIgnoreCase("");
    }

    /**
     * Validate save outsourced boolean.
     *
     * @return the boolean
     */
    public boolean validateSaveOutsourced() {
        return validateSave()
            && this.getCompanyNameField().getText() != null
            && !this.getCompanyNameField().getText().equalsIgnoreCase("");
    }

    /**
     * Handle submit add part form.
     *
     * @param e the ActionEvent
     */
    @FXML
    public void handleSubmitAddPartForm(ActionEvent e) {
        // handle existing part
        if (this.getExistingPart() != null) {
            if (this.getInHouseRadioButton().isSelected()) {

                if (!validateSaveInHouse()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                var updatedPart = new InHouse(
                    this.getExistingPart().getId(),
                    this.getPartNameField().getText(),
                    Double.parseDouble(this.getPartPriceField().getText()),
                    Integer.parseInt(this.getPartStockField().getText()),
                    Integer.parseInt(this.getPartMinField().getText()),
                    Integer.parseInt(this.getPartMaxField().getText()),
                    Integer.parseInt(this.getMachineIdField().getText())
                );

                var index = Inventory.getAllParts().indexOf(this.getExistingPart());
                Inventory.updatePart(index, updatedPart);
            } else {

                if (!validateSaveOutsourced()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                var updatedPart = new Outsourced(
                    this.getExistingPart().getId(),
                    this.getPartNameField().getText(),
                    Double.parseDouble(this.getPartPriceField().getText()),
                    Integer.parseInt(this.getPartStockField().getText()),
                    Integer.parseInt(this.getPartMinField().getText()),
                    Integer.parseInt(this.getPartMaxField().getText()),
                    this.getCompanyNameField().getText()
                );

                var index = Inventory.getAllParts().indexOf(this.getExistingPart());
                Inventory.updatePart(index, updatedPart);
            }
        } else {
            // Handle new part
            if (this.getInHouseRadioButton().isSelected()) {

                if (!validateSaveInHouse()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                Inventory.addPart(
                    new InHouse(
                        Integer.parseInt(this.getPartIdField().getText()),
                        this.getPartNameField().getText(),
                        Integer.parseInt(this.getPartPriceField().getText()),
                        Integer.parseInt(this.getPartStockField().getText()),
                        Integer.parseInt(this.getPartMinField().getText()),
                        Integer.parseInt(this.getPartMaxField().getText()),
                        Integer.parseInt(this.getMachineIdField().getText())
                    )
                );
            } else {
                if (!validateSaveOutsourced()) {
                    var alert = new Alert(Alert.AlertType.ERROR, "No fields can be left empty!");
                    alert.setHeaderText("Missing data error");
                    alert.showAndWait();
                    return;
                }

                Inventory.addPart(
                    new Outsourced(
                        Integer.parseInt(this.getPartIdField().getText()),
                        this.getPartNameField().getText(),
                        Integer.parseInt(this.getPartPriceField().getText()),
                        Integer.parseInt(this.getPartStockField().getText()),
                        Integer.parseInt(this.getPartMinField().getText()),
                        Integer.parseInt(this.getPartMaxField().getText()),
                        this.getCompanyNameField().getText()
                    )
                );
            }
        }

        // Close the window
        var stage = (Stage) this.getSaveButton().getScene().getWindow();
        stage.close();
    }

    /**
     * Handle toggle radio buttons.
     *
     * @param e the ActionEvent
     */
    @FXML
    public void handleToggleRadioButtons(ActionEvent e) {
        var selectedButton = (RadioButton) e.getSource();
        handleCurrentView(selectedButton);
    }

    /**
     * Handle current view.
     *
     * @param button the button
     */
    public void handleCurrentView(RadioButton button) {
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
                this.getPartNameField().setText(existingPart.getName());
                this.getPartStockField().setText(String.valueOf(existingPart.getStock()));
                this.getPartPriceField().setText(String.valueOf(existingPart.getPrice()));
                this.getPartMaxField().setText(String.valueOf(existingPart.getMax()));
                this.getPartMinField().setText(String.valueOf(existingPart.getMin()));

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
                partIdField.setText(String.valueOf(Inventory.currentPartId));
            }

            // Handle enforce types
            var helpers = new Helpers();
            partIdField.textProperty().addListener(helpers.integerFilter);
            this.getPartStockField().textProperty().addListener(helpers.integerFilter);
            this.getPartMaxField().textProperty().addListener(helpers.integerFilter);
            this.getPartMinField().textProperty().addListener(helpers.integerFilter);
            this.getPartPriceField().textProperty().addListener(helpers.decimalFilter);
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

    /**
     * Gets machine id label.
     *
     * @return the machine id label
     */
    public Label getMachineIdLabel() {
        return machineIdLabel;
    }

    /**
     * Sets machine id label.
     *
     * @param machineIdLabel the machine id label
     */
    public void setMachineIdLabel(Label machineIdLabel) {
        this.machineIdLabel = machineIdLabel;
    }

    /**
     * Gets company name label.
     *
     * @return the company name label
     */
    public Label getCompanyNameLabel() {
        return companyNameLabel;
    }

    /**
     * Sets company name label.
     *
     * @param companyNameLabel the company name label
     */
    public void setCompanyNameLabel(Label companyNameLabel) {
        this.companyNameLabel = companyNameLabel;
    }

    /**
     * Gets part id field.
     *
     * @return the part id field
     */
    public TextField getPartIdField() {
        return partIdField;
    }

    /**
     * Sets part id field.
     *
     * @param partIdField the part id field
     */
    public void setPartIdField(TextField partIdField) {
        this.partIdField = partIdField;
    }

    /**
     * Gets part name field.
     *
     * @return the part name field
     */
    public TextField getPartNameField() {
        return partNameField;
    }

    /**
     * Sets part name field.
     *
     * @param partNameField the part name field
     */
    public void setPartNameField(TextField partNameField) {
        this.partNameField = partNameField;
    }

    /**
     * Gets part stock field.
     *
     * @return the part stock field
     */
    public TextField getPartStockField() {
        return partStockField;
    }

    /**
     * Sets part stock field.
     *
     * @param partStockField the part stock field
     */
    public void setPartStockField(TextField partStockField) {
        this.partStockField = partStockField;
    }

    /**
     * Gets part price field.
     *
     * @return the part price field
     */
    public TextField getPartPriceField() {
        return partPriceField;
    }

    /**
     * Sets part price field.
     *
     * @param partPriceField the part price field
     */
    public void setPartPriceField(TextField partPriceField) {
        this.partPriceField = partPriceField;
    }

    /**
     * Gets machine id field.
     *
     * @return the machine id field
     */
    public TextField getMachineIdField() {
        return machineIdField;
    }

    /**
     * Sets machine id field.
     *
     * @param machineIdField the machine id field
     */
    public void setMachineIdField(TextField machineIdField) {
        this.machineIdField = machineIdField;
    }

    /**
     * Gets company name field.
     *
     * @return the company name field
     */
    public TextField getCompanyNameField() {
        return companyNameField;
    }

    /**
     * Sets company name field.
     *
     * @param companyNameField the company name field
     */
    public void setCompanyNameField(TextField companyNameField) {
        this.companyNameField = companyNameField;
    }

    /**
     * Gets part max field.
     *
     * @return the part max field
     */
    public TextField getPartMaxField() {
        return partMaxField;
    }

    /**
     * Sets part max field.
     *
     * @param partMaxField the part max field
     */
    public void setPartMaxField(TextField partMaxField) {
        this.partMaxField = partMaxField;
    }

    /**
     * Gets part min field.
     *
     * @return the part min field
     */
    public TextField getPartMinField() {
        return partMinField;
    }

    /**
     * Sets part min field.
     *
     * @param partMinField the part min field
     */
    public void setPartMinField(TextField partMinField) {
        this.partMinField = partMinField;
    }

    /**
     * Gets radio button group.
     *
     * @return the radio button group
     */
    public ToggleGroup getRadioButtonGroup() {
        return radioButtonGroup;
    }

    /**
     * Sets radio button group.
     *
     * @param radioButtonGroup the radio button group
     */
    public void setRadioButtonGroup(ToggleGroup radioButtonGroup) {
        this.radioButtonGroup = radioButtonGroup;
    }

    /**
     * Gets in house radio button.
     *
     * @return the in house radio button
     */
    public RadioButton getInHouseRadioButton() {
        return inHouseRadioButton;
    }

    /**
     * Sets in house radio button.
     *
     * @param inHouseRadioButton the in house radio button
     */
    public void setInHouseRadioButton(RadioButton inHouseRadioButton) {
        this.inHouseRadioButton = inHouseRadioButton;
    }

    /**
     * Gets outsourced radio button.
     *
     * @return the outsourced radio button
     */
    public RadioButton getOutsourcedRadioButton() {
        return outsourcedRadioButton;
    }

    /**
     * Sets outsourced radio button.
     *
     * @param outsourcedRadioButton the outsourced radio button
     */
    public void setOutsourcedRadioButton(RadioButton outsourcedRadioButton) {
        this.outsourcedRadioButton = outsourcedRadioButton;
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
}
