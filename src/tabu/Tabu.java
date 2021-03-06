/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * 
 */
public class Tabu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //BASE DE DATOS
        int restar_horarios=1;
        int cantidad_maxima_inci=2;
        int cantidad_horarios=2;
        LeerCsv lector= new LeerCsv();
        ArrayList<LugarCobro> lugares = new ArrayList<LugarCobro>();
        lugares=lector.leer();
        int cant_lugares_totales = lugares.size();
        ArrayList<Beneficiario> beneficiarios = new ArrayList<Beneficiario>();
        beneficiarios=lector.leerBene(cantidad_horarios,restar_horarios,cantidad_maxima_inci);
        int cant_bene = beneficiarios.size();
        System.out.println("cargamos");
        System.out.println("Cantidad lugares: "+cant_lugares_totales);
        System.out.println("Cantidad beneficiarios: "+cant_bene);
        
        int[] ubigeosPrueba = {100000,100001,100002,100003,100004,100005,100006,100007,100008,100009,100010};
        //,150137,40101,130101,130111};
        double limite_densidad=0.020;
        double reduccion_densidad=0.40;
        //Long tiempo_ejecucion = System.currentTimeMillis();
        Integer[] horaPreferencial = {8,9};
        int iteraciones = 10;
        Algoritmo algoritmo =  new Algoritmo();
        //List<Integer[]> horarios_sexo = new ArrayList<Integer[]>();
        //List<Integer> agencias_sexo = new ArrayList<Integer>();
        
        for (int i = 0; i < iteraciones; i++){
            List<Integer[]> horarios_sexo = new ArrayList<Integer[]>();
            List<Integer> agencias_sexo = new ArrayList<Integer>();
            int fitnessPersonas=0;
            int fitnessHoras=0;
            int horasMax=0;
            int dias_totales = 0;
            int hora_final = 0;
            int prefMalAsignados = 0;
            int turnos = 0;
            int horariosTotales = 0;
            int horariosMalaDistribucion = 0;
            System.out.println("ITERACION:  "+i);
            List<ResulAgencia> resultado= new ArrayList<ResulAgencia>();
            for (int j=0;j<ubigeosPrueba.length;j++){
                //System.out.println("Ubigeo: "+ubigeosPrueba[j]);
                List<Integer[]> horarios_preferencial= new ArrayList<Integer[]>();
                List<Integer[]> horarios_no_preferencial= new ArrayList<Integer[]>();
                List<Integer> agencias= new ArrayList<Integer>();                
                int ubigeo=ubigeosPrueba[j];                
                //System.out.println("UBIGEO:  "+ubigeo );
                List<Beneficiario> beneDistrito = beneficiarios.stream().filter(a -> a.getDistrito().getUbigeo()==(ubigeo)).collect(Collectors.toList());
               // System.out.println("Cantidad beneficiarios: "+ beneDistrito.size());
                List<LugarCobro> lugaresDistritos = lugares.stream().filter(a -> a.getDistrito().getUbigeo()==(ubigeo)).collect(Collectors.toList());
               // System.out.println("Cantidad lugares de cobro: "+ lugaresDistritos.size());
               //Constante de inicio de horario
               int inicio_horario=23;
               int inicio_sabado=23;
               int ind_resul=resultado.size();
               int paramLugaCobro[][] = new int[lugaresDistritos.size()][5];        
                for (int m = 0; m < lugaresDistritos.size(); m++){
                    LugarCobro l=lugaresDistritos.get(m);
                    paramLugaCobro[m][0] = l.getIdAgencia(); // identificador del lugar de cobro
                    if(l.getDistrito().getDensidad()>limite_densidad){
                        paramLugaCobro[m][1] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad)); // cantidad de personas por hora
                        paramLugaCobro[m][2] = (int)Math.round(l.getCajas()*l.getPerHora()*(1-reduccion_densidad)); // cantidad de cupos restantes
                    }
                    else{
                        paramLugaCobro[m][1] = l.getCajas()*l.getPerHora();
                        paramLugaCobro[m][2] = l.getCajas()*l.getPerHora();
                    }
                    paramLugaCobro[m][3] = 1;//dia
                    paramLugaCobro[m][4] = l.getHoraAperturaLV().getHours(); // hora
                    if(paramLugaCobro[m][4]<inicio_horario){inicio_horario=paramLugaCobro[m][4];}
                    if(l.getHoraAperturaS().getHours()<inicio_sabado){inicio_sabado=l.getHoraAperturaS().getHours();}
                    ResulAgencia r = new ResulAgencia(l.getDistrito().getUbigeo(),l.getIdAgencia(),l.getHoraAperturaLV().getHours(),l.getHoraCierreLV().getHours(),l.getHoraAperturaS().getHours(),l.getHoraCierreS().getHours());
                    resultado.add(r);
                } 
                //System.out.println(resultado.size());
                //Contasntes horarios
                int preferencial = 0;
                boolean todosAsignados = true;
                List<Candidato> listaCandidatos = new ArrayList<Candidato>();
                listaCandidatos=algoritmo.inicializar(beneDistrito);
                int faltantes = listaCandidatos.size();
                int hora_actual=inicio_horario;
                int dia_actual=1;
                while(faltantes>0){
                    //System.out.println("Hora y dia:"+hora_actual+"-"+dia_actual);
                    int pass=1;
                    for(int k = 0; k<lugaresDistritos.size();k++){
                        LugarCobro lug = lugaresDistritos.get(k);
                        if(dia_actual % 6 == 0 ){
                            if(hora_actual<lug.getHoraAperturaS().getHours()||hora_actual>=lug.getHoraCierreS().getHours()){
                            continue;
                            }
                        }
                        else{
                            if(hora_actual<lug.getHoraAperturaLV().getHours()||hora_actual>=lug.getHoraCierreLV().getHours()){
                            continue;
                            }
                        }
                        resultado.get(ind_resul+k).nuevoHorario(dia_actual, hora_actual);
                        pass=0;
                        double hombres = 0;
                        double mujeres = 0;
                        int yeri=0;                       
                        double alpha = 0.1;
                        if(Arrays.asList(horaPreferencial).contains(hora_actual)){
                                   preferencial = 1;
    //                               System.out.println("hora:" +cantidadPersonasHora[k][4]);
                        }else
                                    preferencial = 0;
                        double[] maxmin={0,0};//0: maximo, 1:minimo
                        algoritmo.calcularBondad(listaCandidatos, beneDistrito, preferencial,maxmin, hombres, mujeres);
                        //double ajuste = (maxmin[0] - alpha*(maxmin[0]-maxmin[1]));
                        //System.out.println("Ajuste: "+ajuste);
                        List<Candidato> LCR = listaCandidatos.stream().filter(a -> (a.getBondad()>=(maxmin[0] - alpha*(maxmin[0]-maxmin[1])))).collect(Collectors.toList());
                        int bucle_insercion=0;
                        while(paramLugaCobro[k][2] > 0){
                            if(bucle_insercion==0){
                                maxmin[0]=0;maxmin[1]=0;
                                algoritmo.calcularBondad(listaCandidatos, beneDistrito, preferencial,maxmin, hombres, mujeres);
                                LCR = listaCandidatos.stream().filter(a -> (a.getBondad()>=(maxmin[0] - alpha*(maxmin[0]-maxmin[1])))).collect(Collectors.toList());
                            }
                            //System.out.println("LRC TAMAÑO: "+LCR.size());
                            //System.out.println("BUCLE: "+bucle_insercion);
                            int pos_restringida = new Random().nextInt(LCR.size());
                            int pos_real=LCR.get(pos_restringida).getPosicion();
                            
                            //int v=listaCandidatos.stream().filter(x -> x.getPosicion()==pos_real).findFirst().get().getHorarioRestantes();
                            //System.out.println("Original: "+v);
                            //System.out.println("En lrc "+LCR.get(pos_restringida).getHorarioRestantes());

                            Beneficiario bene= beneDistrito.get(pos_real);
                            int x=0;int min=23;int max1=0;
                            if(bene.getFlagDis()==1||bene.getFlagMayor()==1){
                                x=1;                                
                            }
                            //System.out.println("tamaño LRC: "+LCR.size());
                            //System.out.println("flag: "+"Discapacitado: "+bene.getFlagDis()+" - Adulto mayor: "+bene.getFlagMayor());
                            //System.out.println("asignacion: "+bene.getCodigoHogar()+"-"+(dia_actual*100 + hora_actual)+"-"+LCR.get(pos_restringida).getBondad());
                            boolean insercion= resultado.get(ind_resul+k).nuevobene(bene.getCodigoHogar(),pos_real,bene.getGenero(),x);
                            
                            if(insercion){
                                bucle_insercion=0;
                                turnos++;
                                paramLugaCobro[k][2]--; // se disminuye la cantidad de cupos restantes
                                hombres += bene.getGenero()==1?1:0;
                                mujeres += bene.getGenero()==2?1:0;
                                int hRest =LCR.get(pos_restringida).getHorarioRestantes()-1;                           
                                LCR.get(pos_restringida).setHorarioRestantes(hRest);  
                                if(x==1){
                                    for(int h=0; h<horaPreferencial.length;h++){
                                        int dif=Math.abs(horaPreferencial[h]-hora_actual);
                                        if(dif<min){min=dif;}
                                        if(dia_actual%6==0){
                                            int dif1=Math.abs(horaPreferencial[h]-resultado.get(ind_resul+k).getCierre_S());
                                            if(dif1>max1){max1=dif1;}
                                        }else{
                                            int dif1=Math.abs(horaPreferencial[h]-resultado.get(ind_resul+k).getCierre_LV());
                                            if(dif1>max1){max1=dif1;}
                                        } 
                                    }
                                    if(!Arrays.asList(horaPreferencial).contains(hora_actual))
                                        prefMalAsignados++;                                    
                                }
                                else{
                                    min=0;
                                }                               
                                fitnessHoras+=min;
                                horasMax+=max1;
                                if(hRest==0){
                                    listaCandidatos.remove(listaCandidatos.stream().filter(y -> y.getPosicion()==pos_real).findFirst().get());
                                    faltantes--;
                                    if(faltantes==0){
                                        break;
                                    }
                                }
                                //System.out.println("pruebaa: "+solucionxdistrito[0][0]);
                                //solucionxdistrito[k+1][listaRestringida[posicion][0]+ 1] = cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4];
                                //turnos++;
                                
                            }
                            else{
                                bucle_insercion++;
                            }
                            if(bucle_insercion>5){
                                //System.out.println("bucle insercion "+bucle_insercion);
                                break;
                            }
                            yeri=preferencial;
                        }
                        //System.out.println("hombres: "+hombres+" - Mujeres: "+mujeres);
                        if(hombres>(hombres+mujeres)*0.5){
                            fitnessPersonas+=Math.abs((int)(hombres-(hombres+mujeres)*0.5));
                            horariosMalaDistribucion++;
                            Integer[] horario = new Integer[2];
                            horario[0] = ind_resul + k;//agencia
                            horario[1] = resultado.get(ind_resul + k).getHorarios().size() - 1;//horario   
                            horarios_sexo.add(horario);
                            if (!agencias.contains(horario[0])) {
                                agencias_sexo.add(horario[0]);
                            }
                        }  
                        //turnos+=hombres+mujeres;
                        horariosTotales++;
                        int prioritarios=resultado.get(ind_resul+k).getUltimoHorario().getPrioridad().size();
                        int no_prioritarios=resultado.get(ind_resul+k).getUltimoHorario().getNoPrioridad().size();
                        if((preferencial==1 && no_prioritarios>0)){
                            Integer[] horario=new Integer[2];
                            horario[0]=ind_resul+k;//agencia
                            horario[1]=resultado.get(ind_resul+k).getHorarios().size()-1;//horario
                            horarios_preferencial.add(horario);
                            if(!agencias.contains(horario[0])){
                                agencias.add(horario[0]);
                            }
                        }else if (preferencial==0 && prioritarios>0){                           
                            Integer[] horario=new Integer[2];
                            horario[0]=ind_resul+k;//agencia
                            horario[1]=resultado.get(ind_resul+k).getHorarios().size()-1;//horario   
                            horarios_no_preferencial.add(horario);
                            if(!agencias.contains(horario[0])){
                                agencias.add(horario[0]);
                            }
                        }
                        
                        if(yeri==1 && no_prioritarios>0){
                            //System.out.println("Priritarios: "+prioritarios+" - NO prioritarios: "+no_prioritarios);
                        }else if(yeri==0 && prioritarios>0){
                            //System.out.println("Priritarios: "+prioritarios+" - NO prioritarios: "+no_prioritarios);
                        }    
                        
                        paramLugaCobro[k][2]=paramLugaCobro[k][1];
                        //System.out.println("turnos: "+turnos);
                        if(faltantes==0)break;
                    }
                    if (pass==1){
                        dia_actual++;
                        if(dia_actual%6==0){
                            hora_actual=inicio_sabado;
                        }
                        else{hora_actual=inicio_horario;}
                    }else{hora_actual++;}     
                }
                /*
                for(int n=0;n<resultado.size();n++){
                    ResulAgencia r= resultado.get(n);
                    System.out.println("Agencia ID:"+r.getId());
                    System.out.println("-------------------------------------------------------");
                    for(int t=0;t<r.getHorarios().size();t++){
                        //System.out.println(" ");
                        ResulHorario h = r.getHorarios().get(t);
                        System.out.println("Dia:"+h.getDia()+" Hora:"+h.getHora());
                        int prioritarios=h.sizeProridad();
                        int no_prioritarios=h.sizeNoProridad();
                        System.out.println("Priritarios: "+prioritarios+" - NO prioritarios: "+no_prioritarios);                        
                        System.out.println("Hombres:"+h.getCantHombres());
                        System.out.println("Mujeres:"+h.getCantMujeres());                                                
                        System.out.println("-------------------------------------------------------");
                        List<Integer> np=h.getNoPrioridad();
                        for(int ñ=0;ñ<np.size();ñ++){
                            System.out.print(np.get(ñ)+"");
                            int pos=h.getPosNoPrioridad().get(ñ);
                            System.out.print("  ID corroborar: "+beneDistrito.get(pos).getCodigoHogar());
                            System.out.println("   Mayor:"+beneDistrito.get(pos).getFlagMayor()+"    Discapacitado:"+beneDistrito.get(pos).getFlagDis());
                        }
                        List<Integer> p=h.getPrioridad();
                        for(int ñ=0;ñ<p.size();ñ++){
                            System.out.print(p.get(ñ)+"");
                            int pos=h.getPosprioridad().get(ñ);
                            System.out.print("  ID corroborar: "+beneDistrito.get(pos).getCodigoHogar());
                            System.out.println("   Mayor:"+beneDistrito.get(pos).getFlagMayor()+"      Discapacitado:"+beneDistrito.get(pos).getFlagDis());
                        }
                    //}
                    //System.out.println(" ");
                }*/
                //System.out.println("Resultado del fitness personas:"+fitnessPersonas);
                //System.out.println("-------------------------------------------------------");
                //System.out.println(); 
                //System.out.println("==================================================================================================================0");
                
                for (int m = 0; m < agencias.size(); m++){
                    int nuevo_preferencial=horaPreferencial[horaPreferencial.length-1]+1;
                    int diferencia_nuevo=1;
                    int posAgencia=agencias.get(m);
                    ResulAgencia resulAgencia=resultado.get(posAgencia);
                    int h_inicioLV=resulAgencia.getApertura_LV();
                    int h_cierreLV=resulAgencia.getCierre_LV();
                    int h_inicioS=resulAgencia.getApertura_S();
                    int h_cierreS=resulAgencia.getCierre_S();
                    
                    int bloqueInicio_LV=nuevo_preferencial-h_inicioLV;
                    int bloqueFin_LV=h_cierreLV-nuevo_preferencial;
                    int bloqueInicio_S=nuevo_preferencial-h_inicioS;
                    int bloqueFin_S=h_cierreS-nuevo_preferencial;
                    
                    int posicion_cambio=bloqueInicio_LV;//Horario tentador a cambiar
                    List<Integer[]> horarios_mejora_Agencia = horarios_no_preferencial.stream().filter(a ->a[0]==posAgencia).collect(Collectors.toList());
                    int dia=1;
                    for(int g=0;g<horarios_mejora_Agencia.size();g++){                        
                        Integer[] horario=horarios_mejora_Agencia.get(g);//id agencia y horario a cambiar
                        ResulHorario resulhorario=resulAgencia.getUnHorario(horario[1]);
                        
                        if(g==0 && resulhorario.sizeNoProridad()>0){
                            horarios_mejora_Agencia.add(horario);
                            continue;
                        }                        
                        int hora_horario=resulhorario.getHora();//hora del horario a cambiar
                        int cantPrioritarios=resulhorario.sizeProridad();
                        //int solo_prioritario=0;
                        int diferencia_horaPrioritaria=23;
                        for(int h=0; h<horaPreferencial.length;h++){
                            int dif=Math.abs(horaPreferencial[h]-hora_horario);
                            if(dif<diferencia_horaPrioritaria){diferencia_horaPrioritaria=dif;}
                        }
                        //==============================================================
                        //if(resulhorario.sizeNoProridad()==0){solo_prioritario=1;}
                        //else{continue;}
                        //Por el momento solo se cambia los que tienen horarios no preferenciales llenos de puros prioritarios
                        //==============================================================
                        if(resulhorario.getHora()==nuevo_preferencial){
                            continue;//Horario a cambiar tiene la hora nueva preferencial, por lo tanto no se cambia
                        }
                        else{
                            boolean no_cambiado=true;                            
                            while(no_cambiado){
                                if(posicion_cambio>resulAgencia.getHorarios().size()-1) break;
                                ResulHorario horarioCambio=resulAgencia.getUnHorario(posicion_cambio);
                                if(horarioCambio.sizeProridad()==0){
                                    resultado.get(horario[0]).cambiarHorario(horario[1], posicion_cambio);
                                    fitnessHoras-=cantPrioritarios*(diferencia_horaPrioritaria-diferencia_nuevo);
                                    no_cambiado=false;
                                }
                                if(dia%6!=0){posicion_cambio+=bloqueFin_LV;}                            
                                else{posicion_cambio+=bloqueFin_S;}
                                dia++;
                                if(dia%6!=0){posicion_cambio+=bloqueInicio_LV;}                            
                                else{posicion_cambio+=bloqueInicio_S;}
                                if(posicion_cambio>resulAgencia.getHorarios().size()-1){
                                    nuevo_preferencial++;
                                    diferencia_nuevo++;                                    
                                    bloqueInicio_LV=nuevo_preferencial-h_inicioLV;
                                    bloqueFin_LV=h_cierreLV-nuevo_preferencial;
                                    bloqueInicio_S=nuevo_preferencial-h_inicioS;
                                    bloqueFin_S=h_cierreS-nuevo_preferencial;
                                    posicion_cambio=bloqueInicio_LV;
                                    dia=1;
                                }
                            }                            
                        }
                    }
                }
            //------------------------------------------------------Mejora---------------------------------
            //prioritarios en no preferenciales
            
                
                
                //------------------------------------------------------Mejora---------------------------------
                //prioritarios en no preferenciales
