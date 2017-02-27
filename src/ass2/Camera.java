package ass2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.FloatBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

public class Camera implements GLEventListener {
	/**
	 	
		 = {0,-1};
	 	private double[] myTarget = {0,1};
	    private double myAngle;
	    private double myScale;
	    
	    private double angle = 135;
*/		private double myAspect;
		private double[] myPos;
		public static boolean torch = false;
		
	    public Camera() {
	        myAspect = 1.0;
	    }
	    public double[] cameraPos(){
	    	return myPos;
	    }
	    public static void torch(){
	    	torch = !torch;
//	    	if(torch) {
//	    		Game.myTerrain.sun = false;
//	    	}
//	    	else {
//	    		Game.myTerrain.sun = true;
//	    	}
	    }
	    public void draw(GL2 gl) {	    	
	    	   	
	    	double[] myPos = Avatar.avatarPos();
	    	double[] myTarget = Avatar.targetPos();
	    	double viewX =myPos[0]+(myTarget[0]*0.1);
	    	double viewZ = myPos[1]+(myTarget[1]*0.1);
	    	double camHeight = Terrain.altitude(myPos[0],myPos[1]) + 0.3;
//	    	if(!Terrain.avatar) {
//	    		viewX = myPos[0];
//	    		viewZ = myPos[1];
//	    	}
	    	float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
	        float[] lightColorDiffuse = {0.8f, 0.8f, 0.8f, 1f};
	        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};
	    	GLU glu = new GLU();
	    	
	        gl.glPushMatrix();
	        gl.glMatrixMode(GL2.GL_PROJECTION);  
	        gl.glLoadIdentity();	
	       
	        //Set the torch to shine in front of the avatar
	        //Using a spot light with a  reduced light cutoff angle 
	        float spotDir [] = {(float) (myTarget[0] * 0.7), (float) (-0.7), (float) (myTarget[1]*0.7)};        
	        float light_pos [] = {(float)myPos[0],(float) camHeight,(float) myPos[1], 1.0f};
	        if(torch){
	        	gl.glDisable(GL2.GL_LIGHT0);
	        	gl.glDisable(GL2.GL_LIGHT2);
	        	gl.glEnable(GL2.GL_LIGHT1);
	        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
	        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorDiffuse, 0);
	        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
	        	gl.glLightfv (GL2.GL_LIGHT1, GL2.GL_POSITION, light_pos,0);
	        	gl.glLightf (GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT , 3.0f);
	        	gl.glLightfv (GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spotDir,0);	
	        	gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 35.0f);
	        } else {
	        	gl.glDisable(GL2.GL_LIGHT1);
	        }	        
	        glu.gluPerspective(100, myAspect, 0.1, 2000);	
	        //Look at where the avatar is looking
	        //From behind the avatar
	        if(Terrain.avatar)
	        	glu.gluLookAt(myPos[0]-myTarget[0],camHeight,myPos[1]-myTarget[1], viewX,camHeight,viewZ, 0, 1, 0);
	        else
	        	glu.gluLookAt(myPos[0],camHeight,myPos[1], viewX,camHeight,viewZ, 0, 1, 0);
	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        gl.glLoadIdentity();
	        
	      
	        gl.glPopMatrix();
	    }
	    
	    public static void drawSkyBox(GL2 gl, double x, double z) {
	    	
	    	double width = Game.myTerrain.mySize.getWidth();
	    	double height = Game.myTerrain.mySize.getHeight();
	    	double temp = width > height ? width : height;
	    	width = temp*20;
	    	height = temp*20;
	    	double y = -temp*5;
	    	double altitude = temp * 15 + y;
	    	
	    	
	    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	    	
	    	gl.glBindTexture(GL2.GL_TEXTURE_2D, Game.skyTexture4.getTextureID());     
	    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
	    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		      gl.glBegin(GL2.GL_POLYGON);
		         gl.glTexCoord2d(1, 1); gl.glVertex3d( x - width, y,  z + height);
		         gl.glTexCoord2d(0, 1); gl.glVertex3d( x + width, y,  z + height);
		         gl.glTexCoord2d(0, 0); gl.glVertex3d( x + width, altitude, z + height);
		         gl.glTexCoord2d(1, 0); gl.glVertex3d( x - width, altitude, z + height);
		      gl.glEnd();
		  
		      
		      
//		  temp = width;
//		  width = height;
//		  height = temp;
	      gl.glBindTexture(GL2.GL_TEXTURE_2D, Game.skyTexture3.getTextureID());     
	    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
	    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		      gl.glBegin(GL2.GL_POLYGON);
		         gl.glTexCoord2d(1, 1); gl.glVertex3d( x + width, y,  z + height);
		         gl.glTexCoord2d(0, 1); gl.glVertex3d( x + width, y,  z - height);
		         gl.glTexCoord2d(0, 0); gl.glVertex3d( x + width, altitude, z - height);
		         gl.glTexCoord2d(1, 0); gl.glVertex3d( x + width, altitude, z + height);
		      gl.glEnd();
		      
		      
	      gl.glBindTexture(GL2.GL_TEXTURE_2D, Game.skyTexture2.getTextureID());     
	    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
	    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		      gl.glBegin(GL2.GL_POLYGON);
		         gl.glTexCoord2d(1, 1); gl.glVertex3d( x + width, y,  z - height);
		         gl.glTexCoord2d(0, 1); gl.glVertex3d( x - width, y,  z - height);
		         gl.glTexCoord2d(0, 0); gl.glVertex3d( x - width, altitude, z - height);
		         gl.glTexCoord2d(1, 0); gl.glVertex3d( x + width, altitude, z - height);
		      gl.glEnd();
		  
		      gl.glBindTexture(GL2.GL_TEXTURE_2D, Game.skyTexture1.getTextureID());     
		    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
			      gl.glBegin(GL2.GL_POLYGON);
			         gl.glTexCoord2d(1, 1); gl.glVertex3d( x - width, y,  z - height);
			         gl.glTexCoord2d(0, 1); gl.glVertex3d( x - width, y,  z + height);
			         gl.glTexCoord2d(0, 0); gl.glVertex3d( x - width, altitude, z + height);
			         gl.glTexCoord2d(1, 0); gl.glVertex3d( x - width, altitude, z - height);
			      gl.glEnd();
			      
			      
			      gl.glBindTexture(GL2.GL_TEXTURE_2D, Game.skyTextureTop.getTextureID());     
			    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
			    	gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
				      gl.glBegin(GL2.GL_POLYGON);
				         gl.glTexCoord2d(0, 1); gl.glVertex3d( x - width, altitude,  z + height);
				         gl.glTexCoord2d(0, 0); gl.glVertex3d( x + width, altitude,  z + height);
				         gl.glTexCoord2d(1, 0); gl.glVertex3d( x + width, altitude, z - height);
				         gl.glTexCoord2d(1, 1); gl.glVertex3d( x - width, altitude, z - height);
				      gl.glEnd();
	    }
	    
	    


	  public void setAspect(double aspect) {
	        myAspect = aspect;
	    
	       
	  }
	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}    
	    

}
