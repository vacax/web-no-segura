<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Prueba de ataque Cross Site Request Forgery - CSRF</title>
</head>
<body>
   <h1>
       Prueba de ataque Cross Site Request Forgery - CSRF
   </h1>
    <p>
       Ataca las aplicaciones que se basan en las sesiones del usuarios. El controlador
        si recibe los parametros crea un usuario. Cada vez que es ejecutado la siguiente llamada
        en un site fuera de nuestra aplicacion están creado nuevos usuarios
        <code style="font-weight: bold">
            http://localhost:4567/csrf?usuario=vacax&nombre=Carlos&clave=1234
        </code>
    </p>
</body>
</html>