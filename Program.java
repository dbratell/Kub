
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;

public class Program
{
    private static final boolean DEBUG = true;

    public static void main(String[] args)
    {

        Frame displayWindow = new Frame();
        //    Label mseLabel = new Label("No result");
        //    resultWindow.add(mseLabel,"North");
        //    resultWindow.pack();
        //    resultWindow.show();
        // Image bild = mseLabel.createImage(8,8);
        //    Graphics bg = bild.getGraphics();
        //    Font f = new Font("SansSerif",Font.PLAIN, 8);
        //    bg.setFont(f);
        //    bg.drawString("A",0,8);

        Engine motor = new Engine(new Vertex(10, 6, 4),
                                  new Vertex(-5, -3, -2),
                                  new Vertex(0, 1, 0));

        displayWindow.add(motor, "Center");
        Label counter = new Label("Not set yet");
        displayWindow.add(counter, "South");
        displayWindow.pack();
        displayWindow.show();

        boolean goon = true;
        int i = 0;

        Thing pyramid = getPyramid();
        //    pyramid.move(new Vertex(0.5,-0.5,0));
        motor.addThing(pyramid);
        //    Thing cube = getCube();
        //    cube.move(new Vertex(-0.5,0.5,0));
        //    motor.addThing(cube);

        // Rotate 1/2 degree about the y-axis and 1 degree about the z-axis
        double angle = 0.01745 / 2;
        double[][] transformMatrix1 = {{Math.cos(angle), 0, -Math.sin(angle)},
                                       {0, 1, 0},
                                       {Math.sin(angle), 0, Math.cos(angle)}};

        double[][] transformMatrix2 = {{Math.cos(2 * angle), Math.sin(2 * angle), 0},
                                       {-Math.sin(2 * angle), Math.cos(2 * angle), 0},
                                       {0, 0, 1}};

        double[][] transformMatrix = matrixMultiply(transformMatrix1, transformMatrix2);
        pyramid.setTransformation(transformMatrix);
        //    cube.setTransformation(transformMatrix);

        int wantedSleepTime = 40;
        long sleepUntil = System.currentTimeMillis() + wantedSleepTime;
        long sleepTime;
        while (goon)
        {
            try
            {
                /*	motor.setVRP(Vertex.transform(motor.getVRP(),transformMatrix));
                //	System.out.println("VRP:"+motor.getVRP());
                motor.setViewToOrigin();
                */
                motor.repaint();
                counter.setText("bildrutor: " + i++);
                displayWindow.repaint();

                pyramid.applyTransformation();
                //	cube.applyTransformation();

                sleepTime = Math.max(0, sleepUntil - System.currentTimeMillis());
                //	if(DEBUG) System.out.println("Let's sleep "+sleepTime+" ms.");
                sleepUntil += wantedSleepTime;
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException ex)
            {
            }
        }

    }

    private static double[][] matrixMultiply(double[][] m1, double[][] m2)
    {
        if (m1.length < 1)
            throw new IllegalArgumentException("The matrixes must be nonempty");
        if (m1[0].length < 1)
            throw new IllegalArgumentException("The matrixes must be nonempty");
        if (m1[0].length != m2.length)
            throw new IllegalArgumentException("The matrixes must be of correspondin sizes");
        if (m2[0].length < 1)
            throw new IllegalArgumentException("The matrixes must be nonempty");

        double[][] m = new double[m1.length][m2[0].length];
        for (int row = 0; row < m1.length; row++)
        {
            for (int col = 0; col < m2[0].length; col++)
            {
                double value = 0.0;
                for (int innerColRow = 0; innerColRow < m2.length; innerColRow++)
                {
                    value += m1[row][innerColRow] * m2[innerColRow][col];
                }
                m[row][col] = value;
            }
        }

        //    System.out.println(m1+" gånger "+m2+" blir "+m);
        return m;
    }

