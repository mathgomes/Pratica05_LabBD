/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aula05.oracleinterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTextArea jtAreaDeStatus;
    private JTabbedPane tabbedPane;
    private JPanel pPainelDeExibicaoDeDados;
    private JTable jt;
    private JPanel pPainelDeInsecaoDeDados;
    private DBFuncionalidades bd;
    private final DefaultTableModel tableModel = new DefaultTableModel();

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
        pPainelDeCima.add(jc);

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
        int nColunas = 3;
        /*
        String colunas[] = new String[nColunas];
        colunas[0] = "Coluna1";
        colunas[1] = "Coluna2";
        colunas[2] = "Coluna3";
        int nTuplas = 4;
        String dados[][] = new String[nTuplas][nColunas];
        dados[0][0] = "d00";
        dados[0][1] = "d10";
        dados[0][2] = "d20";
        dados[1][0] = "d10";
        dados[1][1] = "d11";
        dados[1][2] = "d21";
        dados[2][0] = "d20";
        dados[2][1] = "d12";
        dados[2][2] = "d22";
        dados[3][0] = "d30";
        dados[3][1] = "d13";
        dados[3][2] = "d23";
        jt = new JTable(dados, colunas);
        */
        jt = new JTable(tableModel);
        JScrollPane jsp = new JScrollPane(jt);
        pPainelDeExibicaoDeDados.add(jsp);

        /*Tab de inserção*/
        pPainelDeInsecaoDeDados = new JPanel();
        pPainelDeInsecaoDeDados.setLayout(new GridLayout(nColunas, 2));
        pPainelDeInsecaoDeDados.add(new JLabel("Coluna1"));
        pPainelDeInsecaoDeDados.add(new JTextField("Digite aqui"));
        pPainelDeInsecaoDeDados.add(new JLabel("Coluna2"));
        pPainelDeInsecaoDeDados.add(new JTextField("Digite aqui"));
        pPainelDeInsecaoDeDados.add(new JLabel("Coluna3"));
        pPainelDeInsecaoDeDados.add(new JTextField("Digite aqui"));
        tabbedPane.add(pPainelDeInsecaoDeDados, "Inserção");

        this.DefineEventos();
        j.setVisible(true);

        bd = new DBFuncionalidades(jtAreaDeStatus);
        if (bd.conectar()) {
            //System.out.println("Hello world!");
            bd.displayTableNames(jc);
            //bd.DDLLogin();


        }
    }

    private void DefineEventos() {
        jc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        JComboBox jcTemp = (JComboBox) e.getSource();
                        jtAreaDeStatus.setText((String) jcTemp.getSelectedItem());
                        bd.loadDataToTable(tableModel,(String) jcTemp.getSelectedItem());
                        return null;
                    }
                }.execute();
            }
        });
    }
}
