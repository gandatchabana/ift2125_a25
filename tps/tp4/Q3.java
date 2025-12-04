/**
 * Jean-Philippe Salis Routhier
 * 20240919
*/

/*
 * ATTESTATION D’INTÉGRITÉ ACADÉMIQUE
 *
 * [ ] Je certifie n’avoir utilisé aucun outil d’IA générative
 *     pour résoudre ce problème.
 *
 * [X] J’ai utilisé un ou plusieurs outils d’IA générative.
 *
 * Outil utilisé :
 * - ChatGPT
 *
 * Raisons de l’utilisation :
 * J’ai utilisé l’IA comme soutien pédagogique afin de mieux comprendre
 * les arbres binaires de recherche, la récursion et la structure du
 * code. L’IA m’a aidé à :
 *  - clarifier le fonctionnement des algorithmes (insert, recherche, etc.)
 *  - expliquer certains concepts de structure de données
 *  - m’aider à identifier et corriger des erreurs
 *
 * Le code remis a été entièrement relu, adapté et réécrit par moi-même.
 * L’IA n’a pas écrit la solution finale de manière autonome. Elle a
 * servi exclusivement comme outil d’apprentissage et de débogage.
*/

import java.util.*;

public class Q3 {
    private static final int DRAW = 0;
    private static final int MOUSE = 1;
    private static final int CAT = 2;

    private static final int MOUSE_TURN = 0;
    private static final int CAT_TURN = 1;

    // memo[mouse][cat][turn][movesLeft] = résultat (0/1/2 ou -1 si inconnu)
    private static int[][][][] memo;
    private static int MAX_MOVES;

