package mazegame.client.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Container;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Color;

import mazegame.client.Client;
import mazegame.server.ServerSpec;
import mazegame.server.Server;
import mazegame.server.ClientView;
import mazegame.server.Icon;
import mazegame.server.Direction;

public class Gui extends JFrame implements Client, ActionListener {

    private Server server;
    private JPanel controls;
    private JPanel map;
    private JButton goNorth;
    private JButton goSouth;
    private JButton goEast;
    private JButton goWest;

    private static final Dimension TILE_DIM = new Dimension(10, 10);

    public Gui(ServerSpec serverSpec) {
        super("MazeGame");
        server = new Server(serverSpec);
        addControls();
        addMap();
    }

    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    private void createAndShowGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ClientView view = server.getClientView();
        updateMap(view.getTopView());
        triggerGameOver(view.isGameOver());
        pack();
        setVisible(true);
    }

    private void addControls() {
        goNorth = new JButton("N");
        goSouth = new JButton("S");
        goEast = new JButton("E");
        goWest = new JButton("W");

        goNorth.addActionListener(this);
        goSouth.addActionListener(this);
        goEast.addActionListener(this);
        goWest.addActionListener(this);

        JPanel controls = new JPanel(new GridLayout(3, 3));
        controls.add(new JLabel());
        controls.add(goNorth);
        controls.add(new JLabel());
        controls.add(goWest);
        controls.add(new JLabel());
        controls.add(goEast);
        controls.add(new JLabel());
        controls.add(goSouth);
        controls.add(new JLabel());
        add(controls, BorderLayout.SOUTH);
    }

    // adds an initial empty map
    // it will get update later using
    // updateMap().
    private void addMap() {
        map = new JPanel();
        add(map, BorderLayout.CENTER);
    }

    private void updateMap(Icon[][] icons) {
        int numRows = icons.length;
        int numCols = icons[0].length;
        map.removeAll();
        map.setLayout(new GridLayout(numRows+2, numCols+2));
        // north wall
        for (int c=0; c<numCols+2; c++) {
            map.add(newLabel(Icon.WALL));
        }
        for (int r=0; r<numRows; r++) {
            // east wall
            map.add(newLabel(Icon.WALL));
            // map contents
            for (int c=0; c<numCols; c++) {
                map.add(newLabel(icons[r][c]));
            }
            // west wall
            map.add(newLabel(Icon.WALL));
        }
        // south wall
        for (int c=0; c<numCols+2; c++) {
            map.add(newLabel(Icon.WALL));
        }
        map.validate();
        map.repaint();
    }

    private static JLabel newLabel(Icon icon) {
        JLabel label = new JLabel();
        label.setMinimumSize(TILE_DIM);
        label.setPreferredSize(TILE_DIM);
        label.setMaximumSize(TILE_DIM);
        label.setOpaque(true);
        switch (icon) {
            case EMPTY:
                label.setBackground(Color.GRAY);
                break;
            case WALL:
                label.setBackground(Color.BLACK);
                break;
            case END:
                label.setBackground(Color.GREEN);
                break;
            case HERO:
                label.setBackground(Color.WHITE);
                break;
            case FOOTPRINT:
                label.setBackground(Color.LIGHT_GRAY);
                break;
            default:
                throw new IllegalArgumentException(icon.toString());
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (! (o instanceof JButton)) {
            throw new IllegalArgumentException(
                    "e source was not a JButton");
        }
        JButton b = (JButton) o;
        if (b == goNorth) {
            server.moveHero(Direction.NORTH);
        } else if (b == goSouth) {
            server.moveHero(Direction.SOUTH);
        } else if (b == goEast) {
            server.moveHero(Direction.EAST);
        } else if (b == goWest) {
            server.moveHero(Direction.WEST);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported button: " + b.getText());
        }
        ClientView view = server.getClientView();
        updateMap(view.getTopView());
        triggerGameOver(view.isGameOver());
    }

    private void triggerGameOver(boolean gameOver) {
        if (! gameOver) {
            return;
        }
        System.out.println("Game Over");
        dispatchEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING));
    }
}