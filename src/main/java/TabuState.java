
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class TabuState {
    ArrayList<Integer> part;
    ArrayList<Integer> tabu;
    TabuState()
    {
        part = new ArrayList();
        tabu = new ArrayList();
    }
    TabuState(ArrayList<Vertex> part, HashMap tabu)
    {
        this.part = new ArrayList();
        this.tabu = new ArrayList();
        for (Vertex v : part)
            this.part.add(v.index);
        Iterator ir = tabu.entrySet().iterator();
        while (ir.hasNext())
        {
            Map.Entry me = (Map.Entry) ir.next();
            this.tabu.add(((Vertex) me.getKey()).index);
        }
        Collections.sort(this.part);
        Collections.sort(this.tabu);
    }
    boolean isEqual(TabuState ts)
    {        
            if (this.part.size() != ts.part.size())
                return false;
            if (this.tabu.size() != ts.tabu.size())
                return false;
            for (int i = 0; i < this.part.size(); i++)
                if (this.part.get(i) != ts.part.get(i))
                    return false;
            for (int i = 0; i < this.tabu.size(); i++)
                if (this.tabu.get(i) !=ts.tabu.get(i))
                    return false;
            return true;           
    }
}
