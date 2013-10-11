
import java.io.IOException;
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kki8
 */
public class RunPoolsGeneration {
    public static void main(String[] args) throws IOException, Exception
     {
         int nTests = 20;
         double avsol1 = 0;
         double avsol2 = 0;
         for (int i = 0; i < nTests; i++)
         {
             double p = 0.5;
             int n = 20;
             int tabuN = 1;

             DoubleGraph g = new DoubleGraph(n,p);
//             DoubleGraph g1 = new DoubleGraph(g);
//             DoubleGraph g2 = new DoubleGraph(g);
//             DoubleGraph g = new DoubleGraph("Pajek_Nonrel_bad.net");
//             DoubleGraph g = new DoubleGraph("Iter4_Nonrel.net", "Iter4_Nonsep.net");
//             g.printToConsole();
             g.printFilePajek("Pajek");

    //         Cut c = g.findMaxCutNonsep();
    //         c.printToConsole();

             DoubleGraphProcessor dgp = new DoubleGraphProcessor();
//             TestSet ts = dgp.findCliqueTestSetHeur(g);
             TestSet ts = dgp.findCliqueTestSetHeur2(g, tabuN);
    //         ts.printToConsole();
             System.out.println();
//             ts1.printToConsoleCheckFeasibility(g1);
             ts.printToConsoleCheckFeasibility(g);

//             System.out.println("NTests1: " + ts1.getNTests());
//             avsol1+= ts1.getNTests();
             System.out.println("NTests2: " + ts.getNTests());
             avsol2+= ts.getNTests();
         }
//         System.out.println("Avsol1=" + (avsol1/nTests));
         System.out.println("Avsol2=" + (avsol2/nTests));
     }
}
