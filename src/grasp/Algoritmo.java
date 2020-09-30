
package grasp;

import java.util.List;


/**
 *
 * @author USUARIO
 */
public class Algoritmo {
    public int[][] calcularBondad(List<Beneficiario> beneDistrito, int preferencial,double min, double max, int hombres, int mujeres){
        int[][] candidatos = new int[beneDistrito.size()][4];
        int cont = 0;
        for(int i= 0; i< beneDistrito.size(); i++){
            Beneficiario ben = beneDistrito.get(i);
            int hom = ben.getGenero()==1?1:0;
            if(ben.getHorariosRestantes()>0){
                candidatos[cont][0] = i; 
                candidatos[cont][1] = preferencial*( 5*ben.getFlagDis()+ 5*ben.getFlagMayor()) + (1-preferencial) *(5*(1-ben.getFlagDis())+ 5*(1-ben.getFlagMayor())) /1+2*ben.getCantInci() + 3*(((hom*hombres+(1-hom)*mujeres))/(hombres+mujeres)); 
                candidatos[cont][2] = ben.getHorariosRestantes(); 
                candidatos[cont][3] = ben.getGenero();
                cont++;
            }
        }  
        return candidatos;
    }
    
}
