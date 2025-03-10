# Control de Presencia 2025

## Descripción General

Este proyecto consiste en desarrollar una aplicación cliente-servidor para gestionar el registro de presencia de empleados en una empresa. El sistema ofrece dos entornos de uso diferenciados:

*   **Aplicación móvil Android:** Para que los empleados registren su entrada y salida del lugar de trabajo.
*   **Panel web para administración:** Para que un administrador gestione la información de la empresa y los empleados.

## Características Principales

*   **Registro de Presencia:** Los empleados pueden registrar su entrada y salida a través de una aplicación móvil.
*   **Geolocalización:** El registro de entrada y salida solo se permite si el empleado se encuentra geolocalizado en el lugar de trabajo.
*   **Panel Web de Administración:**
    *   Login exclusivo para administradores.
    *   Gestión de la información de la empresa.
    *   Alta, modificación y baja de empleados en el sistema.
*   **Base de Datos MySQL:** Almacenamiento de toda la información del sistema.
*   **API REST:** Interfaz para la comunicación entre la aplicación móvil y el servidor.

## Estructura del Proyecto

El proyecto se organiza en los siguientes archivos principales:

*   `README.md`: Este archivo (documentación general del proyecto).
*   `DOCUMENTACION_API.md`: Documentación del servidor (API REST y panel de administración).
*   `DOCUMENTACION_ANDROID.md`: Documentación de la aplicación cliente Android.
*   `DOCUMENTACION_WEB.md`: Documentación de la interfaz de usuario web (administración).

## Guía de Inicio Rápido

1.  **Clonar el repositorio:**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd ControlPresencia2025
    ```

2.  **Configurar el entorno del servidor (Python/Flask):**

    *   Sigue las instrucciones en [`DOCUMENTACION_API.md`](DOCUMENTACION_API.md) para configurar la base de datos, instalar las dependencias de Python y configurar el servidor Flask en PythonAnywhere.

3.  **Configurar el entorno de la aplicación Android:**

    *   Sigue las instrucciones en [`DOCUMENTACION_ANDROID.md`](DOCUMENTACION_ANDROID.md) para configurar el proyecto en Android Studio, añadir las dependencias necesarias y configurar el acceso a la API REST.

## Autores

*   [Tu Nombre]
*   [Otros Colaboradores (si los hay)]

## Licencia

[Especifica la licencia del proyecto, si la hay. Por ejemplo, MIT License, Apache License 2.0, etc.]

---

### DOCUMENTACION ADICIONAL

A continuación, encontrarás documentación más detallada sobre cada uno de los subproyectos:

*   **Servidor (API REST y Panel de Administración):** [DOCUMENTACION_API.md](DOCUMENTACION_API.md)
*   **Cliente Android:** [DOCUMENTACION_ANDROID.md](DOCUMENTACION_ANDROID.md)
*   **Interfaz de Usuario Web (Administración):** [DOCUMENTACION_WEB.md](DOCUMENTACION_WEB.md)
