/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lojafluxo;

import java.util.Set;

/**
 *
 * @author pedro
 */
public class LojaFluxo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Funcionario Jonas = new Funcionario();
        Cliente Joana = new Cliente ();
        
        Jonas.setNome("Jonas doido");
        Jonas.setCpf(788882146);
        Jonas.setEndereco("RUA DOS PERDIDOS");
        Jonas.setIdFuncional(845641);
        Jonas.setSalario(450.00);
        
        Joana.setNome("Joana soio");
        Joana.setCpf(788522146);
        Joana.setEndereco("RUA DOS ACHADOS");
        Joana.setCreditoBonus(820);
        Joana.setLojaMatriz(82);
        
        
        
        
    }
    
}
