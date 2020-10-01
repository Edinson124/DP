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
import java.util.Random;


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
        
        int[] ubigeosPrueba = {150132};//,150137,40101,130101,130111};
        double limite_densidad=0.020;
        double reduccion_densidad=0.40;
        
        //Matriz de Cantidad de Personas por hora en cada Agencia
//        int cantidadPersonasHora[][] = new int[cant_lugares_totales][3];
//        for (int i = 0; i < cant_lugares_totales; i++){
//            //for (int j = 0; j < 2; j++){
//                LugarCobro l=lugares.get(i);
//                cantidadPersonasHora[i][0] = l.getIdAgencia(); // identificador del lugar de cobro
//                if(l.getDistrito().getDensidad()>limite_densidad){
//                    cantidadPersonasHora[i][1] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad)); // cantidad de personas por hora
//                    cantidadPersonasHora[i][2] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad)); // cantidad de cupos restantes
//                }
//                else{
//                    cantidadPersonasHora[i][1] = l.getCajas()*l.getPerHora();
//                    cantidadPersonasHora[i][2] = l.getCajas()*l.getPerHora();
//                }
//                
//            //}
//        }
        /*for (int i = 0; i < cant_lugares_totales; i++){
            for (int j = 0; j < 2; j++){
                System.out.println(cantidadPersonasHora[i][j]);
            }
        }*/
        
