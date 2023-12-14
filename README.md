<p align="center">
  <img src="https://i.imgur.com/oGIGzaN.png" alt="Imagen del proyecto Meteorology Program">
</p>

## Descripción
Programa desarrollado en Java con Gradle que lee los CSV de la carpeta data y procesa los datos en una base de datos.
Tras subir los datos del CSV a la base de datos H2, se realizan una serie de consultas utilizando la API Stream:

    - Cada día:
        - Lugar de temperatura máxima.
        - Lugar de temperatura mínima.
        - Lugar de precipitación máxima.
        - Precipitación máxima.

    - Agrupación por provincia y día:
        - Máxima temperatura.
        - Mínima temperatura.
        - Media de temperatura.
        - Precipitación máxima y lugar.
        - Precipitación media.
        - Lugares donde ha llovido.

    - Lugar donde más ha llovido.

    - Datos de una provincia (por cada día):
        - Temperatura máxima, mínima y dónde ha sido.
        - Temperatura media máxima.
        - Temperatura media mínima.
        - Precipitación máxima y dónde ha sido.
        - Precipitación media.


Para terminar, el programa exporta los datos de la base de datos de una provincia a un CSV.

## 💡 Instrucciones de uso
- ⚠ **.env:** Este fichero se deberá de crear con los siguientes datos:

        DATABASE_USER=usuario
        DATABASE_PASSWORD=contraseña
  Deberás de modificar el usuario y la contraseña que quieres que tenga la base de datos. La razón por la que el .env no se agrega al repositorio es por motivos de seguridad. Estos datos están aislados del database.properties.

- **database.properties:** Este fichero es el que se deberá modificar si se quiere cambiar la URL, el driver, el nombre de la base de datos o si se quiere forzar el reinicio de la tabla en el inicio del programa (eliminará y volverá a crear la tabla).

## ⚙ Tecnologías
- Java 17.
- Gradle.
- Lombok.
- H2 Database.
- dotenv-kotlin.
- Gson.
- MyBatis.
- Logback Classic.

## Estructura
- Adapters: Adaptadores para poder exportar los datos de fechas y tiempo correctamente.
- Controllers: El controlador de Meteorology. Aquí se realizan las consultas API Stream de Meteorology y también las consultas a la base de datos.
- Exceptions: Se almacenan todas las excepciones personalizadas del programa.
- Models: Clases POJO y DTO (para facilitar el proceso de transferencia de datos).
- Repositories: El manejo de consultas CRUD para interactuar con la base de datos.
- Services: Servicios de la aplicación. 
  - CRUD: Maneja la lógica de las consultas CRUD.
  - Database: Administrar la base de datos y sus conexiones.
  - IO: Entrada y salida de datos. el CSVManager administra el importado de los datos en CSV y el ExportManager la exportación a JSON.
- Utils: Clases de utilidad. En UtilParsers se encuentran todos los métodos de parseo. ApplicationProperties la clase que se encarga de cargar los archivos de propiedades (en este programa, database.properties).
- Main: Clase principal del programa.
- MeteorologyApp: La clase que se encarga de ejecutar toda la lógica del programa. Se le llama en el Main de la aplicación.

## Ejecución
<p align="center">
  <img src="https://i.imgur.com/pbLVCgH.gif" alt="Programa funcionando">
</p>

## Autores
- [Ángel (Madirex)](https://github.com/Madirex)
- [Rubén (Rubenoide03)](https://github.com/Rubenoide03)
