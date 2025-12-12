package Model;

public class Vector2 {
    public final double x;
    public final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Vector2 scale(double factor) {
        return new Vector2(this.x * factor, this.y * factor);
    }

    public double dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector2 normalize() {
        double len = length();
        if (len == 0) {
            return new Vector2(0, 0);
        }
        return new Vector2(x / len, y / len);
    }

    public Vector2 rotate(double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double rx = x * cos - y * sin;
        double ry = x * sin + y * cos;
        return new Vector2(rx, ry);
    }
}

