package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Product product = request.getProduct();
        Inventory inventory = request.getInventory();

        Map<String, String> map = new HashMap<>();
        if (!serviceClass.ValidateProductId(product.getId())) {
            map.put("message", "Id " + product.getId() + " no presente en la base de datos");
            return map;
        }
        productRepository.save(product);
        map.put("message", "Producto actualizado con éxito con id: " + product.getId());

        if (inventory != null) {
            try {
                Inventory result = serviceClass.getInventoryId(inventory);
                if (result != null) {
                    inventory.setId(result.getId());
                    inventoryRepository.save(inventory);
                } else {
                    map.put("message", "No hay datos disponibles para este producto o id de tienda");
                    return map;
                }
            } catch (Exception e) {
                map.put("message", "Error: " + e.getMessage());
                return map;
            }
        }

        return map;
    }

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> map = new HashMap<>();
        try {
            if (serviceClass.validateInventory(inventory)) {
                inventoryRepository.save(inventory);
            } else {
                map.put("message", "Datos ya presentes en el inventario");
                return map;
            }
        } catch (Exception e) {
            map.put("message", "Error: " + e.getMessage());
            return map;
        }
        map.put("message", "Producto agregado al inventario con éxito");
        return map;
    }

    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeid) {
        Map<String, Object> map = new HashMap<>();
        List<Product> result = productRepository.findProductsByStoreId(storeid);
        map.put("products", result);
        return map;
    }

    // Cumple el requerimiento: GET /filter/{category}/{name}/{storeId} con lógica condicional
    @GetMapping("/filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable long storeId) {
        Map<String, Object> map = new HashMap<>();
        if ("null".equals(category)) {
            map.put("product", productRepository.findByNameLike(storeId, name));
            return map;
        } else if ("null".equals(name)) {
            map.put("product", productRepository.findByCategoryAndStoreId(storeId, category));
            return map;
        }
        map.put("product", productRepository.findByNameAndCategory(storeId, name, category));
        return map;
    }

    @GetMapping("/search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable long storeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("product", productRepository.findByNameLike(storeId, name));
        return map;
    }

    @Transactional
    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {
        Map<String, String> map = new HashMap<>();

        if (!serviceClass.ValidateProductId(id)) {
            map.put("message", "Id " + id + " no presente en la base de datos");
            return map;
        }
        inventoryRepository.deleteByProductId(id);
        map.put("message", "Producto eliminado con éxito con id: " + id);
        return map;
    }

    // Cumple el requerimiento: GET /validate/{quantity}/{storeId}/{productId}
    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable int quantity, @PathVariable long storeId, @PathVariable long productId) {
        // Corregido: 'And' con 'A' mayúscula
        Inventory result = inventoryRepository.findByProductIdAndStoreId(productId, storeId);

        // Corregido: Validación de nulos para evitar NullPointerException
        if (result != null && result.getStockLevel() >= quantity) {
            return true;
        }
        return false;
    }
}