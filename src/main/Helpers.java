package main;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Alert;

import java.util.function.Function;

public class Helpers {
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
}
