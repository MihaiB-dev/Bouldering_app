package Bouldering_app.domain;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ImageViewer extends JFrame implements WindowListener {

    //variables for image processing
    private BufferedImage image;
    private JLabel imageLabel;

    public ImageViewer(Path path){
        setTitle("Bouldering App");

        // Add WindowListener to handle window-closing event
        addWindowListener(this);

       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Load the image
        image = loadImage(path);
        // Create a label to display the image
        imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH)));
        // Add the label to the content pane
        getContentPane().add(imageLabel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the window


    }

    private BufferedImage loadImage(Path filePath) {
        try {
            return ImageIO.read(new File(String.valueOf(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void windowClosing(WindowEvent e) {
        // Handle window closing event (e.g., set a flag or signal the main thread)
//        System.exit(0); // Example: Terminate the program
    }

    // Implement other WindowListener methods (optional)
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}


}