//                  List<Integer[]> lista_tabu;
//                for (int m = 0; m < agencias.size(); m++){
//                    
////                    int nuevo_preferencial=horaPreferencial[horaPreferencial.length-1]+1;
////                    int diferencia_nuevo=1;
//                    int posAgencia=agencias.get(m);
//                    ResulAgencia resulAgencia=resultado.get(posAgencia);
////                    
////                    int posicion_cambio=bloqueInicio_LV;//Horario tentador a cambiar
//                    List<Integer[]> horarios_no_preferencial_mejorar = horarios_no_preferencial.stream().filter(a ->a[0]==posAgencia).collect(Collectors.toList());
//                    //List<Integer[]> horarios_preferencial_mejorar = horarios_preferencial.stream().filter(a ->a[0]==posAgencia).collect(Collectors.toList());
//                    //int longitud = horarios_no_preferencial_mejorar.size()<horarios_preferencial_mejorar.size()?horarios_no_preferencial_mejorar.size():horarios_preferencial_mejorar.size();
//                    //if(horarios_no_preferencial_mejorar.size()>1 && horarios_preferencial_mejorar.size()>0){
//                    for(int g=0;g<horarios_no_preferencial_mejorar.size();g++){       
//                        //int posHor1 = new Random().nextInt(horarios_no_preferencial_mejorar.size()-1);
//                        Integer[] horario1=horarios_no_preferencial_mejorar.get(g);//id agencia y horario a cambiar
//                        int posHor2 = new Random().nextInt(resulAgencia.sizeHorarios());
//                        //Integer[] horario2=horarios_no_preferencial_mejorar.get(posHor2);//id agencia y horario a cambiar
//                        ResulHorario hor1 = resultado.get(horario1[0]).getUnHorario(horario1[1]);              
//                        
//                        ResulHorario hor2 = resulAgencia.getUnHorario(posHor2);
//                        
//                        // posicion de los beneficiarios a cambiar
//                        int prioPos = new Random().nextInt(hor1.sizeProridad());
//                        int noPrioPos = 0;
//                        if(hor2.sizeNoProridad()>0)
//                            noPrioPos = new Random().nextInt(hor2.sizeNoProridad());
//                        else 
//                            continue;
//                        // código de los beneficiarios a cambiar
//                        int prio = hor1.getPrioridad().get(prioPos);
//                        int noPrio = hor2.getNoPrioridad().get(noPrioPos);
//                        //género de los beneficiarios a cambiar
//                        int genPrio = hor1.getGenPrioridad().get(prioPos);
//                        int genNoPrio = hor2.getGenNoPrioridad().get(noPrioPos);
//                        // 
//                        int hom1 = hor1.getCantHombres();
//                        int muj1 = hor1.getCantMujeres();
//                        int hom2 = hor2.getCantHombres();
//                        int muj2 = hor2.getCantMujeres();
//                        
//                        if(!hor2.getPrioridad().contains(prio)){ // el prioritario no se encuentra ya en el horario al que se le colocará
//                            if(genPrio != genNoPrio){ // si los beneficiarios a intercambiar son de generos distintos
//                                if(hom1>hom1+muj1*0.5){
//                                    funcion-=Math.abs((int)(hom1-hom1+muj1*0.5)*0.5);
//                                }
//                                if(hom2>hom2+muj2*0.5){
//                                    funcion-=Math.abs((int)(hom2-hom2+muj2*0.5)*0.5);
//                                }
//                                if(genPrio == 1){
//                                    hor1.setCantHombres(hom1-1);
//                                    hor1.setCantMujeres(muj1+1);
//                                    hor2.setCantHombres(hom1+1);
//                                    hor2.setCantMujeres(muj1-1);
//                                }else{
//                                    hor1.setCantHombres(hom1+1);
//                                    hor1.setCantMujeres(muj1-1);
//                                    hor2.setCantHombres(hom1-1);
//                                    hor2.setCantMujeres(muj1+1);
//                                }
//                                if(hor1.getCantHombres()>hor1.getCantHombres()+hor1.getCantMujeres()*0.5){
//                                    funcion+=Math.abs((int)(hor1.getCantHombres()-hor1.getCantHombres()+hor1.getCantMujeres()*0.5)*0.5);
//                                }
//                                if(hor2.getCantHombres()>hor2.getCantHombres()+hor2.getCantMujeres()*0.5){
//                                    funcion+=Math.abs((int)(hor2.getCantHombres()-hor2.getCantHombres()+hor2.getCantMujeres()*0.5)*0.5);
//                                }
//                            }
//                            int min = 23;
//                            for(int h=0; h<horaPreferencial.length;h++){
//                                    int dif=Math.abs(horaPreferencial[h]-hor1.getHora());
//                                    if(dif<min){min=dif;}
//                            }
//                            funcion-=min;
//                            hor1.getPrioridad().remove(prioPos);
//                            hor2.getNoPrioridad().remove(noPrioPos);
//                            hor1.getNoPrioridad().add(noPrio);
//                            hor2.getPrioridad().add(prio);
//                        }
//                    //}
//                        
//                                                
////                        ResulHorario resulhorario=resulAgencia.getUnHorario(horario[1]);
////                        if(ubigeo==100004){
////                            //System.out.println("horario: "+resulhorario.getDia()+" "+resulhorario.getHora());
////                        }
////                        if(g==0 && resulhorario.sizeNoProridad()>0){
////                            horarios_mejora_Agencia.add(horario);
////                            continue;
////                        }                        
////                        int hora_horario=resulhorario.getHora();//hora del horario a cambiar
////                        int cantPrioritarios=resulhorario.sizeProridad();
////                        //int solo_prioritario=0;
////                        int diferencia_horaPrioritaria=23;
////                        for(int h=0; h<horaPreferencial.length;h++){
////                            int dif=Math.abs(horaPreferencial[h]-hora_horario);
////                            if(dif<diferencia_horaPrioritaria){diferencia_horaPrioritaria=dif;}
////                        }
////                        //==============================================================
////                        //if(resulhorario.sizeNoProridad()==0){solo_prioritario=1;}
////                        //else{continue;}
////                        //Por el momento solo se cambia los que tienen horarios no preferenciales llenos de puros prioritarios
////                        //==============================================================
////                        if(resulhorario.getHora()==nuevo_preferencial){
////                            continue;//Horario a cambiar tiene la hora nueva preferencial, por lo tanto no se cambia
////                        }
////                        else{
////                            boolean no_cambiado=true;                            
////                            while(no_cambiado){
////                                if(posicion_cambio>resulAgencia.getHorarios().size()-1) break;
////                                ResulHorario horarioCambio=resulAgencia.getUnHorario(posicion_cambio);
////                                if(horarioCambio.sizeProridad()==0){
////                                    resultado.get(horario[0]).cambiarHorario(horario[1], posicion_cambio);
////                                    funcion=funcion-cantPrioritarios*(diferencia_horaPrioritaria-diferencia_nuevo);
////                                    no_cambiado=false;
////                                }
////                                if(dia%6!=0){posicion_cambio+=bloqueFin_LV;}                            
////                                else{posicion_cambio+=bloqueFin_S;}
////                                dia++;
////                                if(dia%6!=0){posicion_cambio+=bloqueInicio_LV;}                            
////                                else{posicion_cambio+=bloqueInicio_S;}
////                                if(posicion_cambio>resulAgencia.getHorarios().size()-1){
////                                    nuevo_preferencial++;
////                                    diferencia_nuevo++;                                    
////                                    bloqueInicio_LV=nuevo_preferencial-h_inicioLV;
////                                    bloqueFin_LV=h_cierreLV-nuevo_preferencial;
////                                    bloqueInicio_S=nuevo_preferencial-h_inicioS;
////                                    bloqueFin_S=h_cierreS-nuevo_preferencial;
////                                    posicion_cambio=bloqueInicio_LV;
////                                    dia=1;
////                                }
////                            }                            
////                        }
//                    }
//                }
                
                //System.out.println("Resultado con mejora UBIGEO:"+funcion);
                
