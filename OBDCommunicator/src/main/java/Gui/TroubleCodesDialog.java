package Gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Commands.RPMCommand;
import Core.Service;
import Utils.FactoryService;
import Utils.Response;

public class TroubleCodesDialog extends JDialog{
    
    private JFrame parent;
    private JTable table;
    private JPanel buttonPanel;
    private Service service = FactoryService.getService();
    private Map<String, String> dtcMap;
    private DefaultTableModel tableModel;
    public TroubleCodesDialog( JFrame parent ) {
        this.parent= parent;
        dtcMap = service.getDTCMap();
        initParameters();
        initDtc();
        
        

    }

    private void initParameters() {
        setPreferredSize( new Dimension( 700, 700 ) );
        setSize( 700, 700 );
        setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS ) );
        setTitle( "Kody b³êdów sterownika - nazwa do zmiany" );
        setLayout( new BorderLayout() );
        
        table = new JTable();
        
        table.setMaximumSize( new Dimension( 600,600) );
        tableModel = new MyTableModel();
        table.setModel( tableModel);
        add(new JScrollPane( table ));
        table.getColumnModel().getColumn( 0 ).setPreferredWidth( 90 );
        table.getColumnModel().getColumn( 1 ).setPreferredWidth( 510 );
        setVisible( true );
    }
    
    private void initDtc() {
        Set<String> keys = dtcMap.keySet();
        
        for(String key : keys) {
            tableModel.addRow(new Object[]{key, dtcMap.get( key )});
        }
        
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        
        
        return panel;
    }
    
    
    class MyTableModel extends DefaultTableModel{

        private static final long serialVersionUID= 1L;
        private String[] columnNames= {"Kod B³êdu", "Opis" };
        public MyTableModel() {
            
            super(new Object[]{"Kod B³êdu", "Opis" }, 0);
        }
       

    }
    

}
