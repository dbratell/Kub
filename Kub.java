import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;

public class Kub extends Applet implements Runnable {
  
  private Engine motor;
  // Label counter;
  private boolean goon = true;
  private boolean frozen = false;
  private Thing pyramid;
  private Thing cube;
  
  Thread runThread = null;
  
  
  public void init() {
    showStatus("initializing... ");

    frozen = false;
    //    setLayout(new BorderLayout());
    motor = new Engine(new Vertex(6,4,3),
		       new Vertex(-6,-4,-3),
		       new Vertex(0,1,0));

    add(motor);
    //    add(motor,"Center");
    //    counter = new Label("Not set yet");
    //    add(counter, "South");
    //    displayWindow.pack();
    //    displayWindow.show();


    cube = getCube();
    motor.addThing(cube);
  }
  
  public void start() {
    showStatus("starting... ");
    goon = true;
    if(frozen) {
      // Do nothing
      //      System.out.println("Starting frozen animation");
    } else {
      if (runThread == null) {
	runThread = new Thread(this);
      }
      runThread.start();
    }
  }
  
  public void stop() {
    showStatus("stopping... ");
    runThread = null;
    goon = false;
  }
  
  public void destroy() {
    showStatus("preparing for unloading...");
  }
  
  /*
   * Used by the animating thread
   */
  public void run()
  {
    // Rotate a 1/2 degree about the y-axis and 1 degree about the z-axis.
    double angle = 0.01745/2;
    double[][] transformMatrix1 = {{Math.cos(angle), 0, -Math.sin(angle)},
				   {0, 1, 0},
				   {Math.sin(angle), 0, Math.cos(angle)}};

    double[][] transformMatrix2 = {{Math.cos(2*angle), Math.sin(2*angle), 0},
				   {-Math.sin(2*angle), Math.cos(2*angle), 0},
				   {0, 0, 1}};

    double[][] transformMatrix = matrixMultiply(transformMatrix1, transformMatrix2);
    cube.setTransformation(transformMatrix);

    int wantedSleepTime=40;
    long sleepUntil = System.currentTimeMillis()+wantedSleepTime;
    long sleepTime;

    while(goon) {
      try {
	/*
	  //	System.out.println("Move camera");
	  motor.setVRP(Vertex.transform(motor.getVRP(),transformMatrix));
	  //	System.out.println("VRP:"+motor.getVRP());
	  motor.setViewToOrigin();
	  //	counter.setText("bildrutor: "+ i++);
	  */
	motor.repaint();
	invalidate();
	
	cube.applyTransformation();

	sleepTime = max(0,sleepUntil-System.currentTimeMillis());
	sleepUntil += wantedSleepTime;
	Thread.sleep(sleepTime);
      } catch (InterruptedException ex) {}
    }
    
  }
  
  public boolean mouseDown(Event e, int x, int y) {
    if (frozen) {
      frozen = false;
      //Call the method that starts the animation.
      start();
    } else {
      frozen = true;
      //Call the method that stops the animation.
      stop();
    }
    return true;
  }

  
  private static final long max(long a, long b)
  {
    return a>b?a:b;
  }

  private static Thing getCube()
  {
    // Create vertices
    Vertex p000 = new Vertex(-1,-1,-1);
    Vertex p001 = new Vertex(-1,-1,1);
    Vertex p010 = new Vertex(-1,1,-1);
    Vertex p011 = new Vertex(-1,1,1);
    Vertex p100 = new Vertex(1,-1,-1);
    Vertex p101 = new Vertex(1,-1,1);
    Vertex p110 = new Vertex(1,1,-1);
    Vertex p111 = new Vertex(1,1,1);

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
    
    Vertex[] side1 = {p100,p110,p111,p101};
    Vertex[] side2 = {p100,p000,p010,p110};
    Vertex[] side3 = {p100,p101,p001,p000};
    Vertex[] side4 = {p000,p001,p011,p010};
    Vertex[] side5 = {p010,p011,p111,p110};
    Vertex[] side6 = {p001,p101,p111,p011};

    Face face1 = new Face(side1, new Vertex(1,0,0));
    Face face2 = new Face(side2, new Vertex(0,0,-1), Color.blue.darker());
    Face face3 = new Face(side3, new Vertex(0,-1,0), Color.red.darker());
    Face face4 = new Face(side4, new Vertex(-1,0,0), Color.cyan.darker());
    Face face5 = new Face(side5, new Vertex(0,1,0), Color.magenta.darker());
    Face face6 = new Face(side6, new Vertex(0,0,1), Color.orange.darker());
    Face[] cubeFaces = {face1, face2, face3, face4, face5, face6};

    return new Thing(cubeFaces);
  }
  
  private static Thing getPyramid()
  {
    Vertex p000 = new Vertex(-1,-1,-1);
    Vertex p001 = new Vertex(-1,-1,1);
    Vertex p100 = new Vertex(1,-1,-1);
    Vertex p101 = new Vertex(1,-1,1);
    Vertex top = new Vertex(0,Math.sqrt(2),0);

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
    Vertex[] bottom = {p100,p101,p001,p000};
    Vertex[] side1 = {p101,p100,top};
    Vertex[] side2 = {p000,p100,top};
    Vertex[] side3 = {p001,p101,top};
    Vertex[] side4 = {p000,p001,top};
    
    Face face1 = new Face(bottom, new Vertex(0,-1,0));
    Face face2 = new Face(side1, new Vertex(0,1,0), Color.blue.darker());
    Face face3 = new Face(side2, new Vertex(0,1,0), Color.red.darker());
    Face face4 = new Face(side3, new Vertex(0,1,0), Color.cyan.darker());
    Face face5 = new Face(side4, new Vertex(0,1,0), Color.magenta.darker());
    Face[] pyramidFaces = {face1, face2, face3, face4, face5};

    return new Thing(pyramidFaces);
  }

    private static double[][] matrixMultiply(double[][] m1, double[][] m2)
  {
    if(m1.length < 1)
      throw new IllegalArgumentException("The matrixes must be nonempty");
    if(m1[0].length < 1)
      throw new IllegalArgumentException("The matrixes must be nonempty");
    if(m1[0].length != m2.length)
      throw new IllegalArgumentException("The matrixes must be of correspondin sizes");
    if(m2[0].length < 1)
      throw new IllegalArgumentException("The matrixes must be nonempty");

    double[][] m = new double[m1.length][m2[0].length];
    for(int row=0; row < m1.length; row++) {
      for(int col=0; col < m2[0].length; col++) {
	double value = 0.0;
	for(int innerColRow=0; innerColRow < m2.length; innerColRow++) {
	    value += m1[row][innerColRow]*m2[innerColRow][col];
	}
	m[row][col] = value;
      }
    }
    
    //    System.out.println(m1+" gånger "+m2+" blir "+m);
    return m;
  }

}
