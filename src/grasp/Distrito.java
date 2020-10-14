/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grasp;

public class Distrito {

    public Distrito(String id,String ubigeo,String de,String p,String dis,String pob,String con,String den) {
        this.idDistrito=Integer.parseInt(id);
        this.ubigeo=Integer.parseInt(ubigeo);
        this.departamento = de;
        this.provincia = p;
        this.distrito = dis;
        this.poblacion=Integer.parseInt(pob);
        this.contagiados = Integer.parseInt(con);
        this.densidad=Double.parseDouble(den);
    }

    public Distrito() {
    }

    
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public int getContagiados() {
        return contagiados;
    }

    public void setContagiados(int contagiados) {
        this.contagiados = contagiados;
    }

    public int getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(int idDistrito) {
        this.idDistrito = idDistrito;
    }

    public int getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(int ubigeo) {
        this.ubigeo = ubigeo;
    }
    
    private int idDistrito;
    private int ubigeo;
    private String departamento;
    private String provincia;
    private String distrito;
    private int poblacion;
    private int contagiados;
    private double densidad;
    public int getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(int poblacion) {
        this.poblacion = poblacion;
    }

    public double getDensidad() {
        return densidad;
    }

    public void setDensidad(double densidad) {
        this.densidad = densidad;
    }
}
