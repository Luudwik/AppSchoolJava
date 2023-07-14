package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The EditStudentsWindow class represents a window for editing student records.
 * It extends the JFrame class.
 */
public class EditStudentsWindow extends JFrame {

    private JPanel contentPane;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    String tekst = "";
    
    private JPanel contentPaneNorth;
    private JPanel contentPaneCenter;
    private JPanel contentPaneCenterTable;
    private DbConn dbConn = new DbConn();

    /**
     * Launches the EditStudentsWindow application.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditStudentsWindow editStudentsWindow = new EditStudentsWindow();
                    editStudentsWindow.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Sets the modern style for the table.
     * @param table the table to set the style for
     */
    public static void setModernTableStyle(JTable table) { 
        // Set font and text size in the table 
        table.setFont(new Font("Roboto", Font.PLAIN, 40)); 

        // Set background color of the table and cells 
        table.setBackground(new Color(255, 255, 255)); 
        table.setForeground(new Color(51, 51, 51)); 

        // Set grid line color 
        table.setGridColor(new Color(224, 224, 224)); 

        // Set header column color 
        JTableHeader header = table.getTableHeader(); 
        header.setFont(new Font("Roboto", Font.BOLD, 14)); 
        header.setForeground(new Color(255, 255, 255)); 
        header.setBackground(new Color(30, 149, 204)); 
        Dimension headerSize = header.getPreferredSize(); 
        headerSize.height = 40; 
        header.setPreferredSize(headerSize); 

        // Set row height and spacing 
        table.setRowHeight(30); 
        table.setIntercellSpacing(new Dimension(0, 0)); 

        // Set cell padding 
        TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer(); 
        JLabel headerLabel = (JLabel) rendererFromHeader; 
        headerLabel.setHorizontalAlignment(JLabel.CENTER); 
        headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(); 
        renderer.setHorizontalAlignment(JLabel.CENTER); 
        renderer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        table.setDefaultRenderer(Object.class, renderer); 

        // Change row color on mouse hover 
        table.addMouseListener(new MouseAdapter() { 
            @Override 
            public void mouseEntered(MouseEvent e) { 
                int row = table.rowAtPoint(e.getPoint()); 
                if (row >= 0) { 
                    table.setSelectionBackground(new Color(150, 150, 150)); 
                    table.repaint(); 
                } 
            } 

            @Override 
            public void mouseExited(MouseEvent e) { 
                table.setSelectionBackground(table.getBackground()); 
                table.repaint(); 
            } 
        }); 
    } 
    
    /**
     * Custom button class with modern style.
     */
    public class MyButton extends JButton { 

        public MyButton(String label) { 
            super(label); 
            setOpaque(true); 
            setForeground(Color.WHITE); 
            setBackground(new Color(51, 153, 255)); // blue background

            // Rounded corners 
            setBorderPainted(false); 
            setFocusPainted(false); 
            setMargin(new Insets(10, 20, 10, 20)); 
            setPreferredSize(new Dimension(200, 50)); 

            // Add shadow 
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
            setBorder(BorderFactory.createCompoundBorder(getBorder(), 
                    BorderFactory.createEtchedBorder())); 

            // Change font and style 
            setFont(new Font("Arial", Font.BOLD, 16)); 

            // Change color on mouse hover 
            addMouseListener(new MouseAdapter() { 
                public void mouseEntered(MouseEvent evt) { 
                    setBackground(new Color(0, 119, 204)); 
                } 
                public void mouseExited(MouseEvent evt) { 
                    setBackground(new Color(51, 153, 255)); 
                } 
            }); 
        } 
    } 
    
    /**
     * Reads data from the database and populates the table.
     */
    public void readDataFromDatabase() {
        try {
            if (tableModel.getRowCount() > 0) {
                for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                    tableModel.removeRow(i);
                }
            }
            
            Connection con = dbConn.Connect();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM Students");
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                tableModel.insertRow(0, new Object[] {rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getInt("phone"), rs.getInt("id_class")});
            }
            
            if (table != null) {
                table.removeAll();
            }
            
            table = new JTable(tableModel);
            setModernTableStyle(table);
            scrollPane = new JScrollPane(table);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     * @throws SQLException 
     */
    public EditStudentsWindow() throws SQLException {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
        setSize(screenSize.width, screenSize.height);  
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(0, 0, 5, 0));
        setContentPane(contentPane);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("id");
        tableModel.addColumn("name");
        tableModel.addColumn("surname");
        tableModel.addColumn("phone");
        tableModel.addColumn("class");
        contentPane.setLayout(new BorderLayout(0, 0));
        
        contentPaneNorth = new JPanel();
        contentPaneNorth.setBackground(Color.GRAY);
        contentPaneNorth.setBorder(new EmptyBorder(10, 30, 10, 10));
        contentPane.add(contentPaneNorth, BorderLayout.NORTH);
        
