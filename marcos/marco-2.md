# Marco 2: Análise Estrutural de Redes (Grafos Desconexos e Conexos)

Este documento analisa duas topologias de grafos não direcionados com $V=6$, contrastando um cenário de rede fragmentada com um cenário de rede unificada vulnerável a pontos únicos de falha.

---

## 1. Grafo A: Topologia Desconexa (Duas Componentes)

Neste modelo, a rede está nativamente dividida em duas ilhas de comunicação.
*   **Vértices ($V$):** `{1, 2, 3, 4, 5, 6}`
*   **Arestas ($E$):** `{(1,2), (2,3), (3,4), (4,1), (1,3), (5,6)}`

### Listas de Adjacência (Grafo A)
*   **1:** `[2, 3, 4]` | **2:** `[1, 3]` | **3:** `[1, 2, 4]` | **4:** `[1, 3]`
*   **5:** `[6]` | **6:** `[5]`

### Métricas por Componente Conexa
**Componente 1: Vértices {1, 2, 3, 4}**
*   **Excentricidades ($\epsilon$):** $\epsilon(1)=1$, $\epsilon(2)=2$, $\epsilon(3)=1$, $\epsilon(4)=2$
*   **Raio ($r$):** $\min(\epsilon) = 1$
*   **Diâmetro ($d$):** $\max(\epsilon) = 2$
*   **Centro:** Conjunto **{1, 3}**

**Componente 2: Vértices {5, 6}**
*   **Excentricidades ($\epsilon$):** $\epsilon(5)=1$, $\epsilon(6)=1$
*   **Raio ($r$) e Diâmetro ($d$):** $1$
*   **Centro:** Conjunto **{5, 6}**

---

## 2. Grafo B: Topologia Conexa (Cenário TLC)

Neste modelo, adicionamos a aresta `(3,6)`, fundindo a rede numa única componente conexa. No entanto, surgem gargalos estruturais (Pontos de Articulação).
*   **Vértices ($V$):** `{1, 2, 3, 4, 5, 6}`
*   **Arestas ($E$):** `{(1,2), (2,3), (3,4), (4,1), (1,3), (5,6), (3,6)}`

### Listas de Adjacência (Grafo B)
*   **1:** `[2, 3, 4]`
*   **2:** `[1, 3]`
*   **3:** `[1, 2, 4, 6]` *(Nova conexão adicionada)*
*   **4:** `[1, 3]`
*   **5:** `[6]`
*   **6:** `[5, 3]` *(Nova conexão adicionada)*

### Métricas da Única Componente Conexa
Calculando a maior distância (caminho mínimo) de cada nó para todos os outros da rede:
*   $\epsilon(1) = 3$ (distância até o nó 5)
*   $\epsilon(2) = 3$ (distância até o nó 5)
*   $\epsilon(3) = 2$ (distância até o nó 5)
*   $\epsilon(4) = 3$ (distância até o nó 5)
*   $\epsilon(5) = 3$ (distâncias até 1, 2 e 4)
*   $\epsilon(6) = 2$ (distâncias até 1, 2 e 4)

*   **Raio ($r$):** $\min(\epsilon) = 2$
*   **Diâmetro ($d$):** $\max(\epsilon) = 3$
*   **Centro:** Conjunto **{3, 6}**
*   **Lugares Críticos (Pontos de Articulação):** **{3, 6}**. A remoção do 3 isola a ramificação `{5,6}`. A remoção do 6 isola o nó `{5}`.

---

## 3. Rastreamento do Algoritmo de Componentes Conexas (DFS) no Grafo B

O algoritmo utiliza `visited` (inicializado como `False`), `cc_id` (ID da componente) e um contador `count = 0`. O loop principal varre de 1 a 6.

*   **v = 1:** `visited[1]` é `False`. 
    *   `count` = 1.
    *   `DFS(1)`: `visited[1]=True`, `cc_id[1]=1`.
        *   Vizinho 2 -> `visited[2]=False`. `DFS(2)`: `visited[2]=True`, `cc_id[2]=1`.
            *   Vizinho 3 -> `visited[3]=False`. `DFS(3)`: `visited[3]=True`, `cc_id[3]=1`.
                *   Vizinho 4 -> `visited[4]=False`. `DFS(4)`: `visited[4]=True`, `cc_id[4]=1`. (Fim DFS 4).
                *   Vizinho 6 -> `visited[6]=False`. `DFS(6)`: `visited[6]=True`, `cc_id[6]=1`.
                    *   Vizinho 5 -> `visited[5]=False`. `DFS(5)`: `visited[5]=True`, `cc_id[5]=1`. (Fim DFS 5).
                    *   (Fim DFS 6).
                *   (Fim DFS 3).
            *   (Fim DFS 2).
        *   (Fim DFS 1).

*   **v = 2 a 6:** No loop principal, `visited` de todos estes já é `True`. A chamada DFS não é disparada novamente.

**Estado Final:**
*   `visited`: `[True, True, True, True, True, True]`
*   `cc_id`: `[1, 1, 1, 1, 1, 1]`
*   Total de Componentes: `1`

---

## 4. Complexidade e Consultas

### Tempo e Espaço
*   **Tempo:** $O(V + E)$. O loop principal testa todos os vértices ($V$). A recursão DFS percorre cada lista de adjacência, avaliando as arestas no total $2E$ vezes (pois o grafo é não direcionado). A topologia exata (Conexa ou Desconexa) não altera o tempo assintótico limite.
*   **Espaço:** $O(V + E)$. A memória é dominada pelo armazenamento das listas de adjacência. Os arrays auxiliares (`visited`, `cc_id`) e a Call Stack da DFS consomem $O(V)$.

### Consultas de Conectividade
Após este pré-processamento, responder se o nó $X$ alcança o nó $Y$ tem custo de tempo **$O(1)$**. Basta validar a identidade: `return cc_id[X] == cc_id[Y]`.