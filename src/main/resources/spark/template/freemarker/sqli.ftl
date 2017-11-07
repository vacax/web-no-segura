<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Prueba de Inyección de SQL</title>
</head>
<body>
    <h1>Formulario para probar la Inyección de SQL</h1>
    <p>
        El formulario llama al metodo vulnerable donde la consulta se completa con los valores
        obtenidos desde los parametros, si un usuario mal intencionado manda el valor de la clave
        el siguiente valor: <code style="font-weight: bold">' OR ''='</code>, notarán que la aplicación le permite el acceso.
        <#--¿Y si cambio la consulta por <code style="font-weight: bold">' OR ''='; DROP TABLE USUARIOS</code> ?-->
    </p>
    <form action="/procesarSqliVulnerable" method="post">
       Usuario: <input type="text" name="usuario"/><br/>
       Clave: <input type="password" name="clave"/><br/>
       <input type="submit" value="Entrar"/>
    </form>

    <#if error??>
        <p style="color: red"> Error con la contraseña</p>
    </#if>

    <hr/>

    <h1>Consulta de usuario sin controlar La Inyección de SQL</h1>
    <p>
       Mismo resultado que el anterior, prueben la siguiente consulta:
        <code style="font-weight: bold">1; DROP TABLE USUARIOS</code>. Miren que pasa....
    </p>

    <form action="/consultaUsuarioVulnerable" method="get">
        Usuario: <input type="number" name="id"/><br/>
        <input type="submit" value="Entrar"/>
    </form>
    
    <hr/>

    <h1>Formulario controlado la Inyección de SQL</h1>
    <p>
        El metodo que procesa la llamada tiene implementado las funcionalidad para evitar ese error.
        Los ORM implementan una solución para esos fines.
    </p>
    <form action="/procesarSqliNoVulnerable" method="post">
        Usuario: <input type="text" name="usuario"/><br/>
        Clave: <input type="password" name="clave"/><br/>
        <input type="submit" value="Entrar"/>
    </form>
</body>
</html>