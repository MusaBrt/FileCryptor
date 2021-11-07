package me.koply.filecryptor;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class GuiManager {

    private final KriFrame frame;
    private final EventHandler handler;
    public GuiManager(KriFrame frame, EventHandler eventHandler) {
        this.frame = frame;
        handler = eventHandler;
    }

    static JTextField filePath = new JTextField();
    static JTextField privateKeyField = new JTextField();
    static JTextField publicKeyField = new JTextField();

    public void initializeComponents() {
        JLabel path = new JLabel("Path:");
        path.setBounds(5, 10, 50, 30);

        filePath.setBounds(50, 5, 300, 30);

        JButton browseBtn = new JButton("Browse");
        browseBtn.setBounds(360, 5, 100, 30);
        browseBtn.addActionListener(handler::browseButton);
        // 45 ----- first line end

        JLabel privateLabel = new JLabel("Private:");
        privateLabel.setBounds(5, 45, 80, 30);

        privateKeyField.setBounds(70, 40, 100, 30);
        // 75 ------ second line end

        JLabel publicLabel = new JLabel("Public:");
        publicLabel.setBounds(5, 80, 80, 30);

        publicKeyField.setBounds(70, 75, 100, 30);
        // ------ third line end

        JButton generateBtn = new JButton("Generate"); // both of 2. and 3. lines
        generateBtn.setBounds(180, 40, 170, 65);
        generateBtn.addActionListener(handler::generateButton);

        JButton cryptBtn = new JButton("Crypt");
        cryptBtn.setBounds(360, 40, 100, 30);

        JButton decryptBtn = new JButton("Decrypt");
        decryptBtn.setBounds(360, 75, 100, 30);

        JMenu darkThemes = new JMenu("Dark Themes");
        JMenu lightThemes = new JMenu("Light Themes");
        addThemes(darkThemes, lightThemes);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(darkThemes);
        menuBar.add(lightThemes);

        frame.setJMenuBar(menuBar);

        Component[] carr = {path, filePath, privateKeyField, publicKeyField,
                            privateLabel, publicLabel, generateBtn,
                            browseBtn, cryptBtn, decryptBtn};
        _addComponents(carr);
    }

    // name - classname
    private static final FlatAllIJThemes.FlatIJLookAndFeelInfo[] THEMES = FlatAllIJThemes.INFOS;
    private void addThemes(JMenu dark, JMenu light) {
        for (FlatAllIJThemes.FlatIJLookAndFeelInfo theme : THEMES) {
            JMenuItem item = new JMenuItem(theme.getName());
            item.addActionListener((e) -> {
                String classPath = theme.getClassName();
                int lastDot = classPath.lastIndexOf(".");
                String packagePath = classPath.substring(0,lastDot);
                String className = classPath.substring(lastDot+1);

                Reflections ref = new Reflections(packagePath);
                Set<Class<? extends IntelliJTheme.ThemeLaf>> classes = ref.getSubTypesOf(IntelliJTheme.ThemeLaf.class);
                Class<? extends IntelliJTheme.ThemeLaf> selectedThemeClass = null;
                for (Class<? extends IntelliJTheme.ThemeLaf> clazz : classes) {
                    if (clazz.getSimpleName().equals(className)) {
                        selectedThemeClass = clazz;
                        break;
                    }
                }
                if (selectedThemeClass == null) {
                    System.err.println("Selected theme's class not found...");
                    return;
                }

                Method[] methods = selectedThemeClass.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals("install")) {
                        try {
                            method.invoke(null);
                            SwingUtilities.updateComponentTreeUI(frame);
                            SwingUtilities.updateComponentTreeUI(EventHandler.fileChooser);
                        } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                            illegalAccessException.printStackTrace();
                        }
                        break;
                    }
                }

                //IntelliJTheme.install(App.class.getResourceAsStream(theme.getClassName()));
            });
            if (theme.isDark()) {
                dark.add(item);
            } else {
                light.add(item);
            }
        }
    }

    private void _addComponents(Component...o) {
        for (Component ox : o) {
            frame.add(ox);
            //ox.setFont(font);
            ox.setVisible(true);
        }
    }
}