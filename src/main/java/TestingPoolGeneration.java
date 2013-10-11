/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author kki8
 */
public class TestingPoolGeneration {
    public static void main(String[] args)
     {
         double p = 0.75;
         ArrayList<Double> avSolSizes = new ArrayList();
         
         for (int n = 550; n <= 1000; n+=50)
         {
             int nTestings = 2;

             ArrayList<Integer> results = new ArrayList();

             for (int i = 0; i < nTestings; i++)
             {

                DoubleGraph g = new DoubleGraph(n,p);
                DoubleGraphProcessor dgp = new DoubleGraphProcessor();
                TestSet ts = dgp.findCliqueTestSetHeur(g);
                ts.printToConsole();

                results.add(ts.getNTests());
             }

             double avSol = 0;
             for (int i = 0; i < results.size(); i++)
             {
                 avSol+=results.get(i);
                 System.out.print(results.get(i) + " ");
             }
             System.out.println();
             avSol/=results.size();
             System.out.println(avSol);
             avSolSizes.add(avSol);
         }
         
         for (int i = 0; i < avSolSizes.size(); i++)
             System.out.print(avSolSizes.get(i) + " ");
     }
    
    
}