    /**
     * Détermine le résultat du jeu avec jeu optimal des deux côtés.
     * Utilise le helper solve.
     * 
     * @param graph graphe qui représente le jeu
     * @return le chiffre qui représente le résultat du jeu : 0 = DRAW, 1 = MOUSE et 2 = CAT
     */
    public static int gameResult(int[][] graph) {
        int n = graph.length;
        MAX_MOVES = 4 * n + 200;

        memo = new int[n][n][2][MAX_MOVES + 1];
        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                for (int t = 0; t < 2; t++) {
                    for (int k = 0; k <= MAX_MOVES; k++) {
                        memo[m][c][t][k] = -1; // UNKNOWN
                    }
                }
            }
        }

        return solve(graph, 1, 2, MOUSE_TURN, MAX_MOVES);
    }

    private static int solve(int[][] graph, int mouse, int cat, int turn, int movesLeft) {
        // 1. Cas limites
        if (movesLeft == 0) return DRAW;
        if (mouse == 0)    return MOUSE;
        if (mouse == cat)  return CAT;

        int cached = memo[mouse][cat][turn][movesLeft];
        if (cached != -1) return cached;

        int result;

        if (turn == MOUSE_TURN) {
            //la souris essaie de gagner
            boolean hasDraw = false;
            result = CAT; //pire cas : elle perd

            for (int nextMouse : graph[mouse]) {
                int child = solve(graph, nextMouse, cat, CAT_TURN, movesLeft - 1);

                if (child == MOUSE) {
                    result = MOUSE; //elle a un coup gagnant
                    break;
                }
                if (child == DRAW) {
                    hasDraw = true;
                }
            }

            if (result != MOUSE) {
                if (hasDraw) result = DRAW;
                else result = CAT;
            }

        } else { //tour du chat
            boolean hasDraw = false;
            result = MOUSE; //pire cas pour le chat : la souris gagne

            for (int nextCat : graph[cat]) {
                if (nextCat == 0) continue; // refuge interdit au chat
                int child = solve(graph, mouse, nextCat, MOUSE_TURN, movesLeft - 1);

                if (child == CAT) {
                    result = CAT; //coup gagnant pour le chat
                    break;
                }
                if (child == DRAW) {
                    hasDraw = true;
                }
            }

            if (result != CAT) {
                if (hasDraw) result = DRAW;
                else result = MOUSE;
            }
        }

        memo[mouse][cat][turn][movesLeft] = result;
        return result;
    }

    /**
     * Si la Souris peut gagner depuis l’état initial, retourne tous les premiers coups qui mènent
     * à une victoire (en ordre décroissant). Sinon, retourne une liste vide.
     * 
     * @param graph graphe qui représente le jeu
     * @return une liste contenant les premiers coups qui mènent à une victoire en ordre décroissant
     */
    public static List<Integer> getMouseWinningMoves(int[][] graph) {
        //retourne liste vide si la souris ne peut pas gagner
        if (gameResult(graph) != MOUSE) {
            return Collections.emptyList();
        }

        List<Integer> voisins = new ArrayList<>();
        for (int v : graph[1]) {
            if (solve(graph, v, 2, CAT_TURN, MAX_MOVES - 1) == MOUSE) {
                voisins.add(v);
            }
        }
        Collections.sort(voisins, Collections.reverseOrder());

        return voisins;
    }

    /**
     * Si le joueur spécifié (1=Souris, 2=Chat) peut forcer une victoire, retourne le nombre
     * minimum de coups nécessaires pour gagner avec jeu optimal. Retourne -1 si impossible.
     * 
     * @param graph graphe qui représente le jeu
     * @param player chiffre qui représente le jouer spécifié (1=Souris, 2=Chat)
     * @return le nombre minimum de coups nécessaires pour gagner avec un jeu optimale (-1 si impossible)
     */
    public static int minMovesToWin(int[][] graph, int player) {
        //vérifier si player peut gagner
        if (gameResult(graph) != player) return -1;

        //BFS queue: état = (mouse, cat, turn, depth)
        Queue<int[]> q = new LinkedList<>();
        boolean[][][] visited = new boolean[graph.length][graph.length][2];

        q.add(new int[]{1, 2, MOUSE_TURN, 0});
        visited[1][2][MOUSE_TURN] = true;

        while (!q.isEmpty()) {
            int[] s = q.remove();
            int m = s[0], c = s[1], turn = s[2], d = s[3];

            //si on a atteint un état gagnant terminal
            if ((player == MOUSE && m == 0) || (player == CAT && m == c))
                return d;

            if (turn == MOUSE_TURN) {
                for (int nm : graph[m]) {
                    //vérifier si ce move est gagnant d’après solve()
                    if (solve(graph, nm, c, CAT_TURN, MAX_MOVES - 1) == player) {
                        if (!visited[nm][c][CAT_TURN]) {
                            visited[nm][c][CAT_TURN] = true;
                            q.add(new int[]{nm, c, CAT_TURN, d + 1});
                        }
                    }
                }
            } else { //CAT_TURN
                for (int nc : graph[c]) {
                    if (nc == 0) continue; //chat ne peut pas aller dans 0
                    if (solve(graph, m, nc, MOUSE_TURN, MAX_MOVES - 1) == player) {
                        if (!visited[m][nc][MOUSE_TURN]) {
                            visited[m][nc][MOUSE_TURN] = true;
                            q.add(new int[]{m, nc, MOUSE_TURN, d + 1});
                        }
                    }
                }
            }
        }

        return -1; //fallback, normalement impossible si gameResult == player
    }

    /**
     * Détermine le résultat du jeu à partir d’une position arbitraire. Les règles restent les mêmes
     * mais les positions de départ changent.
     * 
     * 
     * @param graph graphe qui représente le jeu
     * @param mousePos le chiffre qui représente le nouveau sommet où commence la souris
     * @param catPos le chiffre qui représente le nouveau sommet où commence le chat
     * @param mouseTurn si la souris commence ou non
     * @return le chiffre qui représente le résultat du jeu : 0 = DRAW, 1 = MOUSE et 2 = CAT
     */
    public static int gameResultFrom(int[][] graph, int mousePos, int catPos, boolean mouseTurn) {
        int n = graph.length;
        MAX_MOVES = 4 * n + 200;

        memo = new int[n][n][2][MAX_MOVES + 1];
        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                for (int t = 0; t < 2; t++) {
                    for (int k = 0; k <= MAX_MOVES; k++) {
                        memo[m][c][t][k] = -1; //UNKNOWN
                    }
                }
            }
        }

        int turn = mouseTurn ? MOUSE_TURN : CAT_TURN;
        return solve(graph, mousePos, catPos, turn, MAX_MOVES);
    }

    /**
     * Retourne toutes les positions (m, c, t) qui mènent à un match nul, formatées comme "m-c-t" où t = 0 
     * (tour souris) ou 1 (tour chat), en ordre lexicographique.
     * 
     * @param graph graphe qui représente le jeu
     * @return toutes les positions (m, c, t) qui mènent à un match nul, formatées comme "m-c-t" où t = 0 
     * tour souris) ou 1 (tour chat), en ordre lexicographique.
     */
    public static List<String> getAllDrawPositions(int[][] graph) {
        List<String> res = new ArrayList<>();
        int n = graph.length;

        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                if (c == 0) continue;      //chat ne peut pas être en 0
                if (m == c) continue;      //sinon le chat gagne immédiatement

                for (int turn = 0; turn < 2; turn++) {
                    int outcome = gameResultFrom(graph, m, c, turn == MOUSE_TURN);
                    if (outcome == DRAW) {
                        res.add(m + "-" + c + "-" + turn);
                    }
                }
            }
        }

        Collections.sort(res);
        return res;
    }

    /**
     * Pour chaque position valide (m, c, t) où m ̸= c, c ̸= 0, calcule le résultat optimal.
     * Retourne une Map : "m-c-t" → résultat (0/1/2).
     * 
     * @param graph graphe qui représente le jeu
     * @return Map : "m-c-t" → résultat (0/1/2)
     */
    public static Map<String, Integer> analyzeAllPositions(int[][] graph) {
        Map<String, Integer> map = new HashMap<>();
        int n = graph.length;

        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                if (c == 0) continue;    //chat ne peut pas commencer en 0
                if (m == c) continue;    //état immédiatement gagnant pour le chat

                for (int turn = 0; turn < 2; turn++) {
                    int outcome = gameResultFrom(graph, m, c, turn == MOUSE_TURN);
                    map.put(m + "-" + c + "-" + turn, outcome);
                }
            }
        }

        return map;
    }

    /**
     * Compte le nombre total de positions (m, c, t) où le joueur spécifié a une stratégie gagnante
     * garantie.
     * 
     * @param graph graphe qui représente le jeu
     * @param player chiffre qui représente le jouer spécifié (1=Souris, 2=Chat)
     * @return le nombre total de positions (m, c, t) où le joueur spécifié a une stratégie gagnante
     */
    public static int countWinningPositions(int[][] graph, int player) {
        Map<String, Integer> map = analyzeAllPositions(graph);
        int count = 0;

        for (int result : map.values()) {
            if (result == player) {
                count++;
            }
        }

        return count;
    }

    /**
     * Retourne tous les nœuds (sauf 0) où si la Souris commence son tour à ce nœud et le Chat
     * est à n’importe quelle autre position valide, la Souris peut toujours forcer une victoire.
     * Retourne en ordre décroissant.
     * 
     * @param graph graphe qui représente le jeu
     * @return tous les nœuds (sauf 0) où si la Souris commence son tour à ce nœud et le Chat
     *         est à n’importe quelle autre position valide, la Souris peut toujours forcer une victoire.
     */
    public static List<Integer> findSafeNodes(int[][] graph) {
        Map<String, Integer> map = analyzeAllPositions(graph);
        List<Integer> safeNodes = new ArrayList<>();
        int n = graph.length;

        for (int x = 1; x < n; x++) {   //on ignore 0 (refuge)
            boolean safe = true;

            for (int c = 1; c < n; c++) {
                if (c == x) continue;  //si le chat est déjà sur x, souris perd direct
                //clé dans la map pour (mouse=x, cat=c, turn=0 = souris)
                String key = x + "-" + c + "-0";

                Integer result = map.get(key);
                if (result == null || result != MOUSE) {
                    safe = false;
                    break;
                }
            }

            if (safe) safeNodes.add(x);
        }

        Collections.sort(safeNodes);
        return safeNodes;
    }

    public static void main(String[] args) {

        // =========================
        // ======= EXEMPLE 1 =======
        // =========================
        int[][] graph1 = {
                {2, 5},     // 0
                {3},        // 1
                {0, 4, 5},  // 2
                {1, 4, 5},  // 3
                {2, 3},     // 4
                {0, 2, 3}   // 5
        };

        System.out.println("===== EXEMPLE 1 =====");

        System.out.println("gameResult -> " + gameResult(graph1));

        System.out.println("getMouseWinningMoves -> " 
                + getMouseWinningMoves(graph1));

        System.out.println("minMovesToWin (Souris) -> "
                + minMovesToWin(graph1, MOUSE));

        System.out.println("minMovesToWin (Chat) -> "
                + minMovesToWin(graph1, CAT));

        System.out.println("findSafeNodes -> "
                + findSafeNodes(graph1));

        System.out.println();

        // =========================
        // ======= EXEMPLE 2 =======
        // =========================
        int[][] graph2 = {
                {1, 3},  // 0
                {0},     // 1
                {3},     // 2
                {0, 2}   // 3
        };

        System.out.println("===== EXEMPLE 2 =====");

        System.out.println("gameResult -> " + gameResult(graph2));

        System.out.println("getMouseWinningMoves -> "
                + getMouseWinningMoves(graph2));

        System.out.println("minMovesToWin (Souris) -> "
                + minMovesToWin(graph2, MOUSE));

        System.out.println("gameResultFrom(1,3,true) -> "
                + gameResultFrom(graph2, 1, 3, true));

        System.out.println("gameResultFrom(3,2,false) -> "
                + gameResultFrom(graph2, 3, 2, false));

        System.out.println("findSafeNodes -> "
                + findSafeNodes(graph2));
    }
}

