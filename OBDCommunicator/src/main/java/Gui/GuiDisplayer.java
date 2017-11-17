package Gui;
import javax.swing.*;

import Core.Service;
import Utils.FactoryService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiDisplayer extends JFrame {
    JFrame frame;
    private Service service = FactoryService.getService();
    private TerminalDialog terminalDialog;
    private ActualParametersDialog actualParametersDialog;

    public GuiDisplayer() {

        super("OBD Explorer");
        frame = this;
        setSize(700, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        add(createMenuBar(), BorderLayout.PAGE_START);
    }
    

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu connectionMenu = new JMenu("Po��czenia");
        JMenu serviceMenu = new JMenu("Us�ugi");

        connectionMenu.add(createConnectMenuItem());
        connectionMenu.add(createCloseConnectionMenuItem());

        serviceMenu.add(createTerminalMenuItem());
        serviceMenu.add(createActualParametersMenuItem());
        menuBar.add(connectionMenu);
        menuBar.add(serviceMenu);

        return menuBar;
    }

    private JMenuItem createCloseConnectionMenuItem() {
        JMenuItem menuItem = new JMenuItem("Zako�cz po��czenie");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                service.closePort();

            }
        });
        return menuItem;
    }

    private JMenuItem createConnectMenuItem() {
        JMenuItem menuItem = new JMenuItem("Po��cz");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new ConnectDialog(frame);
            }
        });

        return menuItem;
    }

    private JMenuItem createTerminalMenuItem() {
        JMenuItem menuItem = new JMenuItem("Terminal");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                terminalDialog = new TerminalDialog(frame);
            }
        });

        return menuItem;
    }

    private JMenuItem createActualParametersMenuItem() {
        JMenuItem menuItem = new JMenuItem("Bierz�ce parametry");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actualParametersDialog = new ActualParametersDialog(frame);
            }
        });

        return menuItem;
    }

    public Service getService() {
        return service;
    }

    public TerminalDialog getTerminalDialog() {
        return terminalDialog;
    }

}
