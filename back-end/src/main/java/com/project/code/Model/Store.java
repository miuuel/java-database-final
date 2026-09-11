package com.project.code.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity; import jakarta.persistence.FetchType; import jakarta.persistence.GeneratedValue; import jakarta.persistence.GenerationType; import jakarta.persistence.Id; import jakarta.persistence.OneToMany; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.NotNull;

@Entity public class Store { @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @NotNull(message = "El nombre no puede ser nulo") @NotBlank(message = "El nombre no puede estar en blanco") private String name; @NotNull(message = "La dirección no puede ser nula") @NotBlank(message = "La dirección no puede estar en blanco") private String address;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER) @JsonManagedReference("inventory-store") private List inventory;

// Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public List getInventory() { return inventory; }

    public void setInventory(List inventory) { this.inventory = inventory; }

// Constructors (if necessary) public Store() { }

    public Store(String name, String address) { this.name = name; this.address = address; } }