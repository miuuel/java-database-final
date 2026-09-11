package com.project.code.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity; import jakarta.persistence.FetchType; import jakarta.persistence.GeneratedValue; import jakarta.persistence.GenerationType; import jakarta.persistence.Id; import jakarta.persistence.OneToMany; import jakarta.persistence.Table; import jakarta.persistence.UniqueConstraint; import jakarta.validation.constraints.NotNull;

@Entity @Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku")) public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    @NotNull(message = "La categoría no puede ser nula")
    private String category;

    @NotNull(message = "El precio no puede ser nulo")
    private Double price;

    @NotNull(message = "El SKU no puede ser nulo")
    private String sku;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonManagedReference("inventory-product")
    private List inventory;

// Getters and Setters

    public Long getId ( ) {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName ( ) {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getCategory ( ) {
        return category;
    }

    public void setCategory (String category) {
        this.category = category;
    }

    public Double getPrice ( ) {
        return price;
    }

    public void setPrice (Double price) {
        this.price = price;
    }

    public String getSku ( ) {
        return sku;
    }

    public void setSku (String sku) {
        this.sku = sku;
    }

    public List getInventory ( ) {
        return inventory;
    }

    public void setInventory (List inventory) {
        this.inventory = inventory;
    }

// Constructors (if necessary) public Product() { }

    public Product (String name , String category , Double price , String sku) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.sku = sku;
    }

    @Override
    public String toString ( ) {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", inventory=" + inventory +
                '}';
    }
}

// public String toString() { return "Product{" + "id=" + id + ", name='" + name + ''' + ", category='" + category + ''' + ", price=" + price + ", sku='" + sku + ''' + '}'; } }

//public String toString() { return "Inventory{" + "id=" + id + ", product=" + (product != null ? product.getId() : "null") + ", store=" + (store != null ? store.getId() : "null") + ", stockLevel=" + stockLevel + '}'; } }

