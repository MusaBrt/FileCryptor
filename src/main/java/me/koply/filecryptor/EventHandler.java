package me.koply.filecryptor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Base64;

public class EventHandler {

    private final JFrame frame;
    public EventHandler(JFrame frame) {
        this.frame = frame;
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // TODO: multiple file encoding
        fileChooser.setMultiSelectionEnabled(false);
    }

    public static File selectedFile;

    public static final JFileChooser fileChooser = new JFileChooser();
    public void browseButton(ActionEvent e) { // fuck you actionevent. todo: stop swearing
        int result = fileChooser.showDialog(frame, "Select File");
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            try {
                GuiManager.filePath.setText(selectedFile.getCanonicalPath());
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(frame, "An unexpected exception occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                ioException.printStackTrace();
            }
        }
    }

    public void generateButton(ActionEvent e) {
        KeyPair pair = RSAUtils.generateRsa();
        assert pair != null;
        byte[] encodedPublic = pair.getPublic().getEncoded();
        byte[] encodedPrivate = pair.getPrivate().getEncoded();
        String publicString = Base64.getEncoder().encodeToString(encodedPublic);
        String privateString = Base64.getEncoder().encodeToString(encodedPrivate);
        GuiManager.publicKeyField.setText(publicString);
        GuiManager.privateKeyField.setText(privateString);
    }

}