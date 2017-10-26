import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ActualParametersDialog extends JDialog {
    GuiDisplayer displayer;
    private JTable table;
    private JScrollPane scrollPane;
    private TableModel dataModel;
    private final String[] headline = {"Parametr", "Wartoœæ", "Jednostka"};
    private  String[][] data = {{"Prêdkoœæ obrotowa", "1920", "rpm"}, {"Obi¹¿enie silnika","35","%"}};
    private int rows;
    private int columns;

    public ActualParametersDialog(JFrame frame) {
        super(frame);
        displayer = (GuiDisplayer) frame;
        try {
            initParameters();
        } catch (InterruptedException e) {}

    }

    private void initParameters() throws InterruptedException {
        setSize(300, 500);
        table = new JTable(data, headline);
        table.setSize(300, 500);
        table.setModel(new DefaultTableModel(data,headline));
        
        scrollPane = new JScrollPane(table);
        add(scrollPane);
        table.getModel().setValueAt("blebleble", 1, 1);
        
        setVisible(true);
    }
    private void testWithMap() {
        Map map = new HashMap<String, String>();
    }

    

}
