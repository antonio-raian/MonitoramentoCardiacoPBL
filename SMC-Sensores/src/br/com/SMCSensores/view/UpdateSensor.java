/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCSensores.view;

import br.com.SMCSensores.conection.ConectionSensor;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author raymendesjr
 */
//Tela para atualizar os dados do sensor
public class UpdateSensor extends java.awt.Dialog {

    /**
     * Creates new form UpdateSensor
     * @param parent
     * @param modal
     */
    private String endereco;
    private int porta;
    public UpdateSensor(java.awt.Frame parent, boolean modal, String endereco, String porta) throws IOException, ClassNotFoundException {
        super(parent, modal);
        this.endereco = endereco;
        this.porta = Integer.parseInt(porta);
        conection();
        initComponents();        
        init();
    }
    public UpdateSensor(java.awt.Frame parent, boolean modal) throws IOException, ClassNotFoundException {
        super(parent, modal);
        init();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnSair = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbxSensores = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        lbMove = new javax.swing.JLabel();
        slMove = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        lbRitmo = new javax.swing.JLabel();
        slRitmo = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        slSistole = new javax.swing.JSlider();
        lbPressao = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        slDiastole = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();

        setLocation(new java.awt.Point(500, 100));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        jPanel2.add(btnSair);

        jLabel5.setText("Sensor:");
        jPanel3.add(jLabel5);

        cbxSensores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSensoresActionPerformed(evt);
            }
        });
        jPanel3.add(cbxSensores);

        lbMove.setText(labelMovimento()
        );

        slMove.setMaximum(2);
        slMove.setValue(0);
        slMove.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                slMoveMouseDragged(evt);
            }
        });

        jLabel6.setText("Movimento:");

        lbRitmo.setText(slRitmo.getValue()+"");

        slRitmo.setMaximum(160);
        slRitmo.setMinimum(40);
        slRitmo.setValue(0);
        slRitmo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                slRitmoMouseDragged(evt);
            }
        });

        jLabel3.setText("   Ritmo Cardiaco:");

        jLabel7.setText("Sístole:");

        jLabel8.setText("Diástole");

        slSistole.setMaximum(20);
        slSistole.setValue(12);
        slSistole.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                slSistoleMouseDragged(evt);
            }
        });

        jLabel4.setText("Pressão sanguínea:");

        slDiastole.setMaximum(12);
        slDiastole.setValue(8);
        slDiastole.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                slDiastoleMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slDiastole, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slSistole, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbPressao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(slMove, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(slRitmo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbRitmo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMove)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slMove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbMove))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbRitmo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slRitmo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPressao, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(slSistole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(slDiastole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(81, 81, 81))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Atualiza Dados");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    //action do botão de sair
    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        timer.cancel();//Cancela a thread de atualização altomatica
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    //action do combobox, serve pra atualizar os dados da tela ao selecionar um item
    private void cbxSensoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSensoresActionPerformed
        String[] aux = str[cbxSensores.getSelectedIndex()].split("-");//Divide o nome do nick
        nick = aux[0];//coloca o nick numa variavel global
        nome = aux[1];//coloca o nome numa variavel global
        String[] valores; //Cria um vetor de String
        try {
            valores = achaValores(nick);//Encontra o restante dos valores para o nick
            slMove.setValue(Integer.parseInt(valores[2]));//seta o valor no jSlider de movimento
            lbMove.setText(labelMovimento());//coloca valores referentes aos jSliders nos labels
            slRitmo.setValue(Integer.parseInt(valores[3]));//seta o valor no jSlider de ritmo
            lbRitmo.setText(slRitmo.getValue()+"");//coloca valores referentes aos jSliders nos labels
            slSistole.setValue(Integer.parseInt(valores[4]));//seta o valor no jSlider da sistole
            slDiastole.setValue(Integer.parseInt(valores[5]));//seta o valor no jSlider da diastole
            lbPressao.setText(labelPressao());//coloca valores referentes aos jSliders nos labels
        } catch (IOException ex) {
            Logger.getLogger(UpdateSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbxSensoresActionPerformed

    //Metodo para alterar os valores do Label de ritmo a medida que move o cursor
    private void slRitmoMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slRitmoMouseDragged
        lbRitmo.setText(slRitmo.getValue()+"");
    }//GEN-LAST:event_slRitmoMouseDragged

    //Metodo para alterar os valores do Label de movimento a medida que move o cursor
    private void slMoveMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slMoveMouseDragged
        lbMove.setText(labelMovimento());
    }//GEN-LAST:event_slMoveMouseDragged

    //Metodo para alterar os valores do Label de pressao a medida que move o cursor
    private void slSistoleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slSistoleMouseDragged
        lbPressao.setText(labelPressao());
    }//GEN-LAST:event_slSistoleMouseDragged

    //Metodo para alterar os valores do Label de pressao a medida que move o cursor
    private void slDiastoleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_slDiastoleMouseDragged
        lbPressao.setText(labelPressao());
    }//GEN-LAST:event_slDiastoleMouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UpdateSensor dialog = null;
                try {
                    dialog = new UpdateSensor(new java.awt.Frame(), true);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateSensor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UpdateSensor.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cbxSensores;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbMove;
    private javax.swing.JLabel lbPressao;
    private javax.swing.JLabel lbRitmo;
    private javax.swing.JSlider slDiastole;
    private javax.swing.JSlider slMove;
    private javax.swing.JSlider slRitmo;
    private javax.swing.JSlider slSistole;
    // End of variables declaration//GEN-END:variables
    private ConectionSensor conect; //Responsável pela conexão
    private DefaultComboBoxModel sensores; //Responsável pela lista usado no combobox
    private String[] str;//Array de string responsável por receber todos os pacientes
    private final int delay = 10000;   // delay de 10 seg.
    private final int intervalo = 5000;  // intervalo de 5 seg.
    Timer timer = new Timer();//Thread responsável por atualizar altomaticamente
    private String nick;
    private String nome;
    
    //Metodo que faz a conexão com o servidor
    private void conection() throws IOException{
        conect = new ConectionSensor(endereco, porta);//Cria-se uma instancia para a classe de conexão
        conect.conectar();//Usa o metodo de conexão
    }
    
    //Metodo que inicia o combobox
    private void init() throws IOException, ClassNotFoundException{
        conection();//conecta-se ao servidor
        str = conect.listarSensores().split("#");//solicita a lista de paciente e a separa pelos "#"
        
        if(str!=null){//Se não estiver nulo
            sensores = new DefaultComboBoxModel(str);//Transforma o array de String na lista suportada pelo combobox
            cbxSensores.setModel(sensores);//Seta a lista no combobox
        }else{
            throw new IOException("Sem clientes cadastrados!");
        }
        //Função q fica atualizando a cada 5 segundos
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    atualiza();//A cada 5 segundos a thread chama o metodo de atualização
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Falha ao atualizar!");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UpdateSensor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, delay, intervalo);
    }
    
    //Metodo que solicita informações de um paciente a partir do seu nick
    private String[] achaValores(String nick) throws IOException{
        conection();//Conecta-se
        return conect.getPaciente(nick).split("#");//Solicita as informações do paciente e Separa todas a partir do "#"
    }
    
    //Metodo que coleta os dados da tela e envia para serem atualizados
    private void atualiza() throws IOException, ClassNotFoundException{
        //Coleta-se todas as informações
        int move = slMove.getValue(), sistole = slSistole.getValue(),diastole = slDiastole.getValue();
        int ritmo = slRitmo.getValue();
        
        conection();//Conecta-se ao servidor
        String s = conect.atualizarSensor(nick,nome, move, ritmo, sistole, diastole);//Envia os dados para atualização
        if(s.equals("$FALHA$")){//Se a resposta do servidor for FALHA
            throw new IOException("Falha ao atualizar!");//é lançada a exceção de falha
        }
    }
   //Metodo usado para alterar o valor do label na tela dependendo do valor do jSlider
    private String labelMovimento(){
        switch (slMove.getValue()){
            case 0:
                return "Repouso";
            case 1:
                return "Moderado";
            case 2:
                return "Esporte";
            default:
                return null;
        }
    }
    
    //Metodo usado para setar valores no label de pressão dependendo dos valores do label
    private String labelPressao(){
        return slSistole.getValue()+"/"+slDiastole.getValue();
    }
}