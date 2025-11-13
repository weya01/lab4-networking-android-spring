package com.agenda.agenda.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.agenda.agenda.model.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // permite Android acessar
public class AgendaController {

    private List<Usuario> usuarios = new ArrayList<>();
    private List<Tarefa> tarefas = new ArrayList<>();

    // Usuários ------------------------------
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    @PostMapping("/usuarios")
    public Usuario adicionarUsuario(@RequestBody Usuario u) {
        u.setId((long) (usuarios.size() + 1));
        usuarios.add(u);
        return u;
    }

    // Tarefas ------------------------------
    @GetMapping("/tarefas")
    public List<Tarefa> listarTarefas() {
        return tarefas;
    }

    @PostMapping("/tarefas")
    public Tarefa adicionarTarefa(@RequestBody Tarefa t) {
        t.setId((long) (tarefas.size() + 1));
        tarefas.add(t);
        return t;
    }

    @PutMapping("/tarefas/{id}")
    public Tarefa atualizarStatus(@PathVariable Long id, @RequestBody Tarefa novo) {
        for (Tarefa t : tarefas) {
            if (t.getId().equals(id)) {
                t.setStatus(novo.getStatus());
                return t;
            }
        }
        return null;
    }

    @DeleteMapping("/tarefas/{id}")
    public String apagarTarefa(@PathVariable Long id) {
        tarefas.removeIf(t -> t.getId().equals(id));
        return "Tarefa removida";
    }
}
