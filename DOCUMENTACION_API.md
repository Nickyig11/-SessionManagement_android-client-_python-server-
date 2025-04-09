# API REST y Panel de Administración (Servidor Python/Flask)
[Volver a la documentación principal](README.md)

## Descripción

Este subproyecto contiene el código del servidor, que se encarga de:

*   Proporcionar una API REST para que la aplicación Android registre entradas y salidas de empleados.
*   Gestionar el panel de administración web para que los administradores puedan gestionar usuarios y configurar el sistema.  Utiliza Flask como framework web, Flask-CORS para habilitar CORS, y WTForms para la gestión de formularios en el panel de administración.

## Estructura del Directorio

*   `api_app.py`: Aplicación principal de la API REST (endpoints para Android) y punto de entrada principal del servidor.
*   `admin.py`: Aplicación principal del panel de administración web.
*   `db_utils.py`: Funciones para interactuar con la base de datos MySQL.
*   `location.py`: Lógica para la geolocalización y verificación de la ubicación de los usuarios.
*   `templates/`: Plantillas HTML para el panel de administración.
*   `static/`: Archivos estáticos (CSS, JavaScript, imágenes) para el panel de administración.
*   `requirements.txt`: Lista de dependencias de Python.

## Configuración

1.  **Base de Datos MySQL:**
    *   Se utiliza una base de datos MySQL para almacenar la información de los usuarios y los registros de entrada/salida.
    *   La configuración de la base de datos (host, usuario, contraseña, nombre de la base de datos) se encuentra en el archivo `db_utils.py`.
    ![imagen](https://github.com/user-attachments/assets/3be286a4-be60-4976-9692-0414b16ab0cf)

2.  **Dependencias de Python:**

    *   Instala las dependencias:

        ```bash
        pip install -r requirements.txt
        ```

3.  **Configuración de PythonAnywhere:**

    *   Ve a la pestaña "Web" en tu panel de control de PythonAnywhere.
    *   Establece el "Working directory" a `/home/nickynicky23/final/api`.
    *   Modifica el archivo WSGI para que apunte a tu archivo `api_app.py` como el punto de entrada principal del servidor.  Asegúrate de que el archivo WSGI importe `api_app` y use `api_app.api_app` como la aplicación Flask.
    *   Configura una URL estática para servir los archivos CSS:

        *   URL: `/static/`
        *   Directorio: `/home/nickynicky23/final/api/static`

## Puntos Clave del Código

*   **Autenticación:**
    *   Los usuarios inician sesión proporcionando su nombre de usuario y contraseña.
    *   La API verifica las credenciales contra la base de datos y devuelve un `user_id` en caso de éxito.
    *   Solo los administradores pueden acceder al panel de administración web. La lógica de autenticación para administradores se encuentra en `admin.py` y la ruta `/web` de `api_app.py`.
*   **Geolocalización:**
    *   La API verifica que el usuario se encuentra en el lugar de trabajo antes de permitir el registro de la entrada o salida.
    *   Las coordenadas del lugar de trabajo (latitud, longitud y radio permitido) se definen en el archivo `location.py`.
*   **API REST:**
    *   La API REST proporciona endpoints para el login de usuarios, la comprobación de la ubicación y el registro de entradas y salidas.
    *   Los endpoints reciben datos en formato JSON y devuelven respuestas en formato JSON.
*   **Panel de Administración:**
    *   El panel de administración permite a los administradores gestionar usuarios (crear, modificar, eliminar) y configurar el sistema.
    *   El panel de administración utiliza plantillas HTML para la interfaz de usuario.

## Documentación del Código

### `api_app.py`

Este archivo contiene la aplicación principal de la API REST.

*   **Importaciones:** Importa las librerías necesarias de Flask, Flask-CORS, los módulos `location` y `db_utils`, y el Blueprint `admin_bp` y el formulario `LoginForm` de `admin.py`.
*   **Configuración:**
    *   Crea una instancia de Flask llamada `api_app`.
    *   Habilita CORS para permitir solicitudes desde diferentes dominios.
    *   Configura una clave secreta para la gestión de sesiones.  Esta clave se obtiene del entorno o se establece como "root" por defecto.
    *   Registra el Blueprint `admin_bp` para integrar las rutas de administración.
*   **Rutas:**
    *   `/web`: Ruta para el inicio de sesión web de administradores.  Utiliza el formulario `LoginForm` de `admin.py` y verifica las credenciales utilizando `db_utils.get_usuario_por_nombre_con_administrador()`.  Redirige al panel de administración en caso de éxito.
    *   `/login`: Ruta para el inicio de sesión de usuarios (aplicación Android).  Recibe el nombre de usuario y la contraseña en formato JSON y verifica las credenciales utilizando `db_utils.get_usuario_por_nombre()`.  Devuelve un `user_id` en caso de éxito.
    *   `/comprobar_ubicacion`:  Recibe latitud y longitud en formato JSON y utiliza `location.comprobar_ubicacion()` para verificar si la ubicación está dentro del rango permitido.
    *   `/registro_entrada`:  Recibe latitud, longitud y nombre de usuario en formato JSON.  Verifica la ubicación utilizando `location.comprobar_ubicacion()` y registra la entrada en la base de datos utilizando `db_utils.registrar_presencia()`.
    *   `/registro_salida`:  Similar a `/registro_entrada`, pero registra la salida del usuario.

### `admin.py`

Este archivo define el panel de administración web. (Ver la sección específica en la documentación de la Interfaz de Usuario Web (Administración) para una descripción detallada.)

### `db_utils.py`

Este archivo contiene las funciones para interactuar con la base de datos MySQL.

*   **Configuración:** Define las constantes `DB_HOST`, `DB_USER`, `DB_PASSWORD` y `DB_NAME` para la conexión a la base de datos.
*   **`get_db_connection()`:**  Función que establece la conexión a la base de datos MySQL utilizando la librería `mysql.connector`.  Maneja posibles errores de conexión.
*   **`get_usuario_por_nombre(nombre_usuario)`:**  Obtiene la información de un usuario por su nombre de usuario.  Retorna un diccionario con la información del usuario (id, nombre, contraseña).
*   **`insertar_usuario(nombre_usuario, contrasena)`:** Inserta un nuevo usuario en la base de datos.
*   **`registrar_presencia(usuario_id, tipo)`:** Registra un evento de entrada o salida para un usuario.
*   **`get_usuario_por_nombre_con_administrador(nombre_usuario)`:** Obtiene la información de un usuario, incluyendo el flag `es_administrador`.
*   **`get_all_usuarios()`:** Obtiene la información de todos los usuarios.
*   **`insertar_nuevo_usuario(nombre_usuario, contrasena, es_administrador)`:** Inserta un nuevo usuario con la posibilidad de asignarlo como administrador.
*   **`eliminar_usuario(usuario_id)`:** Elimina un usuario por su ID.
*   **`get_usuario_por_id(usuario_id)`:** Obtiene un usuario por su ID.
*   **`actualizar_usuario(usuario_id, nombre_usuario, contrasena, es_administrador)`:** Actualiza la información de un usuario.

Todas las funciones gestionan la conexión a la base de datos, la ejecución de las consultas SQL y el manejo de errores.  Utilizan `connection.commit()` para guardar los cambios en la base de datos y `connection.rollback()` para revertir los cambios en caso de error.

### `location.py`

Este archivo contiene la lógica para la geolocalización.

*   **Configuración:** Define las constantes `UBICACION_LATITUD`, `UBICACION_LONGITUD` y `RADIO_PERMITIDO` que representan las coordenadas del lugar de trabajo y el radio permitido alrededor de este lugar.
*   **`calcular_distancia(lat1, lon1, lat2, lon2)`:**  Calcula la distancia entre dos puntos geográficos utilizando la fórmula de Haversine.
*   **`comprobar_ubicacion(latitud, longitud)`:**  Verifica si una ubicación dada (latitud, longitud) se encuentra dentro del radio permitido alrededor del lugar de trabajo.  Retorna un booleano indicando si la ubicación es permitida y un mensaje descriptivo.  Realiza validaciones para asegurarse de que la latitud y longitud sean números válidos.

[Añade una documentación más detallada de cada archivo, función y clase en el código.]
