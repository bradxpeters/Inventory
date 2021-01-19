package parts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PartsController implements Initializable {

    private final String IN_HOUSE_RADIO = "inHouseRadioButton";

    @FXML
    private Label machineIdLabel;

    @FXML
    private Label companyNameLabel;

    @FXML
    private TextField partId;

    @FXML
    private TextField partName;

    @FXML
    private TextField inventoryLevel;

    @FXML
    private TextField costPerUnit;

    @FXML
    private TextField machineIdTextField;

    @FXML
    private TextField companyNameTextField;

    @FXML
    private TextField partMax;

    @FXML
    private TextField partMin;

    @FXML
    private ToggleGroup radioButtonGroup;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private Button addPartFormSaveButton;

    @FXML
    void handleSubmitAddPartForm(ActionEvent e) {
        var selectedButton = (RadioButton) e.getSource();
        if (selectedButton.getId().equals(IN_HOUSE_RADIO)) {
            Inventory.getInstance().addToList(
                new InHouse(
                    Integer.parseInt(partId.getText()),
                    partName.getText(),
                    Integer.parseInt(costPerUnit.getText()),
                    Integer.parseInt(inventoryLevel.getText()),
                    Integer.parseInt(partMin.getText()),
                    Integer.parseInt(partMax.getText()),
                    Integer.parseInt(machineIdTextField.getText())
                )
            );
        } else {
            Inventory.getInstance().addToList(
                new Outsourced(
                    Integer.parseInt(partId.getText()),
                    partName.getText(),
                    Integer.parseInt(costPerUnit.getText()),
                    Integer.parseInt(inventoryLevel.getText()),
                    Integer.parseInt(partMin.getText()),
                    Integer.parseInt(partMax.getText()),
                    companyNameTextField.getText()
                )
            );
        }

        // Close the window
        var stage = (Stage) addPartFormSaveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleToggleRadioButtons(ActionEvent e) {
        var selectedButton = (RadioButton) e.getSource();
        if (selectedButton.getId().equals(IN_HOUSE_RADIO)) {
            machineIdLabel.setVisible(true);
            machineIdTextField.setVisible(true);
            companyNameLabel.setVisible(false);
            companyNameTextField.setVisible(false);
        } else {
            companyNameLabel.setVisible(true);
            companyNameTextField.setVisible(true);
            machineIdLabel.setVisible(false);
            machineIdTextField.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partId.setText(String.valueOf(Inventory.getInstance().getCurrentId()));
    }
}
