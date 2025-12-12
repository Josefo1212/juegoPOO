package Model;

public interface CollisionShape {
    boolean intersects(CollisionShape other);
    double getBoundingRadius();
}

