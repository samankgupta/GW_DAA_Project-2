import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class KruskalsMST {

    // Defines edge structure
    static class Edge {
        int src, dest, weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    // Defines subset element structure
    static class Subset {
        int parent, rank;

        public Subset(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    // Starting point of program execution
    public static void main(String[] args) {
        // Test cases with unique and increasing values for vertices
        int[] verticesSet = {4, 5, 10, 25, 50, 100, 200};
        int edgesOffset = 5; // Offset to ensure edges > vertices

        for (int V : verticesSet) {
            List<Edge> graphEdges = new ArrayList<>();

            // Create edges for the graph (ensuring unique and increasing weights)
            for (int i = 0; i < V; i++) {
                for (int j = i + 1; j < V; j++) {
                    // Ensure at least V + edgesOffset edges
                    if (graphEdges.size() < V + edgesOffset) {
                        graphEdges.add(new Edge(i, j, (i + j) * 3)); // Example weight
                    }
                }
            }

            // Sort the edges in non-decreasing order
            graphEdges.sort(Comparator.comparingInt(edge -> edge.weight));

            // Measure the start time
            long startTime = System.nanoTime();

            // Run Kruskal's algorithm for this test case
            System.out.println("Test Case with V = " + V + " and E = " + graphEdges.size());
            kruskals(V, graphEdges);

            // Measure the end time
            long endTime = System.nanoTime();

            // Calculate the time taken
            long duration = endTime - startTime;
            System.out.println("Time taken: " + duration + " nanoseconds");
            System.out.println();
        }
    }

    // Function to find the MST
    private static void kruskals(int V, List<Edge> edges) {
        int j = 0;
        int noOfEdges = 0;

        // Allocate memory for creating V subsets
        Subset subsets[] = new Subset[V];

        // Allocate memory for results
        Edge results[] = new Edge[V];

        // Create V subsets with single elements
        for (int i = 0; i < V; i++) {
            subsets[i] = new Subset(i, 0);
        }

        // Number of edges to be taken is equal to V-1
        while (noOfEdges < V - 1 && j < edges.size()) {
            // Pick the smallest edge. And increment the index for next iteration
            Edge nextEdge = edges.get(j);
            int x = findRoot(subsets, nextEdge.src);
            int y = findRoot(subsets, nextEdge.dest);

            // If including this edge doesn't cause cycle, include it in result
            if (x != y) {
                results[noOfEdges] = nextEdge;
                union(subsets, x, y);
                noOfEdges++;
            }

            j++;
        }

        // Print the contents of result[] to display the built MST
        System.out.println("Following are the edges of the constructed MST:");
        int minCost = 0;
        for (int i = 0; i < noOfEdges; i++) {
            System.out.println(results[i].src + " -- " + results[i].dest + " == " + results[i].weight);
            minCost += results[i].weight;
        }
        System.out.println("Total cost of MST: " + minCost);
    }

    // Function to unite two disjoint sets
    private static void union(Subset[] subsets, int x, int y) {
        int rootX = findRoot(subsets, x);
        int rootY = findRoot(subsets, y);

        if (subsets[rootY].rank < subsets[rootX].rank) {
            subsets[rootY].parent = rootX;
        } else if (subsets[rootX].rank < subsets[rootY].rank) {
            subsets[rootX].parent = rootY;
        } else {
            subsets[rootY].parent = rootX;
            subsets[rootX].rank++;
        }
    }

    // Function to find parent of a set
    private static int findRoot(Subset[] subsets, int i) {
        if (subsets[i].parent != i) {
            subsets[i].parent = findRoot(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }
}
