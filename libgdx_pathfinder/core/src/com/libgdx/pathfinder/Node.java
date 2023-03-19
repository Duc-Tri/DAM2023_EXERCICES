package com.libgdx.pathfinder;

import java.util.List;

public class Node {

    Vector2int point;
    int cost, heuristic;

    Node parent;

    public Node(Vector2int point) {
        parent = null;
        this.point = point;
        cost = 0;
        heuristic = 0;
    }

    public Node(Node par, int xDiff, int yDiff) {
        this.parent = par;
        this.point = new Vector2int(parent.point.myX + xDiff, parent.point.myY + yDiff);
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

    public Node existsIn(List<Node> list) {
        for (Node n : list) {
            if (n.point.equals(this.point))
                return n;
        }
        return null;
    }

    public boolean existsWithInferiorCostIn(List<Node> list) {
        Node n = existsIn(list);
        if (n != null)
            return n.cost < this.cost;

        return false;
    }
}

