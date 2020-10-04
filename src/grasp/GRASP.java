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
        
        
        Long tiempo_ejecucion = System.currentTimeMillis();
        Integer[] horaPreferencial = {8,9};
        int iteraciones = 1;
        int maxTurnos = 2;
        Algoritmo algoritmo =  new Algoritmo();
        for (int i = 0; i < iteraciones; i++){            
            String solucion[][][] = new String[ubigeosPrueba.length][][];
            for (int j=0;j<ubigeosPrueba.length;j++){                
                int ubigeo=ubigeosPrueba[j];                
                //System.out.println("AYUDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+ubigeo );
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
                
                String solucionxdistrito[][] = new String[beneDistrito.size()+1][lugaresDistritos.size()+1];
                solucion[j] = new String [beneDistrito.size()+1][lugaresDistritos.size()+1];
                
                for(int z = 0; z < beneDistrito.size(); z++){
                    solucionxdistrito[z+1][0]=beneDistrito.get(z).getCodigoHogar()+"";                    
                }
                for(int z = 0; z < lugaresDistritos.size(); z++){
                    solucionxdistrito[0][z+1]=lugaresDistritos.get(z).getIdAgencia()+"";
                    //System.out.println("cod agencia: "+ solucionxdistrito[0][z+1]);
                    
                }
                
                int preferencial = 0;
                int faltantes = beneDistrito.size();
                boolean todosAsignados = true;
                int[][] listaCandidatos = new int[beneDistrito.size()][4];
                while(faltantes>0){                    
                    for(int k = 0; k<lugaresDistritos.size();k++){                        
                        double hombres = 0;
                        double mujeres = 0;                        
                        while(cantidadPersonasHora[k][2] > 0){
                            todosAsignados = true;
                            LugarCobro lug = lugaresDistritos.get(k);
                            
                            
                            if(Arrays.asList(horaPreferencial).contains(cantidadPersonasHora[k][4])){
                               preferencial = 1;
//                               System.out.println("hora:" +cantidadPersonasHora[k][4]);
                            }
                            else
                                preferencial = 0;
    //                        int listaCandidatos[][] = new int[beneDistrito.size()][2];

                            int listaRestringida[][] = new int[beneDistrito.size()][4];                            
                            int cont = 0;
                            double alpha = 0.1;
                            double[] maxmin={0,0};
                            listaCandidatos = algoritmo.calcularBondad(beneDistrito, preferencial,maxmin, hombres, mujeres); // [posicion][bondad][horariosRestantes][sexo]                            
                            todosAsignados = true;
                            
                            for (int n = 0; n<listaCandidatos.length; n++){
                            //for (int n = 0; n<20; n++){
//                                int indice = listaCandidatos[n][0];
//                                if(beneDistrito.get(indice).getCodigoHogar()==267)
//                                    System.out.println("fitness: "+listaCandidatos[n][1]);
                                if(listaCandidatos[n][2] > 0){                                   
                                    if(listaCandidatos[n][1] <= maxmin[0] && listaCandidatos[n][1] >= maxmin[0] - alpha*(maxmin[0]-maxmin[1])){
                                        listaRestringida[cont++] = listaCandidatos[n];
                                        //System.out.println("jsjsjsjss"+listaCandidatos[n][1]);
                                    }
                                    todosAsignados = false; // verificar la cantidad de horarios por asignar a cada beneficiario
                                }

                            }    
//                            System.out.println("tama"+ cont);
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
                            boolean cambiarDia = false;
                            if(solucionxdistrito[posicion+ 1][k+1]!= null){
                                String[] turnos = solucionxdistrito[posicion+ 1][k+1].split(",");
                                for(int t=0;t< turnos.length;t++){
                                    int tur = Integer.parseInt(turnos[t]);
                                    if(tur/100 == cantidadPersonasHora[k][5])
                                        cambiarDia = true;
                                }
                                if(!cambiarDia)
                                    solucionxdistrito[posicion+ 1][k+1] +="," + (cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4])+"";
                            }
                            else
                                solucionxdistrito[posicion+ 1][k+1] =(cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4])+"";
                            int hRestantes = beneDistrito.get(posicion).getHorariosRestantes()-1;
                            if(hRestantes == 0)
                                faltantes--;
                            //System.out.println("falta: "+faltantes);
                            System.out.println("flag: "+"Discapacitado: "+beneDistrito.get(posicion).getFlagDis()+" - Adulto mayor: "+beneDistrito.get(posicion).getFlagMayor());
                            System.out.println("asignacion: "+beneDistrito.get(posicion).getCodigoHogar()+"-"+solucionxdistrito[posicion+1][0]+"-"+(cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4])+"-"+listaRestringida[pos_restringida][1]);
                            beneDistrito.get(posicion).setHorariosRestantes(hRestantes);
                            //System.out.println(beneDistrito.get(posicion).getHorariosRestantes());
                            System.out.println();
                            cantidadPersonasHora[k][2]--; // se disminuye la cantidad de cupos restantes
                            hombres += listaRestringida[pos_restringida][3]==1?1:0;
                            mujeres += listaRestringida[pos_restringida][3]==2?1:0;
                            if(faltantes == 0){
                                System.out.println("Ultimo turno del distrito: "+(cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4]));
                            }
                            if(cantidadPersonasHora[k][2] == 0){ // horario lleno                                
                                if(cantidadPersonasHora[k][4] == (sabado?lug.getHoraCierreS().getHours():lug.getHoraCierreLV().getHours())){
                                    cantidadPersonasHora[k][5]++; // dia siguiente
                                    cantidadPersonasHora[k][4] = viernes?lug.getHoraAperturaS().getHours():lug.getHoraAperturaLV().getHours(); // reinicia horario de apertura
                                }else{
                                    cantidadPersonasHora[k][4]+=1;
                                }                                
                                break;
                            }
                        }
                        System.out.println("hombres: "+hombres+" - Mujeres: "+mujeres);
                        cantidadPersonasHora[k][2]=cantidadPersonasHora[k][1];
                        
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
                        if(solucionxdistrito[y][z]!= null){
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
