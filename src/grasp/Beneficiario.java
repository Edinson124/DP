/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grasp;

public class Beneficiario {

    public Beneficiario() {
    }

    public Beneficiario(String[] arg) {
        this.codigoHogar = Integer.parseInt(arg[0]);
        this.distrito = new Distrito(arg[1],arg[2],arg[3],arg[4],arg[5],arg[6],arg[7],arg[8]);
        this.genero = Integer.parseInt(arg[9]);
        this.flagDis = Integer.parseInt(arg[11]);
        this.flagMayor = Integer.parseInt(arg[12]);
        this.cantInci = Integer.parseInt(arg[13]);
        this.estado = arg[14];
        this.horariosRestantes = 2;
    }

    public int getCodigoHogar() {
        return codigoHogar;
    }

    public void setCodigoHogar(int codigoHogar) {
        this.codigoHogar = codigoHogar;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getFlagDis() {
        return flagDis;
    }

    public void setFlagDis(int flagDis) {
        this.flagDis = flagDis;
    }

    public int getFlagMayor() {
        return flagMayor;
    }

    public void setFlagMayor(int flagMayor) {
        this.flagMayor = flagMayor;
    }

    public int getCantInci() {
        return cantInci;
    }

    public void setCantInci(int cantInci) {
        this.cantInci = cantInci;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    private int codigoHogar;
    private Distrito distrito;

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }
    
    public int getHorariosRestantes() {
        return horariosRestantes;
    }

    public void setHorariosRestantes(int horariosRestantes) {
        this.horariosRestantes = horariosRestantes;
    }
    
    private int genero;
    private int flagDis;
    private int flagMayor;
    private int cantInci;
    private String estado;
    private int horariosRestantes;

  
    
    
    
}
