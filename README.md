# agenda-java
Proyecto Java creado durante la clase de lenguaje de programación 2 de la carrera de ingeniería en sistemas

## Probar aplicación
```
git clone https://github.com/jhobahego/agenda-java.git

Ejecuta la clase de la carpeta src/main/Main.java
```

## Requisitos

### Base de datos
- Base de datos de mariadb llamada `agenda_contactos_lengpro2` o para usar el nombre de base de datos
  que desees colocalo en la linea 10 de la clase Main.java como primer parametro del metodo getConexion del
  objeto de la clase AccesoBD
- Tabla `usuarios` con los campos `usuario_id, nro_de_identificacion, apellido, nombre` `usuario_id` debe ser una primary key autoincremental
- Tabla `agenda` con los campos `agenda_id, nro_de_identificacion, apellido, nombre, contacto, usuario_id` donde `agenda_id` es la primary key
  y `usuario_id` es una llave foranea que referencia a el campo `usuarios.usuario_id`

## Recomendaciones
IDE [netbeans](https://netbeans.apache.org/download/index.html)
