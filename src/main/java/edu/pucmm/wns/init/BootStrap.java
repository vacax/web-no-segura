package edu.pucmm.wns.init;

import edu.pucmm.wns.encapsulaciones.Usuario;
import edu.pucmm.wns.servicios.DataBaseService;
import edu.pucmm.wns.servicios.OperacionesService;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */
public class BootStrap {

    /**
     *
     * @throws SQLException
     */
    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    /**
     *
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

    public static void init() throws SQLException {
        startDb();
        crearTablas();
    }


    /**
     * Metodo para recrear las tablas necesarios
     * @throws SQLException
     */
    private static void crearTablas() throws  SQLException{
        String sql_estudaintes = "CREATE TABLE IF NOT EXISTS ESTUDIANTE\n" +
                "(\n" +
                "  MATRICULA INTEGER PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  APELLIDO VARCHAR(100) NOT NULL,\n" +
                "  TELEFONO VARCHAR(25) NOT NULL,\n" +
                "  CARRERA VARCHAR(50) NOT NULL\n" +
                ");";

        //tabla para fines de demostración1
        String sql_usuarios = "DROP TABLE IF EXISTS USUARIOS; CREATE TABLE IF NOT EXISTS USUARIOS" +
                " (" +
                " ID INT NOT NULL," +
                " USUARIO VARCHAR(100) NOT NULL," +
                " CLAVE VARCHAR(100) NOT NULL," +
                " NOMBRE VARCHAR(100) NOT NULL " +
                ");";
        
        Connection con = DataBaseService.getInstancia().getConexion();
        Statement statement = con.createStatement();
        //statement.execute(sql_estudaintes);
        statement.execute(sql_usuarios);
        statement.close();
        con.close();

        Usuario admin = OperacionesService.getInstancia().getUsuario("admin");
        if(admin==null){
            System.out.println("Creando usuario la primera vez...");
            admin=new Usuario(1,"admin","admin","admin");
            OperacionesService.getInstancia().crearUsuario(admin);
        }

    }


}
