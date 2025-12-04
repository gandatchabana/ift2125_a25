/*
 * Ganda Rioult Tchabana (20125185)
 * Jean-Philippe Salis Routhier (20240919)
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

public class Q2 {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class RBNode {
        int key;
        String value;
        boolean color;
        int accessCount;
        long lastAccessTime;
        RBNode left, right, parent;

        public RBNode(int key, String value, long timestamp) {
            this.key = key;
            this.value = value;
            this.color = RED; //nouveau noeud = RED
            this.accessCount = 1; //on commence avec premier accès
            this.lastAccessTime = timestamp;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    private RBNode root;

    public Q2() {
        this.root = null;
    }

    /* ---------- Helpers génériques ---------- */

    //retourne la couleur du noeud
    private boolean colorOf(RBNode node) {
        return (node == null) ? BLACK : node.color;
    }

    //cherche le noeud par un clée donnée
    private RBNode searchNode(int key) {
        RBNode x = root;
        while (x != null) {
            if (key < x.key) {
                x = x.left;
            } else if (key > x.key) {
                x = x.right;
            } else {
                return x;
            }
        }
        return null;
    }

    //fais la rotation à gauche
    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    //fais la rotation à droite
    private void rightRotate(RBNode x) {
        RBNode y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    //assure que les caractéristiques du RB tree sont respectées lors d'une insertion
    //si non, fais les étapes nécessaires pour avoir un arbre qui respecte les caractéristiques RB tree
    private void insertFixup(RBNode z) {
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                RBNode y = z.parent.parent.right;
                if (colorOf(y) == RED) {
                    // cas 1
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        // cas 2
                        z = z.parent;
                        leftRotate(z);
                    }
                    // cas 3
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else { // symétrique
                RBNode y = z.parent.parent.left;
                if (colorOf(y) == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    //retourne le noeud minimum (le plus à gauche)
    private RBNode minimum(RBNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    //remplace le noeud u par le noeud v
    private void transplant(RBNode u, RBNode v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    //assure que les caractéristiques du RB tree sont respectées lors d'une supression
    //si non, fais les étapes nécessaires pour avoir un arbre qui respecte les caractéristiques RB tree
    private void deleteFixup(RBNode x, RBNode parent) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == (parent != null ? parent.left : null)) {
                RBNode w = parent.right;
                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    leftRotate(parent);
                    w = parent.right;
                }
                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (colorOf(w.right) == BLACK) {
                        if (w.left != null) {
                            w.left.color = BLACK;
                        }
                        w.color = RED;
                        rightRotate(w);
                        w = parent.right;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.right != null) {
                        w.right.color = BLACK;
                    }
                    leftRotate(parent);
                    x = root;
                    break;
                }
            } else {
                RBNode w = parent.left;
                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rightRotate(parent);
                    w = parent.left;
                }
                if (colorOf(w.right) == BLACK && colorOf(w.left) == BLACK) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (colorOf(w.left) == BLACK) {
                        if (w.right != null) {
                            w.right.color = BLACK;
                        }
                        w.color = RED;
                        leftRotate(w);
                        w = parent.left;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.left != null) {
                        w.left.color = BLACK;
                    }
                    rightRotate(parent);
                    x = root;
                    break;
                }
            }
        }
        if (x != null) {
            x.color = BLACK;
        }
    }

    /* ---------- Méthodes de l'énoncé ---------- */

    /**
     * Insert un noeud avec les valeurs souhaitées.
     * 
     * @param key la clée du noeud
     * @param value la valeure associée au noeud
     * @param timestamp valeur de lastAccessTime du noeud
     */
    public void insert(int key, String value, long timestamp) {
        RBNode z = new RBNode(key, value, timestamp);
        RBNode y = null;
        RBNode x = root;

        //insertion BST
        while (x != null) {
            y = x;
            if (key < x.key) {
                x = x.left;
            } else if (key > x.key) {
                x = x.right;
            } else {
                //clé déjà présente : on met à jour la valeur / stats
                x.value = value;
                x.accessCount = 1;
                x.lastAccessTime = timestamp;
                return;
            }
        }
        
        z.parent = y;
        if (y == null) {
            root = z;
        } else if (key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }

        z.color = RED;
        insertFixup(z);
    }

    /**
     * Recherche un noeud donnée et retourne sa valeur. Si trouvé, incrémente accessCount et le met à jour.
     * lastAccessTime. Si non trouvé : retourne null.
     * 
     * @param key clée du noeud à chercher
     * @param timestamp timestamp fournis
     * @return la valeur du noeud trouvé
     */
    public String get(int key, long timestamp) {
        RBNode node = searchNode(key);
        if (node == null) return null;
        node.accessCount++;
        node.lastAccessTime = timestamp;
        return node.value;
    }

    /**
     * supprime un noeud en respectant les propriétés RB. Retourne true si le noeud est supprimé.
     * 
     * @param key clé du noeud à supprimer
     * @return la valeur booléenne qui confirme si le noeud à été supprimé
     */
    public boolean delete(int key) {
        RBNode z = searchNode(key);
        if (z == null) return false;

        RBNode y = z;
        boolean yOriginalColor = y.color;
        RBNode x;
        RBNode xParent;

        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            deleteFixup(x, xParent);
        }
        return true;
    }

    /**
     * Retourne toutes les valeurs comprises dans l'intervalle entre minKey et maxKey en ordre croissant.
     * Utilise le help getRangeValues.
     * 
     * @param minKey clée qui représente le début de l'intervalle
     * @param maxKey clée qui représente la fin de l'intervalle
     * @return toutes les valeurs comprises dans l'intervalle
     */
    public List<String> getRangeValues(int minKey, int maxKey) {
        List<String> res = new ArrayList<>();
        getRangeValues(root, minKey, maxKey, res);
        return res;
    }


    private void getRangeValues(RBNode node, int minKey, int maxKey, List<String> out) {
        if (node == null) return;
        if (node.key > minKey) {
            getRangeValues(node.left, minKey, maxKey, out);
        }
        if (node.key >= minKey && node.key <= maxKey) {
            out.add(node.value);
        }
        if (node.key < maxKey) {
            getRangeValues(node.right, minKey, maxKey, out);
        }
    }

    /**
     * Calcule et retourne la hauteur noire de l'arbre (noeuds noirs sur un chemin racine-feuille).
     * Utilise le helper isBST.
     * 
     * @return hauteur noire de l'arbre
     */
    public int getBlackHeight() {
        int h = 0;
        RBNode n = root;
        while (n != null) {
            if (n.color == BLACK) h++;
            n = n.left;
        }
        return h;
    }

    private boolean isBST(RBNode node, long min, long max) {
        if (node == null) return true;
        if (node.key <= min || node.key >= max) return false;
        return isBST(node.left, min, node.key)
            && isBST(node.right, node.key, max);
    }

    //retourne -1 si violation, sinon hauteur noire à partir de ce nœud
    private int checkRB(RBNode node) {
        if (node == null) return 0;

        //pas de rouge-rouge
        if (node.color == RED) {
            if ((node.left != null && node.left.color == RED) ||
                (node.right != null && node.right.color == RED)) {
                return -1;
            }
        }

        int left = checkRB(node.left);
        int right = checkRB(node.right);
        if (left == -1 || right == -1 || left != right) {
            return -1;
        }
        return left + (node.color == BLACK ? 1 : 0);
    }

    /**
     * Vérifie que l'arbre respecte toutes les propriétés RB.
     * Utilise les helpers isBST et checkRB.
     * 
     * @return le booléen qui confirme si les propriétées sont respectées
     */
    public boolean verifyProperties() {
        if (root == null) return true;
        // racine noire
        if (root.color != BLACK) return false;
        // ordre BST
        if (!isBST(root, Long.MIN_VALUE, Long.MAX_VALUE)) return false;
        // même hauteur noire sur tous les chemins & pas de rouge-rouge
        return checkRB(root) != -1;
    }

    private void collectNodes(RBNode node, List<RBNode> list) {
        if (node == null) return;
        collectNodes(node.left, list);
        list.add(node);
        collectNodes(node.right, list);
    }

    /**
     * Cherche les k clés les plus accédées en ordre décroissant.
     * Utilise le helper collectNodes.
     * 
     * @param k nombre de clés à accéder
     * @return les k clés les plus accédées en ordre décroissant
     */
    public List<Integer> getMostAccessedKeys(int k) {
        List<RBNode> nodes = new ArrayList<>();
        collectNodes(root, nodes);

        Collections.sort(nodes, new Comparator<RBNode>() {
            @Override
            public int compare(RBNode a, RBNode b) {
                if (a.accessCount != b.accessCount) {
                    //décroissant sur accessCount
                    return Integer.compare(b.accessCount, a.accessCount);
                }
                //en cas d’égalité, plus petite clé en premier
                return Integer.compare(a.key, b.key);
            }
        });

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nodes.size() && i < k; i++) {
            res.add(nodes.get(i).key);
        }
        return res;
    }

    private void collectOldKeys(RBNode node, long currentTime, long maxAge, List<Integer> keys) {
        if (node == null) return;
        collectOldKeys(node.left, currentTime, maxAge, keys);
        if (currentTime - node.lastAccessTime > maxAge) {
            keys.add(node.key);
        }
        collectOldKeys(node.right, currentTime, maxAge, keys);
    }

    /**
     * Supprime toutes les entéres où currentTime - lastAccessTime > maxAge.
     * Utilise le helper collectOldKeys.
     * 
     * @param currentTime temps actuel entré
     * @param maxAge âge maximal pour la comparaison
     */
    public void evictOldEntries(long currentTime, long maxAge) {
        List<Integer> toDelete = new ArrayList<>();
        collectOldKeys(root, currentTime, maxAge, toDelete);
        for (int k : toDelete) {
            delete(k);
        }
    }

    private int countRed(RBNode node) {
        if (node == null) return 0;
        int c = (node.color == RED) ? 1 : 0;
        return c + countRed(node.left) + countRed(node.right);
    }

    /**
     * Compte le nombre total de noeuds rouges.
     * Utilise le helper countRed.
     * 
     * @return le nombre total de noeuds rouges
     */
    public int countRedNodes() {
        return countRed(root);
    }

    /**
     * Retourne une Map qui donne le nombre de noeuds rouges pour chaque niveau de la forme suivante :
     * Niveau -> nombre de noeuds rouges à ce niveau.
     * 
     * @return map du nombre de noeuds rouges pour chaque niveau
     */
    public Map<String, Integer> getColorStatisticsByLevel() {
        Map<String, Integer> stats = new TreeMap<>();
        if (root == null) return stats;

        //inicie les queues
        Queue<RBNode> q = new LinkedList<>();
        Queue<Integer> lvl = new LinkedList<>();
        q.add(root);
        lvl.add(0);

        while (!q.isEmpty()) {
            RBNode node = q.poll();
            int level = lvl.poll();

            //veut que le niveau apparaisse même si il n'y a pas de noeud rouge
            stats.putIfAbsent(String.valueOf(level), 0);

            //incrémente si le noeud est rouge
            if (node.color == RED) {
                stats.put(String.valueOf(level), stats.get(String.valueOf(level)) + 1);
            }

            if (node.left != null) {
                q.add(node.left);
                lvl.add(level + 1);
            }
            if (node.right != null) {
                q.add(node.right);
                lvl.add(level + 1);
            }
        }
        return stats;
    }
    public static void main(String[] args) {

        Q2 cache = new Q2();
        // Insertions initiales
        cache.insert(10, "data10", 0);
        cache.insert(20, "data20", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);

        System.out.println("get(15, 100) -> " + cache.get(15, 100));

        System.out.println("getRangeValues(12, 27) -> " + cache.getRangeValues(12, 27));

        System.out.println("getBlackHeight() -> " + cache.getBlackHeight());

        System.out.println("verifyProperties() -> " + cache.verifyProperties());

        System.out.println("countRedNodes() -> " + cache.countRedNodes());

        System.out.println("getColorStatisticsByLevel() -> " +
                cache.getColorStatisticsByLevel());

        // préparer accessCount pour coller au PDF
        cache.get(25, 50);  // 25:2
        cache.get(25, 200); // 25:3

        System.out.println("getMostAccessedKeys(3) -> " +
                cache.getMostAccessedKeys(2));

        System.out.println("lastAccess of key 25 before eviction = " +
            cache.get(25, 200));   // ensures lastAccess = 200

        cache.evictOldEntries(300, 150);
        System.out.println("Après eviction : " +
                cache.getRangeValues(Integer.MIN_VALUE, Integer.MAX_VALUE));

        // Recréer l’arbre pour delete(20)
        cache = new Q2();
        cache.insert(10, "data10", 0);
        cache.insert(20, "data20", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);

        System.out.println("delete(20) -> " + cache.delete(20));
    }

}
