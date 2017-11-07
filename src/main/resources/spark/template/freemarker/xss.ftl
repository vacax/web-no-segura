<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Prueba Ataque XSS</title>
</head>
<body>

    <h1>Prueba de Ataque Cross-Site Scripting - XSS </h1>
    <p>
        El ataque se fundamenta en ejecutar codigo javascript desde el cliente enviados por el servidor.
        Intente enviar en el nombre el siguiente codigo:
        <code style="font-weight: bold">
            Carlos Camacho &lt;script&gt;alert("Hola Mundo XSS");&lt;/script&gt;
        </code>
        .Aunque los navegadores no permite llamada fuera del dominio, el atacante utiliza el mismo
        formulario para guardar la información.
    </p>

    <form action="/crearUsuarioVulnerable" method="post">
        usuario: <input type="text" name="usuario"/><br/>
        nombre: <input type="text" name="nombre"/><br/>
        clave: <input type="password" name="clave"/><br/>
        <input type="submit" value="Crear Vulnerable"/>
    </form>
    <br/>
    <table border="1">
        <thead>
        <tr>
            <th>Id</th>
            <th>Usuario</th>
            <th>Nombre</th>
        </tr>
        </thead>
        <tbody>
            <#list lista as usuario>
               <tr>
                   <td>${usuario.id}</td>
                   <td>${usuario.usuario}</td>
                   <td>${usuario.nombre}</td>
               </tr>
            </#list>
        </tbody>
    </table>

</body>
</html>