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
@Table(name = "funcionarios")
public class Funcionario extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int funcional;
    private String cargo;
    private String departamento;
    private double salario;

    private Funcionario(String nome, String cpf, String endereco, int idade, int funcional, String cargo, String departamento, double salario) {
        super(nome, cpf, endereco, idade);
        this.funcional = funcional;
        this.cargo = cargo;
        this.departamento = departamento;
        this.salario = salario;
    }

    protected Funcionario(){}

    public static Funcionario newFuncionario(
            String nome,
            String cpf,
            String endereco,
            int idade,
            int funcional,
            String cargo,
            String departamento,
            double salario){
        return new Funcionario(nome,cpf,endereco,idade,funcional,cargo,departamento,salario);
    }

    public long getId() {
        return id;
    }
    public int getFuncional() {
        return funcional;
    }
    public String getCargo() {
        return cargo;
    }
    public String getDepartamento() {
        return departamento;
    }
    public double getSalario() {
        return salario;
    }


    public void setSalario(double salario) {
        this.salario = salario;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public void setFuncional(int funcional) {
        this.funcional = funcional;
    }
    public void setId(long id) {
        this.id = id;
    }
}
