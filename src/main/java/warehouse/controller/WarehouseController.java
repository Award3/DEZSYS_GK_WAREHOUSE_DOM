package warehouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import warehouse.model.ProductData;
import warehouse.model.WarehouseData;
import warehouse.service.WarehouseService;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * POST /warehouse - Add a new warehouse location.
     */
    @PostMapping
    public ResponseEntity<WarehouseData> createWarehouse(@RequestBody WarehouseData warehouse) {
        WarehouseData created = warehouseService.createWarehouse(warehouse);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * GET /warehouse - Get all warehouse locations and their stock.
     * Returns each warehouse together with its products.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllWarehouses() {
        List<WarehouseData> warehouses = warehouseService.getAllWarehouses();
        List<Map<String, Object>> result = new java.util.ArrayList<>();

        for (WarehouseData wh : warehouses) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("warehouse", wh);
            entry.put("products", warehouseService.getProductsByWarehouseId(wh.getWarehouseID()));
            result.add(entry);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET /warehouse/{id} - Get a specific warehouse and its stock.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable String id) {
        Optional<WarehouseData> warehouse = warehouseService.getWarehouseById(id);
        if (warehouse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("warehouse", warehouse.get());
        result.put("products", warehouseService.getProductsByWarehouseId(id));
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /warehouse/{id} - Delete a warehouse location and all its products.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable String id) {
        boolean deleted = warehouseService.deleteWarehouse(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

