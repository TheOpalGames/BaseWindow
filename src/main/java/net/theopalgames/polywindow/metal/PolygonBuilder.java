package net.theopalgames.polywindow.metal;

import net.theopalgames.polywindow.Vector;

public class PolygonBuilder {
    private final MetalWindow window;
    private PolygonType type;

    private Vector firstVertex;
    private Vector lastVertex;

    public PolygonBuilder(MetalWindow window) {
        this.window = window;
    }

    public void addVertex(Vector vertex) {
        if (firstVertex == null) {
            firstVertex = vertex;
            return;
        }

        if (lastVertex == null) {
            lastVertex = vertex;
            return;
        }

        switch (type) {
            case OUTLINE:
                window.drawTriangle(firstVertex.x, firstVertex.y, firstVertex.z, lastVertex.x, lastVertex.y, lastVertex.z, vertex.x, vertex.y, vertex.z);
                break;
            case FILL:
                window.fillTriangle(firstVertex.x, firstVertex.y, firstVertex.z, lastVertex.x, lastVertex.y, lastVertex.z, vertex.x, vertex.y, vertex.z);
                break;
        }
    }

    public void reset() {
        firstVertex = null;
        lastVertex = null;
    }

    public boolean isUnused() {
        return firstVertex == null;
    }

    public void setType(PolygonType type) {
        this.type = type;
    }
}
