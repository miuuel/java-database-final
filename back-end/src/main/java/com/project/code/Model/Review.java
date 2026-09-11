package com.project.code.Model;

import org.springframework.data.annotation.Id; import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "reviews") public class Review {

    @Id private String id; // MongoDB usa String para el campo ID

    @NotNull(message = "El cliente no puede ser nulo") private Long customerId; // El cliente que creó la reseña

    @NotNull(message = "El producto no puede ser nulo") private Long productId; // El producto que se está reseñando

    @NotNull(message = "La tienda no puede ser nula") private Long storeId; // La tienda asociada con el producto

    @NotNull(message = "La calificación no puede ser nula") private Integer rating; // Calificación de 1 a 5

    private String comment; // Comentario opcional sobre el producto

// Constructors public Review() { }

    public Review(Long customerId, Long productId, Long storeId, Integer rating, String comment) { this.customerId = customerId; this.productId = productId; this.storeId = storeId; this.rating = rating; this.comment = comment; }

// Getters and Setters public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Long getCustomerId() { return customerId; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public Long getStoreId() { return storeId; }

    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Integer getRating() { return rating; }

    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

}