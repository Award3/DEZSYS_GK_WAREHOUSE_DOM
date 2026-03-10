# Warehouse Middleware - Dokumentenorientierte Middleware mit MongoDB

## 1. Projektbeschreibung

Dokumentenorientierte Middleware auf Basis von **Spring Boot 3.2** und **MongoDB**, die Lagerdaten über eine REST-Schnittstelle empfängt und im JSON-Format in MongoDB speichert. Die Anwendung unterstützt mehrere Lagerstandorte mit Produkten in verschiedenen Kategorien.

## 2. Technologie-Stack

- **Framework:** Spring Boot 3.2.5 (Java 17)
- **Datenbank:** MongoDB (Docker)
- **Build-Tool:** Gradle 8.7
- **REST:** Spring Web MVC
- **Datenzugriff:** Spring Data MongoDB

## 3. Setup & Installation

### 3.1 MongoDB mit Docker starten

```bash
docker pull mongo
docker run -d -p 27017:27017 --name mongo mongo
```

### 3.2 Anwendung bauen und starten

```bash
gradle clean bootRun
```

Beim Start werden automatisch 3 Lagerstandorte und 12 Produkte in 4 Kategorien in der DB angelegt.

### 3.3 Datengenerator (Erweiterte Anforderung)

Alternativ kann der Datengenerator die Daten über die REST-API einspeisen:

```bash
# Terminal 1: Anwendung starten
gradle bootRun

# Terminal 2: Generator starten (nach dem die Anwendung läuft)
gradle -PmainClass=warehouse.generator.DataGeneratorApp run
```

## 4. Datenstruktur

Es werden zwei separate MongoDB Collections verwendet:

### Collection: `warehouseData` (Lagerstandorte)

```json
{
  "_id": "ObjectId(...)",
  "warehouseID": "1",
  "warehouseName": "Hauptlager Wien",
  "warehouseAddress": "Industriestrasse 12",
  "warehouseCity": "Wien",
  "warehouseCountry": "Austria",
  "warehousePostalCode": "1100",
  "warehouseCapacity": 5000,
  "warehouseStatus": "ACTIVE",
  "createdAt": "2025-03-10T14:00:00",
  "updatedAt": "2025-03-10T14:30:00"
}
```

### Collection: `productData` (Produkte mit Lagerbestand)

```json
{
  "_id": "ObjectId(...)",
  "warehouseID": "1",
  "productID": "00-443175",
  "productName": "Bio Orangensaft Sonne",
  "productCategory": "Getraenk",
  "productQuantity": 2500.0,
  "lastUpdated": "2025-03-10T14:30:00"
}
```

### Produktkategorien (min. 3)

| Kategorie    | Anzahl | Beispiele                                    |
|-------------|--------|----------------------------------------------|
| Getraenk    | 4      | Bio Orangensaft, Bio Apfelsaft, Mineralwasser, Cola |
| Waschmittel | 3      | Ariel Color, Persil Gel, Lenor               |
| Tierfutter  | 3      | Mampfi Rind, Royal Canin, Whiskas            |
| Reinigung   | 2      | Staubsaugerbeutel, Glasreiniger              |

### Lagerstandorte (3)

| WarehouseID | Name                 | Stadt |
|-------------|---------------------|-------|
| 1           | Hauptlager Wien      | Wien  |
| 2           | Aussenlager Graz     | Graz  |
| 3           | Logistikzentrum Linz | Linz  |

## 5. REST API Dokumentation

### 5.1 Warehouse Endpoints

#### POST /warehouse

```bash
curl -X POST http://localhost:8080/warehouse \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseID": "4",
    "warehouseName": "Nebenlager Salzburg",
    "warehouseAddress": "Gewerbepark 5",
    "warehouseCity": "Salzburg",
    "warehouseCountry": "Austria",
    "warehousePostalCode": "5020",
    "warehouseCapacity": 2000
  }'
```

#### GET /warehouse — Alle Lagerstandorte und deren Lagerbestand

