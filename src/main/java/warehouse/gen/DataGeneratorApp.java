package warehouse.gen;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
public class DataGeneratorApp {

    private static final String BASE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        System.out.println("=== Warehouse Data Generator ===");
        System.out.println("Sending data to: " + BASE_URL);
        System.out.println();

        createWarehouses();
        createProducts();

        System.out.println();
        System.out.println("=== Data Generation Complete ===");
        System.out.println("Verify: curl http://localhost:8080/warehouse");
        System.out.println("Verify: curl http://localhost:8080/product");
    }

    private static void createWarehouses() {
        System.out.println("--- Creating Warehouse Locations ---");

        postJson("/warehouse", """
            {
                "warehouseID": "1",
                "warehouseName": "Hauptlager Wien",
                "warehouseAddress": "Industriestrasse 12",
                "warehouseCity": "Wien",
                "warehouseCountry": "Austria",
                "warehousePostalCode": "1100",
                "warehouseCapacity": 5000
            }
        """, "Warehouse 1 (Wien)");

        postJson("/warehouse", """
            {
                "warehouseID": "2",
                "warehouseName": "Aussenlager Graz",
                "warehouseAddress": "Lagergasse 45",
                "warehouseCity": "Graz",
                "warehouseCountry": "Austria",
                "warehousePostalCode": "8010",
                "warehouseCapacity": 3000
            }
        """, "Warehouse 2 (Graz)");

        postJson("/warehouse", """
            {
                "warehouseID": "3",
                "warehouseName": "Logistikzentrum Linz",
                "warehouseAddress": "Hafenstrasse 78",
                "warehouseCity": "Linz",
                "warehouseCountry": "Austria",
                "warehousePostalCode": "4020",
                "warehouseCapacity": 4000
            }
        """, "Warehouse 3 (Linz)");
    }

    private static void createProducts() {
        System.out.println("--- Creating Products (12 products, 4 categories) ---");

        // Kategorie: Getraenk
        postProduct("1", "00-443175", "Bio Orangensaft Sonne", "Getraenk", 2500);
        postProduct("1", "00-871895", "Bio Apfelsaft Gold", "Getraenk", 3420);
        postProduct("1", "00-112233", "Mineralwasser Voeslauer", "Getraenk", 5000);
        postProduct("2", "00-445566", "Cola Zero 0.5L", "Getraenk", 1800);

        // Kategorie: Waschmittel
        postProduct("1", "01-926885", "Ariel Waschmittel Color", "Waschmittel", 478);
        postProduct("2", "01-334455", "Persil Universal Gel", "Waschmittel", 620);
        postProduct("2", "01-667788", "Lenor Weichspueler", "Waschmittel", 340);

        // Kategorie: Tierfutter
        postProduct("1", "02-234811", "Mampfi Katzenfutter Rind", "Tierfutter", 1324);
        postProduct("3", "02-556677", "Royal Canin Hundefutter", "Tierfutter", 890);
        postProduct("3", "02-889900", "Whiskas Katzenfutter Huhn", "Tierfutter", 1100);

        // Kategorie: Reinigung
        postProduct("2", "03-893173", "Saugstauberbeutel Ingres", "Reinigung", 7390);
        postProduct("3", "03-112244", "Glasreiniger Ajax", "Reinigung", 450);
    }

    private static void postProduct(String warehouseID, String productID, String name,
                                     String category, double quantity) {
        String json = String.format("""
            {
                "warehouseID": "%s",
                "productID": "%s",
                "productName": "%s",
                "productCategory": "%s",
                "productQuantity": %.1f
            }
        """, warehouseID, productID, name, category, quantity);

        postJson("/product", json, productID + " -> Warehouse " + warehouseID);
    }

    private static void postJson(String path, String json, String label) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            restTemplate.postForEntity(BASE_URL + path, entity, String.class);
            System.out.println("  OK: " + label);
        } catch (Exception e) {
            System.err.println("  FAIL: " + label + " - " + e.getMessage());
        }
    }
}
