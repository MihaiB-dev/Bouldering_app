package Bouldering_app.domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageTabViewer {

    private final JFrame frame;
    private final JTabbedPane tabbedPane;

    public ImageTabViewer(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Optional: Add a listener to close the application on window closing
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void addImageTab(String path, String tabTitle) throws IOException {
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File(path)));
        JLabel imageLabel = new JLabel(imageIcon);
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);

        tabbedPane.addTab(tabTitle, imagePanel);
    }

    public void display() {
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        ImageTabViewer viewer = new ImageTabViewer("Image Tabs");
        viewer.addImageTab("/Users/Admin/Desktop/Facultate/java/Bouldering_app/src/main/java/Bouldering_app/images/0.png", "Image 1");
        viewer.display();
    }
}