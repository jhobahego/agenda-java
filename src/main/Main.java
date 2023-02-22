package main;

import java.sql.Connection;
import javax.swing.JFrame;
import modelos.AccesoBD;
import vistas.FrmPrincipal;

public class Main {
    public static void main(String[] args) {
        Connection conx = AccesoBD.getInstancia().getConexion("agenda_contactos_lengpro2", "127.0.0.1", "3306", "root", "");
        
        FrmPrincipal ventanaPrincipal = new FrmPrincipal();
        ventanaPrincipal.setVisible(true);
        ventanaPrincipal.setLocationRelativeTo(null);
        ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
