package utilities;

public final class Vector2D {
    public double x, y;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public boolean equals(Object o) {
        if (o instanceof Vector2D) {
            Vector2D v = (Vector2D) o;
            return x == v.x && y == v.y;
        } else
            return false;
    }

    public String toString() {
        return "Vector: " + x + "," + y;
    }

    public double mag() {
        return Math.hypot(x, y);
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public double angle(Vector2D other) {
        double result = other.angle() - this.angle();
        if (result > Math.PI) result -= 2 * Math.PI;
        else if (result < -Math.PI) result += 2 * Math.PI;
        return result;
    }

    public Vector2D add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2D addScaled(Vector2D v, double fac) {
        this.x += fac * v.x;
        this.y += fac * v.y;
        return this;
    }

    public Vector2D subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2D mult(double fac) {
        this.x *= fac;
        this.y *= fac;
        return this;
    }

    public Vector2D rotate(double angle) {
        double newX = this.x * Math.cos(angle) - this.y * Math.sin(angle);
        this.y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
        this.x = newX;
        return this;
    }

    public double dot(Vector2D v) {
        return this.x * v.x + this.y * v.y;
    }

    public double dist(Vector2D v) {
        return Math.hypot(this.x - v.x, this.y - v.y);
    }

    public Vector2D normalise() {
        if (mag() != 0) {
            double m = mag();
            this.x /= m;
            this.y /= m;
        }
        return this;

    }

    public Vector2D wrap(double w, double h) {
        if (x>w) x -= w;
        else if (x<0) x+= w;
        if (y>h) y -= h;
        else if (y<0) y += h;
        return this;
    }

    public double distWithWrap(Vector2D v, double w, double h) {
        double xdiff = this.x - v.x;
        if (xdiff>w/2) xdiff = w-xdiff;
        else if (xdiff<-w/2) xdiff = -w-xdiff;
        double ydiff = this.y - v.y;
        if (ydiff>h/2) ydiff = h-ydiff;
        else if (ydiff<-h/2) ydiff = -h-ydiff;
        return Math.hypot(xdiff, ydiff);
    }

    public static Vector2D polar(double angle, double mag) {
        return new Vector2D(mag * Math.cos(angle), mag * Math.sin(angle));
    }

}