//                for(int n=ind_resul;n<resultado.size();n++){
//                    
//                    ResulAgencia r= resultado.get(n);
//                    System.out.println("Agencia ID:"+r.getId());
//                    System.out.println("-------------------------------------------------------");
//                    for(int t=0;t<r.getHorarios().size();t++){
//                        //System.out.println(" ");
//                        ResulHorario h = r.getHorarios().get(t);
//                        System.out.println("Dia:"+h.getDia()+"  Hora:"+h.getHora());
//                        int prioritarios=h.sizeProridad();
//                        int no_prioritarios=h.sizeNoProridad();
//                        System.out.println("Prioritarios: "+prioritarios+" - NO prioritarios: "+no_prioritarios);                        
//                        System.out.println("Hombres:"+h.getCantHombres());
//                        System.out.println("Mujeres:"+h.getCantMujeres());                                                
//                        System.out.println("-------------------------------------------------------");
//                        List<Integer> np=h.getNoPrioridad();
//                        for(int ñ=0;ñ<np.size();ñ++){
//                            System.out.print(np.get(ñ)+"");
//                            int pos=h.getPosNoPrioridad().get(ñ);
//                            System.out.print("  ID corroborar: "+beneDistrito.get(pos).getCodigoHogar());
//                            System.out.println("   Mayor:"+beneDistrito.get(pos).getFlagMayor()+"    Discapacitado:"+beneDistrito.get(pos).getFlagDis());
//                        }
//                        List<Integer> p=h.getPrioridad();
//                        for(int ñ=0;ñ<p.size();ñ++){
//                            System.out.print(p.get(ñ)+"");
//                            int pos=h.getPosprioridad().get(ñ);
//                            System.out.print("  ID corroborar: "+beneDistrito.get(pos).getCodigoHogar());
//                            System.out.println("   Mayor:"+beneDistrito.get(pos).getFlagMayor()+"      Discapacitado:"+beneDistrito.get(pos).getFlagDis());
//                        }
//                    }
//                    System.out.println("\n");
//                }
                
                dias_totales = dia_actual > dias_totales?dia_actual:dias_totales;
                hora_final = hora_actual > hora_final?hora_actual:hora_final;
