# Laboratorio MongoDB — Despliegue Cloud

Proyecto basado en: https://github.com/gpedrazag/Lab-DespliegueCloud.git

## 1. Requisitos previos

1. MongoDB instalado localmente y corriendo en `localhost:27017`.
2. En la consola `mongo.exe`:
   ```
   use ArquiSW
   db
   ```
3. JDK 8, Maven 3 y NetBeans 8.0.2 (o superior).

## 2. Ejecución

```
mvn clean package
java -cp target/classes:target/dependency/* com.example.Main
```

Por defecto Jetty levanta en `http://localhost:8080`.

## 3. Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/competitors/add` | Crea competidor (con vehículo y producto opcionales en el body) |
| POST | `/competitors/vehicle` | Crea competidor con vehículo |
| GET  | `/competitors/` | Lista todos los competidores (ordenados por surname ASC) |
| GET  | `/competitors/name/{name}` | Competidores filtrados por nombre exacto |
| GET  | `/competitors/productos` | **Actividad 3** — Lista todos los productos almacenados |
| GET  | `/competitors/startsWithA` | **Actividad 4** — Competidores cuyo nombre empieza por 'A' |

### Ejemplo de body (POST `/competitors/add`)

```json
{
  "name": "Andres",
  "surname": "Gomez",
  "address": "Calle 42 #18",
  "age": 20,
  "telephone": "7659675",
  "cellphone": "3002345436",
  "city": "Bogota",
  "country": "Colombia",
  "vehicle": {
    "plate": "TRE-465",
    "brand": "BMW",
    "modelYear": 2020
  },
  "producto": {
    "nombre": "Laptop",
    "marca": "Dell",
    "fechaCompra": "2026-01-15"
  }
}
```

## 4. Verificación en Mongo shell

```
db.COMPETITOR.find()
```

## 5. Cambios realizados vs. código base

1. `pom.xml` — agregadas dependencias `org.eclipse.persistence.nosql`, `mongo-java-driver 2.12.3` y `eclipselink 2.5.0-RC1` (con exclusión de `commonj.sdo`). Se dejó una sola dependencia de eclipselink.
2. `persistence.xml` — unidad renombrada a `mongoPU`, target database `MongoPlatform`, host/puerto/db configurados.
3. `PersistenceManager.java` — ahora crea el `EntityManagerFactory` con `"mongoPU"`.
4. `Competitor.java` — `@NoSql(dataFormat=MAPPED)`, id `String` con `@Field("_id")`, embebe `Vehicle` y `Producto`.
5. `Vehicle.java` — nueva clase `@Embeddable @NoSql` con `plate`, `brand`, `modelYear`.
6. `Producto.java` — nueva clase `@Embeddable @NoSql` con `nombre`, `marca`, `fechaCompra` (Actividad 1).
7. `CompetitorDTO.java` — incluye `vehicle` y `producto`.
8. `CompetitorService.java` — endpoints `/add`, `/vehicle`, `/name/{name}`, `/productos`, `/startsWithA`.
