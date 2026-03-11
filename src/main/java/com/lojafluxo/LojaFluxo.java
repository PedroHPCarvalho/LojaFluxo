/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.lojafluxo;

import com.lojafluxo.domain.Cliente;
import com.lojafluxo.domain.Funcionario;
import com.lojafluxo.infrastructure.ClienteRepository;
import com.lojafluxo.infrastructure.FuncionarioRepository;
import com.lojafluxo.infrastructure.JPAConnectionFactory;

import java.sql.SQLException;

/**
 *
 * @author pedro
 */
public class LojaFluxo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Funcionario funcionario = Funcionario.newFuncionario("Pedro","440.705.838-23","rua do karaio", 21, 112342,"Desenvolvedor JR","COMPUTARIA", 7000);
        Funcionario funcionario2 = Funcionario.newFuncionario("Ana","410.705.838-23","rua do karaio", 22, 1125432,"Analista Financeira","Financeiro", 6000);
        Funcionario funcionario3 = Funcionario.newFuncionario("Tanto_FAZ","410.4405.838-23","rua do karaio", 22, 1125432,"Analista Financeira","Financeiro", 6000);
        FuncionarioRepository funcionarioRepository = FuncionarioRepository.newFuncionarioRepository(JPAConnectionFactory.getInstance());
        funcionarioRepository.save(funcionario);
        funcionarioRepository.save(funcionario2);
        funcionarioRepository.save(funcionario3);

        funcionario.setSalario(14000);
        funcionario2.setCargo("Gerente de Financeiro");

        funcionarioRepository.update(funcionario);
        funcionarioRepository.update(funcionario2);


        System.out.println(funcionarioRepository.findById(1).toString());
        System.out.println(funcionarioRepository.findAll().toString());

        funcionarioRepository.delete(3);

        Cliente cliente = Cliente.newCliente("Pedronez", "332.122.332-12","rua longe", 22, "pedroh.fokus@gmail.com", "(11)97698-4325",12000);
        Cliente cliente2 = Cliente.newCliente("Pedronez2", "332.122.332-12","rua longe", 22, "pedroh.fokus@gmail.com", "(11)97698-4325",12000);

        ClienteRepository clienteRepository = ClienteRepository.newClienteRepository(JPAConnectionFactory.getInstance());
        clienteRepository.save(cliente);
        clienteRepository.save(cliente2);
        System.out.println(clienteRepository.findById(1).toString());
        System.out.println(clienteRepository.findAll().toString());

        cliente.setLimitCredit(100000);

        clienteRepository.update(cliente);

        clienteRepository.delete(2);

        try{
            Thread.sleep(13000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try{
            org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
