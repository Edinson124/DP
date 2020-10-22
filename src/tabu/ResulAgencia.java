package tabu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class ResulAgencia {
    private int id;
    private int ubigeo;
    private int apertura_LV;
    private int cierre_LV;
    private int apertura_S;
    private int cierre_S;
    private List<ResulHorario> horarios = new ArrayList<ResulHorario>();
    
    public void cambiarHorario(int x,int y){
        ResulHorario horarioX=horarios.get(x);
        int diaX=horarioX.getDia();
        int horaX=horarioX.getHora();
        ResulHorario horarioY=horarios.get(y);
        int diaY=horarioY.getDia();
        int horaY=horarioY.getHora();
        horarios.set(y, horarioX);
        horarios.set(x, horarioY);
        horarios.get(x).setDia(diaX);
        horarios.get(x).setHora(horaX);
        horarios.get(y).setDia(diaY);
        horarios.get(y).setHora(horaY);
        
    }

    public int getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(int ubigeo) {
        this.ubigeo = ubigeo;
    }
    
    
    
    public ResulHorario getUnHorario(int n){
        return horarios.get(n);
    }
    public int sizeHorarios(){
        return horarios.size();
    }
    
    public int getApertura_LV() {
        return apertura_LV;
    }

    public void setApertura_LV(int apertura_LV) {
        this.apertura_LV = apertura_LV;
    }

    public int getCierre_LV() {
        return cierre_LV;
    }

    public void setCierre_LV(int cierre_LV) {
        this.cierre_LV = cierre_LV;
    }

    public int getApertura_S() {
        return apertura_S;
    }

    public void setApertura_S(int apertura_S) {
        this.apertura_S = apertura_S;
    }

    public int getCierre_S() {
        return cierre_S;
    }

    public void setCierre_S(int cierre_S) {
        this.cierre_S = cierre_S;
    }
    
    
    public ResulHorario getUltimoHorario(){
         int size = horarios.size();
         return horarios.get(size-1);
    }
    public void nuevoHorario(int dia,int hora){
        ResulHorario r= new ResulHorario(dia,hora);
        horarios.add(r);
    }
    public boolean nuevobene(int id, int pos,int gen,int priori){
        int size = horarios.size();
        boolean insercion=horarios.get(size-1).nuevobeneHorario(id, pos,gen,priori);
        return insercion;
    }

    public ResulAgencia(int ubi,int id,int hALV,int HCLV,int HAS,int HCS) {
        this.id = id;
        this.ubigeo = ubi;
        this.apertura_LV=hALV;
        this.cierre_LV=HCLV;
        this.apertura_S=HAS;
        this.cierre_S=HCS;
    }
    
    public ResulAgencia() {
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ResulHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<ResulHorario> horarios) {
        this.horarios = horarios;
    }
    
}
