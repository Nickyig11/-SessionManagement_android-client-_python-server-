# Aplicación Cliente Android

[Volver a la documentación principal](README.md)

## Descripción

Este subproyecto contiene el código de la aplicación cliente desarrollada para Android. La aplicación permite a los empleados registrar su entrada y salida del lugar de trabajo.

## Estructura del Proyecto

*   `MainActivity.java`: Actividad principal (login).
*   `RegistroActivity.java`: Actividad para registrar entradas y salidas.
*   `models/`: Clases de modelo (User, etc.).
*   `api/`: Clases para interactuar con la API REST (ApiInterface, ApiClient).
*   `utils/`: Clases de utilidad (GpsUtils, etc.).
*   `res/`: Recursos de la aplicación (layouts, imágenes, etc.).

## Configuración

1.  **Abre el Proyecto en Android Studio:**
    *   Importa el proyecto existente desde el directorio `androidApp`.

2.  **Añade las Dependencias Necesarias:**

    *   Asegúrate de que tienes las siguientes dependencias en el archivo `build.gradle` (Module: app):

        ```gradle
        dependencies {
            implementation 'com.squareup.retrofit2:retrofit:2.9.0'
            implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
            implementation 'com.google.code.gson:gson:2.9.0'
            // Otras dependencias
        }
        ```

3.  **Configura el Acceso a la API REST:**

    *   Modifica la URL base en la clase `ApiClient.java` para que apunte a la URL de tu servidor Flask en PythonAnywhere.

## Funcionamiento

[Añade una explicación detallada del flujo de la aplicación, cómo se manejan los permisos, etc.]

## Puntos Clave del Código

[Describe los aspectos más importantes del código, las decisiones de diseño, etc.]
