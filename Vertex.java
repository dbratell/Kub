public class Vertex
{
  public double punkt[] = new double[3];
  
  private Vertex moveVertex = null;
  
  Vertex(double x, double y, double z)
  {
    punkt[0] = x;
    punkt[1] = y;
    punkt[2] = z;
  }

  public void transform(double M[][])
  {
    double newpoint[] = new double[3];
    for(int i=0; i<3; i++) {
      newpoint[i] = M[0][i]*punkt[0] + M[1][i]*punkt[1] + M[2][i]*punkt[2];
    }
    for(int i=0; i<3; i++) {
      punkt[i] = newpoint[i];
    }
  }

  public static Vertex normalize(Vertex a)
  {
    double norm = 0.0;
    for(int i=0; i<3; i++) norm += a.punkt[i]*a.punkt[i];
    norm = Math.sqrt(norm);
    return new Vertex(a.punkt[0]/norm, a.punkt[1]/norm, a.punkt[2]/norm);
  }

  public static Vertex cross(Vertex a, Vertex b)
  {
    return new Vertex(a.punkt[1]*b.punkt[2]-a.punkt[2]*b.punkt[1],
		      a.punkt[2]*b.punkt[0]-a.punkt[0]*b.punkt[2],
		      a.punkt[0]*b.punkt[1]-a.punkt[1]*b.punkt[0]);
  }

  public static Vertex scale(Vertex a, double n)
  {
    return new Vertex(a.punkt[0]*n,
		      a.punkt[1]*n,
		      a.punkt[2]*n);
  }

  public static double dot(Vertex a, Vertex b)
  {
    return a.punkt[0]*b.punkt[0] +a.punkt[1]*b.punkt[1]+a.punkt[2]*b.punkt[2];
  }

  public static Vertex minus(Vertex a, Vertex b)
  {
    return new Vertex(a.punkt[0]-b.punkt[0],
		      a.punkt[1]-b.punkt[1],
		      a.punkt[2]-b.punkt[2]);
  }

  public Vertex minus(Vertex a)
  {
    return new Vertex(punkt[0]-a.punkt[0],
		      punkt[1]-a.punkt[1],
		      punkt[2]-a.punkt[2]);
  }

  public static Vertex plus(Vertex a, Vertex b)
  {
    return new Vertex(a.punkt[0]+b.punkt[0],
		      a.punkt[1]+b.punkt[1],
		      a.punkt[2]+b.punkt[2]);
  }

  public Vertex plus(Vertex a)
  {
    return new Vertex(a.punkt[0]+punkt[0],
		      a.punkt[1]+punkt[1],
		      a.punkt[2]+punkt[2]);
  }

  public static Vertex transform(Vertex a, double M[][])
  {
    double newpoint[] = new double[3];
    for(int i=0; i<3; i++) {
      newpoint[i] =
	M[0][i]*a.punkt[0] +
	M[1][i]*a.punkt[1] +
	M[2][i]*a.punkt[2];
    }

    return new Vertex(newpoint[0], newpoint[1], newpoint[2]);
  }

  public String toString()
  {
    return "("+punkt[0]+","+punkt[1]+","+punkt[2]+")";
  }

  public Vertex getTransformedVertex(double[][] trans)
  {
    if(trans.length != 3) {
      throw new IllegalArgumentException("Must be a 3x3 matrix");
    }

    if(trans[0].length != 3) {
      throw new IllegalArgumentException("Must be a 3x3 matrix");
    }

    double newXYZ[] = new double[3];
    for(int i=0; i<3; i++) {
      if(moveVertex == null) {
	newXYZ[i] = trans[i][0]*punkt[0] +
	  trans[i][1]*punkt[1] +
	  trans[i][2]*punkt[2];
      } else {
	newXYZ[i] = trans[i][0]*(punkt[0]-moveVertex.punkt[0]) +
	  trans[i][1]*(punkt[1]-moveVertex.punkt[1]) +
	  trans[i][2]*(punkt[2]-moveVertex.punkt[2]) +
	  moveVertex.punkt[i];
      }
    }

    return new Vertex(newXYZ[0], newXYZ[1], newXYZ[2]);
  }

  public void move(Vertex v)
  {
    if(moveVertex == null) moveVertex = v;
    else {
      moveVertex = plus(moveVertex,v);
    }
    
    punkt[0] += v.punkt[0];
    punkt[1] += v.punkt[1];
    punkt[2] += v.punkt[2];
  }

}
