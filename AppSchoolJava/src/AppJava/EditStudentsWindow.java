package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.mysql.cj.log.Log;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JLabel;
import java.awt.FlowLayout;

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
	 * Launch the application.
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
	
	public static void setModernTableStyle(JTable table) { 
	    // Ustawienie czcionki i rozmiaru tekstu w tabeli 
	    table.setFont(new Font("Roboto", Font.PLAIN, 40)); 
 
	    // Ustawienie koloru tła tabeli i komórek 
	    table.setBackground(new Color(255, 255, 255)); 
	    table.setForeground(new Color(51, 51, 51)); 
 
	    // Ustawienie koloru linii siatki międzykomórkowej 
	    table.setGridColor(new Color(224, 224, 224)); 
 
	    // Ustawienie koloru nagłówków kolumn 
	    JTableHeader header = table.getTableHeader(); 
	    header.setFont(new Font("Roboto", Font.BOLD, 14)); 
	    header.setForeground(new Color(255, 255, 255)); 
	    header.setBackground(new Color(30, 149, 204)); 
	    Dimension headerSize = header.getPreferredSize(); 
	    headerSize.height = 40; 
	    header.setPreferredSize(headerSize); 
 
	    // Ustawienie odstępów między wierszami i wysokości wierszy 
	    table.setRowHeight(30); 
	    table.setIntercellSpacing(new Dimension(0, 0)); 
 
	    // Ustawienie marginesów wewnętrznych komórek 
	    TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer(); 
	    JLabel headerLabel = (JLabel) rendererFromHeader; 
	    headerLabel.setHorizontalAlignment(JLabel.CENTER); 
	    headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
 
	    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(); 
	    renderer.setHorizontalAlignment(JLabel.CENTER); 
	    renderer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
	    table.setDefaultRenderer(Object.class, renderer); 
 
	    // Zmiana koloru wiersza po najechaniu myszką 
	    table.addMouseListener(new java.awt.event.MouseAdapter() { 
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
	
	public class MyButton extends JButton { 
		 
	    public MyButton(String label) { 
	        super(label); 
	        setOpaque(true); 
	        setForeground(Color.WHITE); 
	        setBackground(new Color(51, 153, 255)); // niebieskie tło 
 
	        // zaokrąglenie rogów 
	        setBorderPainted(false); 
	        setFocusPainted(false); 
	        setMargin(new Insets(10, 20, 10, 20)); 
	        setPreferredSize(new Dimension(200, 50)); 
 
	        // dodanie cienia 
	        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
	        setBorder(BorderFactory.createCompoundBorder(getBorder(), 
	                BorderFactory.createEtchedBorder())); 
 
	        // zmiana czcionki i stylu 
	        setFont(new Font("Arial", Font.BOLD, 16)); 
 
	        // zmiana koloru na najazdzie myszy 
	        addMouseListener(new java.awt.event.MouseAdapter() { 
	            public void mouseEntered(java.awt.event.MouseEvent evt) { 
	                setBackground(new Color(0, 119, 204)); 
	            } 
	            public void mouseExited(java.awt.event.MouseEvent evt) { 
	            	setBackground(new Color(51, 153, 255)); 
	            } 
	        }); 
	    } 
	} 
	
	public void ReadDataFromDatabase()
	{
		try {
			
			if (tableModel.getRowCount() > 0) {
			    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
			    	tableModel.removeRow(i);
			    }
			}
			
			Connection con =  dbConn.Connect();
			PreparedStatement pst;
			ResultSet rs;
			pst = con.prepareStatement("SELECT * FROM Students");
			rs = pst.executeQuery();
			while(rs.next())
			{
				tableModel.insertRow(0, new Object[] {rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getInt("phone"), rs.getInt("id_class"),});

			}
			if(table != null) {
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
		// Ustawienie zachowania okna na pełny ekran  
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
		

		
		ReadDataFromDatabase();
		contentPaneCenterTable.add(scrollPane);
		contentPaneCenter.add(contentPaneCenterTable, BorderLayout.CENTER);
		//contentPane.add(scrollPane);
		table.setBounds(100, 100, 1200, 500);
		table.setFont(new Font("SansSerif", Font.BOLD, 10));
		table.setOpaque(false);
		

		
		
		
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
		//btnAdd.setBackground(new Color(77, 255, 77)); 
		contentPaneCenterButtons.add(btnAdd);
		
		MyButton btnUpdate = new MyButton("Edit"); 
		btnUpdate.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if(table.getSelectedRow() == -1) { 
					JOptionPane.showMessageDialog(btnUpdate, "Żaden rekord do edytowania nie jest zaznaczony"); 
				} 
				else { 
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
						// TODO Auto-generated catch block 
						e1.printStackTrace(); 
					} 
					 
				} 
			} 
		}); 
		btnUpdate.setFont(new Font("Arial", Font.PLAIN, 18)); 
		//btnUpdate.setBackground(new Color(252, 173, 3)); 
		contentPaneCenterButtons.add(btnUpdate);
		
		
		
		MyButton btnDelete = new MyButton("Delete"); 
		btnDelete.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if(table.getSelectedRow() == -1) { 
					JOptionPane.showMessageDialog(btnDelete, "Żaden rekord do usunięcia nie jest zaznaczony"); 
				} 
				else { 
					 
					String sqlID = tableModel.getValueAt(table.getSelectedRow(), 0).toString(); 
					 
						try {  
							Connection con = dbConn.Connect(); 
							PreparedStatement pst;
							pst = con.prepareStatement("DELETE FROM `Students` WHERE `Students`.`id` = "+sqlID); 
							pst.execute(); 
							 
						} catch (Exception e1) { 
							// TODO Auto-generated catch block 
							e1.printStackTrace(); 
						} 
					ReadDataFromDatabase(); 
				} 
				 
				 
			} 
		}); 
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 18)); 
		//btnDelete.setBackground(new Color(255, 61, 46));;
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
					// TODO Auto-generated catch block 
					e1.printStackTrace(); 
				} 
				 
			} 
		}); 
		btnBack.setFont(new Font("Arial", Font.PLAIN, 18)); 
		//btnBack.setBackground(new Color(77, 255, 77)); 
		btnBack.setBounds(400, 611, 400, 52); 
		contentPaneCenterButtons.add(btnBack);
		
		JLabel image = new JLabel("");
		image.setHorizontalAlignment(SwingConstants.LEFT);
		Image originalIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/students.png")).getImage();
		contentPaneNorth.setLayout(new BorderLayout(0, 0));
		image.setIcon(new ImageIcon(originalIcon));
		image.setBorder(new EmptyBorder(0, 0, 0, 30));
		contentPaneNorth.add(image, BorderLayout.WEST);
		
		JLabel labelNorth = new JLabel("ZARZĄZDANIE UCZNIAMI");
		labelNorth.setForeground(Color.white);
		labelNorth.setFont(new Font("Arial", Font.BOLD, 25));
		contentPaneNorth.add(labelNorth);
		
	}
}
