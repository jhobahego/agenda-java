/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelos.MdlUsuario;
import modelos.MdlUsuarioBD;
import vistas.FrmUsuarios;

/**
 *
 * @author USUARIO
 */
public class CtrlUsuario extends MouseAdapter implements ActionListener, KeyListener{
    
    private MdlUsuarioBD mdlUsuarioBD;
    
    private FrmUsuarios frm;
    
    public CtrlUsuario(FrmUsuarios frame){
        this.frm = frame;
        limpiarControles();
    }
    
    private boolean datosValidos(int i){
        if(i == 1){
            if(this.frm.txtId.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "Debes consultar un usuario antes, para poder modificarlo");
                limpiarControles();
                return false;
            }
        }
        
        if(i == 0){
            if(this.frm.txtNroDeIdentificacion.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "El campo nro de identificacion no puede estar vacio");
                this.frm.txtNroDeIdentificacion.grabFocus();
                return false;
            }
            
            if(this.frm.txtNombre.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "El campo Nombre no puede estar vacio");
                this.frm.txtNombre.grabFocus();
                return false;
            }
            
            if(this.frm.txtApellido.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "El campo Apellido no puede estar vacio");
                this.frm.txtApellido.grabFocus();
                return false;
            }
        }
        
        return true;
    }
    
    public List<MdlUsuario> consultarUsuarios(){
        mdlUsuarioBD = new MdlUsuarioBD();
        return mdlUsuarioBD.consultarUsuarios();
    }
    
    private boolean validarUsuario(List<MdlUsuario> usuarios){
        boolean esValido = true;
        for (MdlUsuario usuario : usuarios) {
            if (usuario.getNroDeIdentificacion() == Long.parseLong(this.frm.txtNroDeIdentificacion.getText())) {
                esValido = false;
            }
        }
        return esValido;
    }
    
    private void registrarUsuario() {
        List<MdlUsuario> usuarios = consultarUsuarios();
        if (datosValidos(0)) {
            mdlUsuarioBD = new MdlUsuarioBD();
            mdlUsuarioBD.setNroDeIdentificacion(Long.parseLong(this.frm.txtNroDeIdentificacion.getText()));
            mdlUsuarioBD.setApellido(this.frm.txtApellido.getText().toUpperCase());
            mdlUsuarioBD.setNombre(this.frm.txtNombre.getText().toUpperCase());
            
            if(validarUsuario(usuarios)){ // Es un usuario con numero de identificacion diferente
                if (mdlUsuarioBD.registrar()) {
                    JOptionPane.showMessageDialog(frm, "Usuario registrado correctamente");
                    limpiarControles();
                } else {
                    JOptionPane.showMessageDialog(frm, "No se pudo registrar el usuario");
                }
            }else{
                JOptionPane.showMessageDialog(frm, "Cambia el nro de identificacion, este debe ser unico");
                this.frm.txtNroDeIdentificacion.grabFocus();
            }
        }
    }
    
    private void consultarUsuario(){
        Long nroDeIdentificacion;
        if(this.frm.txtNroDeIdentificacion.getText().trim().length() > 0){
            nroDeIdentificacion = Long.parseLong(this.frm.txtNroDeIdentificacion.getText());
            
            if(mdlUsuarioBD == null){
                mdlUsuarioBD = new MdlUsuarioBD();
            }
            
            MdlUsuario per = (MdlUsuario) mdlUsuarioBD.consultar(nroDeIdentificacion);
            
            if(per != null){
                this.frm.txtId.setText(per.getId().toString());
                this.frm.txtNroDeIdentificacion.setText(per.getNroDeIdentificacion().toString());
                this.frm.txtApellido.setText(per.getApellido().toUpperCase());
                this.frm.txtNombre.setText(per.getNombre().toUpperCase());
                
                mostrarUsuarioEnLaTabla(per);
            }else{
                JOptionPane.showMessageDialog(frm, "Nro de identificacion del usuario no encontrado");
            }
        }else{
            datosValidos(0);
        }
        
    }

    private void actualizarUsuario() {
        mdlUsuarioBD = new MdlUsuarioBD();
        if(datosValidos(1)){
            mdlUsuarioBD.setNombre(this.frm.txtNombre.getText().toUpperCase());
            mdlUsuarioBD.setApellido(this.frm.txtApellido.getText().toUpperCase());
            
            List<MdlUsuario> usuarios = mdlUsuarioBD.consultarUsuarios();
            if(!validarUsuario(usuarios)){ //Si es un numero de identificacion existente -puedes actualizar
                if (mdlUsuarioBD.actualizar(Long.parseLong(this.frm.txtId.getText()))) {
                    JOptionPane.showMessageDialog(frm, "Usuario actualizado correctamente");
                    limpiarControles();
                } else {
                    JOptionPane.showMessageDialog(frm, "Error al actualizar el usuario: clase CtrlUsuario");
                }
            }else{
                JOptionPane.showMessageDialog(frm, "Nro de identificacion no encontrado");
                this.frm.txtNroDeIdentificacion.grabFocus();
            }
 
        }
    }   
    
    
    private enum opciones{
        butRegistrar, butConsultar, butLimpiar, butActualizar
    }
    
    private void limpiarControles(){
        this.frm.txtId.setText("");
        this.frm.txtNroDeIdentificacion.setText("");
        this.frm.txtApellido.setText("");
        this.frm.txtNombre.setText("");
        
        this.mdlUsuarioBD = new MdlUsuarioBD();
        this.mdlUsuarioBD.llenarTabla(this.frm.tblUsuarios);
    }
    
    private void rellenarCamposConTabla(int fila){
        this.frm.txtId.setText(this.frm.tblUsuarios.getValueAt(fila, 0).toString());
        this.frm.txtNroDeIdentificacion.setText(this.frm.tblUsuarios.getValueAt(fila, 1).toString());
        this.frm.txtApellido.setText(this.frm.tblUsuarios.getValueAt(fila, 2).toString());
        this.frm.txtNombre.setText(this.frm.tblUsuarios.getValueAt(fila, 3).toString());
    }
    
    private void mostrarUsuarioEnLaTabla(MdlUsuario per){
        DefaultTableModel modelo = (DefaultTableModel) this.frm.tblUsuarios.getModel();
        modelo.getDataVector().clear();
        List fila = new ArrayList();
        fila.add(per.getId());
        fila.add(per.getNroDeIdentificacion());
        fila.add(per.getApellido());
        fila.add(per.getNombre());

        modelo.addRow(fila.toArray());
    }
    
    public void iniciar(){
        this.frm.butRegistrar.setActionCommand("butRegistrar");
        this.frm.butRegistrar.addActionListener(this);
        
        this.frm.btnConsultar.setActionCommand("butConsultar");
        this.frm.btnConsultar.addActionListener(this);
        
        this.frm.btnLimpiar.setActionCommand("butLimpiar");
        this.frm.btnLimpiar.addActionListener(this);
        
        this.frm.btnActualizar.setActionCommand("butActualizar");
        this.frm.btnActualizar.addActionListener(this);
        
        this.frm.txtNroDeIdentificacion.addActionListener(this);
        this.frm.txtApellido.addActionListener(this);
        this.frm.txtNombre.addActionListener(this);
        
        this.frm.tblUsuarios.addMouseListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent arg0) {
        if (arg0.getSource() == this.frm.txtNroDeIdentificacion) {
            if (Character.isDigit(arg0.getKeyChar())) {
            } else {
                arg0.consume();
            }
        } else if (arg0.getSource() == this.frm.txtApellido || arg0.getSource() == this.frm.txtNombre) {
            if (Character.isLetter(arg0.getKeyChar()) || Character.isSpaceChar(arg0.getKeyChar())) {
            } else {
                arg0.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.frm.tblUsuarios) {
            if ((e.getButton() == 1) && (e.getClickCount() == 2)) {
                    rellenarCamposConTabla(((JTable) e.getSource()).getSelectedRow());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch(String.valueOf(arg0.getActionCommand())){
            case "butRegistrar":
                registrarUsuario();
                break;
            case "butConsultar":
                consultarUsuario();
                break;      
            case "butLimpiar":
                limpiarControles();
                break;
            case "butActualizar":
                actualizarUsuario();
                break;
        }
    }
}
