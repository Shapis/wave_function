import java.awt.Frame;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;


public class Main extends GLCanvas implements GLEventListener, MouseWheelListener   {
	
	static int numeroDePontos = 300;
	
	private static final int height = 850;
	private static final int width = height;
	
	
	static double[] vetorAntigo = new double[numeroDePontos];
	static double[] vetorNovo = new double[numeroDePontos];
	static double[] vetorTemp = new double[numeroDePontos];

	public static void main(String[] args) {
		
		
		for (int i = 0 ; i < numeroDePontos ; i++) {
			vetorAntigo[i] = 0	;
			vetorNovo[i] =0	;
		}
		
		
		/*
		for (double i = 0 ; i < numeroDePontos ; i++) {
			vetorAntigo[(int)i] = Math.cos((((i/(numeroDePontos-1))*2*Math.PI)))	;
			vetorNovo[(int)i] = Math.cos((((i/(numeroDePontos-1))*2*Math.PI)))	;
		}*/
		
		
		for (double i = 0 ; i < numeroDePontos ; i++) {
		vetorAntigo[(int)i] = Math.sin(((2*Math.PI/numeroDePontos)*i))	;
		vetorNovo[(int)i] = Math.sin(((2*Math.PI/numeroDePontos)*i))	;
	}
		
		/*for (double i = 0 ; i < numeroDePontos ; i++) {
			double d = (2*(i - (numeroDePontos/2))/numeroDePontos) - 0.5 ;
		vetorAntigo[(int)i] = Math.exp(-(d*d)*100);
		vetorNovo[(int)i] = Math.exp(-(d*d)*100)	;
	}*/
		/*vetorAntigo[0] = 0;
		vetorNovo[0] = 0;
		vetorAntigo[numeroDePontos-1] = 0;
		vetorNovo[numeroDePontos-1] = 0;*/
		
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(capabilities);
        
        
        Frame frame = new Frame("AWT Window Test");
        frame.setSize(height, width);
        frame.add(canvas);
        frame.setVisible(true);

        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }});
        
        canvas.addGLEventListener(new Main());
        canvas.addMouseWheelListener(new Main());
        Animator animator = new Animator(canvas);
        animator.start();
		
		
		
		
		
	}

	private void executeAlgorithm(){
		
		/* Dirichlet
		for (int i = 1 ;  i < numeroDePontos-1 ; i++ ) {
			vetorTemp[i] = vetorNovo[i+1] + vetorNovo[i-1] - vetorAntigo[i];
		}
		
		for (int i = 0 ;  i < numeroDePontos ; i++ ) {
			vetorAntigo[i] = vetorNovo[i];
		}
		
		for (int i = 0 ;  i < numeroDePontos ; i++ ) {
			vetorNovo[i] = vetorTemp[i];
		}
		*/
		
		for (int i = 1 ;  i < numeroDePontos-1 ; i++ ) {
			vetorTemp[i] = vetorNovo[i+1] + vetorNovo[i-1] - vetorAntigo[i];
		}
		
		vetorTemp[0] = vetorNovo[1] + vetorNovo[numeroDePontos-1] - vetorAntigo[0];
		
		vetorTemp[numeroDePontos-1] = vetorNovo[0] + vetorNovo[numeroDePontos-2] - vetorAntigo[numeroDePontos-1];
		
		for (int i = 0 ;  i < numeroDePontos ; i++ ) {
			vetorAntigo[i] = vetorNovo[i];
		}
		
		for (int i = 0 ;  i < numeroDePontos ; i++ ) {
			vetorNovo[i] = vetorTemp[i];
		}
		
		
	}

	private void update() {
		executeAlgorithm();
		
	}

	private void drawCircle (GL2 gl,double positionX, double positionY, double screenHeight , double screenWidth) {
		float red = 1;
		float green = 1;
		float blue = 1;
		
		
		
		
		
		gl.glBegin(GL.GL_POINTS);
		gl.glColor3f(red, green, blue);
	    for(int i =0; i <= 30; i++){
	    double angle = 2 * Math.PI * i / 30;
	    double x = Math.cos(angle);
	    double y = Math.sin(angle);
	    for (double j = 1 ; j >= 0.1 ; j = j - 0.02) {
	    gl.glVertex2d(positionX+((x*0.003)*j),positionY+((y*0.004)*j));
	    }
	    }
	}

	private void render(GLAutoDrawable drawable) {
		
	    GL2 gl =  drawable.getGL().getGL2();
	    
		    gl = drawable.getGL().getGL2();
		    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		    
		    	for (double i = 0 ; i < numeroDePontos ; i++) {
		    		drawCircle (gl,(i-(numeroDePontos/2))/(numeroDePontos/2),vetorNovo[(int)i], height, width);
		    	}
	    gl.glEnd();
	    /*System.out.println("Novo: " + vetorNovo[1]);
	    System.out.println("Antigo: " + vetorAntigo[1])*/;
	   try {
			Thread.sleep(0);
			}catch(InterruptedException ex) {
				
			}
	
	}

	public void display(GLAutoDrawable drawable) {
		 	update();
		    render(drawable);
		    
	}

	public void init(GLAutoDrawable drawable) {
	    // put your OpenGL initialization code here
		GLU glu = new GLU();
		 drawable.getGL().setSwapInterval(1);
		 
	}

	public void dispose(GLAutoDrawable drawable) {
	    // put your cleanup code here
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
	    gl.glViewport(0, 0, width, height);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}