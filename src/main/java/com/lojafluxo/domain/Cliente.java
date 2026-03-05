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

    private Cliente(String nome,String cpf, String end, int idade, String email, String phone, double limitCredit) {
        super(nome,cpf,end,idade);
        this.email = email;
        this.phone = phone;
        this.limitCredit = limitCredit;
    }

    protected Cliente(){}

    public static Cliente newCliente(String nome,String cpf, String end, int idade, String email, String phone, double limitCredit){
        return new Cliente(nome,cpf,end,idade,email,phone,limitCredit);
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
