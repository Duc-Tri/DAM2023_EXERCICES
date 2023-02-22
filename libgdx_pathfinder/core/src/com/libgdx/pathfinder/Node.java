package com.libgdx.pathfinder;

import java.util.List;

public class Node {

    Vector2int point;
    int cost, heuristic;

    Node parent;

    public Node(Vector2int point, Node parent) {
        this.point = point;
        cost = 0;
        heuristic = 0;
        this.parent = parent;
    }

    public Node(int x, int y, Node parent) {
        this(new Vector2int(x, y), parent);
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

    public boolean existIn(List<Node> close) {
        close.contains(this);

        return false;
    }

    public boolean existWithInferiorCost(List<Node> open) {
        for (Node n : open) {
            if (this.equals(n)) {
                return n.cost > this.cost;
            }
        }
        return false;
    }

    public boolean equals(Node other) {
        return (this.point.myX == other.point.myX && this.point.myY == other.point.myY);
    }

}

