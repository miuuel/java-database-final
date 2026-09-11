package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping("/store")
    public Map<String, String> addStore(@RequestBody Store store) {
        Store savedStore = storeRepository.save(store);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Tienda agregada con éxito con id " + savedStore.getId());
        return map;
    }

    // Cumple el requerimiento: GET (validate/store/{id})
    @GetMapping("validate/store/{id}")
    public boolean validateStore(@PathVariable Long id) {
        return storeRepository.existsById(id);
    }

    // Cumple el requerimiento: Bloque try-catch capturando Exception
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequest) {
        Map<String, String> map = new HashMap<>();
        try {
            orderService.saveOrder(placeOrderRequest);
            map.put("message", "Pedido realizado con éxito");
        } catch (Exception e) { // Cambiado de 'Error' a 'Exception'
            map.put("Error", e.getMessage());
        }
        return map;
    }
}