# ArenaGT Lite

Programa de consola en Java para administrar una copa entre empresas con encuentros de fútbol sala, baloncesto y exhibiciones. Incluye registro de equipos, programación de eventos, validación de conflictos, registro de resultados, tabla de posiciones y calendario ordenado.

## Requisitos

- JDK 17 o posterior, con los comandos `java` y `javac` disponibles en la terminal.
- No requiere bibliotecas externas.

## Compilar el programa

Descargar o clonar el repositorio y abrir una terminal en la carpeta del proyecto, donde se encuentran `src` y este archivo `README.md`.

Ejecutar:

```powershell
javac -encoding UTF-8 -d out src/*.java
```

Este comando compila los archivos de `src` y genera los archivos compilados en `out`.

## Ejecutar el programa

Después de compilar, ejecutar:

```powershell
java -cp out Main
```

El programa carga cuatro equipos, cuatro encuentros y una exhibición. Luego muestra un menú para registrar equipos, crear eventos, programarlos o reprogramarlos, registrar resultados y consultar la tabla y el calendario.

Escribir el número de la opción deseada y presionar Enter. Las fechas se ingresan con formato `AAAA-MM-DD` y las horas como enteros entre `0` y `23`. Los eventos iniciales usan los identificadores del `1` al `5`; los nuevos deben tener identificadores positivos y únicos.

Para salir, seleccionar la opción `0`. Los datos se conservan únicamente en memoria y se pierden al cerrar el programa.

## Ejecutar las pruebas

Desde la terminal, después de compilar, ejecutar:

```powershell
java -cp out Pruebas
```

La ejecución correcta termina con el mensaje:

```text
38 casos aprobados y 0 fallos.
```

Para guardar la salida de las pruebas como evidencia:

```powershell
java -cp out Pruebas > evidencia_pruebas.tsv
```

Si el menú del programa está abierto, seleccionar primero `0` para salir y luego ejecutar los comandos en la terminal.
