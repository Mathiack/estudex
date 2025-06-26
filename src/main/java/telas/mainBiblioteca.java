package telas;

import classes.bibliotecaClass;
import com.mycompany.estudex.index;
import dialogos.addLivroDlg;
import dialogos.editLivroDlg;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class mainBiblioteca extends javax.swing.JFrame {

    JTextArea descricaoArea;

    public mainBiblioteca() {
        initComponents();
        setTitle("Biblioteca");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        carregarTabelaLivros();

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

            JLabel tituloLabel = new JLabel("<html><body style='width: 500px;'><p style='margin: 0;'><b>" + livro.getString("nome") + "</b></p></body></html>");
            tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
            tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
            tituloLabel.setOpaque(false);
            tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelCentral.add(tituloLabel);

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
        painelLateral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        buttonList = new javax.swing.JTable();
        painelCentral = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        addLivroBtn = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1400, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jScrollPane1.setViewportView(buttonList);

        javax.swing.GroupLayout painelLateralLayout = new javax.swing.GroupLayout(painelLateral);
        painelLateral.setLayout(painelLateralLayout);
        painelLateralLayout.setHorizontalGroup(
            painelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
        painelLateralLayout.setVerticalGroup(
            painelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(painelLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 780));

        painelCentral.setLayout(new javax.swing.BoxLayout(painelCentral, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(painelCentral, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 950, 640));

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
    private javax.swing.JTable buttonList;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel painelCentral;
    private javax.swing.JPanel painelLateral;
    // End of variables declaration//GEN-END:variables
}