//        int Semana[] = new int[cant_lugares_totales];
//        Arrays.fill(Semana,1);
//        for (int i = 0; i < cant_lugares_totales; i++){
//           System.out.println(Semana[i]);
//        }
        
        
        
        Long tiempo_ejecucion = System.currentTimeMillis();
        int[] horaPreferencial = {8,10};
        int iteraciones = 1;
        Algoritmo algoritmo =  new Algoritmo();
        for (int i = 0; i < iteraciones; i++){            
            int solucion[][][] = new int[ubigeosPrueba.length][][];
            for (int j=0;j<ubigeosPrueba.length;j++){                
                int ubigeo=ubigeosPrueba[j];                
                System.out.println("AYUDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+ubigeo );
                List<Beneficiario> beneDistrito = beneficiarios.stream().filter(a -> a.getDistrito().getUbigeo()==(ubigeo)).collect(Collectors.toList());
               // System.out.println("Cantidad beneficiarios: "+ beneDistrito.size());
                List<LugarCobro> lugaresDistritos = lugares.stream().filter(a -> a.getDistrito().getUbigeo()==(ubigeo)).collect(Collectors.toList());
               // System.out.println("Cantidad lugares de cobro: "+ lugaresDistritos.size());
                
                int cantidadPersonasHora[][] = new int[lugaresDistritos.size()][6];
                for (int m = 0; m < lugaresDistritos.size(); m++){
                    LugarCobro l=lugares.get(m);
                    cantidadPersonasHora[m][0] = l.getIdAgencia(); // identificador del lugar de cobro
                    if(l.getDistrito().getDensidad()>limite_densidad){
                        cantidadPersonasHora[m][1] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad)); // cantidad de personas por hora
                        cantidadPersonasHora[m][2] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad)); // cantidad de cupos restantes
                    }
                    else{
                        cantidadPersonasHora[m][1] = l.getCajas()*l.getPerHora();
                        cantidadPersonasHora[m][2] = l.getCajas()*l.getPerHora();
                    }
                    cantidadPersonasHora[m][3] = 1;
                    cantidadPersonasHora[m][4] = l.getHoraAperturaLV().getHours(); // hora 
                    cantidadPersonasHora[m][5] = 1; // día
                }
                
                               
                
                int solucionxdistrito[][] = new int[beneDistrito.size()+1][lugaresDistritos.size()+1];
                solucion[j] = new int [beneDistrito.size()+1][lugaresDistritos.size()+1];
                
                for(int z = 0; z < beneDistrito.size(); z++){
                    solucionxdistrito[z+1][0]=beneDistrito.get(z).getCodigoHogar();
                }
                for(int z = 0; z < lugaresDistritos.size(); z++){
                    solucionxdistrito[0][z+1]=lugaresDistritos.get(z).getIdAgencia();
                    //System.out.println("cod agencia: "+ solucionxdistrito[0][z+1]);
                    
                }
                
                int preferencial = 0;
                int faltantes = beneDistrito.size();
                boolean todosAsignados = true;
                int[][] listaCandidatos = new int[beneDistrito.size()][4];
                while(faltantes>0){                    
                    for(int k = 0; k<lugaresDistritos.size();k++){                        
                        int hombres = 0;
                        int mujeres = 0;                        
                        while(cantidadPersonasHora[k][2] > 0){
                            todosAsignados = true;
                            LugarCobro lug = lugaresDistritos.get(k);
                            
                            if(Arrays.asList(horaPreferencial).contains(cantidadPersonasHora[k][4]))
                               preferencial = 1;
                            else
                                preferencial = 0;
    //                        int listaCandidatos[][] = new int[beneDistrito.size()][2];

                            int listaRestringida[][] = new int[beneDistrito.size()][4];                            
                            int cont = 0;
                            double alpha = 0.9;
                            double[] maxmin={0,0};
                            listaCandidatos = algoritmo.calcularBondad(beneDistrito, preferencial,maxmin, hombres, mujeres); // [posicion][bondad][horariosRestantes][sexo]                            
                            todosAsignados = true;
                            
                            for (int n = 0; n<listaCandidatos.length; n++){
                                if(listaCandidatos[n][2] > 0){                                   
                                    if(listaCandidatos[n][1] <= maxmin[0] && listaCandidatos[n][1] >= maxmin[0] - alpha*(maxmin[0]-maxmin[1])){
                                        listaRestringida[cont++] = listaCandidatos[n];
                                        //System.out.println("jsjsjsjss"+listaCandidatos[n][1]);
                                    }
                                    todosAsignados = false; // verificar la cantidad de horarios por asignar a cada beneficiario
                                }

                            }    
                            //System.out.println("tama"+ cont);
                            boolean sabado = (cantidadPersonasHora[k][5] % 6 == 0);
                            boolean viernes = (cantidadPersonasHora[k][5] % 6 == 5);
                            if(todosAsignados) {
                                if(cantidadPersonasHora[k][4] == (sabado?lug.getHoraCierreS().getHours():lug.getHoraCierreLV().getHours())){
                                    cantidadPersonasHora[k][5]++; // dia siguiente
                                    cantidadPersonasHora[k][4] = viernes?lug.getHoraAperturaS().getHours():lug.getHoraAperturaLV().getHours(); // reinicia horario de apertura
                                }
                                break;
                            }
                            Random rd = new Random();           
                            int pos_restringida = rd.nextInt(cont);                            
                            int posicion=listaRestringida[pos_restringida][0];
                            //System.out.println("pruebaa: "+solucionxdistrito[0][0]);
                            //solucionxdistrito[k+1][listaRestringida[posicion][0]+ 1] = cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4];
                            solucionxdistrito[listaRestringida[posicion][0]+ 1][k+1] = cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4];
                            int hRestantes = beneDistrito.get(posicion).getHorariosRestantes()-1;
                            if(hRestantes == 0)
                                faltantes--;
                            System.out.println("falta: "+faltantes);
                            //System.out.println("retasta: "+beneDistrito.get(posicion).getCodigoHogar()+"-"+solucionxdistrito[posicion+1][0]);
                            beneDistrito.get(posicion).setHorariosRestantes(hRestantes);
                            //System.out.println(beneDistrito.get(posicion).getHorariosRestantes());
                            cantidadPersonasHora[k][2]--; // se disminuye la cantidad de cupos restantes
                            hombres += listaRestringida[posicion][3]==1?1:0;
                            mujeres += listaRestringida[posicion][3]==2?1:0;
                            
                            if(cantidadPersonasHora[k][2] == 0){ // horario lleno
                                
                                if(cantidadPersonasHora[k][4] == (sabado?lug.getHoraCierreS().getHours():lug.getHoraCierreLV().getHours())){
                                    cantidadPersonasHora[k][5]++; // dia siguiente
                                    cantidadPersonasHora[k][4] = viernes?lug.getHoraAperturaS().getHours():lug.getHoraAperturaLV().getHours(); // reinicia horario de apertura
                                } 
                                break;
                            }
                        }
                        cantidadPersonasHora[k][2]=cantidadPersonasHora[k][1];
                        cantidadPersonasHora[k][4]+=1;
                    }                    
                    //if(todosAsignados) break;
                    solucion[j] = solucionxdistrito;
                }
                //solucion[j] = solucionxdistrito;
                
                System.out.println("------------------------------------------------------------------------");
                System.out.println("UBIGEO: "+ubigeo);
 
                for(int z = 1; z <lugaresDistritos.size() +1; z++){
                    System.out.println("ID LUGAR COBRO: "+solucionxdistrito[0][z]);
                    System.out.println("ID BENEFICIARIO                  TURNO ");
                    System.out.println("---------------------------------------- ");
                    int a=0;
                    for(int y=1;y<beneDistrito.size()+1;y++){
                        if(solucionxdistrito[y][z]>0){
                            a++;
                        System.out.println(solucionxdistrito[y][0]+"                   "+solucionxdistrito[y][z]);
                        }
                    }
                    System.out.println("---------------------------------------- ");
                    System.out.println("TOTAL: "+a);                    
                    System.out.println(" ");
                }
                System.out.println(" ");
            }
        }
        
    }
    
}
