package net.openfractal.maths;

public class Vector2 {
    public float x,y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(v.x + x, v.y + y);
    }

    public Vector2 devide(Vector2 v) {
        return new Vector2(x / v.x, y / v.y);
    }

    public Vector2 devide(float v) {
        return new Vector2(x / v, y / v);
    }

    public Vector2 sub(float v) {
        return new Vector2(x - v, y - v);
    }

    public Vector2 sub(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    public Vector2 mul(float v) {
        return new Vector2(x * v, y * v);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
