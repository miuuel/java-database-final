package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.code.Model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Corregido: Se agregaron las anotaciones @Param y CamelCase en 'And'
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.store.id = :storeId")
    Inventory findByProductIdAndStoreId(@Param("productId") Long productId, @Param("storeId") Long storeId);

    // Corregido: Se especificó el tipo genérico <Inventory>
    List<Inventory> findByStore_Id(Long storeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Inventory i WHERE i.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

}