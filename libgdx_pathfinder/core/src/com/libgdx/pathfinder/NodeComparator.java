package com.libgdx.pathfinder;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        if (node1.heuristic < node2.heuristic) {
            return -1;
        }
        if (node1.heuristic == node2.heuristic) {
            return 0;
        }
        return 1;
    }
}
