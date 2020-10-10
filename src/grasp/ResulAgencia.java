package grasp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class ResulAgencia {
    private int id;
    private List<ResulHorario> horarios = new ArrayList<ResulHorario>();
    
    public void nuevoHorario(int dia,int hora){
        ResulHorario r= new ResulHorario(dia,hora);
        horarios.add(r);
    }
    public void nuevobene(int id, int pos,int gen,int priori){
        int size = horarios.size();
        horarios.get(size-1).nuevobene(id, pos,gen,priori);
    }

    public ResulAgencia(int id) {
        this.id = id;
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
