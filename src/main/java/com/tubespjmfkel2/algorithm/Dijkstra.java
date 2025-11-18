package com.tubespjmfkel2.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {

    static class Node {
        String name;
        int dist;

        Node(String n, int d) {
            name = n;
            dist = d;
        }
    }

    public static class Result {
        public List<String> path;
        public int distance;

        public Result(List<String> p, int d) {
            this.path = p;
            this.distance = d;
        }
    }

    public static Result runWithDistance(Map<String, Map<String, Integer>> adj, String start, String end) {

        if (!adj.containsKey(start) || !adj.containsKey(end))
            return null;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String v : adj.keySet())
            dist.put(v, Integer.MAX_VALUE);

        dist.put(start, 0);
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (cur.name.equals(end))
                break;

            for (String nb : adj.get(cur.name).keySet()) {
                int w = adj.get(cur.name).get(nb);
                int nd = cur.dist + w;

                if (nd < dist.get(nb)) {
                    dist.put(nb, nd);
                    prev.put(nb, cur.name);
                    pq.add(new Node(nb, nd));
                }
            }
        }

        if (!prev.containsKey(end))
            return null;

        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = prev.get(at))
            path.add(at);
        Collections.reverse(path);

        return new Result(path, dist.get(end));
    }

}
