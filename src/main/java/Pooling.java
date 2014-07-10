package Pooling;


import Pooling.DoubleGraphProcessor;
import Pooling.DoubleGraph;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kki8
 */
public class Pooling {
     public static void main(String[] args) throws IOException, Exception
     {
         // pools generation
         
             int n = Integer.parseInt(args[0]);
             double p = Double.parseDouble(args[1]);
//             double t = Double.parseDouble(args[2]);
             int i = Integer.parseInt(args[2]);
         
             int maxWeight = 20;
             int gap = 4;
//             int n = i;
             int tabuN = 1;

//             DoubleGraph g = new DoubleGraph(n,p);
//             g.setThreshold(t);
             double t = 35;
             DoubleGraph g = new DoubleGraph(n,p,t,false);
//             DoubleGraph g = new DoubleGraph(n,maxWeight,gap,t,false);

             DoubleGraph g1 = new DoubleGraph(g);
             g.reduce();

             DoubleGraphProcessor dgp = new DoubleGraphProcessor();
             
             long startTime = System.currentTimeMillis();
             TestSet ts = dgp.findCliqueTestSetHeur2(g, tabuN);
             long tm = System.currentTimeMillis() - startTime;
             
             ts.printToConsole();
             System.out.println();

             System.out.println("NTests2: " + ts.getNTests());
         
         
         
             FileWriter fw = new FileWriter("testResults1_" + n + "_" + p + "_" + i + ".txt");
             fw.write(ts.getNTests() + " " + tm);
             fw.close();
         
         
         
         // inference of samples
 /*        int nSeqPool = 10000;
         
         int n = Integer.parseInt(args[0]);
         int maxPoolSize = Integer.parseInt(args[1]);
         int it = Integer.parseInt(args[2]);
         int clustCoeff = Integer.parseInt(args[3]);
         
         
         String alignMethod = "Nothing";
         String clustMethod = "kGEM";
         
         
          int maxWeight = 10;
          int gap = 4;
          int tabuN = 1;
          
             {
                
                DoubleGraph g = new DoubleGraph(n,maxWeight,gap,maxPoolSize,false);
                g.reduce();
                DoubleGraphProcessor dgp = new DoubleGraphProcessor();
                TestSet ts = dgp.findCliqueTestSetHeur2(g, tabuN);
                ts.complete(n);
                ArrayList<ArrayList<Integer>> partitions = ts.getArrayList();
                int nPools = partitions.size();
         
                PoolsOperator po = new PoolsOperator();
                String outdir = "Test_" + maxPoolSize + "_" + n + "_" + clustCoeff + "_" + it;
                File dir = new File(outdir);
                dir.mkdir();
                po.setOutdir(outdir);
//                ArrayList<ArrayList<Integer>> partitions = po.generatePartitions(n);
                
                for (ArrayList<Integer> ar : partitions)
                {
                     for (Integer i : ar)
                         System.out.print(i + " ");
                     System.out.println();
                }

                System.out.println("-------");
                ArrayList<sampRecovAction> ar = po.calcRecovActionsGeneral(partitions);

//                ArrayList<sampRecovAction> ar = po.calcRecovActionsRecursive(n);
                for (int i = 0; i < ar.size(); i++)
                {
                    System.out.print(i + ": ");
                    ar.get(i).print();
                }

                 PoolSimulator ps = new PoolSimulator("Clean_by_cutoffs_meta10_split_new_1");
                 ArrayList<ArrayList<Double>> frequencies = ps.generateFreqDistr(n, partitions, "Uniform");
//                 ArrayList<ArrayList<Double>> frequencies = ps.generateFreqDistr(n, partitions, "Geometric");
                 String genlogfile = outdir + File.separator + "gen_" + maxPoolSize + "_" + n + "_" + it + ".txt";
                 ArrayList<Pool> pools = ps.generate(n, nSeqPool,frequencies, partitions,genlogfile);
                 for (int i = 0; i < pools.size(); i++)
                 {
                    String outFile = outdir + File.separator + "SimPool" + i + ".fas";
//                    pools.get(i).filterDistNearNeighb(filtNearNeigParam);
                    pools.get(i).ds.delGaps();
                    pools.get(i).printToFile(outFile);
                    pools.get(i).setFileName(outFile);
                    pools.get(i).printToFileUnique(outFile + "_unique.fas");

                 }

                 long startTime = System.currentTimeMillis();
                 ArrayList<Pool> intsDiffs = po.recover(pools, ar, clustCoeff,alignMethod,clustMethod);
                 long tm = System.currentTimeMillis() - startTime;
                 
                 FileWriter fw = new FileWriter(outdir + File.separator +"clustering_" + maxPoolSize + "_" + n + "_" + it + ".txt");

                 for (int i = 0; i < ar.size(); i++)
                     if (ar.get(i).oper.equalsIgnoreCase("i") && (ar.get(i).i != ar.get(i).j))
                     {
                         System.out.println(intsDiffs.get(i).fileName);
                         String addr = outdir + File.separator +"clust_u_" + intsDiffs.get(ar.get(i).i).fileName + "_" + intsDiffs.get(ar.get(i).j).fileName + ".fas" + File.separator + "reads_clustered.fas";
                         fw.write(intsDiffs.get(i).fileName + "\n");
                         fw.write(ps.checkClusteringGetOutString(addr));
                         System.out.println("----------");
                     }
                 fw.close();


                ArrayList<Pool> recovSamp = po.getSamps(intsDiffs);
                
                String resFile = outdir + File.separator + "res_" + maxPoolSize + "_" + n + "_" + it + ".txt";
                fw = new FileWriter(resFile);
                 ArrayList<Boolean> checkRecov = new ArrayList();
                 for (int i = 0; i < recovSamp.size(); i++)
                 {
                     Pool p =recovSamp.get(i);
                     p.printToFileUnique(outdir + File.separator + p.fileName);
                     p.printToFileUnique(outdir + File.separator +"Samp" + (i+1) + "_" + maxPoolSize + "_" + n + "_" + it + ".fas");
                     String s = (i+1) + ": ";
       //              System.out.print(s);
        //             checkRecov.add(ps.checkSolution(p));

                     s = s + ps.checkSolutionGetOutString(p,outdir);
                     System.out.println(s);
                     fw.write(s + "\n");

                     System.out.println();
                 }
                 fw.close();

         //        for (int i = 0; i < recovSamp.size(); i++)
         //            System.out.print(checkRecov.get(i) + " ");
                 


                 System.out.println("Number of pools: " + nPools);
                 System.out.println("Working time: " + tm);
                 ps.generateReport(genlogfile, resFile, nPools, tm, outdir, n);
             } */
     }
}
    
