package products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import parts.Part;

import java.util.ArrayList;

/**
 * The type Product.
 */
public class Product {
    private ObservableList<Part> associatedParts;

    private int id;

    private String name;

    private double price;

    private int stock;

    private int min;

    private int max;

    /**
     * Gets all associated parts.
     *
     * @return the all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        if (associatedParts == null) {
            this.associatedParts = FXCollections.observableList(new ArrayList<>());
        }

        return associatedParts;
    }

    /**
     * Sets associated parts.
     *
     * @param associatedParts the associated parts
     */
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets stock.
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets stock.
     *
     * @param stock the stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets min.
     *
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets min.
     *
     * @param min the min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets max.
     *
     * @param max the max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Instantiates a new Product.
     *
     * @param associatedParts the associated parts
     * @param id              the id
     * @param name            the name
     * @param price           the price
     * @param stock           the stock
     * @param min             the min
     * @param max             the max
     */
    public Product(ObservableList<Part> associatedParts, int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = associatedParts;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Add associated part.
     *
     * @param part the part
     */
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

    /**
     * Delete associated part boolean.
     *
     * @param selectedAssociatedPart the selected associated part
     * @return the boolean
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return this
            .getAllAssociatedParts()
            .removeIf(p -> p.getId() == selectedAssociatedPart.getId());
    }

}
