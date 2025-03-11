# admin.py
from flask import render_template, request, redirect, url_for, session, Blueprint
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField
from wtforms.validators import DataRequired
import db_utils  # Importa el módulo db_utils
import location  # Importa el módulo location

# Crea un Blueprint para las rutas de administración
admin_bp = Blueprint('admin', __name__, template_folder='templates')

class LoginForm(FlaskForm):
    username = StringField('Usuario', validators=[DataRequired()])
    password = PasswordField('Contraseña', validators=[DataRequired()])
    submit = SubmitField('Entrar')

class AltaUsuarioForm(FlaskForm):
    username = StringField('Nombre de Usuario', validators=[DataRequired()])
    password = PasswordField('Contraseña', validators=[DataRequired()])
    es_administrador = BooleanField('Es Administrador')
    submit = SubmitField('Crear Usuario')

class ModificarUsuarioForm(FlaskForm):
    username = StringField('Nombre de Usuario', validators=[DataRequired()])
    password = PasswordField('Contraseña', validators=[DataRequired()])
    es_administrador = BooleanField('Es Administrador')
    submit = SubmitField('Guardar Cambios')

@admin_bp.route('/admin_login', methods=['GET', 'POST'])
def admin_login():
    form = LoginForm()
    if form.validate_on_submit():
        username = form.username.data
        password = form.password.data

        usuario = db_utils.get_usuario_por_nombre(username)

        if usuario and usuario['contrasena'] == password and usuario['es_administrador']:
            # Login exitoso
            session['username'] = username  # Guarda el nombre de usuario en la sesión
            return redirect(url_for('admin.admin_panel'))  # Redirige al panel de administración
        else:
            # Credenciales incorrectas
            return render_template('login.html', form=form, error='Credenciales incorrectas')

    return render_template('login.html', form=form)

@admin_bp.route('/admin_panel')
def admin_panel():
    if 'username' in session:
        # Aquí iría la lógica para el panel de administración
        empresa_nombre = "Gestión de Entradas y Salidas del Trabajo"
        ubicacion_latitud = location.UBICACION_LATITUD
        ubicacion_longitud = location.UBICACION_LONGITUD
        usuarios = db_utils.get_all_usuarios()  # Obtiene todos los usuarios
        return render_template('admin.html', username=session['username'], empresa_nombre=empresa_nombre, ubicacion_latitud=ubicacion_latitud, ubicacion_longitud=ubicacion_longitud, usuarios=usuarios) #Paso el usuario
    else:
        return redirect(url_for('admin.admin_login'))  # Redirige al login si no hay sesión

@admin_bp.route('/admin_logout')
def admin_logout():
    session.pop('username', None)  # Elimina el nombre de usuario de la sesión
    return redirect(url_for('admin.admin_login'))  # Redirige al login

# Ruta para el formulario de alta de usuario
@admin_bp.route('/admin_alta_usuario', methods=['GET', 'POST'])
def admin_alta_usuario():
    if 'username' not in session:
        return redirect(url_for('admin.admin_login'))  # Redirige al login si no hay sesión

    form = AltaUsuarioForm()
    if form.validate_on_submit():
        username = form.username.data
        password = form.password.data
        es_administrador = form.es_administrador.data

        if db_utils.insertar_nuevo_usuario(username, password, es_administrador):
            # Alta exitosa
            return redirect(url_for('admin.admin_panel'))  # Redirige al panel de administración
        else:
            # Error al crear el usuario
            return render_template('alta_usuario.html', form=form, error='Error al crear el usuario')

    return render_template('alta_usuario.html', form=form)

# Ruta para eliminar un usuario
@admin_bp.route('/admin_eliminar_usuario/<int:usuario_id>')
def admin_eliminar_usuario(usuario_id):
    if 'username' not in session:
        return redirect(url_for('admin.admin_login'))  # Redirige al login si no hay sesión

    if db_utils.eliminar_usuario(usuario_id):
        # Eliminación exitosa
        return redirect(url_for('admin.admin_panel'))  # Redirige al panel de administración
    else:
        # Error al eliminar el usuario
        return "Error al eliminar el usuario"  # Puedes mejorar esto con una plantilla de error

#Ruta para modificar un usuario
@admin_bp.route('/admin_modificar_usuario/<int:usuario_id>', methods=['GET', 'POST'])
def admin_modificar_usuario(usuario_id):
    if 'username' not in session:
        return redirect(url_for('admin.admin_login'))  # Redirige al login si no hay sesión

    usuario = db_utils.get_usuario_por_id(usuario_id)
    if not usuario:
        return "Usuario no encontrado"  # Puedes mejorar esto con una plantilla de error

    form = ModificarUsuarioForm(data=usuario)  # Pre-rellena el formulario con los datos del usuario

    if form.validate_on_submit():
        # Aquí iría la lógica para actualizar el usuario en la base de datos
        username = form.username.data
        password = form.password.data
        es_administrador = form.es_administrador.data

        if db_utils.actualizar_usuario(usuario_id,username, password, es_administrador):
          # Eliminación exitosa
          return redirect(url_for('admin.admin_panel'))  # Redirige al panel de administración
        else:
          # Error al eliminar el usuario
          return "Error al actualizar el usuario"  # Puedes mejorar esto con una plantilla de error

    #En caso de que falle la update
    return render_template('modificar_usuario.html', form=form, usuario=usuario)