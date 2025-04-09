# Interfaz de Usuario Web (Administración)

[Volver a la documentación principal](README.md)

## Descripción

Este subproyecto contiene el código de la interfaz de usuario web para el panel de administración. La interfaz permite a los administradores gestionar usuarios y configurar el sistema.

## Estructura del Directorio

*   `admin.py`: Aplicación principal del panel de administración web.
*   `templates/`: Plantillas HTML para el panel de administración (login.html, admin.html, alta_usuario.html, modificar_usuario.html).
*   `static/`: Archivos estáticos (CSS, JavaScript si se incorpora, imágenes) para el panel de administración.

## Plantillas HTML

*   `login.html`: Formulario de inicio de sesión para administradores.
*   `admin.html`: Panel de administración principal, que muestra información de la empresa y permite gestionar usuarios.
*   `alta_usuario.html`: Formulario para crear nuevos usuarios.
*   `modificar_usuario.html`: Formulario para modificar la información de un usuario existente.

  ## Alta de nuevos usuarios:
![image](https://github.com/user-attachments/assets/339e1c53-3a88-4990-b7d1-c68ecf948bfe)

## Archivos Estáticos

*   `style.css`: Estilos para los htmls.

## Contenido de `admin.py`

Este archivo define las rutas y la lógica para la interfaz de administración. A continuación, se describe su funcionalidad principal:

*   **Blueprints:** Utiliza un Blueprint de Flask (`admin_bp`) para organizar las rutas relacionadas con la administración. Esto permite modularizar la aplicación y facilita su mantenimiento.
*   **Formularios:** Define formularios utilizando `FlaskForm` de WTForms para:
    *   `LoginForm`: Inicio de sesión de administradores. Requiere nombre de usuario y contraseña.
    *   `AltaUsuarioForm`: Creación de nuevos usuarios. Incluye campos para nombre de usuario, contraseña y un booleano para indicar si el usuario es administrador.
    *   `ModificarUsuarioForm`: Modificación de usuarios existentes.  Incluye los mismos campos que `AltaUsuarioForm`.
*   **Rutas:**
    *   `/admin_login`: Muestra el formulario de inicio de sesión y gestiona la autenticación.  Verifica las credenciales del usuario contra la base de datos utilizando `db_utils.get_usuario_por_nombre()`.  Si la autenticación es exitosa, guarda el nombre de usuario en la sesión y redirige al panel de administración.
    *   `/admin_panel`: Muestra el panel de administración principal. Requiere que el usuario haya iniciado sesión previamente.  Obtiene la información de configuración (nombre de la empresa, ubicación) y la lista de usuarios de la base de datos a través de `db_utils.get_all_usuarios()`.  Pasa esta información a la plantilla `admin.html` para su renderizado.
    *   `/admin_logout`: Cierra la sesión del administrador eliminando el nombre de usuario de la sesión.
    *   `/admin_alta_usuario`: Muestra el formulario de alta de usuario y gestiona la creación de nuevos usuarios en la base de datos utilizando `db_utils.insertar_nuevo_usuario()`.
    *   `/admin_eliminar_usuario/<int:usuario_id>`: Permite eliminar un usuario de la base de datos, identificado por su `usuario_id`, utilizando `db_utils.eliminar_usuario()`.
    *   `/admin_modificar_usuario/<int:usuario_id>`: Permite modificar un usuario existente. Obtiene la información del usuario a través de `db_utils.get_usuario_por_id()` y pre-rellena el formulario `ModificarUsuarioForm`. Al enviar el formulario, actualiza la información del usuario en la base de datos utilizando `db_utils.actualizar_usuario()`.
*   **Seguridad:** Las rutas del panel de administración (`/admin_panel`, `/admin_alta_usuario`, `/admin_eliminar_usuario`, `/admin_modificar_usuario`) verifican si el usuario ha iniciado sesión (`'username' in session`). Si no hay sesión, se redirige al usuario a la página de inicio de sesión (`/admin_login`).
