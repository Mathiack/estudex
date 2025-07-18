package timer;

import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import org.json.JSONObject;
import timer.mainTimer;

public class editPomodoroDlg extends javax.swing.JFrame {

    private mainTimer mainRef;
    private int index;

    public editPomodoroDlg(mainTimer parent, int index, JSONObject timer) {
        this.mainRef = parent;
        this.index = index;
        initComponents();
        setTitle("Novo Pomodoro");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(parent);
        setResizable(false);

        inputTituloPomodoro.setText(timer.optString("titulo"));
        inputAtivo.setText(String.valueOf(timer.optInt("trabalho")));
        inputPausa.setText(String.valueOf(timer.optInt("descanso")));

        // maskformatter para formatação dos campos de tempo
        try {
            MaskFormatter mascara = new MaskFormatter("##:##:##");
            mascara.setPlaceholderCharacter('0');
            inputAtivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(mascara));
            inputPausa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(mascara));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inputTituloPomodoro = new javax.swing.JTextField();
        cancelBtn = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        inputPausa = new javax.swing.JFormattedTextField();
        inputAtivo = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Descanso");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        jLabel2.setText("Tempo ativo");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel3.setText("Nome do Pomodoro");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));
        getContentPane().add(inputTituloPomodoro, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 220, -1));

        cancelBtn.setText("Cancelar");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        getContentPane().add(cancelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 90, -1));

        addButton.setText("Adicionar");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        getContentPane().add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 90, -1));

        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Formato hh:mm:ss");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 140, -1));
        getContentPane().add(inputPausa, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 100, -1));
        getContentPane().add(inputAtivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 100, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        adicionarPomodoro();
    }//GEN-LAST:event_addButtonActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    //adiciona o pomodoro
    private void adicionarPomodoro() {
        String titulo = inputTituloPomodoro.getText().trim();
        int trabalho, descanso;

        try {
            trabalho = Integer.parseInt(inputAtivo.getText().trim());
            descanso = Integer.parseInt(inputPausa.getText().trim());

            JSONObject novoTimer = new JSONObject();
            novoTimer.put("titulo", titulo);
            novoTimer.put("tipo", "pomodoro");
            novoTimer.put("trabalho", trabalho);
            novoTimer.put("descanso", descanso);

            mainRef.atualizarTimerEditado(index, novoTimer);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Preencha corretamente todos os campos.");
        }
    }

    //converte os valores dos campos de tempo para minutos (meio óbvio né)
    private int converterParaMinutos(String tempo) throws Exception {
        String[] partes = tempo.split(":");
        if (partes.length != 3) {
            throw new Exception("Formato inválido (use hh:mm:ss)");
        }
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        int segundos = Integer.parseInt(partes[2]);
        return horas * 60 + minutos + (segundos >= 30 ? 1 : 0); // arredonda se necessário
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(editPomodoroDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(editPomodoroDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(editPomodoroDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(editPomodoroDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Mock de dados apenas para testar o dialogo
            mainTimer dummyParent = new mainTimer();

            JSONObject timerMock = new JSONObject();
            timerMock.put("titulo", "Teste Pomodoro");
            timerMock.put("tipo", "pomodoro");
            timerMock.put("trabalho", 25);
            timerMock.put("descanso", 5);

            editPomodoroDlg dialog = new editPomodoroDlg(dummyParent, 0, timerMock);
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JFormattedTextField inputAtivo;
    private javax.swing.JFormattedTextField inputPausa;
    private javax.swing.JTextField inputTituloPomodoro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
