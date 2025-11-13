package com.agenda.agenda.model;

public class Usuario {
    private Long id;
    private String nome;
    private String contacto;

    public Usuario() {}
    public Usuario(Long id, String nome, String contacto) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
}
