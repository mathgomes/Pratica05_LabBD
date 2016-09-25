/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aula05.oracleinterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author junio
 */
public class JanelaPrincipal {

    private JFrame j;
    private JPanel pPainelDeCima;
    private JPanel pPainelDeBaixo;
    private JComboBox jc;
    private JButton insertButton;
    private JTextArea jtAreaDeStatus;
    private JTabbedPane tabbedPane;
    private JPanel pPainelDeExibicaoDeDados;
    private JTable jt;
    private JPanel pPainelDeInsecaoDeDados;
    private DBFuncionalidades bd;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private Vector<Pair<JTextField,String>> tupla;

    public void ExibeJanelaPrincipal() {
        /*Janela*/
        j = new JFrame("ICMC-USP - SCC0241 - Pratica 5");
        j.setSize(700, 500);
        j.setLayout(new BorderLayout());
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*Painel da parte superior (north) - com combobox e outras informações*/
        pPainelDeCima = new JPanel();
        j.add(pPainelDeCima, BorderLayout.NORTH);
        jc = new JComboBox();
        insertButton = new JButton("Inserir nova tupla");
        pPainelDeCima.add(jc);
        pPainelDeCima.add(insertButton);

        /*Painel da parte inferior (south) - com área de status*/
        pPainelDeBaixo = new JPanel();
        j.add(pPainelDeBaixo, BorderLayout.SOUTH);
        jtAreaDeStatus = new JTextArea();
        jtAreaDeStatus.setText("Aqui é sua área de status");
        pPainelDeBaixo.add(jtAreaDeStatus);

        /*Painel tabulado na parte central (CENTER)*/
        tabbedPane = new JTabbedPane();
        j.add(tabbedPane, BorderLayout.CENTER);

        /*Tab de exibicao*/
        pPainelDeExibicaoDeDados = new JPanel();
        pPainelDeExibicaoDeDados.setLayout(new GridLayout(1, 1));
        tabbedPane.add(pPainelDeExibicaoDeDados, "Exibição");

        /*Table de exibição*/
        jt = new JTable(tableModel);
        JScrollPane jsp = new JScrollPane(jt);
        pPainelDeExibicaoDeDados.add(jsp);

        /*Tab de inserção*/
        pPainelDeInsecaoDeDados = new JPanel();
        tabbedPane.add(pPainelDeInsecaoDeDados, "Inserção");


        j.setVisible(true);

        bd = new DBFuncionalidades(jtAreaDeStatus);
        if (bd.conectar()) {
            //System.out.println("Hello world!");
            bd.displayTableNames(jc);
            //bd.DDLLogin();


        }
        this.DefineEventos();
    }

    private void DefineEventos() {
        jc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox jcTemp = (JComboBox) e.getSource();
                String selected = (String)jcTemp.getSelectedItem();
                jtAreaDeStatus.setText(selected);
                bd.loadDataToTable(tableModel,selected);
                tupla = bd.displayInsertionMenu(pPainelDeInsecaoDeDados,selected);
                for( Pair<JTextField,String> t : tupla) {
                    System.out.println(t.getLeft().getText() + ", " + t.getRight());
                    if(Objects.equals(t.getRight(), "java.lang.String")) System.out.println("it is");
                }
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bd.insertTuple(tupla,(String)jc.getSelectedItem());
            }
        });
    }
}
