package Bouldering_app.domain;// https://medium.com/@aadimator/how-to-set-up-opencv-in-intellij-idea-6eb103c1d45c

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//for mouse usage

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//for nearest path to edge
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class editImage extends JFrame {

    //variables for image processing
    private BufferedImage image;
    private Point clickedPoint; //OpenCV point
    private Point updatedPoint;

    private String filePath;
    private JLabel imageLabel;

    //for OpenCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public editImage(Path file) {
        setTitle("Bouldering App");

        filePath = String.valueOf(file);
        // Load the image
        image = loadImage(filePath);

        // Create a label to display the image
        imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH)));

        // Add mouse click listener to the image label
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the coordinates of the clicked point
                java.awt.Point awtPoint = e.getPoint();
                image = loadImage(filePath);

                clickedPoint = new Point(awtPoint.x, awtPoint.y);
             //   System.out.println("Clicked point: " + clickedPoint);

                // Perform Canny edge detection and determine margin around the detected object
                updatedPoint = calculateMargin(clickedPoint);
               // System.out.println("Updated point: " + updatedPoint);

                // Update the clicked point with the margin
//                clickedPoint = updatedPoint;

                // Repaint the image label to draw the marker
                drawArrow();

                image = loadImage(filePath);

                // Update the ImageIcon of the JLabel
                ImageIcon updatedIcon = new ImageIcon(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH));
                imageLabel.setIcon(updatedIcon);

                // Repaint the JLabel to reflect the changes
                imageLabel.repaint();

            }
        });
        // Add the label to the content pane
        getContentPane().add(imageLabel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the window
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
    private void drawArrow(){
        // Create a new BufferedImage to draw the image with the arrow
        BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = combined.createGraphics();
        g1.drawImage(image,0,0,null);
        var g = getGraphics();
        // Draw the arrow pointing from the original clicked point to the updated cursor near the edge
        if (clickedPoint != null) {
            g1.setStroke(new BasicStroke(3));
            // Define the size of the arrow
            int arrowLength = 30; // Length of the arrow
            int arrowHeadSize = 15; // Size of the arrowhead

// Calculate the angle between the original clicked point and the updated cursor
            double deltaX = updatedPoint.x - clickedPoint.x;
            double deltaY = updatedPoint.y - clickedPoint.y;
            double angle = Math.atan2(deltaY, deltaX);

// Calculate the coordinates of the arrowhead
            int arrowHeadX = (int) (updatedPoint.x + arrowLength * Math.cos(angle));
            int arrowHeadY = (int) (updatedPoint.y + arrowLength * Math.sin(angle));

// Calculate the coordinates of the lines extending from the arrowhead
            double angleLeft = angle + Math.PI / 4; // 45-degree angle to the left
            double angleRight = angle - Math.PI / 4; // 45-degree angle to the right
            int lineLength = arrowHeadSize ; // Length of the lines
            int lineX1 = (int) (updatedPoint.x + lineLength * Math.cos(angleRight)); // Extend to the right
            int lineY1 = (int) (updatedPoint.y + lineLength * Math.sin(angleRight)); // Extend to the right
            int lineX2 = (int) (updatedPoint.x + lineLength * Math.cos(angleLeft)); // Extend to the left
            int lineY2 = (int) (updatedPoint.y + lineLength * Math.sin(angleLeft)); // Extend to the left

// Draw the lines from the arrowhead
            g1.setColor(Color.CYAN);
            g1.drawLine((int)updatedPoint.x, (int)updatedPoint.y, lineX1, lineY1);
            g1.drawLine((int)updatedPoint.x, (int)updatedPoint.y, lineX2, lineY2);

// Draw the line connecting the arrowhead to the center
            g1.drawLine((int)updatedPoint.x, (int)updatedPoint.y, arrowHeadX, arrowHeadY);
        }
        g1.dispose();

        try {

            ImageIO.write(combined, "PNG", new File(filePath));
//            System.out.println("Image with arrow saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving image with arrow: " + e.getMessage());
        }

    }
    private BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Point calculateMargin(Point clickedPoint) {
        // Convert BufferedImage to Mat
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                mat.put(y, x, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF);
            }
        }

        // Convert clickedPoint to Mat
        Mat pointMat = new Mat(1, 1, CvType.CV_32SC2);
        pointMat.put(0, 0, clickedPoint.x, clickedPoint.y);

        // Perform Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(mat, edges, 100, 200);

        // Find nearest edge to the clicked point
        double[] nearestEdge = findNearestEdge(edges, clickedPoint);

        // If nearest edge found, adjust clicked point to the edge
        if (nearestEdge != null) {
            return new Point((int) nearestEdge[0], (int) nearestEdge[1]);
        } else {
            // If no edge found, return original point
            return clickedPoint;
        }
    }

    private double[] findNearestEdge(Mat edges, Point clickedPoint) {
        int maxIterations = 240;

        // Define the directions: up, down, left, right
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        // Initialize a queue for BFS
        Queue<Point> queue = new LinkedList<>();
        queue.add(clickedPoint);

        // Keep track of visited points
        boolean[][] visited = new boolean[edges.rows()][edges.cols()];
        visited[(int)clickedPoint.y][(int)clickedPoint.x] = true;

        // Perform BFS
        int iteration = 0;
        while (!queue.isEmpty() && iteration < maxIterations) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Point current = queue.poll();
                for (int j = 0; j < 4; j++) {
                    int nx = (int)current.x + dx[j];
                    int ny = (int)current.y + dy[j];
                    if (nx >= 0 && nx < edges.cols() && ny >= 0 && ny < edges.rows() && !visited[ny][nx]) {
                        double[] value = edges.get(ny, nx);
                        if (value[0] == 255) {
                            // Nearest edge found, return its coordinates
                            return new double[]{nx, ny};
                        }
                        queue.add(new Point(nx, ny));
                        visited[ny][nx] = true;
                    }
                }
            }
            iteration++;
        }

        // No edge found within the maximum iterations
        return null;
    }

}