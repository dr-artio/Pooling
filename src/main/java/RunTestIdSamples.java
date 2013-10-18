
import errorcorrection.DataSet;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class RunTestIdSamples {
    public static void main(String[] args) throws IOException, InterruptedException
     {
         int nSamples = 8;
         double cutoff = 0.15;
         double cutoff_align = 0.45;
         double cutoff_len = 325;
         PoolsOperator po = new PoolsOperator();
         ArrayList<ArrayList<Integer>> partitions = po.generatePartitions(nSamples);
         for (ArrayList<Integer> ar : partitions)
         {
             for (Integer i : ar)
                 System.out.print(i + " ");
             System.out.println();
         }
         int nPools = partitions.size();
         ArrayList<Pool> pools = new ArrayList();
         String poolType = "cDNApool";
         for (int i = 0; i < nPools; i++)
         {
//             String poolFile = poolType + (i+1) + "_reversed_unique.fas";
             String poolFile = poolType + (i+1) + "_reversed_unique_all_reads.fas";
             DataSet ds = new DataSet(poolFile);
             pools.add(new Pool(ds,partitions.get(i)));
         }
         
         Integer badSample = new Integer(1);
         for (int i = 0; i < nPools; i++)
             if (pools.get(i).indsamples.contains(badSample))
                 pools.get(i).indsamples.remove(badSample);
         
                 
         
//         int i = 0;
         String refFile = "refer_NCNH_7S.fas";
/*         for (int i = 0; i < nPools; i++)
         {
//             pools.get(i).filter(refFile, cutoff);
//             pools.get(i).filter(refFile, cutoff_align, cutoff_len);
//             pools.get(i).compareWithRef(refFile,poolType + (i+1) + "corrected_haplotypes_align_ref");
//             pools.get(i).compareWithRefPrintLabelledReads(refFile,poolType + (i+1) + "corrected_haplotypes_align_ref");
//             pools.get(i).compareWithRefKmers(refFile,poolType + (i+1)+"_corrected.fas");
//             pools.get(i).ds.PrintUniqueReads(poolType + (i+1) + "MID" + (i+5) + "HVR1_reversed.fas_unique.fas_filtered_refgenotype.fas");
//             pools.get(i).ds.PrintReads(poolType + (i+1) + "MID" + (i+5) + "HVR1_reversed.fas_filtered_refgenotype.fas");
//             String distMatrFile = poolType + (i+1) + "filtered_refgenotype_distMatr.txt";
//             pools.get(i).generateDistanceMatrix();
//             pools.get(i).printDistanceMatrix(distMatrFile);
             
//             pools.get(i).readDistMatr(distMatrFile);
 
//             pools.get(i).separateClusters(poolType + (i+1) + "filtered_refgenotype_clusters.txt");
//             pools.get(i).printClustersReads(poolType + (i+1));
                     
                     
         }*/
         
         
         Pool inter = pools.get(0).intersectKGEM(pools.get(2));
         
         String name = "Inter02";
         inter.printToFile(name);         
         inter.compareWithRefPrintLabelledReads(refFile,name + "_align_ref");
         
         System.out.println();
     }
    
}
