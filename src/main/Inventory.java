package main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import parts.Part;
import products.Product;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The type Inventory.
 */
public class Inventory {


    public static ObservableList<Part> allParts;

    public static ObservableList<Product> allProducts;

    public static Integer currentPartId = 1;

    public static Integer currentProductId = 1;

    private static Inventory inventory;

    /**
     * Add part.
     *
     * @param newPart the new part
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
        currentPartId++;
    }

    /**
     * Add product.
     *
     * @param newProduct the new product
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        currentProductId++;
    }

    /**
     * Lookup part part.
     *
     * @param partId the part id
     * @return the part
     */
    public static Part lookupPart(int partId) {
        return getAllParts()
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
    public static Product lookupProduct(int productId) {
        return getAllProducts()
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
    public static ObservableList<Part> lookupPart (String partName) {
        if (partName != null && !partName.equals("")) {
            return getAllParts()
                .stream()
                .filter(part -> part.getName().contains(partName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else {
           return getAllParts();
        }
    }

    /**
     * Lookup product observable list.
     *
     * @param productName the product name
     * @return the observable list
     */
    public static ObservableList<Product> lookupProduct (String productName) {
        if (productName != null && !productName.equals("")) {
            return getAllProducts()
                .stream()
                .filter(part -> part.getName().contains(productName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else {
            return getAllProducts();
        }
    }

    /**
     * Update part.
     *
     * @param index        the index
     * @param selectedPart the selected part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index,selectedPart);
    }

    /**
     * Update product.
     *
     * @param index      the index
     * @param newProduct the new product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index,newProduct);
    }

    /**
     * Delete part boolean.
     *
     * @param selectedPart the selected part
     * @return the boolean
     */
    public static boolean deletePart(Part selectedPart) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "
                + selectedPart.getName() + "?");
        alert.setHeaderText("Confirmation");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            currentPartId--;
            return allParts.removeIf(p -> p.getId() == (selectedPart.getId()));
        }

        return false;
    }

    /**
     * Delete product boolean.
     * Only allow deletion if there are no associated parts.
     *
     * @param selectedProduct the selected product
     * @return the boolean
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (selectedProduct.getAllAssociatedParts().size() > 0) {
            var alert = new Alert(Alert.AlertType.ERROR, "Cannot delete product, it has associated parts!");
            alert.setHeaderText("Deletion error!");
            alert.showAndWait();
            return false;
        }

        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "
            + selectedProduct.getName() + "?");
        alert.setHeaderText("Confirmation");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            currentProductId--;
            return allProducts.removeIf(p -> p.getId() == (selectedProduct.getId()));
        }

        return false;
    }

    /**
     * Gets all parts.
     *
     * @return the all parts
     */
    public static ObservableList<Part> getAllParts() {
        if (allParts == null) {
            allParts = FXCollections.observableList(new ArrayList<>());
        }

        return allParts;
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public static ObservableList<Product> getAllProducts() {
        if (allProducts == null) {
            allProducts = FXCollections.observableList(new ArrayList<>());
        }

        return allProducts;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Inventory() {
        var partsObsList = FXCollections.observableList(new ArrayList<Part>());
        partsObsList.addListener((ListChangeListener) change -> {});
        allParts = partsObsList;

        var productsObsList = FXCollections.observableList(new ArrayList<Product>());
        productsObsList.addListener((ListChangeListener) change -> {});
        allProducts = productsObsList;
    }
}
