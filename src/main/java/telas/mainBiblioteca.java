package telas;

import classes.bibliotecaClass;
import com.mycompany.estudex.index;
import com.mycompany.estudex.mainIndex;
import dialogos.addLivroDlg;
import dialogos.editLivroDlg;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class mainBiblioteca extends javax.swing.JFrame {

    public mainBiblioteca() {
        initComponents();
        setTitle("Biblioteca");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        addLivroDlg addDlg = new addLivroDlg(() -> carregarTabelaLivros()); // passa callback

        getRootPane().getActionMap().put("addLivroDlg", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDlg.setVisible(true);
            }
        });

        //quando fechar a janela, volta para o index
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new index().setVisible(true);
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "addLivroDlg");

        getRootPane().getActionMap().put("addLivroDlg", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDlg.setVisible(true);
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "recarregarTabela");

        getRootPane().getActionMap().put("recarregarTabela", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarTabelaLivros();
            }
        });

        //carrega a tabela dos livros (não acredito que estou tendo que escrever isso, pois
        //provavelmente alguém não saberia o que isso faz... nunca sobre nada pro beta)
        carregarTabelaLivros();

        //menu de contexto de botão direito
        buttonList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = buttonList.getSelectedRow();
                if (SwingUtilities.isLeftMouseButton(e) && row != -1) {
                    mostrarLivroNaTela(row);
                } else if (SwingUtilities.isRightMouseButton(e) && row != -1) {
                    mostrarMenuContexto(e, row);
                }
            }
        });

        // Listener da JTable
        buttonList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = buttonList.getSelectedRow();
                if (row != -1) {
                    mostrarLivroNaTela(row);
                }
            }
        });
        
        //icones do botão
        
        homeBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24));
        homeBtn.setText("\ue80f");
        
        bibliotecaBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24));
        bibliotecaBtn.setText("\ue8f1");
        
        timerBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24)); // NOI18N
        timerBtn.setText("\ue823");
        
        documentsBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24)); // NOI18N
        documentsBtn.setText("\ue7c3");
        
        tarefasBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24)); // NOI18N
        tarefasBtn.setText("\ue71d");
        
        notasBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24)); // NOI18N
        notasBtn.setText("\ue70b");
        
        sobreBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 24)); // NOI18N
        sobreBtn.setText("\ue946");
    }

    public void carregarTabelaLivros() {
        JSONArray livros = bibliotecaClass.getLivros();
        String[] colunas = {""};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        for (int i = 0; i < livros.length(); i++) {
            JSONObject livro = livros.getJSONObject(i);
            model.addRow(new Object[]{livro.getString("nome")});
        }
        buttonList.setModel(model);
    }

    private void mostrarLivroNaTela(int index) {
        JSONArray livros = bibliotecaClass.getLivros();
        if (index >= 0 && index < livros.length()) {
            JSONObject livro = livros.getJSONObject(index);
            painelCentral.removeAll();
            painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

            String tituloOriginal = livro.getString("nome");
            FontMetrics metrics = getFontMetrics(new Font("Arial", Font.BOLD, 20));
            int larguraMaxima = 480;

            String tituloExibido = tituloOriginal;
            while (metrics.stringWidth(tituloExibido + "...") > larguraMaxima && tituloExibido.length() > 0) {
                tituloExibido = tituloExibido.substring(0, tituloExibido.length() - 1);
            }
            if (!tituloExibido.equals(tituloOriginal)) {
                tituloExibido += "...";
            }

            JTextField tituloField = new JTextField(tituloExibido);
            tituloField.setFont(new Font("Arial", Font.BOLD, 20));
            tituloField.setEditable(false);
            tituloField.setOpaque(false);
            tituloField.setFocusable(false);
            tituloField.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            tituloField.setMaximumSize(new Dimension(500, 35));
            tituloField.setAlignmentX(Component.LEFT_ALIGNMENT);
            tituloField.setToolTipText(tituloOriginal); // Exibe completo no hover

            painelCentral.add(tituloField);

            nomeLabel("Por " + livro.getString("autor") + " - " + livro.getInt("ano"), Font.PLAIN, 16);
            nomeLabel(livro.getInt("paginas") + " páginas", Font.PLAIN, 16);
            nomeLabel(livro.getString("genero"), Font.PLAIN, 16);

            nomeLabel("", Font.PLAIN, 16); // esse é literalmente
            nomeLabel("", Font.PLAIN, 16); // o jeito mais podre
            nomeLabel("", Font.PLAIN, 16); // de deixa
            nomeLabel("", Font.PLAIN, 16); // quebra de linha 
            nomeLabel("", Font.PLAIN, 16); // no layout

            nomeLabel("Descrição:", Font.PLAIN, 16);

            JTextArea descricaoArea = new JTextArea(livro.getString("descricao"));
            descricaoArea.setFont(new Font("Arial", Font.PLAIN, 14));
            descricaoArea.setLineWrap(true);
            descricaoArea.setWrapStyleWord(true);
            descricaoArea.setEditable(false);
            descricaoArea.setBackground(painelCentral.getBackground());
            descricaoArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            descricaoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCentral.add(descricaoArea);

            JScrollPane scrollDescricao = new JScrollPane(descricaoArea);
            scrollDescricao.setPreferredSize(new Dimension(500, 200));
            scrollDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);
            scrollDescricao.setBorder(BorderFactory.createEmptyBorder());
            painelCentral.add(scrollDescricao);

            painelCentral.revalidate();
            painelCentral.repaint();
        }
    }

    private void nomeLabel(String text, int style, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", style, size));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        painelCentral.add(label);
    }

    private void mostrarMenuContexto(MouseEvent e, int row) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem editar = new JMenuItem("Editar");
        editar.addActionListener(ae -> editarLivro(row));
        menu.add(editar);

        JMenuItem excluir = new JMenuItem("Excluir");
        excluir.addActionListener(ae -> excluirLivro(row));
        menu.add(excluir);

        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void editarLivro(int index) {
        JSONArray livros = bibliotecaClass.getLivros();
        JSONObject livro = livros.getJSONObject(index);

        editLivroDlg editDlg = new editLivroDlg(livro, index, () -> carregarTabelaLivros());
        editDlg.setVisible(true);
    }

    private void excluirLivro(int index) {
        bibliotecaClass.removerLivro(index);
        carregarTabelaLivros();
        painelCentral.removeAll();
        painelCentral.revalidate();
        painelCentral.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        painelCentral = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        documentsBtn = new javax.swing.JButton();
        tarefasBtn = new javax.swing.JButton();
        notasBtn = new javax.swing.JButton();
        bibliotecaBtn = new javax.swing.JButton();
        timerBtn = new javax.swing.JButton();
        homeBtn = new javax.swing.JButton();
        sobreBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        buttonList = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        addLivroBtn = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1400, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        painelCentral.setLayout(new javax.swing.BoxLayout(painelCentral, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(painelCentral, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 1030, 630));

        jPanel1.setBackground(new java.awt.Color(48, 52, 63));
        jPanel1.setForeground(new java.awt.Color(48, 52, 63));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        documentsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentsBtnActionPerformed(evt);
            }
        });
        jPanel1.add(documentsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 50, 50));

        tarefasBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarefasBtnActionPerformed(evt);
            }
        });
        jPanel1.add(tarefasBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 50, 50));

        notasBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notasBtnActionPerformed(evt);
            }
        });
        jPanel1.add(notasBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 50, 50));

        bibliotecaBtn.setFont(new java.awt.Font("Segoe Fluent Icons", 0, 12)); // NOI18N
        bibliotecaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bibliotecaBtnActionPerformed(evt);
            }
        });
        jPanel1.add(bibliotecaBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 50, 50));

        timerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timerBtnActionPerformed(evt);
            }
        });
        jPanel1.add(timerBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 50, 50));

        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });
        jPanel1.add(homeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 50));

        sobreBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sobreBtnActionPerformed(evt);
            }
        });
        jPanel1.add(sobreBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 710, 50, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 780));

        buttonList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        buttonList.setPreferredSize(new java.awt.Dimension(75, 90));
        jScrollPane1.setViewportView(buttonList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 250, 780));

        jMenu2.setText("Livros");

        addLivroBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        addLivroBtn.setText("Adicionar");
        addLivroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLivroBtnActionPerformed(evt);
            }
        });
        jMenu2.add(addLivroBtn);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    addLivroDlg add = new addLivroDlg();

    private void addLivroBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLivroBtnActionPerformed
        new addLivroDlg();
        add.setVisible(true);
    }//GEN-LAST:event_addLivroBtnActionPerformed

    private void documentsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentsBtnActionPerformed
        mainDocumento d = new mainDocumento();
        d.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_documentsBtnActionPerformed

    private void tarefasBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarefasBtnActionPerformed
        mainTarefas tar = new mainTarefas();
        tar.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_tarefasBtnActionPerformed

    private void notasBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notasBtnActionPerformed
        mainNotas n = new mainNotas();
        n.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_notasBtnActionPerformed

    private void bibliotecaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bibliotecaBtnActionPerformed
        mainBiblioteca b = new mainBiblioteca();
        b.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_bibliotecaBtnActionPerformed

    private void timerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timerBtnActionPerformed
        mainTimer tim = new mainTimer();
        tim.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_timerBtnActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed

    }//GEN-LAST:event_homeBtnActionPerformed

    private void sobreBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sobreBtnActionPerformed
        mainSobre s = new mainSobre();
        s.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_sobreBtnActionPerformed

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
            java.util.logging.Logger.getLogger(mainBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainBiblioteca().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addLivroBtn;
    private javax.swing.JButton bibliotecaBtn;
    private javax.swing.JTable buttonList;
    private javax.swing.JButton documentsBtn;
    private javax.swing.JButton homeBtn;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton notasBtn;
    private javax.swing.JPanel painelCentral;
    private javax.swing.JButton sobreBtn;
    private javax.swing.JButton tarefasBtn;
    private javax.swing.JButton timerBtn;
    // End of variables declaration//GEN-END:variables
}
