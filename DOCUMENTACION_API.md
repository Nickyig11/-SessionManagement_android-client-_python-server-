# API REST y Panel de Administración (Servidor Python/Flask)

## Descripción

Este subproyecto contiene el código del servidor, que se encarga de:

*   Proporcionar una API REST para que la aplicación Android registre entradas y salidas de empleados.
*   Gestionar el panel de administración web para que los administradores puedan gestionar usuarios y configurar el sistema.

## Estructura del Directorio

*   `api_app.py`: Aplicación principal de la API REST (endpoints para Android).
*   `admin.py`: Aplicación principal del panel de administración web.
*   `db_utils.py`: Funciones para interactuar con la base de datos.
*   `location.py`: Lógica para la geolocalización.
*   `templates/`: Plantillas HTML para el panel de administración.
*   `static/`: Archivos estáticos (CSS, JavaScript, imágenes) para el panel de administración.
*   `requirements.txt`: Lista de dependencias de Python.

## Configuración

1.  **Base de Datos MySQL:**
![imagen](https://github.com/user-attachments/assets/3be286a4-be60-4976-9692-0414b16ab0cf)


2.  **Dependencias de Python:**

    *   Instala las dependencias:

        ```bash
        pip install -r requirements.txt
        ```

3.  **Configuración de PythonAnywhere:**

    *   Ve a la pestaña "Web" en tu panel de control de PythonAnywhere.
    *   Establece el "Working directory" a `/home/nickynicky23/final/api`.
    *   Modifica el archivo WSGI para que apunte a tu archivo `api_app.py` y/o `admin.py`.
    *   Configura una URL estática para servir los archivos CSS:

        *   URL: `/static/`
        *   Directorio: `/home/nickynicky23/final/api/static`

## Puntos Clave del Código

*   **Autenticación:**
    *   Los usuarios inician sesión proporcionando su nombre de usuario y contraseña.
    *   La API verifica las credenciales contra la base de datos y devuelve un token de autenticación (opcional).
    *   Solo los administradores pueden acceder al panel de administración web.
*   **Geolocalización:**
    *   La API verifica que el usuario se encuentra en el lugar de trabajo antes de permitir el registro de la entrada o salida.
    *   Las coordenadas del lugar de trabajo se definen en el archivo `location.py`.

## Documentación del Código

[Añade una documentación más detallada de cada archivo, función y clase en el código.]
