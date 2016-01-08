package de.sam6.geotrace;

import codeanticode.glgraphics.GLConstants;
import codeanticode.glgraphics.GLGraphics;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import de.sam6.geotrace.geo.Connection;
import de.sam6.geotrace.geo.Earth;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import toxi.processing.ToxiclibsSupport;

import javax.media.opengl.GL;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

/**
 * Main class.
 * <p>
 * Make sure bin/ contains config.properties bin/data/ must contain the texture files.
 * @author cmichi
 */
public class Main extends PApplet
{
   private static final long serialVersionUID = -1258821962515660999L;

   /**
    * Colors
    */
   public int WHITE = color(255, 255, 255);

   public int BLACK = color(0, 0, 0);

   public int RED = color(255, 0, 0);

   public int FONT_COLOR = color(255, 237, 178);

   public int FONT_COLOR2 = color(255, 255, 228);

   public int BG_COLOR = color(45, 43, 44);

   /**
    * The color of the connections between trace points alternates
    */
   public int currShiny = 0;

   public int[] SHINY = {color(65, 162, 204), color(159, 0, 0), color(84, 144, 0), color(168, 0, 158)};

   /**
    * Rotate the earth with your mouse
    */
   public float rotationX = 0;

   public float rotationY = 0;

   public float velocityX = 0;

   public float velocityY = 0;

   public float pushBack = 0;

   public ToxiclibsSupport gfx;

   public GL gl;

   public PGraphicsOpenGL pgl;

   public Earth earth;

   public int scale = 250;

   public main.java.de.sam6.geotrace.trace.Trace tracer;

   public ControlP5 controlP5;

   public ControlFont font;

   /**
    * Collections for traces
    */
   public Vector<Connection> connections = new Vector<Connection>();

   public Vector<main.java.de.sam6.geotrace.trace.TraceResult> traces =
      new Vector<main.java.de.sam6.geotrace.trace.TraceResult>();

   public int shiftEarthX = 135;

   /**
    * File containing all paths
    */
   public Properties configFile = new Properties();

   public GLGraphics renderer;

   /**
    * Controls
    */
   private Control ctrl;

   public void setup()
   {
      try
      {
         configFile.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }

      size(1024, 772, GLConstants.GLGRAPHICS);
      gl = ((PGraphicsOpenGL)g).gl;
      pgl = (PGraphicsOpenGL)g;
      gfx = new ToxiclibsSupport(this);

      renderer = (GLGraphics)(g);
      initCtrls();

      tracer = new Trace(this);
      earth = new Earth(this, "world32k.jpg");
      ctrl.draw();
   }

   public void draw()
   {
      background(0);

      gl.glDisable(GL.GL_COLOR);
      gl.glActiveTexture(GL.GL_TEXTURE0);

      rectMode(CORNER);
      renderer.rectMode(CORNER);
      controlP5.draw();

      pushMatrix();
      translate(shiftEarthX, 0, 0);

      ctrl.draw();
      Connection.drawAllConnections(this);

      popMatrix();

      pushMatrix();
      translate(shiftEarthX, 0, 0);

      earth.renderGlobe();
      popMatrix();

		/* simple routine for moving the earth with mouse pans */
      if(mousePressed)
      {
         velocityX += (mouseY - pmouseY) * 0.01f;
         velocityY -= (mouseX - pmouseX) * 0.01f;
      }
   }

   /**
    * Init the controls.
    */
   public void initCtrls()
   {
      controlP5 = new ControlP5(this);
      controlP5.setAutoDraw(false);
      ctrl = new Control(this);
   }

   /**
    * RETURN was pressed in the TextField.
    * @param theEvent
    */
   public void controlEvent(ControlEvent theEvent)
   {
      reset();
      tracer.hostname = ctrl.b.getText();
      tracer.start();
   }

   /**
    * Reset the whole application so that a new trace can be made.
    */
   public void reset()
   {
      tracer.started = false;
      connections = new Vector<Connection>();
      traces = new Vector<TraceResult>();
      tracer = new Trace(this);

      ctrl.reset();
   }

   /**
    * Use to start as a regular Java-Application. Just choose "Run As >> Java Application" and not "Java Applet".
    */
   public static void main(String args[])
   {
      PApplet.main(new String[]{"com.creal.trace.Main"});
   }

}
