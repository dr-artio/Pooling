import errorcorrection.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class Pool {
    DataSet ds;
    ArrayList<Integer> indsamples;
    ArrayList<DataSet> dsClusters;
    double[][] distMatr;
    public Pool()
    {
        
    }
    public Pool(String addr, ArrayList<Integer> s) throws IOException
    {
        ds = new DataSet(addr);
        indsamples = new ArrayList(s);
    }
     public Pool(String addr) throws IOException
    {
        ds = new DataSet(addr);
        indsamples = new ArrayList();
    }
    public Pool (DataSet ds1, ArrayList<Integer> s)
    {
        ds = ds1;
        indsamples = new ArrayList(s);
    }
    public void printToFile(String addr) throws IOException
    {
        String numsamples = "";
        for (Integer i : indsamples)
            numsamples += ("_" + i);
//        ds.PrintReads(addr + numsamples + ".fas");
        ds.PrintUniqueReads(addr + numsamples + ".fas");
    }
    void setIndSample(ArrayList<Integer> is)
    {
        indsamples = is;
    }
    void setDS(DataSet ds1)
    {
        ds = ds1;
    }
    void generateDistanceMatrix() throws IOException
    {
        int n = ds.reads.size();
        distMatr = new double[n][n];
        
        int k = 25;
        ds.setK(k);
//        ds.calculateKMersAndKCounts();
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
            {
                System.out.println("Calculate (" + i + "," + j + ") of " + n);
                distMatr[i][j] = ds.reads.get(i).calcEditDistAlign(ds.reads.get(j),15,6);
//                distMatr[i][j] = ds.reads.get(i).calcEditDistKmer(ds.reads.get(j));
                distMatr[j][i] = distMatr[i][j];           
            }
    }
    void separateClusters(String clustFile) throws FileNotFoundException, IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        BufferedReader br = new BufferedReader(new FileReader(clustFile));
        int nClusters = indsamples.size();
        dsClusters = new ArrayList();
        for (int i = 0; i < nClusters; i++)
            dsClusters.add(new DataSet());
        for (int i = 0; i < ds.reads.size(); i++)
        {
            int cl = Integer.parseInt(br.readLine())-1;
            dsClusters.get(cl).reads.add(ds.reads.get(i));
        }
    }
    // </editor-fold>
    void printClustersUnReads(String addr) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        for (int i = 0; i < dsClusters.size(); i++)
        {
            dsClusters.get(i).PrintUniqueReads(addr + "_Cluster" + (i+1) + ".fas");
        }
    }
    // </editor-fold>
     void printClustersReads(String addr) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        for (int i = 0; i < dsClusters.size(); i++)
        {
            dsClusters.get(i).PrintReads(addr + "_Cluster" + (i+1) + ".fas");
        }
    }
    // </editor-fold>
    void filter(String refFile, double cutoff) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        DataSet refds = new DataSet(refFile,'c');
        ArrayList badreads = new ArrayList();
//        FileWriter fw = new FileWriter("stat_ref.txt");
        for (int i = 0; i < ds.reads.size(); i++)
        {
            System.out.println("Read " + i + " of " + ds.reads.size());
            double mindist = Double.MAX_VALUE;
            Read minRef = null;
            for (Read r : refds.reads)
            {
                double d = ds.reads.get(i).calcEditDistAlign(r);
                if (d < mindist)
                {
                    mindist = d;
                    minRef = r;
                }
            }
//            fw.write(i + " " + minRef.name + " " + mindist + "\n");
            if (mindist > cutoff)
                badreads.add(ds.reads.get(i));
        }
//        fw.close();
        ds.reads.removeAll(badreads);
        
    }
    void filter(String refFile, double cutoff_align, double cutoff_len) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        DataSet refds = new DataSet(refFile,'c');
        ArrayList badreads = new ArrayList();
//        FileWriter fw = new FileWriter("stat_ref.txt");
        for (int i = 0; i < ds.reads.size(); i++)
        {
            System.out.println("Read " + i + " of " + ds.reads.size());
            
            if (ds.reads.get(i).getLength() > cutoff_len)
            {
                badreads.add(ds.reads.get(i));
                continue;
            }
            
            double mindist = Double.MAX_VALUE;
            Read minRef = null;
            for (Read r : refds.reads)
            {
                double d = ds.reads.get(i).calcEditDistAlign(r);
                if (d < mindist)
                {
                    mindist = d;
                    minRef = r;
                }
            }
//            fw.write(i + " " + minRef.name + " " + mindist + "\n");
            if (mindist > cutoff_align)
                badreads.add(ds.reads.get(i));
        }
