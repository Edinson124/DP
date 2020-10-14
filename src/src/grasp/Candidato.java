
package grasp;

/**
 *
 * @author USUARIO
 */
public class Candidato {
    private int posicion;
    private int bondad;
    private int horarioRestantes;

    public Candidato() {
    }

    public Candidato(int posicion, int horarioRestantes) {
        this.posicion = posicion;
        this.horarioRestantes = horarioRestantes;
    }

    public int getHorarioRestantes() {
        return horarioRestantes;
    }

    public void setHorarioRestantes(int horarioRestantes) {
        this.horarioRestantes = horarioRestantes;
    }
    

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public double getBondad() {
        return bondad;
    }

    public void setBondad(int bondad) {
        this.bondad = bondad;
    }
    
    
    
}
