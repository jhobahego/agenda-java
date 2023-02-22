package modelos;

public class MdlContacto {
    private Long id;
    private Long nroDeIdentificacion;
    private String datoContacto;
    
    private String nombre;
    private String apellido;
    private Long usuarioId;
    
    public MdlContacto(Long id, String datoContacto){
        this.id = id;
        this.datoContacto = datoContacto;
    }
    
    public MdlContacto(){
        this.id = 0L;
        this.datoContacto = "";
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getNroDeIdentificacion(){
        return nroDeIdentificacion;
    }
    
    public void setNroDeIdentificacion(Long nroDeIdentificacion){
        this.nroDeIdentificacion = nroDeIdentificacion;
    }

    public String getDatoContacto() {
        return datoContacto;
    }

    public void setDatoContacto(String datoContacto) {
        this.datoContacto = datoContacto;
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
    
    public Long getUsuarioId(){
        return usuarioId;
    }
    public void setUsuarioId(Long usuario_id){
        this.usuarioId = usuario_id;
    }

    @Override
    public String toString() {
        return "Usuario con #identificion: "+getNroDeIdentificacion() +" y nombre: "+getNombre();
    }
}
