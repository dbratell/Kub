import java.awt.Color;

public class Face
{
  public Vertex[] vertices;
  public Vertex facePlane;

  Color faceColor=null;
  
  public Face(Vertex[] v)
  {
    vertices = v;
    if(v.length<3) {
      // A face has to have at least three vertices
      throw new IllegalArgumentException("To few vertices for a face");
    }

    facePlane = Vertex.normalize(Vertex.cross(Vertex.minus(v[1], v[0]),Vertex.minus(v[2], v[1])));

    //    System.out.println("normal: "+facePlane);
  }

  public Face(Vertex[] v, Vertex normal)
  {
    this(v);
    if(Vertex.dot(Vertex.normalize(normal), facePlane) <=0.0) {
      // The angle between the computed normal and the
      // wanted one is more than 90 degress so let's
      // turned it the other way
      facePlane = Vertex.minus(new Vertex(0,0,0), facePlane);
    }
  }

  public Face(Vertex[] v, Vertex normal, Color c)
  {
    this(v, normal);
    faceColor = c;
  }

  public Face(Vertex[] v, Color c)
  {
    this(v);
    faceColor = c;
  }

  public Color getColor()
  {
    return faceColor;
  }

  public void setColor(Color newColor)
  {
    faceColor = newColor;
  }

  public Face getTransformedFace(double[][] trans)
  {
    Vertex[] newV = new Vertex[vertices.length];
    for(int i = 0; i<vertices.length; i++) {
      newV[i] = vertices[i].getTransformedVertex(trans);
    }
    
    return new Face(newV, facePlane.getTransformedVertex(trans), faceColor);
  }

  public void move(Vertex v)
  {
    for(int i = 0; i<vertices.length; i++) {
      vertices[i].move(v);
    }
  }
  

}
