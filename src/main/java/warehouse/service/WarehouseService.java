package warehouse.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import warehouse.model.ProductData;
import warehouse.model.WarehouseData;
import warehouse.repository.WarehouseDataRepository;
import warehouse.repository.WarehouseRepository;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository productRepository;

    @Autowired
    private WarehouseDataRepository warehouseRepository;

    // ==================== Warehouse Operations ====================

    /**
     * Create a new warehouse location.
     */
    public WarehouseData createWarehouse(WarehouseData warehouse) {
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        if (warehouse.getWarehouseStatus() == null) {
            warehouse.setWarehouseStatus("ACTIVE");
        }
        return warehouseRepository.save(warehouse);
    }

    /**
     * Get all warehouses. For each warehouse, also include its products.
     */
    public List<WarehouseData> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    /**
     * Get a single warehouse by warehouseID.
     */
    public Optional<WarehouseData> getWarehouseById(String warehouseID) {
        return warehouseRepository.findByWarehouseID(warehouseID);
    }

    /**
     * Get all products belonging to a warehouse.
     */
    public List<ProductData> getProductsByWarehouseId(String warehouseID) {
        return productRepository.findByWarehouseID(warehouseID);
    }

    /**
     * Delete a warehouse and all its products.
     */
    @Transactional
    public boolean deleteWarehouse(String warehouseID) {
        Optional<WarehouseData> warehouse = warehouseRepository.findByWarehouseID(warehouseID);
        if (warehouse.isPresent()) {
            // Delete all products of this warehouse
            List<ProductData> products = productRepository.findByWarehouseID(warehouseID);
            productRepository.deleteAll(products);
            // Delete the warehouse itself
            warehouseRepository.delete(warehouse.get());
            return true;
        }
        return false;
    }

    // ==================== Product Operations ====================

    /**
     * Add a new product to a warehouse.
     */
    public ProductData addProduct(ProductData product) {
        // Check if warehouse exists
        Optional<WarehouseData> warehouse = warehouseRepository.findByWarehouseID(product.getWarehouseID());
        if (warehouse.isEmpty()) {
            throw new IllegalArgumentException("Warehouse not found: " + product.getWarehouseID());
        }

        product.setLastUpdated(LocalDateTime.now());

        // Update warehouse timestamp
        WarehouseData wh = warehouse.get();
        wh.setUpdatedAt(LocalDateTime.now());
        warehouseRepository.save(wh);

        return productRepository.save(product);
    }

    /**
     * Get all products across all warehouses.
     */
    public List<ProductData> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get a specific product by productID (can exist in multiple warehouses).
     */
    public ProductData getProductById(String productID) {
        return productRepository.findByProductID(productID);
    }

    /**
     * Delete a product by productID.
     */
    @Transactional
    public boolean deleteProduct(String productID) {
        ProductData product = productRepository.findByProductID(productID);
        if (product != null) {
            productRepository.delete(product);
            return true;
        }
        return false;
    }
}
