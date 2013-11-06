
import errorcorrection.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class PoolsOperator {
    public PoolsOperator()
    {
        
    }
    Pool intersection (Pool p1, Pool p2)
    {
        Pool inter = new Pool();
        ArrayList<Integer> inter_indsamples = new ArrayList(p1.indsamples);
        inter_indsamples.retainAll(p2.indsamples);
        inter.setIndSample(inter_indsamples);
        
        int k = p1.indsamples.size() + p2.indsamples.size() - inter.indsamples.size();
        
        return inter;
    }
    ArrayList<ArrayList<Integer>> generatePartitions(int n)
    {
        if (n == 2)
        {
            ArrayList<ArrayList<Integer>> partitions = new ArrayList();
            ArrayList<Integer> part1 = new ArrayList();
            part1.add(1);
            partitions.add(part1);
            ArrayList<Integer> part2 = new ArrayList();
            part2.add(2);
            partitions.add(part2);
            return partitions;
        }
        ArrayList<ArrayList<Integer>> partitionsPrev = generatePartitions((int) n/2);
        ArrayList<ArrayList<Integer>> partitionsNew = new ArrayList();
        for (ArrayList<Integer> partitionPrev : partitionsPrev)
        {
            ArrayList<Integer> partitionNew = new ArrayList();
            for (Integer i : partitionPrev)
            {
                partitionNew.add(2*i-1);
                partitionNew.add(2*i);
            }
            partitionsNew.add(partitionNew);
        }
        ArrayList<Integer> partitionNew = new ArrayList();
        for (int i = 1; i <= n; i+=2)
            partitionNew.add(i);
        partitionsNew.add(partitionNew);
        return partitionsNew;        
    }
    void renameSequences(String addr, String keyword) throws IOException
    {
        DataSet ds = new DataSet(addr,'c');
        FileWriter fw = new FileWriter(addr+"_renamed.fas");
        int i = 1;
        for (Read r : ds.reads)
        {
            fw.write(">" + i + "_" + keyword + "\n" + r.getNucl() + "\n");
            i++;
        }
        fw.close();            
    }
    double[][] generateDistanceMatrix(Pool p1, Pool p2) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        int n1 = p1.ds.reads.size();
        int n2 = p2.ds.reads.size();
        double[][] distMatr = new double[n1+n2][n1+n2];
        
        for (int i = 0; i < n1; i++)
            for (int j = i+1; j < n1; j++)
            {
                distMatr[i][j] = p1.distMatr[i][j];
                distMatr[j][i] = p1.distMatr[i][j];
            }
        
         for (int i = 0; i < n2; i++)
            for (int j = i+1; j < n2; j++)
            {
                distMatr[n1+i][n1+j] = p2.distMatr[i][j];
                distMatr[n1+j][n1+i] = p2.distMatr[i][j];
            }
        
        for (int i = 0; i < n1; i++)
            for (int j = 0; j < n2; j++)
            {
                System.out.println("Calculate (" + i + "," + j + ") of (" + n1 + "," + n2 + ")");
                distMatr[i][n1+j] = p1.ds.reads.get(i).calcEditDistAlign(p2.ds.reads.get(j),15,6);
//                distMatr[i][n1+j] = p1.ds.reads.get(i).calcEditDistKmer(p2.ds.reads.get(j));
                distMatr[n1+j][i] = distMatr[i][n1+j];           
            }
        
        return distMatr;
    }
    // </editor-fold>
     double[][] generateDistanceMatrixDirect(Pool p1, Pool p2) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        int n1 = p1.ds.reads.size();
        int n2 = p2.ds.reads.size();
        double[][] distMatr = new double[n1+n2][n1+n2];
        
        ArrayList<Read> reads = new ArrayList(n1+n2);
        for (int i = 0; i < n1; i++)
            reads.add(p1.ds.reads.get(i));
        
        for (int i = 0; i < n2; i++)
            reads.add(p2.ds.reads.get(i));
        
        for (int i = 0; i < reads.size(); i++)
            for (int j = i; j < reads.size(); j++)
            {
                System.out.println("Calculate (" + i + "," + j + ") of " + reads.size());
                distMatr[i][j] = reads.get(i).calcEditDistAlign(reads.get(j),15,6);
                distMatr[j][i] = distMatr[i][j];           
            }
        
        return distMatr;
    }
    // </editor-fold>
    void clusteringMatlab(int nClust) throws IOException, InterruptedException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        System.out.println("Clustering");
        Runtime run=Runtime.getRuntime();
        String s = "matlab -nodesktop -nosplash -r pooling(" + nClust + ")";

        Process p = Runtime.getRuntime().exec(s);
        p.waitFor();
        
        File f = new File("Union_clusters.txt");
        boolean b = f.exists();
        
        int i = 0;
        while (!b)
        {
            System.out.println("Waiting for Matlab:" + (i++));
            Thread.sleep(1000);
            b = f.exists();
        }

        if (p.exitValue()!= 0)
        {
            System.out.println("Error in Matlab script");
        }
    }
    // </editor-fold>
    void clusteringKGEM(int nClust) throws IOException, InterruptedException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        System.out.println("Clustering");
        Runtime run=Runtime.getRuntime();
        String s = "java -jar ERIF-1.0.jar -g ref.fas -i union_reads.fas -noHashing";

        Process p = Runtime.getRuntime().exec(s);
        
         BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
         while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
         }
        p.waitFor();
         if (p.exitValue()!= 0)
         {
            System.out.println("Error in ERIF");
         }
        
//        s = "java -jar kgem-0.3.1_new.jar reads.sam_ext.txt -t 0 " +  nClust;
        s = "java -jar kgem-0.4.jar aligned_reads.fas " +  nClust + " -c union_reads.fas" ;
        p = Runtime.getRuntime().exec(s);
        stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = stdInput.readLine()) != null) {
           System.out.println(s);
        }
        p.waitFor();
        if (p.exitValue()!= 0)
        {
           System.out.println("Error in kGEM");
        }

        
        File f = new File("haplotypes.fas");
        f.delete();
//        f = new File("haplotypes_cleaned.fas");
//        f.delete();
        f = new File("reads.fas");
        f.delete();
        f = new File("reads_cleaned.fas");
        f.delete();
        f = new File("reads.sam");
        f.delete();
        f = new File("aligned_reads.fas");
        f.delete();
        //f = new File("ref.fas_ext.fasta");
        //f.delete();
        //f = new File("trash.fastq");
        //f.delete();
    }
}
