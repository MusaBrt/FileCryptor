package me.koply.filecryptor;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        new App().start();
    }

    static {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        String os = System.getProperty("os.name").toLowerCase();
        boolean iswin = os.contains("win");
        X = iswin ? 470 : 465;
        Y = iswin ? 165 : 170;
    }

    public static int X;
    public static int Y;

    public void start() {
        JFrame.setDefaultLookAndFeelDecorated( true );
        JDialog.setDefaultLookAndFeelDecorated( true );
        FlatDarkPurpleIJTheme.install();

        KriFrame frame = new KriFrame();
        EventHandler handler = new EventHandler(frame);
        GuiManager manager = new GuiManager(frame,handler);

        SwingUtilities.invokeLater(() -> {
            frame.setTitle("File Cryptor");
            frame.setLayout(null);
            frame.setVisible(true);
            manager.initializeComponents();
        });
    }

}