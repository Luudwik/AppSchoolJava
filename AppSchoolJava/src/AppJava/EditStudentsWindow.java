package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditStudentsWindow extends JFrame {

	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;
	private String tekst = "";
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
	/**
	 * read data from database and display in table
	 */
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
				tableModel.insertRow(0, new Object[] {rs.getInt(1), rs.getString(2), 
						rs.getString(3), rs.getInt(4), rs.getString(5),});

			}
			if(table != null) {
				table.removeAll();
			}
			table = new JTable(tableModel);
			scrollPane = new JScrollPane(table);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public EditStudentsWindow() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(1200,700);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.YELLOW);
		contentPane.setBorder(new EmptyBorder(5, 0, 5, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		tableModel = new DefaultTableModel();
		tableModel.addColumn("id");
		tableModel.addColumn("name");
		tableModel.addColumn("surname");
		tableModel.addColumn("phone");
		tableModel.addColumn("class");
		
		ReadDataFromDatabase();
		
		scrollPane.setBounds(0, 50, 1186, 563);
		contentPane.add(scrollPane);
		table.setBounds(100, 100, 1200, 500);
		table.setFont(new Font("SansSerif", Font.BOLD, 10));
		table.setOpaque(false);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddStudentWindow addStudentWindow = new AddStudentWindow();
				addStudentWindow.setVisible(true);
				dispose();
			}
		});
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 18));
		btnAdd.setBounds(400, 0, 400, 52);
		btnAdd.setBackground(new Color(77, 255, 77));
		contentPane.add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			/**
			 * Delete student from database and table
			 * @param e focus on click
			 */
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Żaden rekord do usunięcia nie jest zaznaczony");
				}
				else {
					
					String sqlID = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
					
						try {
							System.out.println(table.getSelectedRow());
							Connection con =  dbConn.Connect();
							PreparedStatement pst;
							pst = con.prepareStatement("DELETE FROM `Students` WHERE `Students`.`id` = "+sqlID);
							pst.execute();
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
				}
				dispose();
				EditStudentsWindow editStudentsWindow;
				try {
					editStudentsWindow = new EditStudentsWindow();
					editStudentsWindow.setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 18));
		btnDelete.setBounds(0, 0, 400, 52);
		btnDelete.setBackground(new Color(255, 61, 46));
		contentPane.add(btnDelete);
		
		JButton btnUpdate = new JButton("Edit");
		btnUpdate.addActionListener(new ActionListener() {
			/**
			 * Open window to edit student data
			 * @param e focus on click
			 */
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Żaden rekord do edytowania nie jest zaznaczony");
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
		btnUpdate.setBounds(800, 0, 400, 52);
		btnUpdate.setBackground(new Color(252, 173, 3));
		contentPane.add(btnUpdate);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			/**
			 * back to previous window
			 * @param e focus on click
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainWindow mainWindow;
				try {
					mainWindow = new MainWindow();
					mainWindow.setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 18));
		btnBack.setBackground(new Color(77, 255, 77));
		btnBack.setBounds(400, 611, 400, 52);
		contentPane.add(btnBack);
		
	}
}
