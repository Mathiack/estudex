package classes;

import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class bibliotecaClass {

    static final String FILE_PATH = "livros.json";

    public static void addLivro(String nome, String autor, int ano, int paginas, String genero, String descricao) {
        JSONArray livrosArray = new JSONArray();

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
            file.write(livrosArray.toString(4)); // "4" para identação bonita
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJSON(String nomeL, String autorL, int anoL, int paginasL, String generoL, String descricaoL) {

    }
}
