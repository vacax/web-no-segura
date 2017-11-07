package edu.pucmm.wns.servicios;


import edu.pucmm.wns.encapsulaciones.Usuario;
import freemarker.log.Logger;
import org.slf4j.event.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OperacionesService {

    private int contador = 0;
    private static OperacionesService instancia;
    private final static Logger logger = Logger.getLogger(OperacionesService.class.getName());

    /**
     *Implementando el patron Singleton
     */
    private  OperacionesService(){

    }

    /**
     * Retornando la instancia.
     * @return
     */
    public synchronized static OperacionesService getInstancia(){
        if(instancia==null){
            instancia = new OperacionesService();
        }
        return instancia;
    }

    /**
     * 
     * @param id
     * @param usuario
     * @param nombre
     * @param clave
     * @return
     */
    private Usuario completarUsuario(int id, String usuario, String nombre, String clave){
       return new Usuario(id, usuario, nombre, clave);
    }

    /**
     * Metodo utilizando el PreparedStatement para enviar los paremetros, dependiendo de la
     * implementación realiza escape de la
     * @param usuario
     * @return
     */
    public Usuario getUsuario(String usuario) {
        Usuario est = null;
        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            //utilizando los comodines (?)...
            String query = "select * from usuarios where usuario = ?";
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, usuario);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                est =completarUsuario(rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave"));
            }
            
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return est;
    }

    /**
     * Consulta vulnerable notar que el parametro es del tipo String
     * @param id
     * @return
     */
    public Usuario getUsuarioPorIdVulnerable(String id) {
        Usuario est = null;
        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            //utilizando los comodines (?)...
            String query = "select * from usuarios where id = "+id;
            //

            //Ejecuto...
            System.out.println("la consulta vulnerable: "+query);
            ResultSet rs = con.createStatement().executeQuery(query);
            while (rs.next()) {
                est =completarUsuario(rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave"));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return est;
    }


    /**
     *
     * @param est
     * @return
     */
    public boolean crearUsuario(Usuario est){
        boolean ok =false;

        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            String query = "insert into usuarios(id, usuario, nombre, clave) values(?,?,?,?)";
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, (++contador));
            prepareStatement.setString(2, est.getUsuario());
            prepareStatement.setString(3, est.getNombre());
            prepareStatement.setString(4, est.getClave());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0;


        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return ok;
    }

    /**
     * Creacion vulnerable
     * @param est
     * @return
     */
    public boolean crearUsuarioVulnerable(Usuario est){
        boolean ok =false;

        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            String query = "insert into usuarios(id, usuario, nombre, clave)" +
                    " values("+(++contador)+",'"+est.getUsuario()+"','"+est.getNombre()+"','"+est.getClave()+"')";

            //
            System.out.println("La consulta vulnerable para insertar: "+query);
            int fila = con.createStatement().executeUpdate(query);
            ok = fila > 0;


        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return ok;
    }


    /**
     * Metodo seguro, el PreparedStatement aunque depende de la implementación
     * protege sobre la inyeccion de sql
     * @param usuario
     * @param clave
     * @return
     */
    public Usuario getUsuarioPorClaveNoSeguro(String usuario, String clave) {
        Usuario est = null;
        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            //utilizando los comodines (?)...
            String query = "select * from usuarios where usuario = '"+usuario+"' and clave = '"+clave+"'";
          
            System.out.println("Consulta antes de procesar vulnerable: "+query);
            ResultSet rs = con.createStatement().executeQuery(query);

            while (rs.next()) {
                est =completarUsuario(rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave"));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return est;
    }

    

    /**
     * Metodo seguro, el PreparedStatement aunque depende de la implementación
     * protege sobre la inyeccion de sql
     * @param usuario
     * @param clave
     * @return
     */
    public Usuario getUsuarioPorClaveSeguro(String usuario, String clave) {
        Usuario est = null;
        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            //utilizando los comodines (?)...
            String query = "select * from usuarios where usuario = ? and clave = ?";
            //
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, clave);
            //
            System.out.println("Consulta antes de procesar no vulnerable: "+preparedStatement);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                est =completarUsuario(rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave"));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return est;
    }

    /**
     * 
     * @return
     */
    public List<Usuario> getListaUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = DataBaseService.getInstancia().getConexion()) {

            //utilizando los comodines (?)...
            String query = "select * from usuarios ";

            //
            ResultSet rs = con.createStatement().executeQuery(query);

            while (rs.next()) {
                Usuario est =completarUsuario(rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave"));
                lista.add(est);
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return lista;
    }

}
