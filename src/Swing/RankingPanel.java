package Swing;

import Base.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private JButton exit;
    private ArrayList<Player> players;

    public RankingPanel(ArrayList<Player> players){
        this.players = players;
        model = new DefaultTableModel();
        table = new JTable(model);
        exit = new JButton("Exit Game");
        Collections.sort(players, Comparator.comparing(c -> c.score));
        setColumns();
        setRows();
        table.setFont(new Font(Font.SANS_SERIF,Font.CENTER_BASELINE,20));
        table.setRowHeight(50);
//        table.setSize(this.getSize());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setCellSelectionEnabled(false);

        exit.addActionListener(actionEvent -> System.exit(0));
        add(table);
        add(new JScrollPane(table));
        add(exit);
        setBackground(Color.white);
    }

    private void setRows() {
        for(int i = 0; i<players.size();i++){
            Player p = players.get(i);
            model.addRow(new Object[]{i, p.name, p.score, p.timePlayed});
        }
    }

    private void setColumns() {
        model.addColumn("#");
        model.addColumn("Name");
        model.addColumn("Score");
        model.addColumn("Time");
    }
}
