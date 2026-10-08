# Marco 4 - Implementação final e conclusão

Pontos de articulação com DFS (algs4)

---

## 1. Como resolvemos

- Lugares viram vértices e linhas viram arestas
- Lugar crítico = ponto de articulação do grafo
- Usamos a classe `Biconnected` da algs4 (DFS com pre/low)
- Custo: O(V + E) por caso de teste

---

## 2. Lendo a entrada e montando o grafo

```java
int n = Integer.parseInt(line.trim());
if (n == 0) break;

Graph graph = new Graph(n);
// cada linha: origem seguida dos vizinhos, até o "0"
int from = Integer.parseInt(tokenizer.nextToken()) - 1;
while (tokenizer.hasMoreTokens()) {
    int to = Integer.parseInt(tokenizer.nextToken()) - 1;
    graph.addEdge(from, to);
}
```

O `- 1` converte os lugares de 1..N para os índices 0..N-1 do `Graph`.

---

## 3. Contando os lugares críticos

```java
Biconnected biconnected = new Biconnected(graph);
int criticalCount = 0;
for (int vertex = 0; vertex < n; vertex++) {
    if (biconnected.isArticulation(vertex)) {
        criticalCount++;
    }
}
output.append(criticalCount).append('\n');
```

A `Biconnected` faz a DFS; a gente só conta e imprime tudo no final.

---

## 4. Exemplo

Entrada:

```
6
2 1 3
5 4 6 2
0
```

Grafo (críticos entre colchetes):

```
    1               4
     \             /
      [2] ----- [5]
     /             \
    3               6
```

- Sem o lugar 2, os lugares 1 e 3 ficam isolados
- Sem o lugar 5, os lugares 4 e 6 ficam isolados
- Saída: **2**
