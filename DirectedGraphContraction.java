package directedgraphcontraction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
/**
 *
 * @author pranali
 */
public class DirectedGraphContraction
{
    private static class Graph
    {
        private final Map<Integer, Vertex> vertices = new TreeMap<Integer, Vertex>(
                new Comparator<Integer>()
                {
                    @Override
                    public int compare(Integer arg0, Integer arg1)
                    {
                        return arg0.compareTo(arg1);
                    }
                });
        private final List<Edge> edges = new ArrayList<Edge>();
 
        public void addVertex(Vertex v)
        {
            vertices.put(v.lbl, v);
        }
 
        public Vertex getVertex(int lbl)
        {
            Vertex v;
            if ((v = vertices.get(lbl)) == null)
            {
                v = new Vertex(lbl);
                addVertex(v);
            }
            return v;
        }
    }
 
    private static class Vertex
    {
        private final int lbl;
        private final Set<Edge> edges = new HashSet<Edge>();
 
        public Vertex(int lbl)
        {
            this.lbl = lbl;
        }
 
        public void addEdge(Edge edge)
        {
            edges.add(edge);
        }
 
        public Edge getEdgeTo(Vertex v2)
        {
            for (Edge edge : edges)
            {
                if (edge.contains(this, v2))
                    return edge;
            }
            return null;
        }
    }
 
    private static class Edge
    {
        private final List<Vertex> ends = new ArrayList<Vertex>();
 
        public Edge(Vertex fst, Vertex snd)
        {
         //    if (fst == null $$ snd == null)
           
            if (fst == null || snd == null)
            {
                throw new IllegalArgumentException("Both vertices are required");
            }
            ends.add(snd);
            ends.add(fst);
        }
 
        public boolean contains(Vertex v1, Vertex v2)
        {
            return ends.contains(v1) && ends.contains(v2);
        }
 
        public Vertex getOppositeVertex(Vertex v)
        {
            if (!ends.contains(v))
            {
                throw new IllegalArgumentException("Vertex " + v.lbl);
            }
            return ends.get(1- ends.indexOf(v));
        }
 
        public void replaceVertex(Vertex oldV, Vertex newV)
        {
            if (!ends.contains(oldV))
            {
                throw new IllegalArgumentException("Vertex " + oldV.lbl);
            }
            ends.remove(oldV);
            ends.add(newV);
           
        }
    }
   
    private static void printvertex(Vertex oldV, Vertex newV){
         
          System.out.println("Merging vertex :"+oldV.lbl+"&"+newV.lbl);
          System.out.println("Adding new mergedvertex as :"+newV.lbl+","+oldV.lbl);
          System.out.println("In graph it be identify with first vertex i.e : "+newV.lbl);
          System.out.println("");
     }
   
 
    private static void graphcontraction(Graph gr)
    {
        Random rnd = new Random();
        int r;
        while (gr.vertices.size() > 6)
        {
             r=rnd.nextInt(gr.edges.size());
            Edge edge = gr.edges.remove(r); 
            Vertex v1 =  removevertex(gr, edge.ends.get(1), edge);
            Vertex v2 =  removevertex(gr, edge.ends.get(0), edge);
            // contract
            Vertex mergedVertex = new Vertex(v1.lbl);
            redirectEdges(gr, v1, mergedVertex);
            redirectEdges(gr, v2, mergedVertex);
             gr.addVertex(mergedVertex);
             printvertex(v2,mergedVertex);
             printGraph(gr);
            System.out.println("");
        }
       
    }
 
    private static Vertex removevertex(Graph gr, Vertex v, Edge e)
    {
        gr.vertices.remove(v.lbl);
        v.edges.remove(e);
        return v;
    }
 
    private static void redirectEdges(Graph gr, Vertex fromV, Vertex toV)
    {
         Edge e = null;
        for (Iterator<Edge> it = fromV.edges.iterator(); it.hasNext();)
        {
            Edge edge = it.next();
            it.remove();
            if (edge.getOppositeVertex(fromV) == toV)
            {
                //toV.edges.remove(edge);
                fromV.edges.remove(edge);
                gr.edges.remove(edge);
                //toV.addEdge(edge);
                
            }
            else
            {
                edge.replaceVertex(fromV, toV);
                toV.addEdge(edge);
            }
        }
    }
 
    public static int[][] getArray()
    {
        Map<Integer, List<Integer>> vertices = new LinkedHashMap<Integer, List<Integer>>();
     
       
        int[][] array = new int[10][];
        for (Map.Entry<Integer, List<Integer>> entry : vertices.entrySet())
        {
            List<Integer> adjList = entry.getValue();
            int[] adj = new int[adjList.size()];
            for (int i = 0; i < adj.length; i++)
            {
                adj[i] = adjList.get(i);
            }
            array[entry.getKey()] = adj;
        }
        return array;
    }
 
   
    private static Graph createGraph(int[][] array)
    {
        Graph gr = new Graph();
        for (int i =0; i <array.length; i++)
        {
            Vertex v = gr.getVertex(i);
            for (int edgeTo : array[i])
            {
                Vertex v2 = gr.getVertex(edgeTo);
                  Edge e;
                 e = new Edge(v2, v);
                 gr.edges.add(e);
                 v.addEdge(e);
               
            }
        }
        return gr;
    }
 
   
    public static void main(String[] args)
    {
        int [][]arr={{1,2},
                     {3},
                     {8,9},
                     {4,6},
                     {2,6},
                     {1},
                     {7,8},
                     {5,8},
                     {9},
                     {4}};
        
        
        
        
        Graph g = createGraph(arr);
        printGraph(g);
        System.out.println("");
        graphcontraction(g);
           
     
    }
    private static void printGraph(Graph gr)
    {
        System.out.println("CONTRACTION on DIRECTED GRAPH :");
        for(Vertex v : gr.vertices.values())
        {
            System.out.print(v.lbl + ":");
            for(Edge edge : v.edges)
            {
                System.out.print(" " + edge.getOppositeVertex(v).lbl);
            }
            System.out.println();
        }
    }
}

