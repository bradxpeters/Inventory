package products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import parts.Part;

import java.util.ArrayList;

public class Product {
    private ObservableList<Part> associatedParts;

    private int id;

    private String name;

    private double price;

    private int stock;

    private int min;

    private int max;

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Product(int id) {
        this.id = id;
        this.associatedParts = FXCollections.observableList(new ArrayList<Part>());
    }

    public Product(ObservableList<Part> associatedParts, int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = associatedParts;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void addAssociatedPart(Part part) {
        // Don't allow duplicate parts
        var existing = this.getAllAssociatedParts()
            .stream()
            .filter(p -> p.getId() == part.getId())
            .findFirst()
            .orElse(null);

        if (existing == null) {
            this.getAllAssociatedParts().add(part);
        }
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return this
            .getAllAssociatedParts()
            .removeIf(p -> p.getId() == selectedAssociatedPart.getId());
    }

}
