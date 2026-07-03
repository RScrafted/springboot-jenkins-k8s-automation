package com.rsinventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class InventoryController {

    @GetMapping("/inventory")
    public List<Product> getInventory() {

        return Arrays.asList(
                new Product(1, "RS Runner Shoes", 42, 59.99),
                new Product(2, "RS Classic Sneakers", 15, 79.99),
                new Product(3, "RS Street Boots", 8, 99.99),
                new Product(4, "RS Jacket", 21, 89.99)
        );
    }
}