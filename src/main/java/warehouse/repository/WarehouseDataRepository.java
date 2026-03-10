package warehouse.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import warehouse.model.WarehouseData;

public interface WarehouseDataRepository extends MongoRepository<WarehouseData, String> {

    public Optional<WarehouseData> findByWarehouseID(String warehouseID);
    public void deleteByWarehouseID(String warehouseID);

}
