package Bouldering_app.domain;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AddImageApp extends Frame implements ActionListener, WindowListener {

    private final String IMAGE_FOLDER = "/Users/Admin/Desktop/Facultate/java/Bouldering_app/src/main/java/Bouldering_app/images"; // Replace with actual path
    private final String[] ALLOWED_EXTENSIONS = {"jpg", "png", "heic"};
    public Path destinationPath;
    public AddImageApp(int ID) {
        super("Add Image");

        // Add WindowListener to handle window-closing event
        addWindowListener(this);

        // Open the file dialog directly
        FileDialog fileDialog = new FileDialog(this, "Select Route", FileDialog.LOAD);

        fileDialog.setVisible(true);

        String fileName = fileDialog.getFile();

        if (fileName != null) {
            String filePath = fileDialog.getDirectory() + File.separator + fileName;
            File imageFile = new File(filePath);

            // Extract filename and extension
            try {
                int dotIndex = fileName.lastIndexOf('.');
                String extension = "";
                if (dotIndex > 0) {
                    extension = fileName.substring(dotIndex + 1).toLowerCase();
                }

                // Copy the image file to the IMAGE_FOLDER
                Path sourcePath = imageFile.toPath();
                destinationPath = new File(IMAGE_FOLDER + File.separator + ID + "." + extension).toPath();
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image added successfully: " + destinationPath);

                // Close the experiments.AddImageApp window after adding the image
                dispose();
            } catch (IOException ex) {
                System.out.println("Error adding image: " + ex.getMessage());
            }
        } else {
            // If no file is selected, close the window
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used in this implementation
    }

    @Override
    public void windowClosing(WindowEvent e) {
        // Define actions to be taken when window is closing
        dispose(); // Close the window
        System.exit(0); // Exit the application
    }

    // Other methods of WindowListener interface (not used)
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