        contentPaneCenter = new JPanel();
        contentPaneCenter.setBackground(Color.ORANGE);
        contentPaneCenter.setLayout(new BorderLayout(0, 0));
        contentPane.add(contentPaneCenter, BorderLayout.CENTER);
        
        contentPaneCenterTable = new JPanel();
        contentPaneCenterTable.setBorder(new EmptyBorder(5, 0, 5, 0));
        contentPaneCenterTable.setBackground(Color.GRAY);
        contentPaneCenterTable.setLayout(new BorderLayout(0, 0));
        
        readDataFromDatabase();
        contentPaneCenterTable.add(scrollPane);
        contentPaneCenter.add(contentPaneCenterTable, BorderLayout.CENTER);
        
        JPanel contentPaneCenterButtons = new JPanel();
        contentPaneCenterButtons.setBorder(new EmptyBorder(5, 0, 5, 0));
        contentPaneCenterButtons.setBackground(Color.DARK_GRAY);
        contentPaneCenter.add(contentPaneCenterButtons, BorderLayout.NORTH);
        contentPaneCenterButtons.setLayout(new GridLayout(1, 4, 0, 0));
        
        MyButton btnAdd = new MyButton("Add"); 
        btnAdd.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                AddStudentWindow addStudentWindow = new AddStudentWindow(); 
                addStudentWindow.setVisible(true); 
                dispose(); 
            } 
        }); 
        btnAdd.setFont(new Font("Arial", Font.PLAIN, 18)); 
        contentPaneCenterButtons.add(btnAdd);
        
        MyButton btnUpdate = new MyButton("Edit"); 
        btnUpdate.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                if (table.getSelectedRow() == -1) { 
                    JOptionPane.showMessageDialog(btnUpdate, "No record selected for editing"); 
                } else { 
                    UpdateStudentWindow updateStudentWindow; 
                    try { 
                        updateStudentWindow = new UpdateStudentWindow(); 
                        updateStudentWindow.textFieldName.setText(tableModel.getValueAt(table.getSelectedRow(), 1).toString()); 
                        updateStudentWindow.textFieldSurname.setText(tableModel.getValueAt(table.getSelectedRow(), 2).toString()); 
                        updateStudentWindow.textFieldPhone.setText(tableModel.getValueAt(table.getSelectedRow(), 3).toString()); 
                        updateStudentWindow.textFieldClass.setText(tableModel.getValueAt(table.getSelectedRow(), 4).toString()); 
                        updateStudentWindow.sqlId = tableModel.getValueAt(table.getSelectedRow(), 0).toString(); 
                        updateStudentWindow.setVisible(true); 
                        dispose(); 
                    } catch (SQLException e1) { 
                        e1.printStackTrace(); 
                    } 
                } 
            } 
        }); 
        btnUpdate.setFont(new Font("Arial", Font.PLAIN, 18)); 
        contentPaneCenterButtons.add(btnUpdate);
        
        MyButton btnDelete = new MyButton("Delete"); 
        btnDelete.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                if (table.getSelectedRow() == -1) { 
                    JOptionPane.showMessageDialog(btnDelete, "No record selected for deletion"); 
                } else { 
                    String sqlID = tableModel.getValueAt(table.getSelectedRow(), 0).toString(); 
                    try {  
                        Connection con = dbConn.Connect(); 
                        PreparedStatement pst = con.prepareStatement("DELETE FROM `Students` WHERE `Students`.`id` = " + sqlID); 
                        pst.execute(); 
                    } catch (Exception e1) { 
                        e1.printStackTrace(); 
                    } 
                    readDataFromDatabase(); 
                } 
            } 
        }); 
        btnDelete.setFont(new Font("Arial", Font.PLAIN, 18)); 
        contentPaneCenterButtons.add(btnDelete);
        
        MyButton btnBack = new MyButton("Back"); 
        btnBack.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                dispose(); 
                MainWindow mainWindow; 
                try { 
                    mainWindow = new MainWindow(LoginUI.id_teacher); 
                    mainWindow.setVisible(true); 
                } catch (ClassNotFoundException | SQLException e1) { 
                    e1.printStackTrace(); 
                } 
            } 
        }); 
        btnBack.setFont(new Font("Arial", Font.PLAIN, 18)); 
        btnBack.setBounds(400, 611, 400, 52); 
        contentPaneCenterButtons.add(btnBack);
        
        JLabel image = new JLabel("");
        image.setHorizontalAlignment(SwingConstants.LEFT);
        Image originalIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/students.png")).getImage();
        contentPaneNorth.setLayout(new BorderLayout(0, 0));
        image.setIcon(new ImageIcon(originalIcon));
        image.setBorder(new EmptyBorder(0, 0, 0, 30));
        contentPaneNorth.add(image, BorderLayout.WEST);
        
        JLabel labelNorth = new JLabel("STUDENT MANAGEMENT");
        labelNorth.setForeground(Color.white);
        labelNorth.setFont(new Font("Arial", Font.BOLD, 25));
        contentPaneNorth.add(labelNorth);
    }
}
