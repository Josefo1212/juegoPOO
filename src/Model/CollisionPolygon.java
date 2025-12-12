package Model;

import java.util.Arrays;

public class CollisionPolygon implements CollisionShape {
    private final Vector2[] vertices;
    private final Vector2 center;
    private final double boundingRadius;

    public CollisionPolygon(Vector2[] vertices) {
        this(vertices, computeCenter(vertices));
    }

    public CollisionPolygon(Vector2[] vertices, Vector2 center) {
        this.vertices = vertices;
        this.center = center;
        this.boundingRadius = Arrays.stream(vertices)
            .map(v -> v.subtract(center))
            .mapToDouble(Vector2::length)
            .max()
            .orElse(0.0);
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public Vector2 getCenter() {
        return center;
    }

    @Override
    public boolean intersects(CollisionShape other) {
        if (other instanceof CollisionPolygon polygon) {
            return polygonPolygonIntersect(this, polygon);
        }
        if (other instanceof CollisionCircle circle) {
            return polygonCircleIntersect(this, circle);
        }
        return false;
    }

    @Override
    public double getBoundingRadius() {
        return boundingRadius;
    }

    private static boolean polygonPolygonIntersect(CollisionPolygon a, CollisionPolygon b) {
        if (!boundingRadiusCheck(a, b)) {
            return false;
        }
        return satCheck(a.vertices, b.vertices) && satCheck(b.vertices, a.vertices);
    }

    private static boolean polygonCircleIntersect(CollisionPolygon polygon, CollisionCircle circle) {
        if (!boundingRadiusCheck(polygon, circle)) {
            return false;
        }
        Vector2[] verts = polygon.vertices;
        for (int i = 0; i < verts.length; i++) {
            Vector2 current = verts[i];
            Vector2 next = verts[(i + 1) % verts.length];
            Vector2 edge = next.subtract(current);
            Vector2 normal = new Vector2(-edge.y, edge.x).normalize();
            double projCircle = circle.getCenter().dot(normal);
            double maxA = Double.NEGATIVE_INFINITY;
            double minA = Double.POSITIVE_INFINITY;
            for (Vector2 vertex : verts) {
                double projection = vertex.dot(normal);
                maxA = Math.max(maxA, projection);
                minA = Math.min(minA, projection);
            }
            if (projCircle + circle.getRadius() < minA || projCircle - circle.getRadius() > maxA) {
                return false;
            }
        }
        double distToCenter = circle.getCenter().subtract(polygon.center).length();
        return distToCenter <= circle.getRadius() + polygon.boundingRadius;
    }

    private static boolean satCheck(Vector2[] verticesA, Vector2[] verticesB) {
        for (int i = 0; i < verticesA.length; i++) {
            Vector2 current = verticesA[i];
            Vector2 next = verticesA[(i + 1) % verticesA.length];
            Vector2 edge = next.subtract(current);
            Vector2 axis = new Vector2(-edge.y, edge.x).normalize();

            double[] projectionA = project(axis, verticesA);
            double[] projectionB = project(axis, verticesB);

            if (projectionA[1] < projectionB[0] || projectionB[1] < projectionA[0]) {
                return false;
            }
        }
        return true;
    }

    private static double[] project(Vector2 axis, Vector2[] vertices) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (Vector2 vertex : vertices) {
            double projection = vertex.dot(axis);
            min = Math.min(min, projection);
            max = Math.max(max, projection);
        }
        return new double[]{min, max};
    }

    private static Vector2 computeCenter(Vector2[] vertices) {
        double avgX = 0;
        double avgY = 0;
        for (Vector2 vertex : vertices) {
            avgX += vertex.x;
            avgY += vertex.y;
        }
        return new Vector2(avgX / vertices.length, avgY / vertices.length);
    }

    private static boolean boundingRadiusCheck(CollisionShape a, CollisionShape b) {
        Vector2 centerA;
        Vector2 centerB;
        if (a instanceof CollisionPolygon polyA) {
            centerA = polyA.center;
        } else if (a instanceof CollisionCircle circleA) {
            centerA = circleA.getCenter();
        } else {
            centerA = new Vector2(0, 0);
        }
        if (b instanceof CollisionPolygon polyB) {
            centerB = polyB.center;
        } else if (b instanceof CollisionCircle circleB) {
            centerB = circleB.getCenter();
        } else {
            centerB = new Vector2(0, 0);
        }
        double dx = centerA.x - centerB.x;
        double dy = centerA.y - centerB.y;
        double dist2 = dx * dx + dy * dy;
        double sum = a.getBoundingRadius() + b.getBoundingRadius();
        return dist2 <= sum * sum;
    }
}

