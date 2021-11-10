package gdv.ohno.logic;

class Vector2D {
    public int x, y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D sum(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static boolean isEqual(Vector2D a, Vector2D b) {
        if (a.x == b.x && a.y == b.y) return true;
        return false;
    }
}

