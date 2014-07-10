package Pooling;


import Pooling.PoolsOperator;
import Pooling.PoolSimulator;
import Pooling.Pool;
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
public class RunTestIDSimulatedSamples {
     public static void main(String[] args) throws IOException, InterruptedException
     {
         int nSeqPool = 3000;
         int nRef = 3;
         int[] ns = {8};
         int[] maxPoolSizes = {25};
         int nTests = 1;
         int filtNearNeigParam = 4;
         int clustCoeff = 2;
         
         String alignMethod = "Nothing";
         String clustMethod = "kGEM";
         
         
          int maxWeight = 10;
          int gap = 4;
          int tabuN = 1;
          
         for (int sz = 0; sz < maxPoolSizes.length; sz++)
         for (int in = 0; in < ns.length; in++)
             for (int it = 0; it < nTests; it++)
             {
                int n = ns[in];
                double maxPoolSize = maxPoolSizes[sz];
                
                DoubleGraph g = new DoubleGraph(n,maxWeight,gap,maxPoolSize,false);
                g.reduce();
                DoubleGraphProcessor dgp = new DoubleGraphProcessor();
                TestSet ts = dgp.findCliqueTestSetHeur2(g, tabuN);
                ts.complete(n);
                ArrayList<ArrayList<Integer>> partitions = ts.getArrayList();
                int nPools = partitions.size();
         
                PoolsOperator po = new PoolsOperator();
                String outdir = "Test_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + it + "_forFigure";
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
                 String genlogfile = outdir + File.separator + "gen_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + it + ".txt";
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
                 
                 FileWriter fw = new FileWriter(outdir + File.separator +"clustering_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + it + ".txt");

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


        /*        ArrayList<Pool> recovSamp = new ArrayList();
                for (int i = intsDiffs.size()-n; i < intsDiffs.size(); i++)
                    recovSamp.add(intsDiffs.get(i));*/

                ArrayList<Pool> recovSamp = po.getSamps(intsDiffs);
                
                String resFile = outdir + File.separator + "res_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + it + ".txt";
                fw = new FileWriter(resFile);
                 ArrayList<Boolean> checkRecov = new ArrayList();
                 for (int i = 0; i < recovSamp.size(); i++)
                 {
                     Pool p =recovSamp.get(i);
                     p.printToFileUnique(outdir + File.separator + p.fileName);
                     p.printToFileUnique(outdir + File.separator +"Samp" + (i+1) + "_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + it + ".fas");
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
             }
                 
     }
    
}
