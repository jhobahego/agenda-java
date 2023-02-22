package modelos;

public class MdlUsuario {
    Long id;
    Long nroDeIdentificacion;
    String nombre;
    String apellido;
    
    public MdlUsuario(Long id, Long nroDeidentificacion, String nombre, String apellido){
        this.id = id;
        this.nroDeIdentificacion = nroDeidentificacion;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    public MdlUsuario(){
        this.id = 0L;
        this.nombre = "";
        this.apellido = "";
    }
    
    public Long getNroDeIdentificacion() {
        return nroDeIdentificacion;
    }

    public void setNroDeIdentificacion(Long nroDeIdentificacion) {
        this.nroDeIdentificacion = nroDeIdentificacion;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Nombre: "+getNombre(); //To change body of generated methods, choose Tools | Templates.
    }
}
