/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author kki8
 */
public class TestSet {
    ArrayList<ArrayList<Vertex>> testSet;
    TestSet()
    {
        testSet = new ArrayList();
    }
    void addTest(ArrayList<Vertex> alv)
    {
        testSet.add(alv);
    }
    void printToConsole()
    {
        System.out.println("Test set");
        int i = 1;
        for (ArrayList<Vertex> alv : testSet)
        {
            System.out.println("Test " + i);
            for (Vertex v : alv)
                System.out.print(v.index + " ");
            System.out.println();
            i++;
        }
    }
    int getNTests()
    {
        return testSet.size();
    }
    void printToConsoleCheckFeasibility(DoubleGraph g)
    {
        System.out.println("Test set");
        int nset = 1;
        for (ArrayList<Vertex> alv : testSet)
        {
            System.out.println("Test " + nset);
            for (Vertex v : alv)
                System.out.print(v.index + " ");
            System.out.println();
            nset++;
            for (int i = 0; i < alv.size(); i++)
                for (int j = i+1; j < alv.size(); j++)
                {
                    if (!g.adjListsNonrel.get(alv.get(i)).contains(alv.get(j)))
                        System.out.println("The pair (" + alv.get(i).index + "," + alv.get(j).index + ") is infeasible");
                }
            System.out.println();
            
        }
    }
}
