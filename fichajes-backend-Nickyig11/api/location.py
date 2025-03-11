import math

# Coordenadas de la ubicación permitida
UBICACION_LATITUD = 36.5386335
UBICACION_LONGITUD = -4.6277128
RADIO_PERMITIDO = 100  # Metros

# Función para calcular la distancia entre dos puntos geográficos (Haversine)
def calcular_distancia(lat1, lon1, lat2, lon2):
    R = 6371  # Radio de la Tierra en km
    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)
    a = math.sin(dlat / 2) * math.sin(dlat / 2) + math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * math.sin(dlon / 2) * math.sin(dlon / 2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distancia = R * c * 1000  # Convertir a metros
    return distancia

def comprobar_ubicacion(latitud, longitud):
    if latitud is None or longitud is None:
        return False, 'Latitud y longitud son requeridas'

    try:
        latitud = float(latitud)
        longitud = float(longitud)
    except ValueError:
        return False, 'Latitud y longitud deben ser números'

    distancia = calcular_distancia(latitud, longitud, UBICACION_LATITUD, UBICACION_LONGITUD)

    if distancia <= RADIO_PERMITIDO:
        return True, 'Ubicación permitida'
    else:
        return False, 'Ubicación fuera del rango permitido'