package com.libgdx.pathfinder;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {

    /* La première liste, appelée liste ouverte, va contenir tous les noeuds étudiés. Dès que l'algorithme va se pencher sur
    un noeud du graphe, il passera dans la liste ouverte (sauf s'il y est déjà).
    */
    private List<Node> open;

    /*  La seconde liste, appelée liste fermée, contiendra tous les noeuds qui, à un moment où à un autre, ont été considérés
    comme faisant partie du chemin solution. Avant de passer dans la liste fermée, un noeud doit d'abord passer dans la
    liste ouverte, en effet, il doit d'abord être étudié avant d'être jugé comme bon.
    */
    private List<Node> close;
    private Maze myMaze;

    public AStar(Maze maze) {
        this.myMaze = maze;

    }

    public List<Vector2int> FindPath(Node pointDepart, Node pointArrivee) {
        open = new ArrayList<>();
        open.add(pointDepart);
        while (!open.isEmpty()) {
            Collections.sort(open, new NodeComparator());
            Node u = open.get(0);
            if (u.point.equals(pointArrivee.point)) {
                return restitutePath(u);
            }
        }


        return null;
    }

    private List<Vector2int> restitutePath(Node u) {
        return null;
    }

    private List<Node> getNeighbours(Node point) {

        Node node1, node2, node3, node4;

        return null;
    }

    private boolean isValidNode(Node node) {

        boolean isXValide = node.point.myX < myMaze.array.length && node.point.myX >= 0;
        boolean isYValide = node.point.myY < myMaze.array.length && node.point.myY >= 0;

        if (!isXValide || !isYValide) {
            return false;
        }

        int val = myMaze.array[node.point.myX][node.point.myY] ;

        return (val == 0);
    }

}

