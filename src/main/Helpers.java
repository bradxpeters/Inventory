package main;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Alert;

/**
 * The type Helpers.
 */
public class Helpers {
    /**
     * The Integer filter.
     */
    public ChangeListener<String> integerFilter = (obs, oldValue, newValue) -> {
        if (!newValue.matches("[0-9]*")) {
            ((StringProperty) obs).set(oldValue);
            var alert = new Alert(Alert.AlertType.ERROR, "Must be an integer value");
            alert.setHeaderText("Input error");
            alert.showAndWait();
        } else {
            ((StringProperty) obs).set(newValue);
        }
    };

    /**
     * The Decimal filter.
     */
    public ChangeListener<String> decimalFilter = (obs, oldValue, newValue) -> {
        if (!newValue.matches("[0-9.]*")) {
            ((StringProperty) obs).set(oldValue);
            var alert = new Alert(Alert.AlertType.ERROR, "Must be an decimal value");
            alert.setHeaderText("Input error");
            alert.showAndWait();
        } else {
            ((StringProperty) obs).set(newValue);
        }
    };

    /**
     * Validate stock levels.
     * Ensures the max is greater than the min and that stock is between those two value
     *
     */
    public boolean stockLevelsValid(int max, int min, int stock) {
        var valid = max > min && stock > min && stock < max;

        if (!valid) {
            var alert = new Alert(Alert.AlertType.ERROR, "Max must be greater than min and inventory " +
                    "must be a value between the max and the min!");
            alert.setHeaderText("Inventory error!");
            alert.showAndWait();
        }

        return valid;
    }
}
