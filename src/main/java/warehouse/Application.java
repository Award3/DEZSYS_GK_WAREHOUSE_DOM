package warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import warehouse.model.ProductData;
import warehouse.model.WarehouseData;
import warehouse.repository.WarehouseDataRepository;
import warehouse.repository.WarehouseRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private WarehouseRepository repository;

    @Autowired
    private WarehouseDataRepository warehouseDataRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Initialize repositories
        repository.deleteAll();
        warehouseDataRepository.deleteAll();

        // ==================== Create Warehouse Locations ====================
        warehouseDataRepository.save(new WarehouseData("1", "Hauptlager Wien", "Industriestrasse 12", "Wien", "Austria", "1100", 5000));
        warehouseDataRepository.save(new WarehouseData("2", "Aussenlager Graz", "Lagergasse 45", "Graz", "Austria", "8010", 3000));
        warehouseDataRepository.save(new WarehouseData("3", "Logistikzentrum Linz", "Hafenstrasse 78", "Linz", "Austria", "4020", 4000));

        System.out.println();
        System.out.println("Warehouse locations created:");
        System.out.println("-------------------------------");
        for (WarehouseData wh : warehouseDataRepository.findAll()) {
            System.out.println(wh);
        }

        // ==================== Create Products (min. 10 in 3 categories) ====================

        // Kategorie: Getraenk (Warehouse 1)
        repository.save(new ProductData("1", "00-443175", "Bio Orangensaft Sonne", "Getraenk", 2500));
        repository.save(new ProductData("1", "00-871895", "Bio Apfelsaft Gold", "Getraenk", 3420));
        repository.save(new ProductData("1", "00-112233", "Mineralwasser Vöslauer", "Getraenk", 5000));
        repository.save(new ProductData("2", "00-445566", "Cola Zero 0.5L", "Getraenk", 1800));

        // Kategorie: Waschmittel (Warehouse 1 + 2)
        repository.save(new ProductData("1", "01-926885", "Ariel Waschmittel Color", "Waschmittel", 478));
        repository.save(new ProductData("2", "01-334455", "Persil Universal Gel", "Waschmittel", 620));
        repository.save(new ProductData("2", "01-667788", "Lenor Weichspüler", "Waschmittel", 340));

        // Kategorie: Tierfutter (Warehouse 1 + 3)
        repository.save(new ProductData("1", "02-234811", "Mampfi Katzenfutter Rind", "Tierfutter", 1324));
        repository.save(new ProductData("3", "02-556677", "Royal Canin Hundefutter", "Tierfutter", 890));
        repository.save(new ProductData("3", "02-889900", "Whiskas Katzenfutter Huhn", "Tierfutter", 1100));

        // Kategorie: Reinigung (Warehouse 2 + 3)
        repository.save(new ProductData("2", "03-893173", "Saugstauberbeutel Ingres", "Reinigung", 7390));
        repository.save(new ProductData("3", "03-112244", "Glasreiniger Ajax", "Reinigung", 450));

        System.out.println();

        // Fetch all products
        System.out.println("ProductData found with findAll():");
        System.out.println("-------------------------------");
        for (ProductData productdata : repository.findAll()) {
            System.out.println(productdata);
        }
        System.out.println();

        // Fetch single product
        System.out.println("Record(s) found with ProductID(\"00-871895\"):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByProductID("00-871895"));
        System.out.println();

        // Fetch all products of Warehouse 1
        System.out.println("Record(s) found with findByWarehouseID(\"1\"):");
        System.out.println("--------------------------------");
        for (ProductData productdata : repository.findByWarehouseID("1")) {
            System.out.println(productdata);
        }
        System.out.println();

        // Fetch all products of Warehouse 2
        System.out.println("Record(s) found with findByWarehouseID(\"2\"):");
        System.out.println("--------------------------------");
        for (ProductData productdata : repository.findByWarehouseID("2")) {
            System.out.println(productdata);
        }
        System.out.println();

        // Fetch all products of Warehouse 3
        System.out.println("Record(s) found with findByWarehouseID(\"3\"):");
        System.out.println("--------------------------------");
        for (ProductData productdata : repository.findByWarehouseID("3")) {
            System.out.println(productdata);
        }
    }
}
