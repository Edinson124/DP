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
        //Long tiempo_ejecucion = System.currentTimeMillis();
        Integer[] horaPreferencial = {8,9};
        int iteraciones = 1;
        int maxTurnos = 2;
        Algoritmo algoritmo =  new Algoritmo();
        int funcion=0;
        List<Integer[]> horarios_preferencial= new ArrayList<Integer[]>();
        List<Integer[]> horarios_no_preferencial= new ArrayList<Integer[]>();
        for (int i = 0; i < iteraciones; i++){            
            List<ResulAgencia> resultado= new ArrayList<ResulAgencia>();
            for (int j=0;j<ubigeosPrueba.length;j++){                
                int ubigeo=ubigeosPrueba[j];                
                //System.out.println("AYUDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+ubigeo );
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
                    LugarCobro l=lugares.get(m);
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
                    ResulAgencia r = new ResulAgencia(l.getIdAgencia(),l.getHoraAperturaLV().getHours(),l.getHoraCierreLV().getHours(),l.getHoraAperturaS().getHours(),l.getHoraCierreS().getHours());
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
                    System.out.println("Hora y dia:"+hora_actual+"-"+dia_actual);
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
                        double alpha = 0.3;
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
                            int x=0;int min=23;
                            if(bene.getFlagDis()==1||bene.getFlagMayor()==1){
                                x=1;                                
                            }
                            //System.out.println("tamaño LRC: "+LCR.size());
                            //System.out.println("flag: "+"Discapacitado: "+bene.getFlagDis()+" - Adulto mayor: "+bene.getFlagMayor());
                            //System.out.println("asignacion: "+bene.getCodigoHogar()+"-"+(dia_actual*100 + hora_actual)+"-"+LCR.get(pos_restringida).getBondad());
                            boolean insercion= resultado.get(ind_resul+k).nuevobene(bene.getCodigoHogar(),pos_real,bene.getGenero(),x);
                            
                            if(insercion){
                                bucle_insercion=0;
                                int hRest =LCR.get(pos_restringida).getHorarioRestantes()-1;                           
                                LCR.get(pos_restringida).setHorarioRestantes(hRest);                                
                                if(hRest==0){
                                    listaCandidatos.remove(listaCandidatos.stream().filter(y -> y.getPosicion()==pos_real).findFirst().get());
                                    faltantes--;
                                    if(faltantes==0){
                                        break;
                                    }
                                }
                                for(int h=0; h<horaPreferencial.length;h++){
                                    int dif=Math.abs(horaPreferencial[h]-hora_actual);
                                    if(dif<min){min=dif;}
                                }
                                funcion+=min;
                                //System.out.println("pruebaa: "+solucionxdistrito[0][0]);
                                //solucionxdistrito[k+1][listaRestringida[posicion][0]+ 1] = cantidadPersonasHora[k][5]*100 + cantidadPersonasHora[k][4];
                                paramLugaCobro[k][2]--; // se disminuye la cantidad de cupos restantes
                                hombres += bene.getGenero()==1?1:0;
                                mujeres += bene.getGenero()==2?1:0;
                            }
                            else{
                                bucle_insercion++;
                            }
                            if(bucle_insercion>5)break;
                            yeri=preferencial;
                        }
                        //System.out.println("hombres: "+hombres+" - Mujeres: "+mujeres);
                        funcion+=Math.abs(hombres-mujeres);
                        int prioritarios=resultado.get(ind_resul+k).getUltimoHorario().getPrioridad().size();
                        int no_prioritarios=resultado.get(ind_resul+k).getUltimoHorario().getNoPrioridad().size();
                        if((preferencial==1 && no_prioritarios>0)){
                            Integer[] horario=new Integer[3];
                            horario[0]=ind_resul+k;//agencia
                            horario[1]=resultado.get(ind_resul+k).getHorarios().size()-1;//horario
                            horarios_preferencial.add(horario);
                        }else if (preferencial==0 && prioritarios>0){
                            Integer[] horario=new Integer[3];
                            horario[0]=ind_resul+k;//agencia
                            horario[1]=resultado.get(ind_resul+k).getHorarios().size()-1;//horario   
                            horarios_no_preferencial.add(horario);
                        }
                        
                        if(yeri==1 && no_prioritarios>0){
                            System.out.println("Priritarios: "+prioritarios+" - NO prioritarios: "+no_prioritarios);
                        }else if(yeri==0 && prioritarios>0){
                            System.out.println("Priritarios: "+prioritarios+" - NO prioritarios: "+no_prioritarios);
                        }                        
                        paramLugaCobro[k][2]=paramLugaCobro[k][1];
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
                //------------------------------------------------------Mejora---------------------------------
                //prioritarios en no preferenciales
                int nuevo_preferencial=horaPreferencial[horaPreferencial.length-1]+1;      
                
                for(int g=0;g<horarios_no_preferencial.size();g++){
                    Integer[] horario=horarios_no_preferencial.get(g);
                    ResulAgencia resulAgencia=resultado.get(horario[0]);
                    ResulHorario resulhorario=resultado.get(horario[0]).getHorarios().get(horario[1]);
                    if(resulhorario.getHora()==nuevo_preferencial){
                        break;
                    }
                    else{
                        int h_inicio=resulAgencia.getApertura_LV();
                        int h_cierre=resulAgencia.getCierre_LV();
                        boolean cambio=true;
                        int bloque=h_cierre-h_inicio;
                        int horario_cambio=nuevo_preferencial-h_inicio;//posicion del horario tentativo cambiar
                        while(cambio){
                            break;
                        }
                                           
                        
                    }
                       
                    
                }
                
                
                
                
                
                for(int n=0;n<resultado.size();n++){
                    ResulAgencia r= resultado.get(n);
                    System.out.println("Agencia ID:"+r.getId());
                    System.out.println("-------------------------------------------------------");
                    for(int t=0;t<r.getHorarios().size();t++){
                        ResulHorario h = r.getHorarios().get(t);
                        System.out.println("Dia:"+h.getDia());
                        System.out.println("Hora:"+h.getHora());
                        System.out.println("Hombres:"+h.getCantHombres());
                        System.out.println("Mujeres:"+h.getCantMujeres());
                        if((r.getId()==10&&(h.getCantHombres()+h.getCantMujeres())<20)||(r.getId()==1&&(h.getCantHombres()+h.getCantMujeres())<18)){
                         
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
                        }

                        System.out.println(" ");
                    }
                    System.out.println("-------------------------------------------------------");
                    System.out.println();  
                
                }
            }
            
            System.out.println("Resultado de funcion:"+funcion);
            funcion=0;
        }
        
    }
    
    
}
