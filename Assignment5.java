import java.util.*;

public class Assignment5 {

    static class WeightedGraph {
        private Map<String, List<Edge>> adjList = new HashMap<>();

        static class Edge {
            String destination;
            int weight;

            Edge(String destination, int weight) {
                this.destination = destination;
                this.weight = weight;
            }
        }

        public void addEdge(String source, String destination, int weight) {
            adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge(destination, weight));
            adjList.computeIfAbsent(destination, k -> new ArrayList<>()).add(new Edge(source, weight));
        }

        public List<String> shortestPath(String start, String end) {
            PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
            Map<String, Integer> distances = new HashMap<>();
            Map<String, String> previous = new HashMap<>();

            for (String city : adjList.keySet()) {
                distances.put(city, Integer.MAX_VALUE);
            }
            distances.put(start, 0);
            queue.add(new Node(start, 0));

            while (!queue.isEmpty()) {
                Node currentNode = queue.poll();
                String currentCity = currentNode.city;

                if (currentCity.equals(end)) {
                    break;
                }

                for (Edge edge : adjList.getOrDefault(currentCity, Collections.emptyList())) {
                    int newDist = distances.get(currentCity) + edge.weight;
                    if (newDist < distances.get(edge.destination)) {
                        distances.put(edge.destination, newDist);
                        previous.put(edge.destination, currentCity);
                        queue.add(new Node(edge.destination, newDist));
                    }
                }
            }

            List<String> path = new LinkedList<>();
            for (String at = end; at != null; at = previous.get(at)) {
                path.add(at);
            }
            Collections.reverse(path);

            return path;
        }

        public int getDistance(String start, String end) {
            List<String> path = shortestPath(start, end);
            int distance = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                String current = path.get(i);
                String next = path.get(i + 1);
                for (Edge edge : adjList.get(current)) {
                    if (edge.destination.equals(next)) {
                        distance += edge.weight;
                        break;
                    }
                }
            }
            return distance;
        }

        static class Node {
            String city;
            int distance;

            Node(String city, int distance) {
                this.city = city;
                this.distance = distance;
            }
        }
    }

    public static void main(String[] args) {
        WeightedGraph graph = new WeightedGraph();
        graph.addEdge("Olympia", "Salem", 160);
        graph.addEdge("Salem", "Sacramento", 536);
        graph.addEdge("Sacramento", "Carson City", 130);
        graph.addEdge("Carson City", "Boise", 450);
        graph.addEdge("Boise", "Helena", 488);
        graph.addEdge("Carson City", "Salt Lake City", 546);
        graph.addEdge("Salt Lake City", "Denver", 516);
        graph.addEdge("Denver", "Cheyenne", 102);
        graph.addEdge("Cheyenne", "Lincoln", 486);
        graph.addEdge("Lincoln", "Pierre", 390);
        graph.addEdge("Pierre", "Bismarck", 205);

        List<String> path = graph.shortestPath("Olympia", "Denver");
        int distance = graph.getDistance("Olympia", "Denver");

        System.out.println("Shortest path from Olympia to Denver: " + path);
        System.out.println("Total distance: " + distance + " miles");
    }
}