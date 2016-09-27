/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aula05.oracleinterface;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
        System.out.println("Hello world!");
        conectar();
        DDLLogin();
    }

    public boolean conectar(){       
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Dentro dos laboratorios
            /*connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@192.168.183.15:1521:orcl",
                    "a8532321",
                    "a8532321");*/
            // Fora dos laboratorios
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl",
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
    public void displayTableNames(JComboBox jc){
        String s = "";
        try {
            s = "select table_name from user_tables";
            rs = stmt.executeQuery(s);
            while (rs.next()) {
                jc.addItem(rs.getString("table_name"));
            }
            s = "select view_name from user_views";
            rs = stmt.executeQuery(s);
            while (rs.next()) {
                jc.addItem(rs.getString("view_name") + " view");
            }
            rs.close();
        } catch (SQLException ex) {
            //jtAreaDeStatus.setText("Erro na consulta: \"" + s + "\"");
            jtAreaDeStatus.setText(ex.getMessage());
        }

    }

    /**
     * Carrega o conteudo de uma tabela selecionada para a JTable
     * @param tablemodel
     * @param name
     */
    public void loadDataToTable(DefaultTableModel tablemodel, String name) {

        try {
            rs = stmt.executeQuery("select * from "+ name);
            ResultSetMetaData metaData = rs.getMetaData();

            // Names of columns
            Vector<String> columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            // Data of the table
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            tablemodel.setDataVector(data, columnNames);
        } catch (Exception ex) {
            jtAreaDeStatus.setText(ex.getMessage());
        }
    }

    /**
     * Mostra o menu de inserção com as colunas de cada tabela
     * @param insertPane O painel de inserção
     * @param name O nome da tabela a receber uma inserção
     * @return
     */
    public Vector<Pair<JTextField,String>> displayInsertionMenu(JPanel insertPane, String name) {

        try {

            rs = stmt.executeQuery("select * from "+ name);
            ResultSetMetaData metaData = rs.getMetaData();
            // Names of columns
            int columnCount = metaData.getColumnCount();

            insertPane.setLayout(new GridLayout(columnCount, 2));
            insertPane.removeAll();

            Vector<Pair<JTextField,String>> data = new Vector<>();

            for (int i = 1; i <= columnCount; i++) {
                JTextField field = new JTextField("Digite o valor da coluna");
                insertPane.add(new JLabel(metaData.getColumnName(i)));
                insertPane.add(field);
                data.add(new Pair<>(field, metaData.getColumnClassName(i)));
            }
            return data;
        } catch (SQLException ex) {
            jtAreaDeStatus.setText(ex.getMessage());
        }
        return null;
    }

    /**
     * Insere uma nova tupla na tabela selecionada pelo JComboBox
     * @param tupla Par com o nome do atributo e o tipo a ser inserido
     * @param name O nome da tabela
     */
    public void insertTuple(Vector<Pair<JTextField,String>> tupla, String name) {

        String query = "insert into "+ name+ " values (";

        for(Pair<JTextField,String> t : tupla) {
            if(Objects.equals(t.getRight(), "java.lang.String")) {
                query += "'"+t.getLeft().getText()+"'"+",";
            }
            else if( Objects.equals(t.getRight(), "java.math.BigDecimal")) {
                query += t.getLeft().getText()+",";
            }
        }
        query = query.substring(0,query.length() - 1) + ")";
        System.out.println(query);

        try {
            rs = stmt.executeQuery(query);
            jtAreaDeStatus.setText("Tupla inserida com sucesso");
        } catch (SQLException ex) {
            jtAreaDeStatus.setText(ex.getMessage());
        }

    }
    public String DDLLogin(){
        String s = "";
        String saida = "";
        ResultSet retorno;
        try {
            PreparedStatement psts = connection.prepareStatement("EXEC dbms_metadata.set_transform_param(dbms_metadata.session_transform,'STORAGE',false)" +
                                                                 "EXEC dbms_metadata.set_transform_param(dbms_metadata.session_transform,'SEGMENT_ATTRIBUTES',false)" +
                                                                 "EXEC dbms_metadata.set_transform_param(dbms_metadata.session_transform,'SQLTERMINATOR',true)");
            s = "select table_name from user_tables";
            rs = stmt.executeQuery(s);
            while (rs.next()) {
                s = "SELECT dbms_metadata.get_ddl('TABLE','" + rs.getString(1) + "') AS DDL FROM DUAL";
                System.out.println(s);
                retorno = psts.executeQuery(s);
                while(retorno.next()){
                   // System.out.println(retorno.getString(1));
                    saida = saida + "\n" + retorno.getString(1); }
            }
            
        } catch (SQLException ex) {
            //jtAreaDeStatus.setText("Erro na consulta: \"" + s + "\"");
            jtAreaDeStatus.setText(ex.getMessage());
            saida = ex.getMessage();
        }    
        return saida;
    }

  }