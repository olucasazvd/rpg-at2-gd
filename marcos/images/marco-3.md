# Marco 3 — Modelagem, referências da algs4 e análise

## 1. Propriedade estrutural central

**Pergunta do problema:** quantas cidades, se caírem, partem a rede em pedaços?

**Conceito de grafos:** cada cidade é um vértice e cada cabo é uma aresta (grafo não direcionado). Uma cidade cuja remoção desconecta o grafo é um **vértice de articulação** (*articulation point* / *cut vertex*). A resposta do problema é a **quantidade de vértices de articulação** do grafo.

### Como reconhecer um vértice de articulação

Testar cidade por cidade (remover e verificar a conectividade) custa O(V · (V + E)). Em vez disso, usa-se uma única **busca em profundidade (DFS)**, o algoritmo de Tarjan. Durante a busca, cada vértice `v` recebe dois números:

- **`pre[v]`** (ou `tin[v]`): ordem em que a DFS chegou em `v` (0º, 1º, 2º, …);
- **`low[v]`**: o menor `pre` alcançável a partir de `v` e dos vértices abaixo dele na árvore da DFS, usando no máximo **uma aresta de retorno** (aresta para um vértice já visitado anteriormente que não seja o pai).

Cálculo de `low[v]`:

- ao entrar em `v`: `low[v] = pre[v]`;
- para cada vizinho `w` ainda não visitado (aresta de árvore): após a chamada recursiva, `low[v] = min(low[v], low[w])`;
- para cada vizinho `w` já visitado que não é o pai (aresta de retorno): `low[v] = min(low[v], pre[w])`.

**Regra de decisão:**

- **Vértice não raiz `v`:** é articulação se existe um filho `c` na árvore da DFS com `low[c] ≥ pre[v]`. Nesse caso, a subárvore de `c` não tem atalho para antes de `v`, então `v` é o único caminho entre ela e o resto da rede.
- **Raiz da DFS:** é articulação se tiver **2 ou mais filhos** na árvore da DFS. Com dois filhos, não existe aresta entre as subárvores (senão a DFS teria visitado uma a partir da outra).

### Como chegar na resposta

Executar a DFS a partir de cada vértice ainda não visitado (para cobrir grafos desconexos), marcar os vértices de articulação em um vetor booleano e **contar quantos foram marcados**.

## 2. Implementações de referência da algs4

A algs4 é a biblioteca do livro *Algorithms* (Sedgewick & Wayne).

| Arquivo | Papel na solução |
|---|---|
| `Graph.java` | Representa o grafo não direcionado com listas de adjacência (`Bag<Integer>[] adj`). |
| `Biconnected.java` | Faz a DFS com `pre`/`low` e expõe `isArticulation(v)`. É o núcleo da solução. |
| `StdIn.java` / `StdOut.java` | Leitura da entrada e escrita da saída. |

**Adaptações necessárias:**

1. **Leitura:** fazer o parse do formato de entrada do UVa (vários casos de teste) em vez do formato padrão `V E` + arestas da algs4.
2. **Índices:** as cidades vêm numeradas de `1..N` e a algs4 usa `0..N-1`, então cada cidade `x` vira o vértice `x - 1`.
3. **Cabos repetidos:** ignorar arestas duplicadas (matriz/conjunto de arestas já vistas). O `Biconnected.java` identifica a volta ao pai comparando o **vértice** (`w != u`), não a aresta. Uma aresta paralela seria tratada como aresta de retorno e poderia alterar `low`.
4. **Contagem:** em vez de imprimir os vértices, contar quantos `v` têm `isArticulation(v) == true`.

## 3. Instância pequena e rastreamento manual

Cabos: `1-2, 1-3, 2-3, 3-4, 4-5`.

```
1 ── 2
 \  /
  3 ── 4 ── 5
```

Intuição: o triângulo 1-2-3 tem caminhos alternativos, mas a "cauda" 3-4-5 não. Remover 3 separa {1, 2} de {4, 5}; remover 4 isola o 5. **Cidades críticas esperadas: 3 e 4.**

### 3.1 Tabela de rastreamento (Biconnected / Tarjan)

Convenções (iguais às do `Biconnected.java`):

- `tin[v]` = `pre[v]`: ordem de descoberta, começando em 0.
- `low[v]`: menor `tin` alcançável a partir da subárvore de `v` usando no máximo uma aresta de retorno.
- Ordem dos vizinhos (a `Bag` da algs4 itera do último inserido para o primeiro), com os cabos lidos na ordem acima:
  `1: [3, 2]` · `2: [3, 1]` · `3: [4, 2, 1]` · `4: [5, 3]` · `5: [4]`
- DFS iniciada na cidade 1 (raiz).

#### Estado final

| Cidade | tin | low | Articulação? | Motivo |
|---|---|---|---|---|
| 1 | 0 | 0 | não | 
| 2 | 4 | 0 | não | 
| 3 | 1 | 0 | **sim** | 
| 4 | 2 | 2 | **sim** | 
| 5 | 3 | 3 | não |

**Resposta: 2 cidades críticas (3 e 4).**

## 4. Complexidade de tempo e memória

Seja `V` o número de cidades e `E` o número de cabos.

**Memória do grafo:** listas de adjacência com cada aresta armazenada duas vezes → **O(V + E)**.

**Memória extra do algoritmo:**

- vetores `pre`, `low` e `articulation`: O(V);
- pilha da recursão da DFS: O(V) no pior caso (grafo em forma de caminho);
- matriz booleana `V × V` para descartar cabos repetidos: O(V²). Alternativa com conjunto de pares (hash): O(E).

**Tempo:**

- leitura e construção do grafo: O(V + E);
- DFS: cada vértice é visitado uma vez e cada aresta é examinada duas vezes (uma em cada extremidade) → **O(V + E)**;
- contagem final: O(V).

**Total: O(V + E) de tempo** por caso de teste, mais O(V²) de inicialização se for usada a matriz de duplicatas.
