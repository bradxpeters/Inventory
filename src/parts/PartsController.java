package parts;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Inventory;

import java.net.URL;
import java.util.ResourceBundle;

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

    @FXML
    void handleCancelButton(ActionEvent e) {
        // Close the window
        var stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    };

    @FXML
    void handleSubmitAddPartForm(ActionEvent e) {
        if (inHouseRadioButton.isSelected()) {
            Inventory.getInstance().addToList(
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
            Inventory.getInstance().addToList(
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

        // Close the window
        var stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleToggleRadioButtons(ActionEvent e) {
        var selectedButton = (RadioButton) e.getSource();
        handleCurrentView(selectedButton);
    }

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
                partIdField.setText(String.valueOf(Inventory.getInstance().getCurrentId()));
            }
        });


    }

    public Part getExistingPart() {
        return existingPart;
    }

    public void setExistingPart(Part existingPart) {
        this.existingPart = existingPart;
    }
}
