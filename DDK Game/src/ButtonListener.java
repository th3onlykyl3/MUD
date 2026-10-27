import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


/**
 * An ActionListener class for switching between images displayed by a label.
 * 
 * @author Tom Kelliher
 *
 */
public class ButtonListener implements ActionListener {

	private JLabel label;
	private ImageIcon[] images;
	private int currentImage;
	
	/**
	 * Constructs a ButtonListener object/ 
	 * @param label the label whose image is being changed.
	 * @param images an array of images, one of which will be displayed by the
	 * label.
	 */
	public ButtonListener(JLabel label, ImageIcon[] images) {
		this.label = label;
		this.images = images;
		currentImage = 0;
	}

	/* (non-Javadoc)
	 * Update which image is the "current image," and change the image on the label.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		currentImage = (currentImage + 1) % images.length;
		label.setIcon(images[currentImage]);
	}
}