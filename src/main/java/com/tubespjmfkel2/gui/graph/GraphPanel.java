package com.tubespjmfkel2.gui.graph;

import com.mxgraph.swing.mxGraphComponent;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {

    public GraphPanel(GraphManager gm) {
        setLayout(new java.awt.BorderLayout());
        add(new mxGraphComponent(gm.graph), java.awt.BorderLayout.CENTER);
    }

}
