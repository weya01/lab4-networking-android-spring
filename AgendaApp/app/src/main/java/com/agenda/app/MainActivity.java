package com.agenda.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    EditText editNome, editDescricao, editId;
    TextView txtResultado;
    Button btnListar, btnAdicionar, btnAtualizar, btnApagar;

    String BASE_URL = "http://192.168.1.2:8080/api/tarefas"; // Corrigido com :8080

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ⚠️ Ligações corretas ao XML
        editNome = findViewById(R.id.editNome);
        editId = findViewById(R.id.editId);
        editDescricao = findViewById(R.id.editDescricao);
        txtResultado = findViewById(R.id.txtResultado);
        btnListar = findViewById(R.id.btnListar);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnApagar = findViewById(R.id.btnApagar);


        btnAdicionar.setOnClickListener(v -> adicionarTarefa());
        btnListar.setOnClickListener(v -> listarTarefas());
        btnAtualizar.setOnClickListener(v -> atualizarTarefa());
        btnApagar.setOnClickListener(v -> apagarTarefa());
    }

    private void adicionarTarefa() {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);

                // Dados da tarefa a partir dos inputs
                String jsonInput = "{"
                        + "\"nome\":\"" + editNome.getText().toString() + "\","
                        + "\"descricao\":\"" + editDescricao.getText().toString() + "\","
                        + "\"status\":\"por fazer\""
                        + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonInput.getBytes("utf-8"));
                }

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);

                runOnUiThread(() -> txtResultado.setText("Tarefa adicionada:\n" + sb.toString()));

            } catch (Exception e) {
                runOnUiThread(() -> txtResultado.setText("Erro ao adicionar: " + e.getMessage()));
            }
        }).start();
    }

    private void listarTarefas() {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line).append("\n");

                runOnUiThread(() -> txtResultado.setText("Tarefas:\n" + sb.toString()));

            } catch (Exception e) {
                runOnUiThread(() -> txtResultado.setText("Erro ao listar: " + e.getMessage()));
            }
        }).start();
    }

    private void atualizarTarefa() {
        new Thread(() -> {
            try {
                String id = editId.getText().toString();
                URL url = new URL(BASE_URL + "/" + id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);

                String jsonInput = "{"
                        + "\"nome\":\"" + editNome.getText().toString() + "\","
                        + "\"descricao\":\"" + editDescricao.getText().toString() + "\""
                        + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonInput.getBytes("utf-8"));
                }

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    sb.append(line);

                runOnUiThread(() ->
                        txtResultado.setText("Atualizado:\n" + sb.toString())
                );

            } catch (Exception e) {
                runOnUiThread(() ->
                        txtResultado.setText("Erro no PUT: " + e.getMessage())
                );
            }
        }).start();
    }

    private void apagarTarefa() {
        new Thread(() -> {
            try {
                String id = editId.getText().toString();
                URL url = new URL(BASE_URL + "/" + id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");

                int responseCode = conn.getResponseCode();

                runOnUiThread(() -> {
                    if (responseCode == 200 || responseCode == 204) {
                        txtResultado.setText("Tarefa " + id + " apagada com sucesso!");
                    } else {
                        txtResultado.setText("Falha ao apagar. Código: " + responseCode);
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        txtResultado.setText("Erro no DELETE: " + e.getMessage())
                );
            }
        }).start();
    }


}