    private static Thing getCube()
    {
        // Create vertices
        Vertex p000 = new Vertex(-1, -1, -1);
        Vertex p001 = new Vertex(-1, -1, 1);
        Vertex p010 = new Vertex(-1, 1, -1);
        Vertex p011 = new Vertex(-1, 1, 1);
        Vertex p100 = new Vertex(1, -1, -1);
        Vertex p101 = new Vertex(1, -1, 1);
        Vertex p110 = new Vertex(1, 1, -1);
        Vertex p111 = new Vertex(1, 1, 1);

        /*
        // Make edges of vertices
        Edge e01 = new Edge(p000,p001);
        Edge e02 = new Edge(p000,p010);
        Edge e04 = new Edge(p000,p100);
        Edge e13 = new Edge(p001,p011);
        Edge e15 = new Edge(p001,p101);
        Edge e23 = new Edge(p010,p011);
        Edge e26 = new Edge(p010,p110);
        Edge e45 = new Edge(p100,p101);
        Edge e46 = new Edge(p100,p110);
        Edge e67 = new Edge(p110,p111);
        Edge e57 = new Edge(p101,p111);
        Edge e37 = new Edge(p011,p111);
        */

        Vertex[] side1 = {p100, p110, p111, p101};
        Vertex[] side2 = {p100, p000, p010, p110};
        Vertex[] side3 = {p100, p101, p001, p000};
        Vertex[] side4 = {p000, p001, p011, p010};
        Vertex[] side5 = {p010, p011, p111, p110};
        Vertex[] side6 = {p001, p101, p111, p011};

        Face face1 = new Face(side1, new Vertex(1, 0, 0));
        Face face2 = new Face(side2, new Vertex(0, 0, -1), Color.blue.darker());
        Face face3 = new Face(side3, new Vertex(0, -1, 0), Color.red.darker());
        Face face4 = new Face(side4, new Vertex(-1, 0, 0), Color.cyan.darker());
        Face face5 = new Face(side5, new Vertex(0, 1, 0), Color.magenta.darker());
        Face face6 = new Face(side6, new Vertex(0, 0, 1), Color.orange.darker());
        Face[] cubeFaces = {face1, face2, face3, face4, face5, face6};

        return new Thing(cubeFaces);
    }

    private static Thing getPyramid()
    {
        Vertex p000 = new Vertex(-1, -1, -1);
        Vertex p001 = new Vertex(-1, -1, 1);
        Vertex p100 = new Vertex(1, -1, -1);
        Vertex p101 = new Vertex(1, -1, 1);
        Vertex top = new Vertex(0, Math.sqrt(2), 0);

        /*
        // Edges
        Edge e01 = new Edge(p000,p001);
        Edge e04 = new Edge(p000,p100);
        Edge e15 = new Edge(p001,p101);
        Edge e45 = new Edge(p100,p101);
        Edge e0t = new Edge(p000,top);
        Edge e1t = new Edge(p001,top);
        Edge e4t = new Edge(p100,top);
        Edge e5t = new Edge(p101,top);
        */

        // Faces
        // Bottom
        Vertex[] bottom = {p100, p101, p001, p000};
        Vertex[] side1 = {p101, p100, top};
        Vertex[] side2 = {p000, p100, top};
        Vertex[] side3 = {p001, p101, top};
        Vertex[] side4 = {p000, p001, top};

        Face face1 = new Face(bottom, new Vertex(0, -1, 0));
        Face face2 = new Face(side1, new Vertex(0, 1, 0), Color.blue.darker());
        Face face3 = new Face(side2, new Vertex(0, 1, 0), Color.red.darker());
        Face face4 = new Face(side3, new Vertex(0, 1, 0), Color.cyan.darker());
        Face face5 = new Face(side4, new Vertex(0, 1, 0), Color.magenta.darker());
        Face[] pyramidFaces = {face1, face2, face3, face4, face5};

        return new Thing(pyramidFaces);
    }
}