//                System.out.println("Horarios mala distribución: "+horariosMalaDistribucion);
//                System.out.println("Horarios totales: "+horariosTotales);
//                System.out.println("Turnos totales: "+turnos);
//                System.out.println("Duración del algoritmo(días): "+dias_totales);
//                System.out.println("Hora final: "+hora_final);
//                System.out.println("Resultado del fitness personas despues de mejora:"+fitnessPersonas);
//                System.out.println("Resultado del fitness horas despues de mejora:"+fitnessHoras);
//                System.out.println("Prefernciales mal asignados: " + prefMalAsignados);
//                System.out.println("fitness final: " +horasMax);
                
                    //System.out.println("-------------------------------------------------------");
                    //System.out.println();               
            }
            for (Integer[] hr : horarios_sexo) {
                int agencia=hr[0];
                int horario=hr[1];
                //System.out.print("Agencia: " + agencia+"  Horario: "+horario);
            }           
            for (Integer[] hr : horarios_sexo) {
                int prefe=0;
                int agencia=hr[0];
                int horario=hr[1];
                ResulAgencia r=resultado.get(agencia);
                //System.out.println("Ubigeo: "+ r.getUbigeo());
                //System.out.print("Agencia: " + r.getId());
                ResulHorario ho=resultado.get(agencia).getHorarios().get(horario);
                //System.out.println(" Hora: " +ho.getHora() +" Dia: "+ ho.getDia());
                if (Arrays.asList(horaPreferencial).contains(ho.getHora())){
                    prefe=1;
                }
                double p_i=100.0*ho.getCantHombres()/(ho.getCantHombres()+ho.getCantMujeres());
                int index  = IntStream.range(0, resultado.size()).filter(a-> resultado.get(a).getUbigeo() == r.getUbigeo()).findFirst().orElse(-1);
                for(int l=index;l<resultado.size();l++){
                    ResulAgencia Acambio=resultado.get(l);
                    if(Acambio.getUbigeo()!=r.getUbigeo()){
                        break;
                    }
                    int tam=Acambio.getHorarios().size();
                    for(int j=0;j<tam;j++){
                        ResulHorario Hcambio=Acambio.getUnHorario(j);
                        int cambio_preferencial=0;
                        if (Arrays.asList(horaPreferencial).contains(Hcambio.getHora())){
                            cambio_preferencial=1;
                        }
                        if(prefe!=cambio_preferencial){
                            continue;
                        }
                        double p_j= 100.0*Hcambio.getCantHombres()/(Hcambio.getCantHombres()+Hcambio.getCantMujeres());
                        if(p_j<50){                            
                            p_j=100.0*(Hcambio.getCantHombres()+1)/((Hcambio.getCantHombres()+1)+(Hcambio.getCantMujeres()-1));
                            if(p_j<=50){
                                //System.out.println("Cambio hecho");
                                resultado.get(l).getUnHorario(j).setCantHombres(Hcambio.getCantHombres()+1);
                                resultado.get(l).getUnHorario(j).setCantMujeres(Hcambio.getCantMujeres()-1);
                                resultado.get(agencia).getUnHorario(horario).setCantHombres(ho.getCantHombres()-1);
                                resultado.get(agencia).getUnHorario(horario).setCantMujeres(ho.getCantMujeres()+1);
                                ho=resultado.get(agencia).getHorarios().get(horario);
                                p_i=100.0*ho.getCantHombres()/(ho.getCantHombres()+ho.getCantMujeres());
                            }
                        }
                    }
                    if(p_i<50){
                        //System.out.println("Remover");
                        horariosMalaDistribucion--;
                        break;
                    }
                }
            }
                double fitness1 = (double)100*horariosMalaDistribucion/horariosTotales;
                double fitness2 = (double)200*fitnessHoras/horasMax;
                double fitnessFinal = fitness1 + fitness2;
                System.out.println("fitness 1: " +fitness1);
                System.out.println("fitness 2: " +fitness2);
                System.out.println("fitness final: " +fitnessFinal);
//            System.out.println("Resultado de funcion despues de mejora:"+funcion);
//            fitnessHoras=0;
//            fitnessPersonas=0;
        }
    }
    
}
