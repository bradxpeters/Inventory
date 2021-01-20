package main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import parts.Part;
import products.Product;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Inventory {
    private static Inventory inventory;

    private Integer currentPartId = 1;

    private Integer currentProductId = 1;

    private ObservableList<Part> allParts = null;

    private ObservableList<Product> allProducts = null;

    public static Inventory getInstance() {
        if (inventory == null) {
            inventory = new Inventory();
        }
        return inventory;
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

    public void addPart(Part newPart) {
        allParts.add(newPart);
        currentPartId++;
    }

    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        currentProductId++;
    }

    public Part lookupPart(int partId) {
        return Inventory
            .getInstance()
            .getAllParts()
            .stream()
            .filter(part -> part.getId() == partId)
            .findFirst()
            .orElse(null);
    }

    public Product lookupProduct(int productId) {
        return Inventory
            .getInstance()
            .getAllProducts()
            .stream()
            .filter(product -> product.getId() == productId)
            .findFirst()
            .orElse(null);
    }

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

    public void updatePart(int index, Part selectedPart) {

    }

    public void updateProduct(int index, Product newProduct) {

    }

    public boolean deletePart(Part selectedPart) {
        currentPartId--;
        return allParts.removeIf(p -> p.getId() == (selectedPart.getId()));
    }

    public boolean deleteProduct(Product selectedProduct) {
        currentPartId--;
        return allProducts.removeIf(p -> p.getId() == (selectedProduct.getId()));
    }

    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
    }

    public Integer getCurrentPartId() {
        return currentPartId;
    }

    public Integer getCurrentProductId() {
        return currentProductId;
    }
}
