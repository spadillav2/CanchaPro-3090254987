# CanchaPro - Sistema de Gestión de Complejos Deportivos

Sistema de escritorio desarrollado en Java (POO) para la administración integral de reservas, disponibilidad de canchas, pagos, reportería analítica y control opcional de tienda para complejos deportivos.

---

## 1. Requisitos del Sistema

* **Java Runtime Environment (JRE) / JDK:** Versión 17 o superior (compilado en Java 26).
* **Sistema Operativo:** Windows, Linux o macOS con soporte gráfico (Java Swing).
* **Dependencias Externas (incluidas):** `jcalendar-1.4.jar` y `AbsoluteLayout`.

---

## 2. Estructura de la Carpeta de Entrega

Para garantizar el correcto funcionamiento del ejecutable en cualquier equipo sin necesidad de abrir un IDE, la carpeta de distribución debe mantener exactamente esta estructura al mismo nivel:

```text
CanchaPro-Entrega/
├── CanchaPro-SandraPadilla.jar   # Ejecutable principal de la aplicación
├── lib/                          # Carpeta obligatoria con librerías externas
│   └── jcalendar-1.4.jar
├── clientes.csv                  # Archivos de persistencia de datos (o .txt según configuración)
├── canchas.csv
├── tarifas.csv
├── servicios.csv
├── reservas.csv
├── pagos.csv
├── productos.csv                 # Datos del módulo de tienda
├── ventas.csv
└── config.properties             # Archivo de configuración modular

Importante: No mueva el archivo CanchaPro-SandraPadilla.jar fuera de esta carpeta ni elimine la subcarpeta lib/, ya que el programa requiere ambas rutas relativas para cargar el selector de fechas (JDateChooser).

## 3. Instrucciones de Ejecución
Opción A: Ejecución directa (Recomendada)
Asegúrese de tener instalado Java en el equipo.

Haga doble clic sobre el archivo CanchaPro-SandraPadilla.jar.

Opción B: Ejecución desde consola / terminal
Si el sistema operativo no asocia los archivos .jar con Java automáticamente:

Abra una terminal o símbolo del sistema (CMD) dentro de la carpeta CanchaPro-Entrega/.

Ejecute el siguiente comando:

java -jar CanchaPro-SandraPadilla.jar

4. Restauración, Respaldo y Reinicio de Datos
El sistema utiliza persistencia local en archivos planos estructurados (sin gestor de base de datos externo), reconstruyendo los objetos en memoria al iniciar.

* Cómo realizar un respaldo (Backup)
Cierre completamente la aplicación CanchaPro para asegurar que todos los flujos de escritura en disco hayan finalizado.
Copie todos los archivos de datos (.csv / .txt) ubicados junto al .jar y guárdelos en una carpeta segura (por ejemplo, Respaldo_Fecha/).
* Cómo restaurar datos previos.
Cierre la aplicación si está en ejecución.
Copie los archivos respaldados y péguelos en la carpeta raíz junto a CanchaPro-SandraPadilla.jar, reemplazando los actuales.
Inicie nuevamente el programa; el paquete canchaproArchivos cargará automáticamente los registros restaurados.
* Cómo iniciar el sistema desde cero (Datos limpios)
* Si desea probar el sistema sin registros previos o si algún archivo de datos fue eliminado accidentalmente:
Elimine o mueva los archivos de datos existentes fuera de la carpeta del ejecutable
.Ejecute CanchaPro-SandraPadilla.jar.
El sistema detectará la ausencia de los archivos y los regenerará automáticamente vacíos para evitar errores de lectura (FileNotFoundException).

5. Configuración del Módulo de Tienda
El módulo de tienda opera de forma desacoplada del núcleo de reservas. Para habilitarlo o deshabilitarlo sin recompilar el código:
Abra el archivo de configuración (config.properties o configuracion.txt) con el Bloc de notas
.Modifique el parámetro correspondiente (modulo_tienda=true para activar / modulo_tienda=false para desactivar).
Guarde los cambios y reinicie la aplicación.

6. Arquitectura del Proyectocanchapro.interfaces:
 Vistas gráficas en Java Swing (Login, Principal y paneles dinámicos).canchapro.codigo:
 Modelos de dominio (Cliente, Cancha, Reserva, Pago, Tarifa, ServicioAdicional) y clases gestoras con reglas de negocio y arreglos estáticos[cite: 2, 3].
canchaproArchivos: Capa de persistencia encargada de la lectura, validación y escritura de archivos locales. 
