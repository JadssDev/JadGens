package ml.jadss.jadgens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
//import java.util.Random;

public class Main implements ActionListener {

    //This is for people who execute the jar! we want to say that it is indeed a plugin!

    JFrame frame;
    JPanel panel;
    JLabel label;
    JLabel label2;
    JLabel label3;
    JButton button;

    public Main() {
        //Setup panels.
        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel("This file is not meant to be started");
        label2 = new JLabel("this is a minecraft plugin,");
        label3 = new JLabel("please refer to your spigot manual.");
        button = new JButton("Spigot page (click)");

        //setup sh**
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(label, BorderLayout.PAGE_START, 0);
        panel.add(label2, BorderLayout.PAGE_START, 1);
        panel.add(label3, BorderLayout.PAGE_START, 2);

        //setup frames.
        frame.add(panel, BorderLayout.CENTER);
        frame.add(button, BorderLayout.PAGE_END);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("JadGens");
        frame.pack();
        frame.setResizable(false);

        //Setup click event.
        button.addActionListener(this);

        //Show it Right now.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = "https://dev.jadss.ml/jadgens";
//        if (new Random(1).nextInt(25) == 1)
//            url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
//        else
//            url = "https://dev.jadss.ml/jadgens";
        try {
            Desktop.getDesktop().browse(URI.create(url));
            frame.setVisible(false);
            System.exit(0);
        } catch (IOException ioException) {
            frame.setVisible(false);
        }
    }
}
