package classes;

import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.awt.event.*;

public class bibliotecaClass {

    static final String FILE_PATH = "data/livros.json";
    static JSONArray livrosArray = new JSONArray();
    static JSONArray livrosArrayLocal = new JSONArray();

    public static void addLivro(String nome, String autor, int ano, int paginas, String genero, String descricao) {

        // Lê JSON existente (se existir)
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            livrosArray = new JSONArray(jsonText.toString());
        } catch (IOException e) {
            // Arquivo não existe ou vazio: criar novo
            livrosArray = new JSONArray();
        }

        // Novo livro
        JSONObject novoLivro = new JSONObject();
        novoLivro.put("nome", nome);
        novoLivro.put("autor", autor);
        novoLivro.put("ano", ano);
        novoLivro.put("paginas", paginas);
        novoLivro.put("genero", genero);
        novoLivro.put("descricao", descricao);

        livrosArray.put(novoLivro);

        // Salva no arquivo
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(livrosArray.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void atualizarLivro(int index, String nome, String autor, int ano, int paginas, String genero, String descricao) {
        livrosArray = getLivros(); // usa o array global corretamente
        if (index >= 0 && index < livrosArray.length()) {
            JSONObject livro = livrosArray.getJSONObject(index);
            livro.put("nome", nome);
            livro.put("autor", autor);
            livro.put("ano", ano);
            livro.put("paginas", paginas);
            livro.put("genero", genero);
            livro.put("descricao", descricao);

            try (FileWriter file = new FileWriter(FILE_PATH)) {
                file.write(livrosArray.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addLivroNaLista(JPanel painelLateral, JPanel painelCentral) {
        painelLateral.removeAll();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            livrosArrayLocal = new JSONArray(jsonText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < livrosArrayLocal.length(); i++) {
            JSONObject livro = livrosArrayLocal.getJSONObject(i);
            String nomeLivro = livro.getString("nome");

            JButton btnLivro = new JButton(nomeLivro);
            int index = i; // necessário para lambda

            btnLivro.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostrarLivro(painelCentral, livrosArrayLocal.getJSONObject(index));
                }
            });

            painelLateral.add(btnLivro);
        }

        painelLateral.revalidate();
        painelLateral.repaint();
    }

    private static void mostrarLivro(JPanel painelCentral, JSONObject livro) {
        painelCentral.removeAll();

        painelCentral.add(new JLabel("Nome: " + livro.getString("nome")));
        painelCentral.add(new JLabel("Autor: " + livro.getString("autor")));
        painelCentral.add(new JLabel("Ano: " + livro.getInt("ano")));
        painelCentral.add(new JLabel("Páginas: " + livro.getInt("paginas")));
        painelCentral.add(new JLabel("Gênero: " + livro.getString("genero")));
        painelCentral.add(new JLabel("Descrição: " + livro.getString("descricao")));

        painelCentral.revalidate();
        painelCentral.repaint();
    }

    public static JSONArray getLivros() {
        JSONArray livrosArray = new JSONArray();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            livrosArray = new JSONArray(jsonText.toString());
        } catch (IOException e) {
            // Se o arquivo não existir, retorna array vazio
            System.out.println("Arquivo livros.json não encontrado. Criando novo.");
        }
        return livrosArray;
    }

    public static void removerLivro(int index) {
        JSONArray livrosArray = getLivros(); // lê JSON atual
        if (index >= 0 && index < livrosArray.length()) {
            livrosArray.remove(index);
            try (FileWriter file = new FileWriter(FILE_PATH)) {
                file.write(livrosArray.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
