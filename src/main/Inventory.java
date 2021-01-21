package main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import parts.Part;
import products.Product;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The type Inventory.
 */
public class Inventory {


    private ObservableList<Part> allParts;

    private ObservableList<Product> allProducts;

    private Integer currentPartId = 1;

    private Integer currentProductId = 1;

    private static Inventory inventory;

    /**
     * Add part.
     *
     * @param newPart the new part
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
        currentPartId++;
    }

    /**
     * Add product.
     *
     * @param newProduct the new product
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        currentProductId++;
    }

    /**
     * Lookup part part.
     *
     * @param partId the part id
     * @return the part
     */
    public Part lookupPart(int partId) {
        return Inventory
            .getInstance()
            .getAllParts()
            .stream()
            .filter(part -> part.getId() == partId)
            .findFirst()
            .orElse(null);
    }

    /**
     * Lookup product product.
     *
     * @param productId the product id
     * @return the product
     */
    public Product lookupProduct(int productId) {
        return Inventory
            .getInstance()
            .getAllProducts()
            .stream()
            .filter(product -> product.getId() == productId)
            .findFirst()
            .orElse(null);
    }

    /**
     * Lookup part observable list.
     *
     * @param partName the part name
     * @return the observable list
     */
    public ObservableList<Part> lookupPart (String partName) {
        if (partName != null && !partName.equals("")) {
            return Inventory
                .getInstance()
                .getAllParts()
                .stream()
                .filter(part -> part.getName().contains(partName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else {
           return this.getAllParts();
        }
    }

    /**
     * Lookup product observable list.
     *
     * @param productName the product name
     * @return the observable list
     */
    public ObservableList<Product> lookupProduct (String productName) {
        if (productName != null && !productName.equals("")) {
            return Inventory
                .getInstance()
                .getAllProducts()
                .stream()
                .filter(part -> part.getName().contains(productName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else {
            return this.getAllProducts();
        }
    }

    /**
     * Update part.
     *
     * @param index        the index
     * @param selectedPart the selected part
     */
    public void updatePart(int index, Part selectedPart) {
        this.allParts.set(index,selectedPart);
    }

    /**
     * Update product.
     *
     * @param index      the index
     * @param newProduct the new product
     */
    public void updateProduct(int index, Product newProduct) {
        this.allProducts.set(index,newProduct);
    }

    /**
     * Delete part boolean.
     *
     * @param selectedPart the selected part
     * @return the boolean
     */
    public boolean deletePart(Part selectedPart) {
        currentPartId--;
        return allParts.removeIf(p -> p.getId() == (selectedPart.getId()));
    }

    /**
     * Delete product boolean.
     *
     * @param selectedProduct the selected product
     * @return the boolean
     */
    public boolean deleteProduct(Product selectedProduct) {
        currentProductId--;
        return allProducts.removeIf(p -> p.getId() == (selectedProduct.getId()));
    }

    /**
     * Gets all parts.
     *
     * @return the all parts
     */
    public ObservableList<Part> getAllParts() {
        if (this.allParts == null) {
            this.allParts = FXCollections.observableList(new ArrayList<>());
        }

        return this.allParts;
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public ObservableList<Product> getAllProducts() {
        if (this.allProducts == null) {
            this.allProducts = FXCollections.observableList(new ArrayList<>());
        }

        return this.allProducts;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Inventory() {
        var partsObsList = FXCollections.observableList(new ArrayList<Part>());
        partsObsList.addListener((ListChangeListener) change -> {});
        this.allParts = partsObsList;

        var productsObsList = FXCollections.observableList(new ArrayList<Product>());
        productsObsList.addListener((ListChangeListener) change -> {});
        this.allProducts = productsObsList;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Inventory getInstance() {
        if (inventory == null) {
            inventory = new Inventory();
        }
        return inventory;
    }

    /**
     * Gets current part id.
     *
     * @return the current part id
     */
    public Integer getCurrentPartId() {
        return currentPartId >= 1 ? currentPartId : 1;
    }

    /**
     * Gets current product id.
     *
     * @return the current product id
     */
    public Integer getCurrentProductId() {
        return currentProductId  >= 1 ? currentProductId : 1;
    }
}
