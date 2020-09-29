/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grasp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GRASP {
    public static void main(String[] args) {
        //BASE DE DATOS
        LeerCsv lector= new LeerCsv();
        ArrayList<LugarCobro> lugares = new ArrayList<LugarCobro>();
        lugares=lector.leer();
        int cant_lugares_totales = lugares.size();
        ArrayList<Beneficiario> beneficiarios = new ArrayList<Beneficiario>();
        beneficiarios=lector.leerBene();
        int cant_bene = beneficiarios.size();
        System.out.println("cargamos");
        System.out.println("Cantidad lugares: "+cant_lugares_totales);
        System.out.println("Cantidad beneficiarios: "+cant_bene);
        
        int[] ubigeosPrueba = {150132,150137,40101,130101,130111};
        double limite_densidad=0.020;
        double reduccion_densidad=0.40;
        
        //Matriz de Cantidad de Personas por hora en cada Agencia
        int cantidadPersonasHora[] = new int[cant_lugares_totales];
        for (int i = 0; i < cant_lugares_totales; i++){
            //for (int j = 0; j < 2; j++){
                LugarCobro l=lugares.get(i);
                //cantidadPersonasHora[i][0] = l.getIdAgencia();
                if(l.getDistrito().getDensidad()>limite_densidad)
                    cantidadPersonasHora[i] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad));
                else cantidadPersonasHora[i] = l.getCajas()*l.getPerHora();
            //}
        }
        /*for (int i = 0; i < cant_lugares_totales; i++){
            for (int j = 0; j < 2; j++){
                System.out.println(cantidadPersonasHora[i][j]);
            }
        }*/
        
        int Semana[] = new int[cant_lugares_totales];
        Arrays.fill(Semana,1);
        for (int i = 0; i < cant_lugares_totales; i++){
           System.out.println(Semana[i]);
        }
        
        
        
        Long tiempo_ejecucion = System.currentTimeMillis();
        int iteraciones = 25;
        for (int i = 0; i < iteraciones; i++){
            for (int j=0;j<ubigeosPrueba.length;j++){
                List<Beneficiario> beneDistrito = beneficiarios.stream().filter(a -> a.getDistrito().getUbigeo()==(ubigeosPrueba[j])).collect(Collectors.toList());
                System.out.println("Cantidad beneficiarios: "+ beneDistrito.size());
                List<LugarCobro> lugaresDistritos = lugares.stream().filter(a -> a.getDistrito().getUbigeo()==(ubigeosPrueba[j])).collect(Collectors.toList());
                System.out.println("Cantidad lugares de cobro: "+ lugaresDistritos.size());
                
                
                
                
                
                
                
                
                
        
            break;
            }
            break;
        }
        
        
    }
    
}
