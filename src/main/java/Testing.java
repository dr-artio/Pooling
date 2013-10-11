
import errorcorrection.DataSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kki8
 */
public class Testing {
    public static void main(String[] args) throws IOException, InterruptedException
    {

               
        String name = "cDNApool4_reversed_unique.fas";
        Pool p = new Pool(name);
        p.ds.PrintUniqueReads("union_reads.fas");       
        PoolsOperator po = new PoolsOperator();
        int k = 3;
        po.clusteringKGEM(k);
        

        
    }
    
}
