import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main implements ActionListener {
    final JFrame frame = new JFrame("Filegrab v0.1.1");
    final JMenuBar menu = new JMenuBar();
    final JMenuItem about = new JMenuItem("About");
    final JPanel mainPanel = new JPanel();
    final JPanel inPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final JPanel outPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final JPanel runPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    final JTextField inputPath = new JTextField("/", 60);
    final JTextField outputPath = new JTextField("/", 60);
    final JButton inputButton = new JButton("...");
    final JButton outputButton = new JButton("...");
    final JButton runButton = new JButton("Run");
    final JCheckBox includeExtension = new JCheckBox("Include file extension?", false);
    final JFileChooser fc = new JFileChooser();

    private void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Add Components to this panel.
        inPanel.add(inputPath);
        inPanel.add(inputButton);

        outPanel.add(outputPath);
        outPanel.add(outputButton);

        runPanel.add(includeExtension);
        runPanel.add(runButton);

        // Make label panels
        final JLabel inputLabel = new JLabel("Input directory path");
        final JPanel inputLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputLabelPanel.add(inputLabel);

        // final var outputLabel = new JLabel("Output path");
        // final var outputLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // outputLabelPanel.add(outputLabel);

        // Display the window.
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputLabelPanel);
        mainPanel.add(inPanel);
        // mainPanel.add(outputLabelPanel);
        // mainPanel.add(outPanel);
        mainPanel.add(runPanel);

        // Add action listeners
        inputButton.addActionListener(this);
        outputButton.addActionListener(this);
        runButton.addActionListener(this);
        about.addActionListener(this);

        frame.add(mainPanel);

        about.setMaximumSize(about.getPreferredSize());
        menu.add(about);
        frame.setJMenuBar(menu);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == about) {
            JOptionPane.showMessageDialog(null,
                    "Filegrab v0.1.1\n\n" +
                            "Scraping files like it's 1997!\n\n" +
                            "Copyright Andrew Rowe 2024 \"All rights reserved\"\n" +
                            "This software is provided for free with no warranty.\n\n" +
                            "For the Glory of God!");
            return;
        }

        if (e.getSource() == runButton) {
            final String entries = run(inputPath.getText(), includeExtension.isSelected());
            StringSelection stringSelection = new StringSelection(entries);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(null, "Entries copied to your clipboard!");
        } else {
            int option = fc.showOpenDialog(frame);

            if (option == JFileChooser.APPROVE_OPTION) {
                if (e.getSource() == inputButton) {
                    inputPath.setText(fc.getSelectedFile().getAbsolutePath());
                } else if (e.getSource() == outputButton) {
                    outputPath.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        }
    }

    private static String run(final String inputPath, boolean includeFileExtension) {
        final File[] files = new File(inputPath).listFiles();
        final StringBuilder builder = new StringBuilder();

        for (File f : files) {
            String filename = includeFileExtension ? f.getName() : removeFileName(f.getName());
            builder.append(filename + System.lineSeparator());
        }

        return builder.toString();
    }

    private static String removeFileName(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}