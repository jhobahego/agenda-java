/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import javax.swing.JComboBox;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class MdlContactosBD extends MdlContacto implements OperacionesBD{

    public MdlContactosBD(Long id, String datoContacto){
        super(id, datoContacto);
    }
    
    public MdlContactosBD(){
    }
    
    @Override
    public boolean registrar() {
        boolean res = false;
        
        MdlUsuarioBD mdlUsuarioBD = new MdlUsuarioBD();
        
        try{
            String sql = "INSERT INTO agenda VALUES(0, ?, ?, ?, ?, ?)";
            PreparedStatement psm = cnx.prepareStatement(sql);
            psm.setLong(1, this.getNroDeIdentificacion());
            psm.setString(2, this.getApellido());
            psm.setString(3, this.getNombre());
            psm.setString(4, this.getDatoContacto());
            psm.setLong(5, this.getUsuarioId()); // id del contacto de la tabla usuarios (foreign key)
            res = ( psm.executeUpdate() > 0 );
            
        } catch (SQLException ex) {
            System.out.println("METODO REGISTRAR CLASE MdlContactosBD " +ex.toString());
        }
        return res;
    }

    @Override
    public Object consultar(Long usuarioId) {
        MdlContacto pers = null;
        try {
            String sql = "SELECT * FROM agenda WHERE nro_de_identificacion = ?";
            PreparedStatement psm = cnx.prepareStatement(sql);
            psm.setLong(1, usuarioId);
            
            ResultSet rs = psm.executeQuery();
            
            if(rs.next()){
                pers = new MdlContacto();
                pers.setId(rs.getLong(1));
                pers.setNroDeIdentificacion(rs.getLong(2));
                pers.setApellido(rs.getString(3));
                pers.setNombre(rs.getString(4));
                pers.setDatoContacto(rs.getString(5));
                pers.setUsuarioId(rs.getLong(6));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "METODO CONSULTAR CLASE MdlContactosBD"+ex);
        }
        return pers;
    }

    @Override
    public boolean actualizar(Long usuarioId) {
        boolean actualizado = false;
        try{
            String sql = "UPDATE agenda SET contacto = ? WHERE agenda_id = ?";
            
            PreparedStatement psm = cnx.prepareStatement(sql);
            
            psm.setString(1, this.getDatoContacto().toUpperCase());
            psm.setLong(2, usuarioId);
            
            actualizado = psm.executeUpdate() > 0;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Probleme al actualizar contacto "+e.getMessage());
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
        try {
            String sql = "SELECT * FROM agenda ORDER BY usuario_id ASC";
            PreparedStatement psm = cnx.prepareStatement(sql);

            ResultSet rs = psm.executeQuery();
            
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            modelo.getDataVector().clear();
            
            while (rs.next()) {    
                ArrayList fila = new ArrayList();
                fila.add(rs.getLong(1));
                fila.add(rs.getLong(2));
                fila.add(rs.getString(3));
                fila.add(rs.getString(4));
                fila.add(rs.getString(5));
                fila.add(rs.getLong(6));
                modelo.addRow(fila.toArray());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "METODO CONSULTAR CLASE MdlContactosBD" + ex);
        }
        
    }

    @Override
    public List<MdlContacto> consultarUsuarios() {
        List<MdlContacto> contactos = new ArrayList<>();
        
        try{
            String sql = "SELECT * FROM agenda";
            PreparedStatement psm = cnx.prepareStatement(sql);
            ResultSet rs = psm.executeQuery();
            
            while(rs.next()){
                MdlContacto contacto = new MdlContacto();
                contacto.setId(rs.getLong(1));
                contacto.setNroDeIdentificacion(rs.getLong(2));
                contacto.setApellido(rs.getString(3));
                contacto.setNombre(rs.getString(4));
                contacto.setDatoContacto(rs.getString(5));
                contacto.setUsuarioId(rs.getLong(6));
                
                contactos.add(contacto);
            }
        }catch(SQLException e){
            
        }
        
        return contactos;
    }
    
}
