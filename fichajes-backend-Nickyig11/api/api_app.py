from flask import Flask, request, jsonify, session, redirect, url_for, render_template  # Importa 'session'
from flask_cors import CORS
import location
import db_utils  # Importa el módulo db_utils
from admin import admin_bp, LoginForm  # Importa el Blueprint y el formulario
import os

api_app = Flask(__name__)  # Crea una instancia de Flask para la API
CORS(api_app)

# Configura la clave secreta en la aplicación principal
api_app.config['SECRET_KEY'] = os.environ.get('CLAVE_SECRETA', 'root')

# Registra el Blueprint de administración
api_app.register_blueprint(admin_bp)

# Nueva ruta para la parte web (solo para administradores)
@api_app.route('/web', methods=['GET', 'POST'])
def web_login():
    form = LoginForm()
    if form.validate_on_submit():
        username = form.username.data
        password = form.password.data

        # Usa la nueva función para obtener el usuario con información de administrador
        usuario = db_utils.get_usuario_por_nombre_con_administrador(username)

        if usuario:
            if usuario['contrasena'] == password:
                if usuario['es_administrador']:
                    # Login exitoso, redirige al panel de administración
                    session['username'] = username  # Guarda el nombre de usuario en la sesión
                    return redirect(url_for('admin.admin_panel'))
                else:
                    # Usuario no es administrador
                    return render_template('login.html', form=form, error='Usuario no es administrador')
            else:
                # Contraseña incorrecta
                return render_template('login.html', form=form, error='Credenciales incorrectas')
        else:
            # Usuario no encontrado
            return render_template('login.html', form=form, error='Credenciales incorrectas')

    return render_template('login.html', form=form)

@api_app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    usuario = db_utils.get_usuario_por_nombre(username)

    if usuario and password == usuario['contrasena']:
        # Credenciales correctas
        return jsonify({'message': 'Login exitoso', 'user_id': usuario['id']}), 200
    else:
        # Credenciales incorrectas
        return jsonify({'message': 'Credenciales invalidas'}), 401

@api_app.route('/comprobar_ubicacion', methods=['POST'])
def comprobar_ubicacion():
    data = request.get_json()
    latitud = data.get('latitud')
    longitud = data.get('longitud')

    ubicacion_permitida, mensaje = location.comprobar_ubicacion(latitud, longitud)

    if ubicacion_permitida:
        return jsonify({'message': mensaje}), 200
    else:
        return jsonify({'message': mensaje}), 403

@api_app.route('/registro_entrada', methods=['POST'])
def registro_entrada():
    data = request.get_json()
    latitud = data.get('latitud')
    longitud = data.get('longitud')
    username = data.get('username')

    usuario = db_utils.get_usuario_por_nombre(username)

    if not usuario:
        return jsonify({'message': 'Usuario no encontrado'}), 404

    usuario_id = usuario['id']

    ubicacion_permitida, mensaje = location.comprobar_ubicacion(latitud, longitud)

    if ubicacion_permitida:
        if db_utils.registrar_presencia(usuario_id, 'entrada'):
            return jsonify({'message': 'Entrada registrada correctamente'}), 200
        else:
            return jsonify({'message': 'Error al registrar la entrada en la base de datos'}), 500
    else:
        return jsonify({'message': mensaje}), 403

@api_app.route('/registro_salida', methods=['POST'])
def registro_salida():
    data = request.get_json()
    latitud = data.get('latitud')
    longitud = data.get('longitud')
    username = data.get('username')

    usuario = db_utils.get_usuario_por_nombre(username)

    if not usuario:
        return jsonify({'message': 'Usuario no encontrado'}), 404

    usuario_id = usuario['id']

    ubicacion_permitida, mensaje = location.comprobar_ubicacion(latitud, longitud)

    if ubicacion_permitida:
        if db_utils.registrar_presencia(usuario_id, 'salida'):
            return jsonify({'message': 'Salida registrada correctamente'}), 200
        else:
            return jsonify({'message': 'Error al registrar la salida en la base de datos'}), 500
    else:
        return jsonify({'message': mensaje}), 403

if __name__ == '__main__':
    api_app.run(debug=True)