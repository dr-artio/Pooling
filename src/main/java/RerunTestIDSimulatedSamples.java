/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pooling;

import ErrorCorrection.DataSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *
 * @author kki8
 */
public class RerunTestIDSimulatedSamples {
    public static void main(String[] args) throws IOException, InterruptedException
     {
         int[] maxPoolSizes = {15};
         int[] ns = {100};
         int[] Tests = {1};
         int[] clcoeffs = {2};
         int clustCoeff = 3;
         
         String alignMethod = "Nothing";
         String clustMethod = "kGEM";
         
         
          int maxWeight = 10;
          int gap = 4;
          int tabuN = 1;
          
         for (int sz = 0; sz < maxPoolSizes.length; sz++)
         for (int in = 0; in < ns.length; in++)
             for (int it = 0; it < Tests.length; it++)
                 for (int cl = 0; cl < clcoeffs.length; cl++)
                 {
                    int n = ns[in];
                    double maxPoolSize = maxPoolSizes[sz];

                    String outdir = "Test_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + clcoeffs[cl] + "_" + Tests[it] + "_rerun";
                    String outdir_prev = "Test_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + clcoeffs[cl] + "_" + Tests[it];

                    PoolSimulator ps = new PoolSimulator();

                    ArrayList<ArrayList<Integer>> partitions = ps.retrievePartitions(outdir_prev);
                    int nPools = partitions.size();

                    PoolsOperator po = new PoolsOperator();
                    File dir = new File(outdir);
                    dir.mkdir();
                    po.setOutdir(outdir);

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


                     String genlogfile_prev = outdir_prev + File.separator + "gen_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + Tests[it] + ".txt";
                     String genlogfile = outdir + File.separator + "gen_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + Tests[it] + ".txt";
                     File f = new File(genlogfile);
                     if (f.exists())
                         f.delete();
                     Files.copy(new File(genlogfile_prev).toPath(), new File(genlogfile).toPath());

                     ArrayList<Pool> pools = new ArrayList();
                     for (int i = 0; i < nPools; i++)
                     {
                        String poolFile = outdir_prev + File.separator + "SimPool" + i + ".fas_unique.fas";
                        for (Integer j : partitions.get(i))
                            poolFile += ("_" + j);
                        poolFile += ".fas";
                        DataSet ds = new DataSet(poolFile,"ET");
                        pools.add(new Pool(ds,partitions.get(i)));
    //                 
                        String outFile = outdir + File.separator + "SimPool" + i + ".fas";
                        pools.get(i).ds.delGaps();
                        pools.get(i).printToFile(outFile);
                        pools.get(i).setFileName(outFile);
                        pools.get(i).printToFileUnique(outFile + "_unique.fas");

                     }

                     long startTime = System.currentTimeMillis();
                     ArrayList<Pool> intsDiffs = po.recover(pools, ar, clustCoeff,alignMethod,clustMethod);
                     long tm = System.currentTimeMillis() - startTime;

                     FileWriter fw = new FileWriter(outdir + File.separator +"clustering_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + Tests[it] + ".txt");

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

     //                for (int i = 0; i < intsDiffs.size(); i++)
     //                    intsDiffs.get(i).printToFileUnique(outdir + File.separator + intsDiffs.get(i).fileName);



                    ArrayList<Pool> recovSamp = po.getSamps(intsDiffs);

                    String resFile = outdir + File.separator + "res_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + Tests[it] + ".txt";
                    fw = new FileWriter(resFile);
                     ArrayList<Boolean> checkRecov = new ArrayList();
                     for (int i = 0; i < recovSamp.size(); i++)
                     {
                         Pool p =recovSamp.get(i);
                         p.printToFileUnique(outdir + File.separator + p.fileName);
                         p.printToFileUnique(outdir + File.separator +"Samp" + (i+1) + "_" + maxPoolSizes[sz] + "_" + ns[in] + "_" + Tests[it] + ".fas");
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
