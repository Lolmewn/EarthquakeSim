package nl.lolmewn.rug.quakesensor.gui;

import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakesensor.SensorMain;
import nl.lolmewn.rug.quakesensor.net.Server;

/**
 *
 * @author Lolmewn
 */
public class MainGUI extends javax.swing.JFrame implements Runnable, Observer {
    
    private final SensorMain main;

    /**
     * Initializes the MainGUI
     *
     * @param main Main program
     */
    public MainGUI(SensorMain main) {
        this.main = main;
        this.setResizable(false);
        initComponents();
        this.yAxis.setUI(new VerticalLabelUI(false));
        this.yAxis.setText("Ground Acceleration (m/s^2)");
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        loadServersToUI();
        initQuakeDrawer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Tabs = new javax.swing.JTabbedPane();
        sensorPanel = new javax.swing.JPanel();
        packetLabel = new javax.swing.JLabel();
        packetCounter = new javax.swing.JLabel();
        quakeGraphPanel = new QuakeGraph();
        xAxis = new javax.swing.JLabel();
        yAxis = new javax.swing.JLabel();
        serversPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        serversTable = new javax.swing.JTable();
        addNewServerButton = new javax.swing.JButton();
        removeServerButton = new javax.swing.JButton();

        setSize(new java.awt.Dimension(500, 350));

        packetLabel.setText("Quake data packets sent: ");

        packetCounter.setText("0");

        xAxis.setText("time(seconds)");

        javax.swing.GroupLayout quakeGraphPanelLayout = new javax.swing.GroupLayout(quakeGraphPanel);
        quakeGraphPanel.setLayout(quakeGraphPanelLayout);
        quakeGraphPanelLayout.setHorizontalGroup(
            quakeGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quakeGraphPanelLayout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(xAxis)
                .addContainerGap(211, Short.MAX_VALUE))
        );
        quakeGraphPanelLayout.setVerticalGroup(
            quakeGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quakeGraphPanelLayout.createSequentialGroup()
                .addGap(0, 186, Short.MAX_VALUE)
                .addComponent(xAxis))
        );

        yAxis.setText("o");

        javax.swing.GroupLayout sensorPanelLayout = new javax.swing.GroupLayout(sensorPanel);
        sensorPanel.setLayout(sensorPanelLayout);
        sensorPanelLayout.setHorizontalGroup(
            sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sensorPanelLayout.createSequentialGroup()
                .addComponent(yAxis, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quakeGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sensorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(packetLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packetCounter)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sensorPanelLayout.setVerticalGroup(
            sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sensorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(packetLabel)
                    .addComponent(packetCounter))
                .addGroup(sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sensorPanelLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(yAxis)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sensorPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addComponent(quakeGraphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );

        Tabs.addTab("Sensor", sensorPanel);

        serversTable.setAutoCreateRowSorter(true);
        serversTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP", "Port", "Available"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        serversTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(serversTable);
        if (serversTable.getColumnModel().getColumnCount() > 0) {
            serversTable.getColumnModel().getColumn(0).setResizable(false);
            serversTable.getColumnModel().getColumn(1).setResizable(false);
            serversTable.getColumnModel().getColumn(2).setResizable(false);
        }

        addNewServerButton.setText("Add new");
        addNewServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewServerButtonActionPerformed(evt);
            }
        });

        removeServerButton.setText("Remove selected");

        javax.swing.GroupLayout serversPanelLayout = new javax.swing.GroupLayout(serversPanel);
        serversPanel.setLayout(serversPanelLayout);
        serversPanelLayout.setHorizontalGroup(
            serversPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serversPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addNewServerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeServerButton)
                .addContainerGap())
        );
        serversPanelLayout.setVerticalGroup(
            serversPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serversPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(serversPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewServerButton)
                    .addComponent(removeServerButton))
                .addGap(0, 53, Short.MAX_VALUE))
        );

        Tabs.addTab("Servers", serversPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addNewServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewServerButtonActionPerformed
        AddNewServerPanel panel = new AddNewServerPanel() {
            @Override
            public void save(String IP, int port) {
                addServerToUI(IP, port, "Checking...");
                Threader.runTask(() -> {
                    boolean up = main.getServerManager().checkAvailability(IP, port);
                    updateServer(IP, port, up);
                });
            }
        };
        panel.setVisible(true);
    }//GEN-LAST:event_addNewServerButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tabs;
    private javax.swing.JButton addNewServerButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel packetCounter;
    private javax.swing.JLabel packetLabel;
    private javax.swing.JPanel quakeGraphPanel;
    private javax.swing.JButton removeServerButton;
    private javax.swing.JPanel sensorPanel;
    private javax.swing.JPanel serversPanel;
    private javax.swing.JTable serversTable;
    private javax.swing.JLabel xAxis;
    private javax.swing.JLabel yAxis;
    // End of variables declaration//GEN-END:variables

    private void loadServersToUI() {
        main.getServerManager().getServers().stream().forEach((server) -> {
            addServerToUI(server.getAddress().getAddress(), server.getAddress().getPort(), server.isConnected() ? "Online" : "Offline");
            server.addObserver(this);
        });
    }
    
    private void addServerToUI(String IP, int port, String status) {
        DefaultTableModel dtm = (DefaultTableModel) serversTable.getModel();
        dtm.addRow(new Object[]{IP, port, status});
    }
    
    private void initQuakeDrawer() {
        Threader.runTask(this);
    }
    
    @Override
    public void run() {
        while (true) {
            this.quakeGraphPanel.repaint();
            try {
                Thread.sleep(1000 / SensorMain.POLLS_PER_SECOND);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void update(Observable o, Object o1) {
        if (o instanceof Server) {
            Server server = (Server) o;
            updateServer(server.getAddress().getAddress(), server.getAddress().getPort(), server.isConnected());
        }
    }
    
    public void updateServer(String IP, int port, boolean online) {
        EventQueue.invokeLater(() -> {
            // Find idx for row
            DefaultTableModel dtm = (DefaultTableModel) serversTable.getModel();
            for (int row = 0; row < dtm.getRowCount(); row++) {
                if (dtm.getValueAt(row, 0).equals(IP) && dtm.getValueAt(row, 1).equals(port)) {
                    dtm.setValueAt(online ? "Online" : "Offline", row, 2);
                }
            }
        });
    }
}
