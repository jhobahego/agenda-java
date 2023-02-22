package modelos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class AccesoBD {
    
    private static AccesoBD instancia = null;
    
    private boolean conectado = false;
    
    private Connection conexion = null;
    
    String laBaseDeDatos;
    String ipBD;
    String puertoBD;
    String usuarioBD;
    String passBD;
    
    private AccesoBD(){
    }
    
    public static AccesoBD getInstancia(){
        if(instancia == null){
            instancia = new AccesoBD();
        }
        return instancia;
    }
    
    public boolean crearConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Driver encontrado correctamente: ACCESOBD");
        }catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null,"ERROR CON EL DRIVER DE CONEXION\n" + e, "", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        try{
            String url = "jdbc:mysql://"+ipBD+":"+puertoBD+"/"+laBaseDeDatos+"?useSSL=false";
            conexion = DriverManager.getConnection(url,usuarioBD, passBD);
            conectado = true;
            System.out.println("Conectado exitosamente");
        }catch(SQLException e){
            System.out.println("FALLO AL CREAR CONEXION EN ACCESOBD");
            JOptionPane.showMessageDialog(null,"ERROR AL CREAR LA CONEXION\n" + e.toString(), "Conexión Cerrada", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
    
    public boolean cerrarConexion(){
        try {
            conexion.close();
            conexion = null;
            conectado = false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"ERROR AL CERRAR LA CONEXION\n" + e.toString(), "Conexión Cerrada", JOptionPane.INFORMATION_MESSAGE);
           return false;
        }
        return true;
    }
    
    public Connection getConexion(String baseDeDatos, String ip, String puerto, String usuario, String pass){
        laBaseDeDatos = baseDeDatos;
        ipBD = ip;
        puertoBD = puerto;
        usuarioBD = usuario;
        passBD = pass;
        
        if(conexion == null){
            crearConexion();
        }
        return conexion;
    }
    
    public Connection getConexion(){
        if(conexion == null){
            crearConexion();
        }
        return conexion;
    }
}
