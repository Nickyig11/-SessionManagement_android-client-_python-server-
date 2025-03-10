# Aplicación Cliente Android

[Volver a la documentación principal](README.md)

## Descripción

Este subproyecto contiene el código de la aplicación cliente desarrollada para Android. La aplicación permite a los empleados registrar su entrada y salida del lugar de trabajo, utilizando la API REST provista por el servidor Python/Flask.

## Estructura del Proyecto

Aquí se describe la estructura del proyecto Android, con los paquetes y clases principales:

*   `MainActivity.java`: Actividad principal, que se encarga del inicio de sesión del usuario.
*   `RegistroActivity.java`: Actividad para registrar entradas y salidas del usuario, incluyendo la verificación de la ubicación.
*   `api/`:
    *   `ApiClient.java`: Cliente de Retrofit para interactuar con la API REST del servidor. Define la URL base del servidor.
    *   `ApiInterface.java`: Interfaz que define los endpoints de la API REST (login, comprobar ubicación, registrar entrada, registrar salida). Utiliza anotaciones de Retrofit para mapear las llamadas a la API a métodos Java.
*   `models/`:
    *   `User.java`: Clase de modelo que representa a un usuario (nombre de usuario, contraseña, etc.).  Esta clase se utiliza para serializar/deserializar objetos JSON desde/hacia la API.
    *   `SessionManager.java`: Clase para gestionar la sesión del usuario (guardar el estado del login, obtener datos del usuario, etc.).  Utiliza SharedPreferences para almacenar la información de la sesión de forma persistente.
*   `utils/`:
    *   `GpsUtils.java`: Clase de utilidad para obtener la ubicación del dispositivo utilizando los servicios de GPS de Android.  Gestiona los permisos de ubicación y proporciona métodos para obtener la latitud y longitud del dispositivo.
*   `res/`:
    *   `layout/`:
        *   `activity_main.xml`: Layout para la actividad de inicio de sesión (MainActivity).
        *   `activity_registro.xml`: Layout para la actividad de registro de entradas y salidas (RegistroActivity).
    *   `drawable/`:  Recursos de imagen utilizados en la aplicación.

## Configuración

1.  **Abrir el Proyecto en Android Studio:**
    *   Importa el proyecto existente desde el directorio `androidApp`.

2.  **Añadir las Dependencias Necesarias:**

    *   Las dependencias del proyecto (Retrofit, Gson, Location services, etc.) se especifican en el archivo `libs.versions.toml`. Este archivo se utiliza para definir las versiones de las dependencias y luego se referencian en el archivo `build.gradle.kts` del módulo `app`.

3.  **Configurar el Acceso a la API REST:**

    *   Modifica la URL base en la clase `ApiClient.java` (o donde la tengas definida) para que apunte a la URL de tu servidor Flask en PythonAnywhere. Ejemplo:

        ```java
        public class ApiClient {
            private static final String BASE_URL = "https://nickynicky23.eu.pythonanywhere.com/"; 
        }
        ```

## Funcionamiento

El flujo de la aplicación es el siguiente:

1.  **Inicio de Sesión:**
    *   El usuario abre la aplicación y se muestra la actividad `MainActivity`.
    *   El usuario introduce su nombre de usuario y contraseña.
    *   La aplicación envía una solicitud POST al endpoint `/login` del servidor (utilizando Retrofit).
    *   Si las credenciales son correctas, el servidor devuelve un `user_id`.
    *   La aplicación guarda el `user_id` en la sesión utilizando `SessionManager`.
2.  **Registro de Entradas y Salidas:**
    *   Si el inicio de sesión es exitoso, la aplicación muestra la actividad `RegistroActivity`.
    *   `RegistroActivity` utiliza `GpsUtils` para obtener la ubicación actual del dispositivo.
    *   La aplicación envía una solicitud POST al endpoint `/comprobar_ubicacion` del servidor (utilizando Retrofit) con la latitud y longitud.
    *   El servidor verifica si la ubicación está dentro del rango permitido.
    *   Si la ubicación es permitida, el usuario puede registrar su entrada o salida.
    *   Al registrar la entrada o salida, la aplicación envía una solicitud POST al endpoint `/registro_entrada` o `/registro_salida` del servidor (utilizando Retrofit) con la latitud, longitud y nombre de usuario.
    *   El servidor registra la entrada o salida en la base de datos.
3.  **Permisos:**
    *   La aplicación requiere permisos de ubicación (ACCESS_FINE_LOCATION y ACCESS_COARSE_LOCATION).
    *   `GpsUtils` se encarga de solicitar los permisos de ubicación al usuario si no están concedidos.
    *   Es importante manejar los permisos de ubicación correctamente para evitar que la aplicación falle si el usuario no concede los permisos.
