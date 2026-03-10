package warehouse.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "warehouseData")
public class WarehouseData {

    @Id
    private String ID;

    private String warehouseID;
    private String warehouseName;
    private String warehouseAddress;
    private String warehouseCity;
    private String warehouseCountry;
    private String warehousePostalCode;
    private int warehouseCapacity;
    private String warehouseStatus; // ACTIVE, INACTIVE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor
     */
    public WarehouseData() {
    }

    public WarehouseData(String warehouseID, String warehouseName, String warehouseAddress,
                         String warehouseCity, String warehouseCountry, String warehousePostalCode,
                         int warehouseCapacity) {
        super();
        this.warehouseID = warehouseID;
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        this.warehouseCity = warehouseCity;
        this.warehouseCountry = warehouseCountry;
        this.warehousePostalCode = warehousePostalCode;
        this.warehouseCapacity = warehouseCapacity;
        this.warehouseStatus = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getWarehouseCity() {
        return warehouseCity;
    }

    public void setWarehouseCity(String warehouseCity) {
        this.warehouseCity = warehouseCity;
    }

    public String getWarehouseCountry() {
        return warehouseCountry;
    }

    public void setWarehouseCountry(String warehouseCountry) {
        this.warehouseCountry = warehouseCountry;
    }

    public String getWarehousePostalCode() {
        return warehousePostalCode;
    }

    public void setWarehousePostalCode(String warehousePostalCode) {
        this.warehousePostalCode = warehousePostalCode;
    }

    public int getWarehouseCapacity() {
        return warehouseCapacity;
    }

    public void setWarehouseCapacity(int warehouseCapacity) {
        this.warehouseCapacity = warehouseCapacity;
    }

    public String getWarehouseStatus() {
        return warehouseStatus;
    }

    public void setWarehouseStatus(String warehouseStatus) {
        this.warehouseStatus = warehouseStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Methods
     */
    @Override
    public String toString() {
        String info = String.format("Warehouse Info: WarehouseID = %s, Name = %s, City = %s, Country = %s, Capacity = %d, Status = %s",
            warehouseID, warehouseName, warehouseCity, warehouseCountry, warehouseCapacity, warehouseStatus);
        return info;
    }
}
