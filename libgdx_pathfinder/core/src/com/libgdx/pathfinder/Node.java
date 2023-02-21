package com.libgdx.pathfinder;

public class Node {

    Vector2int point;
    int cost, heuristic;

    public Node(Vector2int point) {
        this.point = point;
        cost = 0;
        heuristic = 0;
    }

    public int compareHeuristic(Node node2) {
        if (this.heuristic < node2.heuristic) {
            return 1;
        }
        if (this.heuristic == node2.heuristic) {
            return 0;
        }
        return -1;
    }
}

