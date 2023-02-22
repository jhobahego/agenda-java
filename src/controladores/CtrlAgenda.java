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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelos.AccesoBD;
import modelos.MdlContacto;
import modelos.MdlContactosBD;
import modelos.MdlUsuario;
import modelos.MdlUsuarioBD;
import vistas.FrmContactos;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author USUARIO
 */
public class CtrlAgenda extends MouseAdapter implements ActionListener, KeyListener{
    
    private MdlContactosBD mdlContactoBD;
    private MdlUsuarioBD mdlUsuarioBD;
    private MdlContacto contacto;
    
    private FrmContactos frm;
    
    private final int idDelUsuarioVacio = 1;
    private final int todosLosCamposVacios = 0;
    
    public CtrlAgenda(FrmContactos frame){
        this.frm = frame;
        limpiarControles();
    }    

    private boolean datosValidos(int i){        
        if(i == 1){
            if(this.frm.txtNroDeIdentificacion.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "El campo nro de identificacion no puede estar vacio");
                this.frm.txtNroDeIdentificacion.grabFocus();
                return false;
            }
        }
        
        if(i == 0){
            if(this.frm.txtNroDeIdentificacion.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "El campo nro de identificacion no puede estar vacio");
                this.frm.txtNroDeIdentificacion.grabFocus();
                return false;
            }
            if(this.frm.txtContacto.getText().trim().length() == 0){
                JOptionPane.showMessageDialog(frm, "El campo de contacto no puede estar vacio");
                this.frm.txtContacto.grabFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private boolean validarContacto(int numCampos){
        boolean esValido = false;
        
        List<MdlContacto> contactos = mdlContactoBD.consultarUsuarios();
        
        if (numCampos == 1) {
            for (MdlContacto contacto : contactos) {
                if (contacto.getNroDeIdentificacion() == Long.parseLong(this.frm.txtNroDeIdentificacion.getText())) {
                    esValido = false;
                }
            }
        }
        
        if (numCampos == 2) {
            for (MdlContacto contacto : contactos) {
                if (contacto.getDatoContacto().equals(this.frm.txtContacto.getText().toUpperCase())) {    
                    System.out.println(contacto.getDatoContacto() + " : " + this.frm.txtContacto.getText());
                    esValido = true;
                }
            }
        }
        
        return esValido;
    }
    
    private void registrarContacto() {
        Long nroDeIdentificacion;
        if (datosValidos(todosLosCamposVacios)) {
            
            mdlUsuarioBD = new MdlUsuarioBD();
            mdlContactoBD = new MdlContactosBD();
            
            nroDeIdentificacion = Long.parseLong(this.frm.txtNroDeIdentificacion.getText());
            
            MdlUsuario per = (MdlUsuario) mdlUsuarioBD.consultar(nroDeIdentificacion);

            if (per != null && Objects.equals(per.getNroDeIdentificacion(), nroDeIdentificacion)) {
                mdlContactoBD.setNroDeIdentificacion(per.getNroDeIdentificacion());
                mdlContactoBD.setApellido(per.getApellido());
                mdlContactoBD.setNombre(per.getNombre());
                mdlContactoBD.setDatoContacto(this.frm.txtContacto.getText().toUpperCase());
                mdlContactoBD.setUsuarioId(per.getId());
            } else {
                JOptionPane.showMessageDialog(frm, "Numero de identificacion invalido, primero registre este usuario en la tabla usuarios");
                this.frm.txtNroDeIdentificacion.grabFocus();
                return;
            }

            if (!validarContacto(1)) { // Validar si el Nro de identificacion no ha sido registrado
                if (mdlContactoBD.registrar()) {
                    JOptionPane.showMessageDialog(frm, "Contacto registrado correctamente");
                    limpiarControles();
                } else {
                    JOptionPane.showMessageDialog(frm, "Este numero de identificacion ya ha sido registrado, comprueba tu nro de identificacion");
                    this.frm.txtNroDeIdentificacion.grabFocus();
                }
            }else {
                JOptionPane.showMessageDialog(frm, "Nro de identificacion o contacto ya existentes");
                this.frm.txtNroDeIdentificacion.grabFocus();
            }
            
        }
    }
    
    private List<MdlContacto> consultarContactos(){
        return new MdlContactosBD().consultarUsuarios();
    }
    
    private void consultarContacto(){
        if(datosValidos(idDelUsuarioVacio)){

            if (mdlContactoBD == null) {
                mdlContactoBD = new MdlContactosBD();
            }

            MdlContacto per = (MdlContacto) mdlContactoBD.consultar(Long.parseLong(this.frm.txtNroDeIdentificacion.getText()));

            if (per != null) {
                this.frm.txtAgendaId.setText(per.getId().toString());
                this.frm.txtNroDeIdentificacion.setText((per.getNroDeIdentificacion().toString()));
                this.frm.txtContacto.setText(per.getDatoContacto());

                mostrarUsuarioEnLaTabla(per);
            } else {
                JOptionPane.showMessageDialog(frm, "Usuario no encontrado, coloque un numero de identificacion existente");
                limpiarControles();
                this.frm.txtNroDeIdentificacion.grabFocus();
            }
        }
        
    }

    private void actualizarContacto() {
        mdlContactoBD = new MdlContactosBD();
        if (this.frm.txtAgendaId.getText().trim().length() != 0) {
            
            if (!validarContacto(2)) { // Si no existe este dato de contacto ya registrado
                
                if (this.frm.txtContacto.getText().trim().length() != 0) {
                    mdlContactoBD.setDatoContacto(this.frm.txtContacto.getText());

                    if (mdlContactoBD.actualizar(Long.parseLong(this.frm.txtAgendaId.getText()))) {
                        JOptionPane.showMessageDialog(frm, "Usuario actualizado correctamente");
                        limpiarControles();
                    } else {
                        JOptionPane.showMessageDialog(frm, "Primero consulta un usuario antes de actualizarlo");
                    }
                } else {
                    JOptionPane.showMessageDialog(frm, "El campo contacto no puede estar vacio");
                    this.frm.txtContacto.grabFocus();
                }
                
            }else{
                JOptionPane.showMessageDialog(frm, "Contacto ya existentes");
            }
           
        } else {
            JOptionPane.showMessageDialog(frm, "Primero busca el usuario antes de actualizarlo");
            limpiarControles();
        }

    }   
    
    
    private enum opciones{
        butRegistrar, butConsultar, butLimpiar, butActualizar
    }
    
    private void limpiarControles(){
        this.frm.txtAgendaId.setText("");
        this.frm.txtNroDeIdentificacion.setText("");
        this.frm.txtContacto.setText("");
        
        this.mdlContactoBD = new MdlContactosBD();
        this.mdlContactoBD.llenarTabla(this.frm.tblContactos);
    }
    
    private void rellenarCamposConTabla(int fila){
        this.frm.txtAgendaId.setText(this.frm.tblContactos.getValueAt(fila, 0).toString().toUpperCase());
        this.frm.txtNroDeIdentificacion.setText(this.frm.tblContactos.getValueAt(fila, 1).toString().toUpperCase());
        this.frm.txtContacto.setText(this.frm.tblContactos.getValueAt(fila, 4).toString());
    }
    
    private void mostrarUsuarioEnLaTabla(MdlContacto per){
        DefaultTableModel modelo = (DefaultTableModel) this.frm.tblContactos.getModel();
        modelo.getDataVector().clear();
        List fila = new ArrayList();
        fila.add(per.getId());
        fila.add(per.getNroDeIdentificacion());
        fila.add(per.getApellido());
        fila.add(per.getNombre());
        fila.add(per.getDatoContacto());
        fila.add(per.getUsuarioId());
        
        modelo.addRow(fila.toArray());
    }
    
        private void crearReporte() {
        String fileJasper = "/reportes/rptContactos.jasper";
        String fileSalida = "contactos.pdf";
        HashMap hm = new HashMap();
        
        try {
            //llenar el reporte
            InputStream jasperIS = getClass().getResourceAsStream(fileJasper);
            JasperPrint print = JasperFillManager.fillReport( jasperIS, hm, AccesoBD.getInstancia().getConexion() );
            
            //El exportador a PDF
            JRExporter exporter = new JRPdfExporter();
            
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileSalida);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            
            exporter.exportReport();
            
        } catch (JRException e) {
            System.out.println("JR: " + e.toString() );
        }catch (Exception ex) {
            System.out.println("E: " + ex.toString() );
        }
    }
    
