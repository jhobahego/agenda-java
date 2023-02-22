/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import javax.swing.JComboBox;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class MdlUsuarioBD extends MdlUsuario implements OperacionesBD{

    public MdlUsuarioBD(Long id, Long nroDeIdentificacion, String nombre, String apellido){
        super(id, nroDeIdentificacion, nombre, apellido);
    }
    
    public MdlUsuarioBD(){
    }
    
    @Override
    public boolean registrar() {
        boolean res = false;
        
        try{
            String sql = "INSERT INTO usuarios VALUES(0, ?, ?, ?)";
            PreparedStatement psm = cnx.prepareStatement(sql);
            psm.setLong(1, this.getNroDeIdentificacion());
            psm.setString(2, this.getApellido());
            psm.setString(3, this.getNombre());
            res = ( psm.executeUpdate() > 0 );
            
        } catch (SQLException ex) {
            System.out.println("METODO REGISTRAR CLASE MdlUsuarioBD " +ex.toString());
        }
        return res;
    }
    
    
    @Override
    public List<MdlUsuario> consultarUsuarios(){
        List<MdlUsuario> usuarios = new ArrayList<>();
        try{
            String sql = "SELECT * FROM usuarios";
            
            PreparedStatement psm = cnx.prepareStatement(sql);
            ResultSet rs = psm.executeQuery();
            
            while(rs.next()){
                MdlUsuario usuario = new MdlUsuario();
                usuario.setId(rs.getLong(1));
                usuario.setNroDeIdentificacion(rs.getLong(2));
                usuario.setApellido(rs.getString(3));
                usuario.setNombre(rs.getString(4));

                usuarios.add(usuario);
            }
            
        }catch(SQLException e){
            
        }
        return usuarios;
    }

    @Override
    public Object consultar(Long nroDeIdentificacion) {
        MdlUsuario pers = null;
        try {
            String sql = "SELECT * FROM usuarios WHERE nro_de_identificacion = ?";
            PreparedStatement psm = cnx.prepareStatement(sql);
            psm.setLong(1, nroDeIdentificacion);
            
            ResultSet rs = psm.executeQuery();
            
            if(rs.next()){
                pers = new MdlUsuario();
                pers.setId(rs.getLong(1));
                pers.setNroDeIdentificacion(rs.getLong(2));
                pers.setApellido(rs.getString(3));
                pers.setNombre(rs.getString(4));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "METODO CONSULTAR CLASE MdlUsuarioBD"+ex);
        }
        return pers;
    }

    @Override
    public boolean actualizar(Long contactId) {
        boolean actualizado = false;
        try{
            String sql = "UPDATE usuarios SET nombre = ?, apellido = ? WHERE usuario_id = ?";
            
            PreparedStatement psm = cnx.prepareStatement(sql);
            psm.setString(1, this.getNombre());
            psm.setString(2, this.getApellido());
            psm.setLong(3, contactId);
            
            actualizado = psm.executeUpdate() > 0;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Probleme al actualizar usuario"+e.getMessage());
        }
        return actualizado;
    }

    @Override
    public boolean eliminar(Long contactId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void llenarListaDesplegable(JComboBox jc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void llenarTabla(JTable tabla){
        List<MdlUsuario> usuarios = consultarUsuarios();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.getDataVector().clear();
        
        for(MdlUsuario usuario : usuarios){
            ArrayList fila = new ArrayList();
            fila.add(usuario.getId());
            fila.add(usuario.getNroDeIdentificacion());
            fila.add(usuario.getApellido());
            fila.add(usuario.getNombre());
            modelo.addRow(fila.toArray());
        }
        
    }
    
}
