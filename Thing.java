
import java.util.Enumeration;
import java.util.Vector;

/*
 * Thing is an object containing some mFaces and some edges and
 * maybe some more information.
 */

public class Thing
{
    Vector mFaces;

    private double[][] ackTransformation = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    private double[][] currentTransformation = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

    private Thing()
    {
        mFaces = new Vector();
    }

    public Thing(Face[] f)
    {
        this();
        for (int i = 0; i < f.length; i++)
        {
            mFaces.addElement(f[i]);
        }
    }

    /*
     * Returns an enumeration of the mFaces this thing
     * is composed of.
     */
    public Enumeration faceElements()
    {
        return mFaces.elements();
    }

    public void setTransformation(double[][] newTransformation)
    {
        currentTransformation = newTransformation;
    }

    public void applyTransformation()
    {
        ackTransformation = matrixMultiply(currentTransformation,
                                           ackTransformation);
    }

    public double[][] getAckTransformation()
    {
        return ackTransformation;
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

    public void move(Vertex v)
    {
        Face face;
        for (Enumeration e = faceElements(); e.hasMoreElements();)
        {
            ((Face)e.nextElement()).move(v);
        }
    }


}
