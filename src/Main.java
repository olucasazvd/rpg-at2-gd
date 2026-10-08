import algs4.Biconnected;
import algs4.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder output = new StringBuilder();

        while (true) {
            String line = nextNonEmptyLine(reader);
            if (line == null) {
                break;
            }

            int n = Integer.parseInt(line.trim());
            if (n == 0) {
                break;
            }

            Graph graph = new Graph(n);

            while (true) {
                String connections = nextNonEmptyLine(reader);
                if (connections == null) {
                    break;
                }

                connections = connections.trim();
                if (connections.equals("0")) {
                    break;
                }

                StringTokenizer tokenizer = new StringTokenizer(connections);
                int from = Integer.parseInt(tokenizer.nextToken()) - 1;

                while (tokenizer.hasMoreTokens()) {
                    int to = Integer.parseInt(tokenizer.nextToken()) - 1;
                    graph.addEdge(from, to);
                }
            }

            Biconnected biconnected = new Biconnected(graph);
            int criticalCount = 0;
            for (int vertex = 0; vertex < n; vertex++) {
                if (biconnected.isArticulation(vertex)) {
                    criticalCount++;
                }
            }

            output.append(criticalCount).append('\n');
        }

        System.out.print(output);
    }

    private static String nextNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line;
            }
        }
        return null;
    }
}