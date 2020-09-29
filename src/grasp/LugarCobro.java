/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grasp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LugarCobro {

    public LugarCobro() {
    }

    public LugarCobro(String[] arg)  {
        try {
            this.idAgencia = Integer.parseInt(arg[0]);
            this.codigo = Integer.parseInt(arg[1]);
            this.nombre = arg[2];
            this.distrito = new Distrito(arg[3],arg[4],arg[5],arg[6],arg[7],arg[8],arg[9],arg[10]);
            this.direccion = arg[11];
            this.horaAperturaLV = new SimpleDateFormat("HH:mm").parse(arg[12]);
            this.horaCierreLV = new SimpleDateFormat("HH:mm").parse(arg[13]);
            this.horaAperturaS = new SimpleDateFormat("HH:mm").parse(arg[14]);
            this.horaCierreS = new SimpleDateFormat("HH:mm").parse(arg[15]);
            this.tipo = arg[16];
            this.cajas = Integer.parseInt(arg[17]);
            this.perHora = Integer.parseInt(arg[18]);
            this.estado = arg[19];
        } catch (ParseException ex) {
            Logger.getLogger(LugarCobro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(int idAgencia) {
        this.idAgencia = idAgencia;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }
    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getHoraAperturaLV() {
        return horaAperturaLV;
    }

    public void setHoraAperturaLV(Date horaAperturaLV) {
        this.horaAperturaLV = horaAperturaLV;
    }

    public Date getHoraCierreLV() {
        return horaCierreLV;
    }

    public void setHoraCierreLV(Date horaCierreLV) {
        this.horaCierreLV = horaCierreLV;
    }

    public Date getHoraAperturaS() {
        return horaAperturaS;
    }

    public void setHoraAperturaS(Date horaAperturaS) {
        this.horaAperturaS = horaAperturaS;
    }

    public Date getHoraCierreS() {
        return horaCierreS;
    }

    public void setHoraCierreS(Date horaCierreS) {
        this.horaCierreS = horaCierreS;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCajas() {
        return cajas;
    }

    public void setCajas(int cajas) {
        this.cajas = cajas;
    }

    public int getPerHora() {
        return perHora;
    }

    public void setPerHora(int perHora) {
        this.perHora = perHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    private int idAgencia;
    private Distrito distrito;
    private int codigo;
    private String nombre;
    private String direccion;
    private Date horaAperturaLV;
    private Date horaCierreLV;
    private Date horaAperturaS;
    private Date horaCierreS;
    private String tipo;
    private int cajas;
    private int perHora;
    private String estado;
    
}
