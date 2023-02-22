package modelos;

import java.sql.*;
import java.util.*;
import javax.swing.JComboBox;
import javax.swing.JTable;

public interface OperacionesBD {
    
    Connection cnx = AccesoBD.getInstancia().getConexion();
    
    public boolean registrar();
    
    public Object consultar(Long usuario_id);
    
    public List consultarUsuarios();
    
    public boolean actualizar(Long usuario_id);
    
    public boolean eliminar(Long usuario_id);
    
    public void llenarListaDesplegable(JComboBox jc);
    
    public void llenarTabla(JTable tabla);
    
}
