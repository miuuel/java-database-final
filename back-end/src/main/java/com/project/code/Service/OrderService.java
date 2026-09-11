package com.project.code.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional // Garantiza que si algo falla, se reviertan todos los cambios (Rollback)
    public void saveOrder (PlaceOrderRequestDTO placeOrderRequest) {

        // 1. Recuperar o crear el Cliente
        Customer existingCustomer = customerRepository.findByEmail ( placeOrderRequest.getCustomerEmail () );
        Customer customer;

        if (existingCustomer == null) {
            customer = new Customer ( existingCustomer.getName () , existingCustomer.getEmail () , existingCustomer.getPhone () );
            customer.setName ( placeOrderRequest.getCustomerName () );
            customer.setEmail ( placeOrderRequest.getCustomerEmail () );
            customer.setPhone ( placeOrderRequest.getCustomerPhone () );
            customer = customerRepository.save ( customer );
        } else {
            customer = existingCustomer;
        }

        // 2. Recuperar la Tienda
        Store store = storeRepository.findById ( placeOrderRequest.getStoreId () )
                .orElseThrow ( ( ) -> new RuntimeException ( "Tienda no encontrada con ID: " + placeOrderRequest.getStoreId () ) );

        // 3. Crear OrderDetails
        OrderDetails orderDetails = new OrderDetails ();
        orderDetails.setCustomer ( customer );
        orderDetails.setStore ( store );
        orderDetails.setTotalPrice ( placeOrderRequest.getTotalPrice () );
        orderDetails.setDate ( LocalDateTime.now () );

        orderDetails = orderDetailsRepository.save ( orderDetails );

        // 4. Crear y guardar OrderItems (productos comprados)
        // Corregido: Se especifica el tipo genérico <PurchaseProductDTO>
        List<PurchaseProductDTO> purchaseProducts = placeOrderRequest.getPurchaseProduct ();

        for ( PurchaseProductDTO productDTO : purchaseProducts ) {
            // Validar que el producto exista
            Product product = productRepository.findById ( productDTO.getId () )
                    .orElseThrow ( ( ) -> new RuntimeException ( "Producto no encontrado con ID: " + productDTO.getId () ) );

            // Corregido: 'And' con A mayúscula + validación de existencia
            Inventory inventory = inventoryRepository.findByProductIdandStoreId ( productDTO.getId () , placeOrderRequest.getStoreId () );

            if (inventory == null) {
                throw new RuntimeException ( "No hay inventario registrado para el producto ID: " + productDTO.getId () );
            }

            if (inventory.getStockLevel () < productDTO.getQuantity ()) {
                throw new RuntimeException ( "Stock insuficiente para el producto: " + product.getName () );
            }

            // Descontar inventario
            inventory.setStockLevel ( inventory.getStockLevel () - productDTO.getQuantity () );
            inventoryRepository.save ( inventory );

            // Crear ítem del pedido
            //OrderItem(OrderDetails order, Product product, Integer quantity, Double price)
            OrderItem orderItem = new OrderItem ();
            orderItem.setOrder ( orderDetails );
            orderItem.setProduct ( product );
            orderItem.setQuantity ( productDTO.getQuantity () );
            orderItem.setPrice ( productDTO.getPrice () * productDTO.getQuantity () );

            orderItemRepository.save ( orderItem );
        }
    }
}