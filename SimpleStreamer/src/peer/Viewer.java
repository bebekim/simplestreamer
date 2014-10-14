package peer;
// Viewer.java
// Responsible for rendering JFrame windows

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Viewer extends JPanel{

	private static final long serialVersionUID = 1L; // Change?
	private BufferedImage image;
	private JFrame frame;
	
	// Resolution to be rendered
	private int width;
	private int height;

    private int[] toIntArray(byte[] barr) {
            int[] result = new int[barr.length];
            for(int i=0;i<barr.length;i++)result[i]=barr[i];
            return result;
    }

    public Viewer(int width, int height, String hostname, int peer_no) {
    	
    	this.width = width;
    	this.height = height;
    	
    	image = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
    	
    	frame = new JFrame(peer_no+" - "+hostname);
    	frame.setVisible(true);
    	frame.setSize(width, height);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.add(this);
    }
    
    // Decompressed image goes here
    public void ViewerInput(byte[] image_bytes){
    	WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, toIntArray(image_bytes));
		frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }
    
    // Close viewer
    public void close() {
    	System.err.println("Closing Viewer!");
    	frame.setVisible(false);
    	frame.dispose();
    }
}
