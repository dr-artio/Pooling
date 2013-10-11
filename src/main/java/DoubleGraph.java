/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kki8
 */
import java.util.*;
import java.io.*;

public class DoubleGraph {
    ArrayList<Vertex> vertices;
    HashMap<Vertex,HashSet<Vertex>> adjListsNonrel;
    HashMap<Vertex,HashSet<Vertex>> adjListsNonsep;
    ArrayList<Edge> edgeListNonrel;
    ArrayList<Edge> edgeListNonsep;

    DoubleGraph(String addr) throws Exception
    {
        adjListsNonrel = new HashMap();
        adjListsNonsep = new HashMap();
        this.edgeListNonrel = new ArrayList();
        this.edgeListNonsep = new ArrayList();
        
        FileReader fr = new FileReader(addr);
        BufferedReader br = new BufferedReader(fr);
        
        String s = br.readLine();
        StringTokenizer st = new StringTokenizer(s," ");
        st.nextToken();
        int nVert = Integer.parseInt(st.nextToken());
        vertices = new ArrayList(nVert);        
        
        for (int i = 0; i < nVert; i++)
        {
            br.readLine();
            Vertex  v = new Vertex((i+1));
            adjListsNonrel.put(v,new HashSet());
            adjListsNonsep.put(v,new HashSet());
            vertices.add(v);
            this.adjListsNonrel.put(v, new HashSet());
            this.adjListsNonsep.put(v, new HashSet());
            
            
        }
        br.readLine();
        s = br.readLine();
        while (s != null)
        {
            st = new StringTokenizer(s," ");
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            adjListsNonrel.get(this.vertices.get(u-1)).add(vertices.get(v-1));
            adjListsNonrel.get(this.vertices.get(v-1)).add(vertices.get(u-1));   
            edgeListNonrel.add(new Edge(vertices.get(u-1),vertices.get(v-1)));
            s = br.readLine();
        }
        for (int i = 0; i < nVert; i++)
            for (int j = i+1; j < nVert; j++)
            {
                adjListsNonsep.get(vertices.get(i)).add(vertices.get(j));
                adjListsNonsep.get(vertices.get(j)).add(vertices.get(i));
                edgeListNonsep.add(new Edge(vertices.get(i),vertices.get(j)));
            }
        br.close();
    }
    DoubleGraph(String addr_nonrel, String addr_nonsep) throws Exception
    {
        adjListsNonrel = new HashMap();
        adjListsNonsep = new HashMap();
        this.edgeListNonrel = new ArrayList();
        this.edgeListNonsep = new ArrayList();
        
        FileReader fr = new FileReader(addr_nonrel);
        BufferedReader br = new BufferedReader(fr);
        
        String s = br.readLine();
        StringTokenizer st = new StringTokenizer(s," ");
        st.nextToken();
        int nVert = Integer.parseInt(st.nextToken());
        vertices = new ArrayList(nVert);        
        
        for (int i = 0; i < nVert; i++)
        {
            br.readLine();
            Vertex  v = new Vertex((i+1));
            adjListsNonrel.put(v,new HashSet());
            adjListsNonsep.put(v,new HashSet());
            vertices.add(v);
            this.adjListsNonrel.put(v, new HashSet());
            this.adjListsNonsep.put(v, new HashSet());
            
            
        }
        br.readLine();
        s = br.readLine();
        while (s != null)
        {
            st = new StringTokenizer(s," ");
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            adjListsNonrel.get(this.vertices.get(u-1)).add(vertices.get(v-1));
            adjListsNonrel.get(this.vertices.get(v-1)).add(vertices.get(u-1));   
            edgeListNonrel.add(new Edge(vertices.get(u-1),vertices.get(v-1)));
            s = br.readLine();
        }
        
        br.close();
        fr.close();
        
        fr = new FileReader(addr_nonsep);
        br = new BufferedReader(fr);
        
        s = br.readLine();
        st = new StringTokenizer(s," ");
        st.nextToken();
        for (int i = 0; i < nVert; i++)
            br.readLine();
        br.readLine();
        s = br.readLine();
        while (s != null)
        {
            st = new StringTokenizer(s," ");
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            adjListsNonsep.get(this.vertices.get(u-1)).add(vertices.get(v-1));
            adjListsNonsep.get(this.vertices.get(v-1)).add(vertices.get(u-1));   
            edgeListNonsep.add(new Edge(vertices.get(u-1),vertices.get(v-1)));
            s = br.readLine();
        }

    }
    DoubleGraph(int n, double p)
    {
        int nVert = n;
        vertices = new ArrayList(nVert);
        adjListsNonrel = new HashMap();
        adjListsNonsep = new HashMap();
        edgeListNonrel = new ArrayList();
        edgeListNonsep = new ArrayList();
        
                
        for (int i = 0; i < nVert; i++)
        {
            Vertex v = new Vertex(i+1);
            adjListsNonrel.put(v,new HashSet());
            adjListsNonsep.put(v,new HashSet());  
            vertices.add(v);
            
        }

        for (int i = 0; i < nVert; i++)
            for (int j = i+1; j < nVert; j++)
            {
                    Vertex v = vertices.get(i);
                    Vertex u = vertices.get(j);
                    adjListsNonsep.get(v).add(u);
                    adjListsNonsep.get(u).add(v); 
                    edgeListNonsep.add(new Edge(v,u));

                    double d = Math.random();
                    if (d <= p)
                    {
                        adjListsNonrel.get(v).add(u);
                        adjListsNonrel.get(u).add(v); 
                        edgeListNonrel.add(new Edge(v,u));
                    }
            }
    }
    DoubleGraph(ArrayList vertices1, HashMap<Vertex,HashSet<Vertex>> adjListsNonrel1, HashMap<Vertex,HashSet<Vertex>> adjListsNonsep1,ArrayList<Edge> edgeListNonrel1,ArrayList<Edge> edgeListNonsep1)
    {
        vertices = new ArrayList(vertices1);
        adjListsNonrel = new HashMap(adjListsNonrel1);
        adjListsNonsep = new HashMap(adjListsNonsep1);
        edgeListNonrel = new ArrayList(edgeListNonrel1);
        edgeListNonsep = new ArrayList(edgeListNonsep1);
    }
    DoubleGraph(DoubleGraph g)
    {
        this.vertices = new ArrayList(g.vertices);
        this.adjListsNonrel = new HashMap(g.adjListsNonrel);
        this.adjListsNonsep = new HashMap(g.adjListsNonsep);
        this.edgeListNonrel = new ArrayList(g.edgeListNonrel);
        this.edgeListNonsep = new ArrayList(g.edgeListNonsep);
    }
    void addCrownNonsep()
    {
        ArrayList<Vertex> newvertices = new ArrayList();
        for (Vertex v : vertices)
        {
            Vertex u_v = new Vertex(-v.index);
            newvertices.add(u_v);
            adjListsNonsep.get(v).add(u_v);
            edgeListNonsep.add(new Edge(v,u_v));
            HashSet<Vertex> alnonsep_u_v = new HashSet();
            alnonsep_u_v.add(v);
            adjListsNonsep.put(u_v, alnonsep_u_v);
            HashSet<Vertex> alnonrel_u_v = new HashSet();
            adjListsNonrel.put(u_v, alnonrel_u_v);
        }
        vertices.addAll(newvertices);
    }
    int getNVertices()
    {
        return vertices.size();
    }
    int getNEdgesNonrel()
    {
        return edgeListNonrel.size();
    }
    int getNEdgesNonsep()
    {
        return edgeListNonsep.size();
    }
    void removeEdgesNonsep(ArrayList<Edge> hse)
    {
        for (Edge e : hse)
        {
            adjListsNonsep.get(e.u).remove(e.v);
            adjListsNonsep.get(e.v).remove(e.u);
        }
        edgeListNonsep.removeAll(hse); 
    }
    Vertex findMinDegreeVertexNonrel()
    {
        Vertex minvert = null;
        int mindeg = Integer.MAX_VALUE;
        for (Vertex v : vertices)
            if (adjListsNonrel.get(v).size() < mindeg)
            {
                mindeg = adjListsNonrel.get(v).size();
                minvert = v;
            }
        return minvert;
    }
    DoubleGraph subDoubleGraph(ArrayList<Vertex> subset)
    {
        HashMap<Vertex,HashSet<Vertex>> adjListsNonrelSub = new HashMap();
        HashMap<Vertex,HashSet<Vertex>> adjListsNonsepSub = new HashMap();
        ArrayList<Edge> edgeListNonrelSub = new ArrayList();
        ArrayList<Edge> edgeListNonsepSub = new ArrayList();
        
        HashSet subset_hash = new HashSet();
        for (Vertex v : subset)
            subset_hash.add(v);
        
        for (Vertex v : subset)
        {
            HashSet hsNonrel = new HashSet(adjListsNonrel.get(v));
            hsNonrel.retainAll(subset_hash);
            adjListsNonrelSub.put(v, hsNonrel);
            
            HashSet hsNonsep = new HashSet(adjListsNonsep.get(v));
            hsNonsep.retainAll(subset_hash);
            adjListsNonsepSub.put(v, hsNonsep);
        }
        
        for (Edge e : edgeListNonrel)
            if ((subset.contains(e.u)) && (subset.contains(e.v)))
                edgeListNonrelSub.add(e);
        
         for (Edge e : edgeListNonsep)
            if ((subset.contains(e.u)) && (subset.contains(e.v)))
                edgeListNonsepSub.add(e);
        
        
        return new DoubleGraph(subset,adjListsNonrelSub,adjListsNonsepSub,edgeListNonrelSub,edgeListNonsepSub);
    }
    Cut findMaxCutNonsep()
    {
/*        for (Edge e: edgeListNonsep)
            e.weight = 1.0;
        Collections.sort(edgeListNonsep, new EdgeComparator());*/
        ArrayList<Vertex> part1 = new ArrayList();
        ArrayList<Vertex> part2 = new ArrayList();
        HashSet<Vertex> unsorted = new HashSet(vertices);
        for (Edge e : edgeListNonsep)
        {
            if (part1.contains(e.u) && (unsorted.contains(e.v)))
            {
                part2.add(e.v);
                unsorted.remove(e.v);
                continue;
            }
            if (part1.contains(e.v) && (unsorted.contains(e.u)))
            {
                part2.add(e.u);
                unsorted.remove(e.u);
                continue;
            }
            if (part2.contains(e.u) && (unsorted.contains(e.v)))
            {
                part1.add(e.v);
                unsorted.remove(e.v);
                continue;
            }
            if (part2.contains(e.v) && (unsorted.contains(e.u)))
            {
                part1.add(e.u);
                unsorted.remove(e.u);
                continue;
            }
            if (unsorted.contains(e.u) && unsorted.contains(e.v))
            {
                part1.add(e.u);
                part2.add(e.v);
                unsorted.remove(e.v);
                unsorted.remove(e.u);
            }            
        }
        
        boolean toIter = true;
        while (toIter)
        {
            toIter = false;
            for (Vertex v : part1)
            {
                int degIn = 0;
                int degOut = 0;
                for (Vertex u : adjListsNonsep.get(v))
                {
                    if (part1.contains(u))
                        degIn++;
                    else
                        degOut++;                                
                }
                if (degIn > degOut)
                {
                    part1.remove(v);
                    part2.add(v);
                    toIter = true;
                    break;
                }
            }
            if (toIter)
                continue;
            for (Vertex v : part2)
            {
                int degIn = 0;
                int degOut = 0;
                for (Vertex u : adjListsNonsep.get(v))
                {
                    if (part2.contains(u))
                        degIn++;
                    else
                        degOut++;                                
                }
                if (degIn > degOut)
                {
                    part2.remove(v);
                    part1.add(v);
                    toIter = true;
                    break;
                }
            }
        }
        
        ArrayList<Vertex> part1_ar = new ArrayList(part1.size());
        ArrayList<Vertex> part2_ar = new ArrayList(part2.size());
        
        for (Vertex v : part1)
            part1_ar.add(v);
        for (Vertex v : part2)
            part2_ar.add(v);
        
        Cut c =new Cut(part1_ar, part2_ar);
        
        
        return c;
    }
    Cut findMaxCutMixed()
    {
        HashSet<Vertex> part1 = new HashSet();
        HashSet<Vertex> part2 = new HashSet();
        HashSet<Vertex> unsorted = new HashSet(vertices);
        for (Edge e : edgeListNonsep)
        {
            if (part1.contains(e.u) && (unsorted.contains(e.v)))
            {
                part2.add(e.v);
                unsorted.remove(e.v);
                continue;
            }
            if (part1.contains(e.v) && (unsorted.contains(e.u)))
            {
                part2.add(e.u);
                unsorted.remove(e.u);
                continue;
            }
            if (part2.contains(e.u) && (unsorted.contains(e.v)))
            {
                part1.add(e.v);
                unsorted.remove(e.v);
                continue;
            }
            if (part2.contains(e.v) && (unsorted.contains(e.u)))
            {
                part1.add(e.u);
                unsorted.remove(e.u);
                continue;
            }
            if (unsorted.contains(e.u) && unsorted.contains(e.v))
            {
                part1.add(e.u);
                part2.add(e.v);
                unsorted.remove(e.v);
                unsorted.remove(e.u);
            }            
        }
        
        boolean toIter = true;
        while (toIter)
        {
            toIter = false;
            for (Vertex v : part1)
            {
                int degIn = 0;
                int degOut = 0;
                for (Vertex u : adjListsNonsep.get(v))
                {
                    if (part1.contains(u))
                        degIn++;
                    else
                        degOut++;                                
                }
                if (degIn > degOut)
                {
                    part1.remove(v);
                    part2.add(v);
                    toIter = true;
                    break;
                }
            }
            if (toIter)
                continue;
            for (Vertex v : part2)
            {
                int degIn = 0;
                int degOut = 0;
                for (Vertex u : adjListsNonsep.get(v))
                {
                    if (part2.contains(u))
                        degIn++;
                    else
                        degOut++;                                
                }
                if (degIn > degOut)
                {
                    part2.remove(v);
                    part1.add(v);
                    toIter = true;
                    break;
                }
            }
        }
        
        ArrayList<Vertex> part1_ar = new ArrayList(part1.size());
        ArrayList<Vertex> part2_ar = new ArrayList(part2.size());
        
        for (Vertex v : part1)
            part1_ar.add(v);
        for (Vertex v : part2)
            part2_ar.add(v);
        
        Cut c =new Cut(part1_ar, part2_ar);
        
        
        return c;
    }
    void printToConsole()
    {
        System.out.println("Vertices");
        for (Vertex v : vertices)
            System.out.println(v.index);
        System.out.println("Nonrel edges");
        for (Edge e: edgeListNonrel)
            System.out.println(e.u.index + " " + e.v.index);
        System.out.println("Nonsep edges");
        for (Edge e: edgeListNonsep)
            System.out.println(e.u.index + " " + e.v.index);
        
    }
    void removeVertex(Vertex v)
    {
        vertices.remove(v);
        adjListsNonrel.remove(v);
        adjListsNonsep.remove(v);
        for (Vertex u : vertices)
        {
            adjListsNonrel.get(u).remove(v);
            adjListsNonsep.get(u).remove(v);
        }
        ArrayList<Edge> toRemoveNonrel = new ArrayList();
        for (Edge e : edgeListNonrel)
            if (e.u.equals(v) || e.v.equals(v))
                toRemoveNonrel.add(e);
        edgeListNonrel.removeAll(toRemoveNonrel);
        
        ArrayList<Edge> toRemoveNonsep = new ArrayList();
        for (Edge e : edgeListNonsep)
            if (e.u.equals(v) || e.v.equals(v))
                toRemoveNonsep.add(e);
        edgeListNonsep.removeAll(toRemoveNonsep);
                
    }
    ArrayList<Vertex> findMaxCliqueNonrelHeuristics()
    {
        DoubleGraph g = new DoubleGraph(this);
        while (2*g.getNEdgesNonrel() < g.getNVertices()*(g.getNVertices() - 1))
        {
            Vertex v = g.findMinDegreeVertexNonrel();
            g.removeVertex(v);
        }
        return g.vertices;
    }
    ArrayList<Edge> cutEdgesNonsep(ArrayList<Vertex> subset)
    {
        ArrayList<Edge> cutEdges = new ArrayList();
        for (Edge e : edgeListNonsep)
        {
            if (subset.contains(e.u) && (!subset.contains(e.v)))
            {
                cutEdges.add(e);
                continue;
            }
            if (subset.contains(e.v) && (!subset.contains(e.u)))
                cutEdges.add(e);
        }
        return cutEdges;
    }
    Graph generateMixedGraph()
    {
        ArrayList<Edge> edgeList = new ArrayList();
        HashMap<Vertex,HashSet<Edge>> adjListsEdges = new HashMap();
        for (Vertex v : vertices)
            adjListsEdges.put(v, new HashSet<Edge>());
        
        int M = edgeListNonsep.size();
        for (int i = 0; i < vertices.size(); i++)
            for (int j = i+1; j < vertices.size(); j++)
            {
                Vertex v = vertices.get(i);
                Vertex u = vertices.get(j);
                Edge e = null;
                if ((!adjListsNonrel.get(v).contains(u)) && (adjListsNonsep.get(v).contains(u)))
                    e = new Edge(v,u, 1+0.5*M);
                if ((!adjListsNonrel.get(v).contains(u)) && (!adjListsNonsep.get(v).contains(u)))
                    e = new Edge(v,u, 0.5*M);
                if ((adjListsNonrel.get(v).contains(u)) && (adjListsNonsep.get(v).contains(u)))
                    e = new Edge(v,u, 1);
                
                if (e != null)
                {
                    edgeList.add(e);
                    adjListsEdges.get(v).add(e);
                    adjListsEdges.get(u).add(e);
                }
            }
        return new Graph(vertices,adjListsEdges,edgeList);        
    }
    Graph generateMixedGraph(double lambda)
    {
        ArrayList<Edge> edgeList = new ArrayList();
        HashMap<Vertex,HashSet<Edge>> adjListsEdges = new HashMap();
        for (Vertex v : vertices)
            adjListsEdges.put(v, new HashSet<Edge>());
        
        double M = lambda;
        for (int i = 0; i < vertices.size(); i++)
            for (int j = i+1; j < vertices.size(); j++)
            {
                Vertex v = vertices.get(i);
                Vertex u = vertices.get(j);
                Edge e = null;
                if ((!adjListsNonrel.get(v).contains(u)) && (adjListsNonsep.get(v).contains(u)))
                    e = new Edge(v,u, 1+0.5*M);
                if ((!adjListsNonrel.get(v).contains(u)) && (!adjListsNonsep.get(v).contains(u)) && (M > 0))
                    e = new Edge(v,u, 0.5*M);
                if ((adjListsNonrel.get(v).contains(u)) && (adjListsNonsep.get(v).contains(u)))
                    e = new Edge(v,u, 1);
                
                if (e != null)
                {
                    edgeList.add(e);
                    adjListsEdges.get(v).add(e);
                    adjListsEdges.get(u).add(e);
                }
            }
        return new Graph(vertices,adjListsEdges,edgeList);        
    }
    void printFilePajek(String addr) throws IOException
    {
        FileWriter fw1 = new FileWriter(addr + "_Nonsep.net");
        FileWriter fw2 = new FileWriter(addr + "_Nonrel.net");
        fw1.write("*Vertices " + vertices.size() + "\n");
        fw2.write("*Vertices " + vertices.size() + "\n");
        for (int i = 0; i < this.vertices.size(); i++)
        {
            fw1.write((i+1) + " \"" + vertices.get(i).index + "\"\n");
            fw2.write((i+1) + " \"" + vertices.get(i).index + "\"\n");
        }
        fw1.write("*Edges\n");
        fw2.write("*Edges\n");
        for (Edge e : this.edgeListNonsep)
            fw1.write(e.u.index + " " + e.v.index + " 1\n");
        for (Edge e : this.edgeListNonrel)
            fw2.write(e.u.index + " " + e.v.index + " 1\n");
        fw1.close();
        fw2.close();
                
    }
    boolean isClique(ArrayList<Vertex> vset)
    {
        for (int i = 0; i < vset.size(); i++)
                for (int j = i+1; j < vset.size(); j++)
                {
                    if (!adjListsNonrel.get(vset.get(i)).contains(vset.get(j)))
                        return false;
                }
        return true;
    }
    double getNonrelEPartDiff(ArrayList<Vertex> subset)
    {
        double w1 = 0;
        double w2 = 0;
        for (int i = 0; i < this.vertices.size(); i++)
            for (int j = i+1; j < this.vertices.size(); j++)
            {
                Vertex u = vertices.get(i);
                Vertex v = vertices.get(j);
                if (subset.contains(u) && subset.contains(v) && !adjListsNonrel.get(u).contains(v))
                    w1++;
                if (!subset.contains(u) && !subset.contains(v) && !adjListsNonrel.get(u).contains(v))
                    w2++;
            }
        return 0.5*this.getNEdgesNonsep()*(w2-w1);
                    
    }
}
