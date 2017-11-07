package edu.pucmm.wns.encapsulaciones;

/**
 * Clase de usuario para fines de demostración
 */
public class Usuario {
    int id;
    String usuario;
    String nombre;
    String clave;

    public Usuario() {
    }

    public Usuario(String usuario, String nombre, String clave) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.clave = clave;
    }

    public Usuario(int id,String usuario, String nombre, String clave) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.clave = clave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
