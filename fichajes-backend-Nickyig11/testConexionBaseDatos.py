import mysql.connector

DB_HOST = 'nickynicky23.mysql.eu.pythonanywhere-services.com'
DB_USER = 'nickynicky23'
DB_PASSWORD = 'rootroot'
DB_NAME = 'nickynicky23$db_nicky'

def test_db_connection():
    try:
        connection = mysql.connector.connect(
            host=DB_HOST,
            user=DB_USER,
            password=DB_PASSWORD,
            database=DB_NAME
        )
        if connection.is_connected():
            print("Conexión exitosa a la base de datos")
        connection.close()
    except mysql.connector.Error as err:
        print(f"Error al conectar a la base de datos: {err}")

test_db_connection()