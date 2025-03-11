# db_utils.py
import mysql.connector

# Configuración de la base de datos MySQL
DB_HOST = 'nickynicky23.mysql.eu.pythonanywhere-services.com'
DB_USER = 'nickynicky23'
DB_PASSWORD = 'rootroot'
DB_NAME = 'nickynicky23$db_nicky'

# Función para conectar a la base de datos
def get_db_connection():
    try:
        connection = mysql.connector.connect(
            host=DB_HOST,
            user=DB_USER,
            password=DB_PASSWORD,
            database=DB_NAME,
            autocommit=False
        )
        return connection
    except mysql.connector.Error as err:
        print(f"Error al conectar a la base de datos: {err}")
        return None

# Función para obtener un usuario por su nombre
def get_usuario_por_nombre(nombre_usuario):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor(dictionary=True)  # Para obtener los resultados como diccionarios
        try:
            query = "SELECT id, nombre, contrasena FROM usuarios WHERE nombre = %s"  # Selecciona el id y el nombre y contraseña
            values = (nombre_usuario,)
            cursor.execute(query, values)
            usuario = cursor.fetchone()  # Obtiene el primer resultado
            return usuario
        except mysql.connector.Error as err:
            print(f"Error al obtener el usuario: {err}")
            return None
        finally:
            connection.close()
    else:
        return None

# Función para insertar un nuevo usuario
def insertar_usuario(nombre_usuario, contrasena):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor()
        try:
            query = "INSERT INTO usuarios (nombre, contrasena) VALUES (%s, %s)"
            values = (nombre_usuario, contrasena)
            cursor.execute(query, values)
            connection.commit()  # Guarda los cambios
            return True
        except mysql.connector.Error as err:
            print(f"Error al insertar el usuario: {err}")
            connection.rollback()  # Revierte los cambios en caso de error
            return False
        finally:
            connection.close()
    else:
        return False

# Función para registrar un registro de entrada/salida
def registrar_presencia(usuario_id, tipo):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor()
        try:
            query = "INSERT INTO registros (usuario_id, tipo) VALUES (%s, %s)"
            values = (usuario_id, tipo)
            cursor.execute(query, values)
            connection.commit()
            return True
        except mysql.connector.Error as err:
            print(f"Error al registrar la presencia: {err}")
            connection.rollback()
            return False
        finally:
            connection.close()
    else:
        return False

# Función para obtener un usuario por su nombre (con información de administrador)
def get_usuario_por_nombre_con_administrador(nombre_usuario):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor(dictionary=True)  # Para obtener los resultados como diccionarios
        try:
            query = "SELECT id, nombre, contrasena, es_administrador FROM usuarios WHERE nombre = %s"  # Selecciona es_administrador
            values = (nombre_usuario,)
            cursor.execute(query, values)
            usuario = cursor.fetchone()  # Obtiene el primer resultado
            return usuario
        except mysql.connector.Error as err:
            print(f"Error al obtener el usuario: {err}")
            return None
        finally:
            connection.close()
    else:
        return None

# Función para obtener todos los usuarios
def get_all_usuarios():
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor(dictionary=True)
        try:
            query = "SELECT id, nombre, contrasena, es_administrador FROM usuarios"
            cursor.execute(query)
            usuarios = cursor.fetchall()
            return usuarios
        except mysql.connector.Error as err:
            print(f"Error al obtener los usuarios: {err}")
            return None
        finally:
            connection.close()
    else:
        return None

def insertar_nuevo_usuario(nombre_usuario, contrasena, es_administrador):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor()
        try:
            query = "INSERT INTO usuarios (nombre, contrasena, es_administrador) VALUES (%s, %s, %s)"
            values = (nombre_usuario, contrasena, es_administrador)
            cursor.execute(query, values)
            connection.commit()
            return True
        except mysql.connector.Error as err:
            print(f"Error al insertar el usuario: {err}")
            connection.rollback()
            return False
        finally:
            connection.close()
    else:
        return False

#Función para eliminar un usuario
def eliminar_usuario(usuario_id):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor()
        try:
            query = "DELETE FROM usuarios WHERE id = %s"
            values = (usuario_id,)
            cursor.execute(query, values)
            connection.commit()
            return True
        except mysql.connector.Error as err:
            print(f"Error al eliminar el usuario: {err}")
            connection.rollback()
            return False
        finally:
            connection.close()
    else:
        return False

#Función para obtener un usuario por su id
def get_usuario_por_id(usuario_id):
    connection = get_db_connection()
    if connection:
        cursor = connection.cursor(dictionary=True)
        try:
            query = "SELECT id, nombre, contrasena, es_administrador FROM usuarios WHERE id = %s"
            values = (usuario_id,)
            cursor.execute(query, values)
            usuario = cursor.fetchone()
            return usuario
        except mysql.connector.Error as err:
            print(f"Error al obtener el usuario: {err}")
            return None
        finally:
            connection.close()
    else:
        return None

#Funcion para Actualizar un usuario
def actualizar_usuario(usuario_id, nombre_usuario, contrasena, es_administrador):
        connection = get_db_connection()
        if connection:
            cursor = connection.cursor()
            try:
                query = "UPDATE usuarios SET nombre = %s, contrasena = %s, es_administrador = %s WHERE id = %s"
                values = (nombre_usuario, contrasena, es_administrador, usuario_id)
                cursor.execute(query, values)
                connection.commit()
                return True
            except mysql.connector.Error as err:
                print(f"Error al actualizar el usuario: {err}")
                connection.rollback()
                return False
            finally:
                connection.close()
        else:
            return False