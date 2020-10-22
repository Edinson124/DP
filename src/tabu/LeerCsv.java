package tabu;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LeerCsv {
    public ArrayList<LugarCobro> leer(){
        ArrayList<LugarCobro> datos = new ArrayList<LugarCobro>();
        Path filepath = Paths.get("C:\\Users\\USUARIO\\Downloads\\datos_dp\\a100.csv");
        try{
            BufferedReader br = Files.newBufferedReader(filepath);
            String linea,cab;
            cab=br.readLine();
            while((linea=br.readLine())!=null){
                String[] datosDeLinea =linea.split(";");
                LugarCobro lugar= new LugarCobro(datosDeLinea,1);
                datos.add(lugar);
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        return datos;
    }
    public ArrayList<Beneficiario> leerBene(int horarios,int restar, int max_inci){
        ArrayList<Beneficiario> datos = new ArrayList<Beneficiario>();
        Path filepath = Paths.get("C:\\Users\\USUARIO\\Downloads\\datos_dp\\b100000.csv");
        try{
            BufferedReader br = Files.newBufferedReader(filepath);
            String linea,cab;
            cab=br.readLine();
            while((linea=br.readLine())!=null){
                String[] datosDeLinea =linea.split(";");
                Beneficiario lugar= new Beneficiario(datosDeLinea,horarios,restar,max_inci);
                datos.add(lugar);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return datos;
    }
    
}