//        fw.close();
        ds.reads.removeAll(badreads);
        
    }
    // </editor-fold>
    Pool intersectMatlab(Pool p) throws IOException, InterruptedException
             // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        Pool inter = new Pool();
        ArrayList<Integer> inter_indsamples = new ArrayList(this.indsamples);
        inter_indsamples.retainAll(p.indsamples);
        inter.setIndSample(inter_indsamples);
        
        int k = this.indsamples.size() + p.indsamples.size() - inter.indsamples.size();
        DataSet inter_ds = new DataSet();
        inter.setDS(inter_ds);
        
        PoolsOperator po = new PoolsOperator();
        double[][] matr = po.generateDistanceMatrix(this, p);
        
        FileWriter fw = new FileWriter("Union_distMatr.txt");
        for (int l = 0; l < matr.length; l++)
        {
            for (int j = 0; j < matr.length; j++)
                fw.write(matr[l][j] + " ");
            fw.write("\n");
        }
        fw.close();
        
        int n1 = this.ds.reads.size();
        int n2 = p.ds.reads.size();
        
        po.clusteringMatlab(k);
        int[] clusters = new int[n1+n2];
        String clustFile = "Union_clusters.txt";
        BufferedReader br = new BufferedReader(new FileReader(clustFile));
        for (int i = 0; i < clusters.length; i++)
            clusters[i] = Integer.parseInt(br.readLine())-1;
        br.close();
        
        File fl = new File("Union_clusters.txt");
        fl.delete();
        
        
        DataSet ds = new DataSet();
        for (int i = 0; i < n1; i++)
            ds.reads.add(this.ds.reads.get(i));        
        for (int i = 0; i < n2; i++)
            ds.reads.add(p.ds.reads.get(i));
        for (int i = 0; i < ds.reads.size(); i++)
            ds.reads.get(i).name = "Cluster" + clusters[i];
        
        ds.PrintReads("Union_reads.fas");
        
        
        int[] clustNPools = new int[k];
        
        
        for (int i = 0; i < n1; i++)
            if(clustNPools[clusters[i]] == 0)
                clustNPools[clusters[i]] = 1;
        
        for (int i = n1; i < n1+n2; i++)
        {
            if(clustNPools[clusters[i]] == 1)
                clustNPools[clusters[i]] = 2;
        }
        
        HashMap<String,Integer> seq = new HashMap<String, Integer>();
        ArrayList<Integer> interSeqInd = new ArrayList();
        for (int i = 0; i < n1; i++)
            if (clustNPools[clusters[i]] == 2)
            {
                interSeqInd.add(i);
                Read r = this.ds.reads.get(i);
                if (seq.containsKey(r.getNucl()))
                    seq.put(r.getNucl(), seq.get(r.getNucl())+r.getFreq());
		else
                    seq.put(r.getNucl(), r.getFreq());	
            }
        for (int i = 0; i < n2; i++)
            if (clustNPools[clusters[n1+i]] == 2)
            {
                interSeqInd.add(n1+i);
                Read r = p.ds.reads.get(i);
                if (seq.containsKey(r.getNucl()))
                    seq.put(r.getNucl(), seq.get(r.getNucl())+r.getFreq());
		else
                    seq.put(r.getNucl(), r.getFreq());	
            }
        
        Iterator it = seq.entrySet().iterator();
	int count = 1;
	while (it.hasNext())
	{
            Map.Entry me = (Map.Entry)it.next();
            String nucleo = (String) me.getKey(); 
            int f = (Integer) me.getValue();
            String nm = "read" + count;
            inter.ds.reads.add(new Read(nucleo,nm,f));
            count++;
	}
        
        
        return inter;
    }
    // </editor-fold>
    Pool intersectKGEM(Pool p) throws IOException, InterruptedException
             // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        Pool inter = new Pool();
        ArrayList<Integer> inter_indsamples = new ArrayList(this.indsamples);
        inter_indsamples.retainAll(p.indsamples);
        inter.setIndSample(inter_indsamples);
        
        int k = this.indsamples.size() + p.indsamples.size() - inter.indsamples.size();
        DataSet inter_ds = new DataSet();
        inter.setDS(inter_ds);
        
        PoolsOperator po = new PoolsOperator();        
        FileWriter fw = new FileWriter("union_reads.fas");
        int id = 1;
        for (Read r : this.ds.reads)
            for (int i = 0; i < r.getFreq(); i++)
                fw.write(">" + r.name + "_" + id++ + "\n" + r.getNucl() + "\n");
        for (Read r : p.ds.reads)
            for (int i = 0; i < r.getFreq(); i++)
                fw.write(">" + r.name + "_" + id++ + "\n" + r.getNucl() + "\n");
        fw.close();
        
        
        po.clusteringKGEM(k);
        DataSet union_ds = new DataSet("reads_clustered.fas",'c');
        
        int[] clusters = new int[union_ds.reads.size()];
        for (int i = 0; i < union_ds.reads.size(); i++)
        {
            double[] haploProb = new double[k];
            StringTokenizer st = new StringTokenizer(union_ds.reads.get(i).name,"_");
//            System.out.println(union_ds.reads.get(i).name);
            String s1 = st.nextToken();
//            for (int j = 0; j < k; j++)
//            {
//                s1=st.nextToken();
//                haploProb[j] = Double.parseDouble(st.nextToken());
//            }
            int bestHaplo = Integer.parseInt(s1.substring(1));
//            for (int j = 1; j < k; j++)
//                if (haploProb[j] > haploProb[bestHaplo])
//                    bestHaplo = j;
            clusters[i] = bestHaplo;
        }
        
        File fl = new File("reads_clustered.fas");
        //fl.delete();
        fl = new File("union_reads.fas");
        //fl.delete();
        
        for (int i = 0; i < union_ds.reads.size(); i++)
            union_ds.reads.get(i).name = "Cluster" + clusters[i];
        
        union_ds.PrintUniqueReads("Union_reads_u.fas");
        
        
        ArrayList<HashSet<Integer>> clustPools = new ArrayList(k);
        for (int i =0; i < k; i++)
            clustPools.add(i, new HashSet());
        
        for (int i = 0; i < union_ds.reads.size(); i++)
        {
            boolean b1 = this.ds.containRead(union_ds.reads.get(i));
            boolean b2 = p.ds.containRead(union_ds.reads.get(i));
            if (b1)
                clustPools.get(clusters[i]).add(1);
            if (b2)
                clustPools.get(clusters[i]).add(2);                
        }
        
        
        HashMap<String,Integer> seq = new HashMap<String, Integer>();
        ArrayList<Integer> interSeqInd = new ArrayList();
        for (int i = 0; i < union_ds.reads.size(); i++)
            if (clustPools.get(clusters[i]).size() == 2)
            {
                interSeqInd.add(i);
                Read r = union_ds.reads.get(i);
                if (seq.containsKey(r.getNucl()))
                    seq.put(r.getNucl(), seq.get(r.getNucl())+r.getFreq());
		else
                    seq.put(r.getNucl(), r.getFreq());	
            }
        
        Iterator it = seq.entrySet().iterator();
	int count = 1;
	while (it.hasNext())
	{
            Map.Entry me = (Map.Entry)it.next();
            String nucleo = (String) me.getKey(); 
            int f = (Integer) me.getValue();
            String nm = "read" + count;
            inter.ds.reads.add(new Read(nucleo,nm,f));
            count++;
	}
        
        
        return inter;
    }
    // </editor-fold>
    void printDistanceMatrix(String addr) throws IOException
    {
             FileWriter fw = new FileWriter(addr);
             for (int l = 0; l < distMatr.length; l++)
             {
                 for (int j = 0; j < distMatr.length; j++)
                     fw.write(distMatr[l][j] + " ");
                 fw.write("\n");
             }
             fw.close(); 
    }
    void readDistMatr(String addr) throws FileNotFoundException, IOException
    {
        int n = 0;
        BufferedReader br = new BufferedReader(new FileReader(addr));
        String s = "";
        s = br.readLine();
        while (s!= null)
        {
            n++;
            s = br.readLine();
        }
        br.close();
        
        distMatr = new double[n][n];
        
        br = new BufferedReader(new FileReader(addr));
        s = "";
        int i = 0;
        s = br.readLine();
        while (s!= null)
        {
            System.out.println("Reading line " + (i+1) + " of " + n);
            StringTokenizer st = new StringTokenizer(s," \n");
            int j = 0;
            while (st.hasMoreTokens())
            {
                distMatr[i][j] = Double.parseDouble(st.nextToken());
                j++;
            }
            i++;
            s = br.readLine();
        }
        br.close();
     }
     void compareWithRef(String refFile, String nameTag) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        DataSet refds = new DataSet(refFile,'c');        
        FileWriter fw = new FileWriter(nameTag + "_stat_ref.txt");
        for (int i = 0; i < ds.reads.size(); i++)
        {
            System.out.println("Read " + i + " of " + ds.reads.size());
            double mindist = Double.MAX_VALUE;
            Read minRef = null;
            for (Read r : refds.reads)
            {
                double d = ds.reads.get(i).calcEditDistAlign(r,15,6);
//                double d = ds.reads.get(i).calcEditDistKmer(r);
                if (d < mindist)
                {
                    mindist = d;
                    minRef = r;
                }
            }
            if (mindist < 0)
                System.out.println();
            fw.write(i + " " + ds.reads.get(i).getLength() + " " + minRef.name + " " + mindist + "\n");
        }
        fw.close();
        
    }
    // </editor-fold>
     void compareWithRefPrintLabelledReads(String refFile, String nameTag) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        DataSet refds = new DataSet(refFile,'c');        
        FileWriter fw = new FileWriter(nameTag + "_stat_ref.txt");
        FileWriter fw_seq = new FileWriter(nameTag + "_label.fas");
        for (int i = 0; i < ds.reads.size(); i++)
        {
            System.out.println("Read " + i + " of " + ds.reads.size());
            double mindist = Double.MAX_VALUE;
            Read minRef = null;
            for (Read r : refds.reads)
            {
                double d = ds.reads.get(i).calcEditDistAlign(r, 15, 6);
//                double d = ds.reads.get(i).calcEditDistAbsAlign(r);
//                double d = ds.reads.get(i).calcEditDistKmer(r);
                if (d < mindist)
                {
                    mindist = d;
                    minRef = r;
                }
            }
            fw.write(i + " " + minRef.name + " " + mindist + "\n");
            fw_seq.write(">" + minRef.name + "\n" + ds.reads.get(i).getNucl() + "\n");
        }
        fw.close();
        fw_seq.close();
        
    }
    // </editor-fold>
      void compareWithRefKmers(String refFile, String nameTag) throws IOException
            // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    {
        DataSet refds = new DataSet(refFile,'c');        
        FileWriter fw = new FileWriter(nameTag + "_stat_ref.txt");
        int k = 25;
        ds.setK(k);
        ds.calculateKMersAndKCounts();
        refds.setK(k);
        refds.calculateKMersAndKCounts();
        for (int i = 0; i < ds.reads.size(); i++)
        {
            System.out.println("Read " + i + " of " + ds.reads.size());
            double mindist = Double.MAX_VALUE;
            Read minRef = null;
            for (Read r : refds.reads)
            {
                double d = ds.reads.get(i).calcEditDistKmer(r);
                if (d < mindist)
                {
                    mindist = d;
                    minRef = r;
                }
            }
            fw.write(i + " " + minRef.name + " " + mindist + "\n");
        }
        fw.close();
        
    }
    // </editor-fold>
      Pool minus(Pool p)
      {
            Pool minus = new Pool();
            ArrayList<Integer> minus_indsamples = new ArrayList(this.indsamples);
            minus_indsamples.removeAll(p.indsamples);
            minus.setIndSample(minus_indsamples);

            DataSet minus_ds = new DataSet();
            
            minus_ds.reads = new ArrayList();
            for (Read r : this.ds.reads)
                if (!p.ds.containRead(r))
                    minus_ds.reads.add(r);
            
            
            minus.setDS(minus_ds);
            return minus;
      }

}
