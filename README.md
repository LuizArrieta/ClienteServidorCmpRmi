# Conversión de Metros a Pies con RMI estándar de Java

Aplicación cliente-servidor que convierte metros a pies mediante **Java RMI estándar** (`java.rmi`). El cliente tiene una interfaz gráfica en Swing y el cálculo se realiza únicamente en el servidor.

**Autor:** Luis Camilo Arrieta Muriel
**Universidad:** Unicolombo
**Profesor:** Jhon Arrieta

---

## Estructura del proyecto

El repositorio contiene tres proyectos de NetBeans (Ant):

| Proyecto | Rol |
|---|---|
| `rmi-conversion-lib` | Contrato compartido: interfaz remota `IConversionRemota` y el DTO `DatosCmp` |
| `rmi-conversion-servidor` | Implementación del cálculo y publicación del objeto remoto en el registro RMI |
| `rmi-conversion-cliente` | Interfaz Swing que invoca el método remoto |

El paquete base es `luisarrieta.rmi.conversion` y el contrato compartido vive en `luisarrieta.rmi.conversion.lib`.

El cliente solo conoce el contrato (interfaz + DTO), nunca la fórmula de conversión.

## Cómo funciona

- `IConversionRemota` extiende `Remote` y su método `convertir(DatosCmp)` declara `throws RemoteException`.
- `DatosCmp` implementa `Serializable` para poder viajar por la red.
- `ConversionRemotaImplem` extiende `UnicastRemoteObject` y aplica la fórmula `metros × 3.28084`, validando que los metros no sean negativos.
- El servidor crea el registro con `LocateRegistry.createRegistry(9008)` y publica el objeto con `rebind("ConversionCmp", ...)`.
- El cliente obtiene el registro con `LocateRegistry.getRegistry(ip, puerto)`, busca el servicio con `lookup("ConversionCmp")` y llama al método remoto en un hilo aparte para no congelar la interfaz.

## Requisitos

- JDK 15 o superior
- Apache NetBeans (proyectos basados en Ant)

## Compilación

1. Abrir los tres proyectos en NetBeans.
2. Ejecutar **Clean and Build** sobre `rmi-conversion-lib`; se genera `dist/rmi-conversion-lib.jar`.
3. Copiar ese `.jar` a la carpeta `lib/` de `rmi-conversion-servidor` y de `rmi-conversion-cliente`.
4. Ejecutar **Clean and Build** sobre el servidor y sobre el cliente.

## Ejecución

1. Ejecutar primero `rmi-conversion-servidor`. En la consola aparece: `Servidor RMI disponible en el puerto 9008`. El servidor no tiene ventana.
2. Ejecutar `rmi-conversion-cliente`.
3. En la ventana, dejar `127.0.0.1` y el puerto `9008`, y pulsar **Conectar**.
4. Ingresar una longitud en metros y pulsar **Convertir a pies**.

## Pruebas simples

| Metros | Pies esperados |
|---|---|
| 1 | 3.28 |
| 10 | 32.80 |
| 50 | 164.04 |

Un valor negativo debe devolver un mensaje de error indicando que la longitud debe ser mayor o igual a 0.

## Tecnologías

Java, Java RMI, Swing, NetBeans (Ant).

## Licencia

Proyecto desarrollado con fines académicos.
