/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grasp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GRASP {
    public static void main(String[] args) {
        LeerCsv lector= new LeerCsv();
        //BASE DE DATOS
        ArrayList<LugarCobro> lugares = new ArrayList<LugarCobro>();
        lugares=lector.leer();
        int cant_lugares = lugares.size();
        ArrayList<Beneficiario> beneficiarios = new ArrayList<Beneficiario>();
        beneficiarios=lector.leerBene();
        int cant_bene = beneficiarios.size();
        System.out.println("cargamos");
        System.out.println("Cantidad lugares: "+cant_lugares);
        System.out.println("Cantidad beneficiarios: "+cant_bene);
        
        Long tiempo_ejecucion = System.currentTimeMillis();
        int iteraciones = 25;
        for (int i = 0; i < 5; i++){
            List<Beneficiario> beneDistrito = beneficiarios.stream().filter(a -> a.getDistrito().getDistrito().equals("SAN JUAN DE LURIGANCHO")).collect(Collectors.toList());
            System.out.println("Cantidad beneficiarios: "+ beneDistrito.size());
            List<LugarCobro> lug = lugares.stream().filter(a -> a.getDistrito().getDistrito().equals("SAN JUAN DE LURIGANCHO")).collect(Collectors.toList());
            System.out.println("Cantidad lugares de cobro: "+ lug.size());
            break;
        }
        
        
    }
    
}
