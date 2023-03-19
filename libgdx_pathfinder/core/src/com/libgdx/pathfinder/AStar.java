package com.libgdx.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {

    /* La première liste, appelée liste ouverte, va contenir tous les noeuds étudiés. Dès que l'algorithme va se pencher sur
    un noeud du graphe, il passera dans la liste ouverte (sauf s'il y est déjà).
    */
    private List<Node> openList;

    /*  La seconde liste, appelée liste fermée, contiendra tous les noeuds qui, à un moment où à un autre, ont été considérés
    comme faisant partie du chemin solution. Avant de passer dans la liste fermée, un noeud doit d'abord passer dans la
    liste ouverte, en effet, il doit d'abord être étudié avant d'être jugé comme bon.
    */
    private List<Node> closedList;
    private Maze myMaze;

    public AStar(Maze maze) {
        this.myMaze = maze;
    }

    /* PSEUDO CODE
     Fonction cheminPlusCourt(g:Graphe, objectif:Nœud, depart:Nœud)
       closedLists = File()
       openList = FilePrioritaire(comparateur = compareParHeuristique)
       openList.ajouter(depart)

       tant que openList n'est pas vide
           u = openList.defiler()

           si u.x == objectif.x et u.y == objectif.y
               reconstituerChemin(u)
               terminer le programme

           pour chaque voisin v de u dans g
               si non(   v existe dans closedLists
                            ou v existe dans openList avec un coût inférieur)
                    v.cout = u.cout +1
                    v.heuristique = v.cout + distance([v.x, v.y], [objectif.x, objectif.y])
                    openLists.ajouter(v)
           closedLists.ajouter(u)
       terminer le programme (avec erreur)
     */

    public List<Vector2int> FindPath(Node pointDepart, Node pointArrivee) {
        closedList = new ArrayList<>();
        openList = new ArrayList<>();
        openList.add(pointDepart);
        while (!openList.isEmpty()) {
            Collections.sort(openList, new NodeComparator()); // utile ?
            Node currentNode = openList.get(0);
            openList.remove(0);

            if (currentNode.point.equals(pointArrivee.point)) {
                return restitutePath(currentNode);
            }

            for (Node neighbour : getNeighbours(currentNode)) {

                if (neighbour.existsIn(closedList) == null && !neighbour.existsWithInferiorCostIn(openList)) {
                    neighbour.cost = currentNode.cost + 1;
                    neighbour.heuristic = neighbour.cost + neighbour.point.getDistanceManhattan(pointArrivee.point);
                    openList.add(neighbour);
                }
            }

            closedList.add(currentNode);
            System.out.println("OPEN■ " + openList.size() + " CLOSED■ " + closedList.size());
        }

        return null;
    }

    private List<Vector2int> restitutePath(Node node) {
        List<Vector2int> path = new ArrayList<>();
        path.add(node.point);

        while (node.parent != null) {
            node = node.parent;
            path.add(node.point);
        }

        return path;
    }

    private List<Node> getNeighbours(Node point) {
        Node nodeEast = new Node(point, +1, +0);
        Node nodeWest = new Node(point, -1, +0);
        Node nodeSouth = new Node(point, +0, -1);
        Node nodeNorth = new Node(point, +0, +1);

        List<Node> neighbours = new ArrayList<>();

        if (isValidNode(nodeEast)) neighbours.add(nodeEast);
        if (isValidNode(nodeWest)) neighbours.add(nodeWest);
        if (isValidNode(nodeSouth)) neighbours.add(nodeSouth);
        if (isValidNode(nodeNorth)) neighbours.add(nodeNorth);

        return neighbours;
    }

    private boolean isValidNode(Node node) {

        boolean isXValid = node.point.myX < myMaze.array[0].length && node.point.myX >= 0;
        boolean isYValid = node.point.myY < myMaze.array.length && node.point.myY >= 0;

        if (!isXValid || !isYValid) {
            return false;
        }

        int val = myMaze.array[node.point.myY][node.point.myX];
        System.out.println("isValidNode === " + val);
        return (val == Maze.FLOOR);
    }

}

