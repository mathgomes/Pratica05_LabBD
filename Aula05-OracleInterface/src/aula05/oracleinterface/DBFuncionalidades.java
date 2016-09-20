/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aula05.oracleinterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author junio
 */
public class DBFuncionalidades {
    Connection connection;
    Statement stmt;


    ResultSet rs;
    JTextArea jtAreaDeStatus;
    
    public DBFuncionalidades(JTextArea jtaTextArea){
        jtAreaDeStatus = jtaTextArea;
    }

    public boolean conectar(){       
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@192.168.183.15:1521:orcl",
                    "a8532321",
                    "a8532321");
            stmt = connection.createStatement();
            return true;
        } catch (ClassNotFoundException ex) {
            jtAreaDeStatus.setText("Problema: verifique o driver do banco de dados");
        } catch(SQLException ex){
            jtAreaDeStatus.setText("Problema: verifique seu usuário e senha");
        }
        
        return false;
    }
        public void displayTableNames(JComboBox jc, String query){
        try {

            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                
            }
            stmt.close();
        } catch (SQLException ex) {
            //jtAreaDeStatus.setText("Erro na consulta: \"" + s + "\"");
            jtAreaDeStatus.setText(ex.getMessage());
        }        
    }
    public void displayTableNames(JComboBox jc){
        String s = "";
        try {
            s = "select view_name from user_views";
            rs = stmt.executeQuery(s);
            while (rs.next()) {
                jc.addItem(rs.getString("view_name") + " view");
            }
            s = "select table_name from user_tables";
            rs = stmt.executeQuery(s);
            while (rs.next()) {
                jc.addItem(rs.getString("table_name"));
            }
        } catch (SQLException ex) {
            //jtAreaDeStatus.setText("Erro na consulta: \"" + s + "\"");
            jtAreaDeStatus.setText(ex.getMessage());
        }        
    }

    public void exibeDados(JTable tATable, String sTableName){
        
    }
    //public void preencheComboBoxComRestricoesDeCheck
    //public void preencheComboBoxComValoresReferenciados
    //
    public Statement getStmt() {
        return stmt;
    }
}
