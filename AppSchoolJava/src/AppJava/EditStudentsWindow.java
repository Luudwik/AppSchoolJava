package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditStudentsWindow extends JFrame {

	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;
	String tekst = "";

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
	public void ReadDataFromDatabase()
	{
		try {
			
			if (tableModel.getRowCount() > 0) {
			    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
			    	tableModel.removeRow(i);
			    }
			}
			
			DbConn dbConn = new DbConn();
			dbConn.Connect();
			dbConn.pst = dbConn.con.prepareStatement("SELECT * FROM Students");
			dbConn.rs = dbConn.pst.executeQuery();
			while(dbConn.rs.next())
			{
				tableModel.insertRow(0, new Object[] {dbConn.rs.getInt(1), dbConn.rs.getString(2), dbConn.rs.getString(3), dbConn.rs.getInt(4), dbConn.rs.getString(5),});

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
		setBounds(100, 100, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
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
		
		scrollPane.setBounds(0, 50, 1186, 613);
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
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(btnDelete, "Żaden rekord do usunięcia nie jest zaznaczony");
				}
				else {
					
					String sqlID = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
					
						try {
							DbConn dbConn = new DbConn();
							dbConn.Connect();
							dbConn.pst = dbConn.con.prepareStatement("DELETE FROM `Students` WHERE `Students`.`id` = "+sqlID);
							dbConn.pst.execute();
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					ReadDataFromDatabase();
				}
				
				
			}
		});
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 18));
		btnDelete.setBounds(0, 0, 400, 52);
		btnDelete.setBackground(new Color(255, 61, 46));
		contentPane.add(btnDelete);
		
		JButton btnUpdate = new JButton("Edit");
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
		btnUpdate.setBounds(800, 0, 400, 52);
		btnUpdate.setBackground(new Color(252, 173, 3));
		contentPane.add(btnUpdate);
	}
}