```bash
curl http://localhost:8080/warehouse
```

#### GET /warehouse/{id} — Einen Lagerstandort und dessen Lagerbestand

```bash
curl http://localhost:8080/warehouse/1
```

#### DELETE /warehouse/{id} — Lagerstandort löschen

```bash
curl -X DELETE http://localhost:8080/warehouse/1
```

### 5.2 Product Endpoints

#### POST /product — Produkt zu Lagerstandort hinzufügen

```bash
curl -X POST http://localhost:8080/product \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseID": "1",
    "productID": "04-111111",
    "productName": "Taschentuecher Tempo",
    "productCategory": "Hygiene",
    "productQuantity": 2000
  }'
```

#### GET /product — Alle Produkte und Lagerstandort

```bash
curl http://localhost:8080/product
```

#### GET /product/{id} — Ein Produkt und Lagerstandort

```bash
curl http://localhost:8080/product/00-871895
```

#### DELETE /product/{id} — Produkt löschen

```bash
curl -X DELETE http://localhost:8080/product/00-871895
```

## 6. MongoDB Shell Befehle (5 CRUD Operationen)

```bash
docker exec -it mongo bash
mongosh
use warehouse
```

### 6.1 CREATE — Produkt hinzufügen

```javascript
db.productData.insertOne({
  warehouseID: "1",
  productID: "04-999999",
  productName: "Küchenrolle Zewa",
  productCategory: "Hygiene",
  productQuantity: 650,
  lastUpdated: new Date(),
  _class: "warehouse.model.ProductData"
})
// Ergebnis: { acknowledged: true, insertedId: ObjectId("...") }
```

### 6.2 READ — Alle Produkte eines Lagers anzeigen

```javascript
db.productData.find({ warehouseID: "1" })
// Ergebnis: alle Produkte mit warehouseID "1"
```

### 6.3 UPDATE — Lagerbestand ändern

```javascript
db.productData.updateOne(
  { productID: "00-443175" },
  { $set: { productQuantity: 3000, lastUpdated: new Date() } }
)
// Ergebnis: { modifiedCount: 1 }
```

### 6.4 DELETE — Produkt entfernen

```javascript
db.productData.deleteOne({ productID: "04-999999" })
// Ergebnis: { deletedCount: 1 }
```

### 6.5 AGGREGATE — Gesamtbestand pro Kategorie

```javascript
db.productData.aggregate([
  { $group: {
      _id: "$productCategory",
      totalQuantity: { $sum: "$productQuantity" },
      productCount: { $sum: 1 }
  }},
  { $sort: { totalQuantity: -1 } }
])
// Ergebnis: [{ _id: "Reinigung", totalQuantity: 7840, ... }, { _id: "Getraenk", ... }, ...]
```

## 7. Fragestellungen

**Was ist der Unterschied zwischen einer dokumentenorientierten und einer relationalen Datenbank?**
Dokumentenorientierte DBs (MongoDB) speichern Daten als flexible JSON-Dokumente ohne festes Schema. Relationale DBs (PostgreSQL, MySQL) nutzen Tabellen mit Spalten und Fremdschlüssel-Beziehungen. Dokument-DBs eignen sich für flexible, sich ändernde Datenstrukturen und horizontale Skalierung.

**Warum zwei Collections statt Embedding?**
Die Trennung in `warehouseData` und `productData` entspricht dem bestehenden Code-Template mit eigenständigem `ProductData`-Modell und `WarehouseRepository`. Produkte referenzieren ihr Lager über `warehouseID`. Für sehr viele Produkte pro Lager (>1000) vermeidet dies das 16MB-Dokumentlimit von MongoDB.

**Wie wird die kontinuierliche Speicherung gewährleistet?**
Jedes Produkt hat einen `lastUpdated`-Timestamp, jedes Warehouse hat `createdAt` und `updatedAt`. Daten können jederzeit über die REST API eingespielt werden. Der Datengenerator simuliert das periodische Einspeisen von Daten.
