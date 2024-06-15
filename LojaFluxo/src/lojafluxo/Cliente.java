/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lojafluxo;

import java.util.Set;

/**
 *
 * @author pedro
 */
public class Cliente extends Pessoa {
    private int lojaMatriz;
    private double creditoBonus;

    public int getLojaMatriz() {
        return lojaMatriz;
    }

    public void setLojaMatriz(int lojaMatriz) {
        this.lojaMatriz = lojaMatriz;
    }

    public double getCreditoBonus() {
        return creditoBonus;
    }

    public void setCreditoBonus(double creditoBonus) {
        this.creditoBonus = creditoBonus;
    }

    void setNome(String joana_soio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public.Cliente(String nome,long cpf, String end)
    {
    super.setNome(nome);
    super.setCpf(lojaMatriz);
     
    }
}
