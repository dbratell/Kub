
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Enumeration;
import java.util.Vector;

public class Engine extends Canvas
{
    private Vector mThings;

    private Vertex r;
    private Vertex n;
    private Vertex v;
    private Vertex u;

    private Vertex eye;

    private int width = 533;
    private int height = 400;

    private final static double scale = 0.5;

    private Engine()
    {
        /*
          edges = new Vector();
          mFaces = new Vector();
          */
        mThings = new Vector();
    }

    public Engine(Vertex vrp, Vertex vpn, Vertex up)
    {
        this();
        r = vrp;
        n = Vertex.normalize(vpn);
        Vertex upprim = Vertex.normalize(up);
        upprim = Vertex.minus(upprim, Vertex.scale(n, Vertex.dot(upprim, n)));
        v = Vertex.normalize(upprim);
        u = Vertex.cross(n, v);
        eye = new Vertex(0, 0, -8);

        /*
          System.out.println("r="+r);
        System.out.println("n="+n);
        System.out.println("v="+v);
        System.out.println("u="+u);
        Vertex test= new Vertex(0,1.4,0);
        System.out.println("test="+worldToView(test));
        */

    }


    private Vertex worldToView(Vertex worldpoint)
    {
        Vertex temp = Vertex.minus(worldpoint, r);
        return new Vertex(Vertex.dot(temp, u),
                          Vertex.dot(temp, v),
                          Vertex.dot(temp, n));
    }


    private Vertex viewToWorld(Vertex viewpoint)
    {
        return new Vertex(viewpoint.punkt[0] * u.punkt[0] + viewpoint.punkt[1] * v.punkt[0] + viewpoint.punkt[2] * n.punkt[0] + r.punkt[0],
                          viewpoint.punkt[0] * u.punkt[1] + viewpoint.punkt[1] * v.punkt[1] + viewpoint.punkt[2] * n.punkt[1] + r.punkt[1],
                          viewpoint.punkt[0] * u.punkt[2] + viewpoint.punkt[1] * v.punkt[2] + viewpoint.punkt[2] * n.punkt[2] + r.punkt[2]);
    }

    private Point project(Vertex point)
    {
        double ustar,vstar;

        if (eye.punkt[2] >= point.punkt[2])
        {
            // The object is "behind" the camera
            return new Point(9999999, 9999999);
        }

        ustar = (eye.punkt[2] * point.punkt[0] - eye.punkt[0] * point.punkt[2]) /
                (eye.punkt[2] - point.punkt[2]);

        vstar = (eye.punkt[2] * point.punkt[1] - eye.punkt[1] * point.punkt[2]) /
                (eye.punkt[2] - point.punkt[2]);

        double scaleConstant = Math.min(width, height) * scale;

        /* The height must be treated different because screen-
         * coordinates are the other way. (Zero at the top and
         * increasing downwards).
         */
        return new Point((int)((ustar * scaleConstant + width / 2) + 0.5),
                         (int)((-vstar * scaleConstant + height / 2) + 0.5));

    }

    public void update(Graphics g)
    {
        paint(g);
    }

    private Image offscreenImage = null;

    public void paint(Graphics g)
    {
        //    System.out.println("Engine-paint called");
        Dimension displaySize = getSize();

        // local variabels that shadow the global ones.
        int width = displaySize.width;
        int height = displaySize.height;

        if (offscreenImage == null)
        {
            offscreenImage = createImage(width, height);
        }

        Graphics offg = offscreenImage.getGraphics();

        offg.setColor(Color.black);
        offg.fillRect(0, 0, width, height);

        Thing thing;
        for (Enumeration e = mThings.elements(); e.hasMoreElements();)
        {
            thing = (Thing)e.nextElement();
            drawThing(offg, thing);
        }

        /*    offg.setColor(Color.gray);
        Face face;
        for (Enumeration e = mFaces.elements() ; e.hasMoreElements() ;) {
          face = (Face)e.nextElement();
          drawFace(offg, face);
        }
        //    System.out.println("");

        */
        /*    offg.setColor(Color.green);
        Edge edge;
        for (Enumeration e = edges.elements() ; e.hasMoreElements() ;) {
          edge = (Edge)e.nextElement();
          drawEdge(offg, edge);
        }
        */

        /*
        // Draw a red dot at the origin
        Point p1 = project(worldToView(new Vertex(0,0,0)));
        offg.setColor(Color.red);
        offg.fillOval(p1.x-3, p1.y-3, 6, 6);
        */

        g.drawImage(offscreenImage, 0, 0, this);
    }

    private void drawThing(Graphics g, Thing thing)
    {
        Face face;
        for (Enumeration e = thing.faceElements(); e.hasMoreElements();)
        {
            face = ((Face)e.nextElement()).getTransformedFace(thing.getAckTransformation());
            drawFace(g, face);
        }

    }


    private void drawFace(Graphics g, Face f)
    {
        // Check if backface
        // See page 536 in Hill's "Computer Graphics"
        Vertex worldeye = viewToWorld(eye);
        double cosangle = Vertex.dot(f.facePlane,
                                     Vertex.normalize(Vertex.minus(worldeye, f.vertices[0])));
        if (cosangle <= 0.0)
        {
            // Backplane!
            //System.out.print("Backface("+(int)(Math.acos(cosangle)*57.2)+").");
            return;
        }
        else
        {
            //System.out.print("Frontface("+(int)(Math.acos(cosangle)*57.2)+").");
        }

        // Make the face a 2D polygon and draw the polygon
        Polygon poly = new Polygon();
        Point p1;
        for (int i = 0; i < f.vertices.length; i++)
        {
            p1 = project(worldToView(f.vertices[i]));
            poly.addPoint(p1.x, p1.y);
        }

        // int intensity = 128 + (int)(128*(cosangle-0.5));
        Color color = f.getColor();
        if (color == null) color = new Color(128, 128, 128);
        float[] HSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        // Change intensity
        HSB[2] += 0.5 * (cosangle - 0.5);
        HSB[2] = Math.max((float)0.0, HSB[2]);
        HSB[2] = Math.min((float)1.0, HSB[2]);
        color = Color.getHSBColor(HSB[0], HSB[1], HSB[2]);
        g.setColor(color);
        g.fillPolygon(poly);

    }

    public void setEye(Vertex neweye)
    {
        eye = neweye;
    }

    public Vertex getEye()
    {
        return eye;
    }

    public void setVRP(Vertex newr)
    {
        r = newr;
    }

    public Vertex getVRP()
    {
        return r;
    }

    /*
     * Sets the view to look at (0,0,0).
     */
    public void setViewToOrigin()
    {
        // System.out.println("Före : n = "+n+", v = "+v+", u= "+u);
        n = Vertex.normalize(Vertex.minus(new Vertex(0, 0, 0), r));
        Vertex upprim = v;
        upprim = Vertex.minus(upprim, Vertex.scale(n, Vertex.dot(upprim, n)));
        v = Vertex.normalize(upprim);
        u = Vertex.cross(n, v);
        //    System.out.println("Efter: n = "+n+", v = "+v+", u= "+u);
    }

    /*
     * Adds a face to show
     */
    /*
    public void addFace(Face face)
    {
      mFaces.addElement(face);
    }
    */

    /*
     * Adds an object to show
     */
    public void addThing(Thing thing)
    {
        mThings.addElement(thing);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }
}
