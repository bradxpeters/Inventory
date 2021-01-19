package parts;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Inventory {
    private static Inventory parts;

    private Integer currentId = 1;

    private ObservableList<Part> allParts = null;

    private ObservableList<Part> allProducts = null;

    public static Inventory getInstance() {
        if (parts == null) {
            parts = new Inventory();
        }
        return parts;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Inventory() {
        var list = new ArrayList<Part>();
        var observableList = FXCollections.observableList(list);
        observableList.addListener((ListChangeListener) change -> {});
        this.allParts = observableList;
    }

    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    public ObservableList<Part> getAllProducts() {
        return this.allProducts;
    }

    public void addToList(Part part) {
        allParts.add(part);
        currentId++;
    }

    public void removeFromList(Part partToRemove) {
        allParts.removeIf(p -> p.getId() == (partToRemove.getId()));
        currentId--;
    }

    public Integer getCurrentId() {
        return currentId;
    }
}
