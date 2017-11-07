package edu.pucmm.wns.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class DataBaseService {

    private static DataBaseService instancia;
    private String URL = "jdbc:h2:tcp://localhost/~/web-no-sql"; //Modo Server...

    /**
     *Implementando el patron Singleton
     */
    private DataBaseService(){
        registrarDriver();
    }

    /**
     * Retornando la instancia.
     * @return
     */
    public static DataBaseService getInstancia(){
        if(instancia==null){
            instancia = new DataBaseService();
        }
        return instancia;
    }

    /**
     * Metodo para el registro de driver de conexión.
     */
    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public void testConexion() {
        try {
            getConexion().close();
            System.out.println("Conexión realizado con exito...");
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
