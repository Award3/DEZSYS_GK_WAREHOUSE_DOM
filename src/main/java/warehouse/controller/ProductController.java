package warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import warehouse.model.ProductData;
import warehouse.service.WarehouseService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * POST /product - Add a new product and its stock to a warehouse location.
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductData product) {
        try {
            ProductData created = warehouseService.addProduct(product);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /product - Get all products/stock and their warehouse location.
     */
    @GetMapping
    public ResponseEntity<List<ProductData>> getAllProducts() {
        List<ProductData> products = warehouseService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /product/{id} - Get a specific product and its warehouse locations.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        ProductData product = warehouseService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    /**
     * DELETE /product/{id} - Delete a product from a warehouse location.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        boolean deleted = warehouseService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
