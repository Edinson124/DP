package grasp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class ResulHorario {
    
    private int dia;
    private int hora;
    private int cantMujeres;
    private int cantHombres;
    private List<Integer> noPrioridad = new ArrayList<Integer>();
    private List<Integer> posNoPrioridad = new ArrayList<Integer>();
    private List<Integer> prioridad = new ArrayList<Integer>();
    private List<Integer> posprioridad = new ArrayList<Integer>();

    public ResulHorario() {
    }

    public ResulHorario(int dia, int hora) {
        this.dia = dia;
        this.hora = hora;
        this.cantMujeres = 0;
        this.cantHombres = 0;
    }
    
    public void nuevobene(int indice, int pos,int gen,int priori){
        if (gen == 1){cantHombres++;}
        else{cantMujeres++;}
        if(priori==1){
            noPrioridad.add(indice);
            posNoPrioridad.add(pos);
            
        }else{
            prioridad.add(indice);
            posprioridad.add(pos);
        }
        
        }
    
    
    

    public List<Integer> getPosNoPrioridad() {
        return posNoPrioridad;
    }

    public void setPosNoPrioridad(List<Integer> posNoPrioridad) {
        this.posNoPrioridad = posNoPrioridad;
    }

    public List<Integer> getPosprioridad() {
        return posprioridad;
    }

    public void setPosprioridad(List<Integer> posprioridad) {
        this.posprioridad = posprioridad;
    }
    
    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getCantMujeres() {
        return cantMujeres;
    }

    public void setCantMujeres(int cantMujeres) {
        this.cantMujeres = cantMujeres;
    }

    public int getCantHombres() {
        return cantHombres;
    }

    public void setCantHombres(int cantHombres) {
        this.cantHombres = cantHombres;
    }

    public List<Integer> getNoPrioridad() {
        return noPrioridad;
    }

    public void setNoPrioridad(List<Integer> noPrioridad) {
        this.noPrioridad = noPrioridad;
    }

    public List<Integer> getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(List<Integer> prioridad) {
        this.prioridad = prioridad;
    }
    
    
}
