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

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> map = new HashMap<>();
        if (!serviceClass.validateProduct(product)) {
            map.put("message", "El producto ya está presente en la base de datos");
            return map;
        }
        try {
            productRepository.save(product);
            map.put("message", "Producto agregado con éxito");
        } catch (DataIntegrityViolationException e) {
            map.put("message", "SKU debe ser único");
        }
        return map;
    }

    // Cumple el requerimiento: GET /product/{id}
    @GetMapping("/{id}")
    public Map<String, Object> getProductbyId(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        // Corregido: findById() nativo de Spring Data JPA
        Product result = productRepository.findById(id).orElse(null);
        map.put("products", result);
        return map;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> map = new HashMap<>();
        try {
            productRepository.save(product);
            map.put("message", "Datos actualizados con éxito");
        } catch (Exception e) { // Cambiado de Error a Exception
            map.put("message", "Ocurrió un error");
        }
        return map;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable String name, @PathVariable String category) {
        Map<String, Object> map = new HashMap<>();
        if ("null".equals(name)) {
            map.put("products", productRepository.findByCategory(category));
            return map;
        } else if ("null".equals(category)) {
            map.put("products", productRepository.findProductBySubName(name));
            return map;
        }
        map.put("products", productRepository.findProductBySubNameAndCategory(name, category));
        return map;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> map = new HashMap<>();
        map.put("products", productRepository.findAll());
        return map;
    }

    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable String category, @PathVariable long storeid) {
        Map<String, Object> map = new HashMap<>();
        List<Product> result = productRepository.findProductByCategory(category, storeid);
        map.put("product", result);
        return map;
    }

    // Cumple el requerimiento: DELETE /product/{id} eliminando producto e inventario
    @Transactional
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {
        Map<String, String> map = new HashMap<>();

        if (!serviceClass.ValidateProductId(id)) {
            map.put("message", "Id " + id + " no presente en la base de datos");
            return map;
        }
        inventoryRepository.deleteByProductId(id);
        orderItemRepository.deleteByProductId(id);
        productRepository.deleteById(id);

        map.put("message", "Producto eliminado con éxito con id: " + id);
        return map;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("products", productRepository.findProductBySubName(name));
        return map;
    }
}