package com.libgdx.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStarTempMaze {

    /* La première liste, appelée liste ouverte, va contenir tous les noeuds étudiés. Dès que l'algorithme va se pencher sur
    un noeud du graphe, il passera dans la liste ouverte (sauf s'il y est déjà).
    */
    private List<Node> openList;

    /*  La seconde liste, appelée liste fermée, contiendra tous les noeuds qui, à un moment où à un autre, ont été considérés
    comme faisant partie du chemin solution. Avant de passer dans la liste fermée, un noeud doit d'abord passer dans la
    liste ouverte, en effet, il doit d'abord être étudié avant d'être jugé comme bon.
    */
    private List<Node> close;
    private TempMaze myMaze;

    public AStarTempMaze(TempMaze maze) {
        this.myMaze = maze;

    }

    /*Fonction cheminPlusCourt(g:Graphe, objectif:Nœud, depart:Nœud)
    closedLists = File()
    openList = FilePrioritaire(comparateur = compareParHeuristique)
       openList.ajouter(depart)
    tant que openList n'est pas vide
    u = openList.defiler()


    pour chaque voisin v de u dans g
    {
        si non(   v existe dans closedLists    ou    v existe dans openList avec un coût inférieur)
        {
            v.cout = u.cout +1
            v.heuristique = v.cout + distance([v.x, v.y], [objectif.x, objectif.y])
            openLists.ajouter(v)
        }
    }
    closedLists.ajouter(u)

    terminer le programme (avec erreur)
    */

    public List<Vector2int> FindPath(Node pointDepart, Node pointArrivee) {
        close = new ArrayList<>();
        openList = new ArrayList<>();
        openList.add(pointDepart);

        while (!openList.isEmpty()) {
            Collections.sort(openList, new NodeComparator());
            Node u = openList.get(0);
            openList.remove(0);

            /*
            si u.x == objectif.x et u.y == objectif.y
            reconstituerChemin(u)
            terminer le programme
            */
            if (u.point.equals(pointArrivee.point)
                /*|| close.size() > 1000 */) {
                return restitutePath(u);
            }

            for (Node voisin : getNeighbours(u)) {
                if (!(voisin.existIn(close) || voisin.existWithInferiorCost(openList))) {
                    voisin.cost = u.cost + 1;
                    voisin.heuristic = voisin.cost + voisin.point.getDistanceManhattan(pointArrivee.point);

                    openList.add(voisin); // mets un voisin pour le prochain examen
                }
            }

            close.add(u);
        }

        return null;
    }

    private List<Vector2int> restitutePath(Node end) {

        List<Vector2int> path = new ArrayList<>();

        path.add(end.point);

        while (end.parent != null) {
            path.add(end.parent.point);
            end = end.parent;
        }

        return path;
    }

    private List<Node> getNeighbours(Node point) {

        Node nodeGauche, nodeDroite, nodeHaut, nodeBas;
        nodeGauche = makeNeighbour(point, -1, 0);
        nodeDroite = makeNeighbour(point, +1, 0);
        nodeHaut = makeNeighbour(point, 0, +1);
        nodeBas = makeNeighbour(point, 0, -1);

        List<Node> Neighbours = new ArrayList<>();

        if (isValidNode(nodeGauche)) Neighbours.add(nodeGauche);

        if (isValidNode(nodeDroite)) Neighbours.add(nodeDroite);

        if (isValidNode(nodeHaut)) Neighbours.add(nodeHaut);

        if (isValidNode(nodeBas)) Neighbours.add(nodeBas);

        return Neighbours;
    }

    private Node makeNeighbour(Node parent, int xDiff, int yDiff) {
        return new Node(parent.point.myX + xDiff, parent.point.myY + yDiff, parent);
    }

    private boolean isValidNode(Node node) {

        boolean isXValide = node.point.myX < myMaze.array[0].length && node.point.myX >= 0;

        boolean isYValide = node.point.myY < myMaze.array.length && node.point.myY >= 0;

        if (!isXValide || !isYValide) {
            return false;
        }

        int val = myMaze.array[node.point.myY][node.point.myX];

        return (val == 0);
    }
}

