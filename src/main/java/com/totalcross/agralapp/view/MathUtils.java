package com.totalcross.agralapp.view;

import totalcross.ui.gfx.Coord;

public class MathUtils {
    // (0<𝐀𝐌⋅𝐀𝐁<𝐀𝐁⋅𝐀𝐁)∧(0<𝐀𝐌⋅𝐀𝐃<𝐀𝐃⋅𝐀𝐃)
    public static boolean isInsideSquare(Coord point, Coord [] squarePoints) {

        int p1 = 0, p2 = 1, p4 = 3;
        Coord p1p2 = new Coord(squarePoints[p2].x - squarePoints[p1].x, squarePoints[p2].y - squarePoints[p1].y);
        Coord p1p4 = new Coord(squarePoints[p4].x - squarePoints[p1].x, squarePoints[p4].y - squarePoints[p1].y);
        Coord aPoint = new Coord(point.x - squarePoints[p1].x, point.y - squarePoints[p1].y);

        return
                 dotProduct(p1p2, aPoint) > 0
                && dotProduct(p1p2, aPoint) < dotProduct(p1p2, p1p2)
                && dotProduct(p1p4, aPoint) > 0
                && dotProduct(p1p4, aPoint) < dotProduct(p1p4, p1p4)
        ;
    }

    public static int dotProduct (Coord v1, Coord v2) {
        return v1.x*v2.x + v1.y*v2.y;
    }
    /**
     * Return points in clockwise starting from the left topmost point
     *      p1 ---- p2
     *      |       |
     *      |       |
     *      p4 ---- p3
     *
     * @param c1
     * @param c2
     * @param diameter
     * @return
     */
    public static Coord [] getSquarePoints(Coord c1, Coord c2, int diameter) {
        double cosAlpha = (c1.y - c2.y)/Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
        double senAlpha = (c1.x - c2.x)/Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));

        Coord p1 = new Coord((int) (c1.x + cosAlpha*(diameter/2)), (int ) (c1.y - senAlpha*(diameter/2)));
        Coord p2 = new Coord((int) (c1.x - cosAlpha*(diameter/2)), (int ) (c1.y + senAlpha*(diameter/2)));
        Coord p3 = new Coord((int) (c2.x - cosAlpha*(diameter/2)), (int ) (c2.y + senAlpha*(diameter/2)));
        Coord p4 = new Coord((int) (c2.x + cosAlpha*(diameter/2)), (int ) (c2.y - senAlpha*(diameter/2)));
        return new Coord[] {p1, p2, p3, p4};
    }
}