    public void iniciar(){
        this.frm.btnRegistrar.setActionCommand("butRegistrar");
        this.frm.btnRegistrar.addActionListener(this);
        
        this.frm.btnConsultar.setActionCommand("butConsultar");
        this.frm.btnConsultar.addActionListener(this);
        
        this.frm.btnLimpiar.setActionCommand("butLimpiar");
        this.frm.btnLimpiar.addActionListener(this);
        
        this.frm.btnActualizar.setActionCommand("butActualizar");
        this.frm.btnActualizar.addActionListener(this);
        
        this.frm.btnReporte.setActionCommand("butReporte");
        this.frm.btnReporte.addActionListener(this);
        
        this.frm.txtNroDeIdentificacion.addKeyListener(this);
        
        this.frm.tblContactos.addMouseListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent arg0) {
        if (arg0.getSource() == this.frm.txtNroDeIdentificacion) {
            if (Character.isLetter(arg0.getKeyChar())) {
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
        if (e.getSource() == this.frm.tblContactos) {
            if ((e.getButton() == 1) && (e.getClickCount() == 2)) {
                rellenarCamposConTabla(((JTable) e.getSource()).getSelectedRow());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch(String.valueOf(arg0.getActionCommand())){
            case "butRegistrar":
                registrarContacto();
                break;
            case "butConsultar":
                consultarContacto();
                break;      
            case "butLimpiar":
                limpiarControles();
                break;
            case "butActualizar":
                actualizarContacto();
                break;
            case "butReporte":
                crearReporte();
                break;
        }
    }
}