/**
 * a) Le match nul est définis par la répétition infinis des mêmes coups si les deux joueurs jouent optimalement.
 *    
 *    Sans limite sur le nombre de coups, l’algorithme pourrait ne jamais terminer ou serait
 *    obligé de trancher arbitrairement en faveur de la Souris ou du Chat, donnant de mauvais
 *    résultats. On introduit donc une limite sur le nombre de coups restants, qui garantit
 *    la terminaison de l’algorithme et permet de considérer qu’au-delà de ce seuil, la partie
 *    est raisonnablement un match nul.
 * 
 * b) Le tableau mémo est indexé comme suit :
 *    - mouse : n possibilités
 *    - cat : n possibilités
 *    - turn : 2 possibilités
 *    - movesLeft : 4n +200 = O(n)
 * 
 *    Donc #état = n * n * 2 * O(n) = O(n^3)
 * 
 *    À partir d'un état, l'algorithme explore tous les voisins du sommet du joueur courant, ce qui coûte 0(deg(sommet)).
 *    Le degré moyen dans un graphe non orienté est 2m/n.
 * 
 *    On obtient donc la complexité temporelle :
 *    T(n, m) = O(#état * degré moyen)
 *            = O(n^3 * (2m/n)) 
 *            = O(n^2 * m)
 * 
 * c) Le jeu est toujours gagné par la souris, peu importe le reste du graphe, lorsque le refuge 0 est adjacent au sommet de départ de la souris,
 *    c'est à dire lorsqu'il existe une arête {0, 1} dans le graphe.
 * 
 *    Comme la souris joue toujours en premier, elle peut gagner dès le premier coup en se déplaceant du du sommet 1 au 0.
 *    La partie se termine alors immédiatement sans même que le chat puisse jouer. Il est donc impossible pour le chat d'attraper la souris, quelle que
 *    soit la structure du graphe ailleurs.
 */
