package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.code.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Hereda findAll() correctamente de JpaRepository<Product, Long>

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findBySku(String sku);

    Product findByName(String name);

    // Corregido: Se agregaron genéricos y anotaciones @Param
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Product> findByNameLike(@Param("storeId") long storeId, @Param("pname") String pname);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND i.product.category = :category")
    List<Product> findByNameAndCategory(@Param("storeId") long storeId, @Param("pname") String pname, @Param("category") String category);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    List<Product> findByCategoryAndStoreId(@Param("storeId") long storeId, @Param("category") String category);

    @Query("SELECT i FROM Product i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Product> findProductBySubName(@Param("pname") String pname);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId")
    List<Product> findProductsByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT i.product FROM Inventory i WHERE i.product.category = :category AND i.store.id = :storeId")
    List<Product> findProductByCategory(@Param("category") String category, @Param("storeId") long storeId);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND i.product.category = :category")
    List<Product> findProductBySubNameAndCategory(@Param("pname") String pname, @Param("category") String category);
}