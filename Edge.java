public class Edge
{
  public Vertex start;
  public Vertex end;
  
  Edge(Vertex s, Vertex e)
  {
    start = s;
    end = e;
  }

  /*  public void transform(double M[][])
  {
    double newpoint[] = new double[3];
    for(int i=0; i<3; i++) {
      newpoint[i] = M[0][i]*start[0] + M[1][i]*start[1] + M[2][i]*start[2];
    }
    for(int i=0; i<3; i++) {
      start[i] = newpoint[i];
    }

    for(int i=0; i<3; i++) {
      newpoint[i] = M[0][i]*end[0] + M[1][i]*end[1] + M[2][i]*end[2];
    }
    for(int i=0; i<3; i++) {
      end[i] = newpoint[i];
    }
  }
  */
}
