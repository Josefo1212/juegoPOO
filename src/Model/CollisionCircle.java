package Model;

public class CollisionCircle implements CollisionShape {
    private final Vector2 center;
    private final double radius;

    public CollisionCircle(Vector2 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Vector2 getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean intersects(CollisionShape other) {
        if (other instanceof CollisionCircle circle) {
            double dx = center.x - circle.center.x;
            double dy = center.y - circle.center.y;
            double dist2 = dx * dx + dy * dy;
            double sum = radius + circle.radius;
            return dist2 <= sum * sum;
        }
        if (other instanceof CollisionPolygon polygon) {
            return polygon.intersects(this);
        }
        return false;
    }

    @Override
    public double getBoundingRadius() {
        return radius;
    }
}

