
import errorcorrection.DataSet;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class PoolSimulator {
    ArrayList<DataSet> samples;
    PoolSimulator(ArrayList<DataSet> ar)
    {
        samples = ar;
    }
    ArrayList<Pool> generate()
    {
        PoolsOperator po = new PoolsOperator();
        int n = samples.size();
        ArrayList<ArrayList<Integer>> partitions = po.generatePartitions(n);
        ArrayList<Pool> mixtures = new ArrayList();
        for (ArrayList<Integer> partition : partitions)
        {
//            DataSet dspool = new DataSet();
            DataSet dspool = null;
            for (Integer i : partition)
                dspool.reads.addAll(samples.get(i-1).reads);
            Pool p = new Pool(dspool,partition);
            mixtures.add(p);
        }        
        return mixtures;
    }
}
