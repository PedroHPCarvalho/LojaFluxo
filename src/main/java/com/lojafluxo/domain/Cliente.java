/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lojafluxo.domain;

import jakarta.persistence.*;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "clientes")
public class Cliente extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String phone;
    @Column(name = "limit_credit")
    private double limitCredit;

    // Construtor privado para proteger a criação de objetos, para serem gerados sempre objetos validos
    private Cliente(String nome,String cpf, String end, int idade, String email, String phone, double limitCredit) {
        super(nome,cpf,end,idade);
        this.email = email;
        this.phone = phone;
        this.limitCredit = limitCredit;
    }

    // Construtor protegido para ser usado pelo JPA, onde o JPA pode criar objetos da classe sem a necessidade de usar o Factory Method, mas não pode ser usado por outras classes
    protected Cliente(){}

    // Factory Method, traz um metodo de criação seguro, onde o objeto criado sempre sera valido
    public static Cliente newCliente(
            String nome,
            String cpf,
            String end,
            int idade,
            String email,
            String phone,
            double limitCredit){
        return new Cliente(nome,cpf,end,idade,email,phone,limitCredit);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                "nome='" + getNome() + '\'' +
                "cpf='" + getCpf() + '\'' +
                "endereco='" + getEndereco() + '\'' +
                "idade='" + getIdade() + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", limitCredit=" + limitCredit +
                '}';
    }

    public long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public double getLimitCredit() {
        return limitCredit;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setLimitCredit(double limitCredit) {
        this.limitCredit = limitCredit;
    }
}
