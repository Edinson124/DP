
package tabu;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author USUARIO
 */
public class Algoritmo {
    public List<Candidato> inicializar(List<Beneficiario> beneDistrito){
        List<Candidato> candidatos= new ArrayList<Candidato>();
        for(int i= 0; i< beneDistrito.size(); i++){
            Beneficiario ben = beneDistrito.get(i);
            int hom = ben.getGenero()==1?1:0;
            if(ben.getHorariosRestantes()>0){
                Candidato c= new Candidato(i,ben.getHorariosRestantes());
                candidatos.add(c);
            }
        } 
        return candidatos;
        
    }
    public void calcularBondad(List<Candidato> candidatos,List<Beneficiario> beneDistrito, int preferencial,double[] maxmin, double hombres, double mujeres){
        int pri=1;
        for(int i= 0; i< candidatos.size(); i++){
            int pos=candidatos.get(i).getPosicion();
            Beneficiario ben = beneDistrito.get(pos);
            int hom = ben.getGenero()==1?1:0;
                pri=0;
                int prio=0;
                if((ben.getFlagDis()==1)||(ben.getFlagMayor()==1)){
                    prio=1;
                }
                //int bond= (int)((preferencial*( 5*ben.getFlagDis()+ 5*ben.getFlagMayor()) + (1-preferencial) *(5*(1-ben.getFlagDis())+ 5*(1-ben.getFlagMayor()))) /(1+2*ben.getCantInci()+ 3*(((hom*hombres+(1-hom)*mujeres))/(1+(hombres+mujeres))))); 
                int bond= (int)((preferencial*(5*prio) + (1-preferencial) *(5*(1-prio))+(2*candidatos.get(i).getHorarioRestantes())) /(1+ 3*(((hom*hombres+(1-hom)*mujeres))/(1+(hombres+mujeres))))); 
                //System.out.println(bond);
                candidatos.get(i).setBondad(bond);
                
                if(pri==1){maxmin[0]=bond;maxmin[1]=bond;}
                else {
                    if(bond>maxmin[0]) maxmin[0]=bond;
                    if(bond<=maxmin[1]) maxmin[1]=bond;
                }
            }
        }  
       // Quicksort(candidatos, 0, cont-1);
    
    
    
    public void Quicksort(int[][] v, int prim, int ult){
 if (prim < ult)
 {
  /*Selecciona 1 elemento del vector y coloca los menores
   que él a su izq y los mayores a su derech*/
  int p = Pivote(v, prim, ult, ult);
 
  /* Repite el proceso para c/u de las particiones
     generadas en el paso anterior */
  Quicksort(v, prim, p - 1);
  Quicksort(v, p + 1, ult);
 }
}
 
/* Implementación no clásica de la función Pivote. En lugar de
recorrer el vector simultáneamente desde ambos extremos hasta el
cruce de índices, se recorre desde el comienzo hasta el final */
int Pivote(int[][] v, int prim, int ult, int piv)
{
 int p = v[piv][1];
 int j = prim;
 
 // Mueve el pivote a la última posición del vector
 Intercambia(v, piv, ult);
 
 /* Recorre el vector moviendo los elementos menores
  o iguales que el pivote al comienzo del mismo */
 for (int i = prim; i < ult; i++)
 {
  if (v[i][1] >= p)
  {
   Intercambia(v, i, j);
   j++;
  }
 }
 
 // Mueve el pivote a la posición que le corresponde
 Intercambia(v, j, ult);
 
 return j;
}
 
void Intercambia(int[][] v, int a, int b){
 if (a != b) {
  int[] tmp = v[a];
  v[a] = v[b];
  v[b] = tmp;
 }
}
    
}


