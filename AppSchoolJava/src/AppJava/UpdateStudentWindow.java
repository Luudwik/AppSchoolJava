package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UpdateStudentWindow extends JFrame {

	private JPanel contentPane;
	public JTextField textFieldName;
	public JTextField textFieldSurname;
	public JTextField textFieldPhone;
	public JTextField textFieldClass;
	public String sqlId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateStudentWindow frame = new UpdateStudentWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public UpdateStudentWindow() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//DbConn dbConn = new DbConn();
		//dbConn.pst = dbConn.con.prepareStatement("SELECT * FROM Students");
		//dbConn.rs = dbConn.pst.executeQuery();
		
		//try {
		//	dbConn.Connect();
			
		//} catch (ClassNotFoundException e1) {
		//	// TODO Auto-generated catch block
		//	e1.printStackTrace();

		//}
		
		textFieldName = new JTextField();
		textFieldName.setBounds(180, 47, 180, 49);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblName = new JLabel("name");
		lblName.setFont(new Font("Arial", Font.PLAIN, 20));
		lblName.setBounds(40, 47, 140, 49);
		contentPane.add(lblName);
		
		textFieldSurname = new JTextField();
		textFieldSurname.setColumns(10);
		textFieldSurname.setBounds(180, 128, 180, 49);
		contentPane.add(textFieldSurname);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setColumns(10);
		textFieldPhone.setBounds(180, 202, 180, 49);
		contentPane.add(textFieldPhone);
		
		textFieldClass = new JTextField();
		textFieldClass.setColumns(10);
		textFieldClass.setBounds(180, 281, 180, 49);
		contentPane.add(textFieldClass);
		
		JLabel lblSurname = new JLabel("surname");
		lblSurname.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSurname.setBounds(40, 127, 140, 49);
		contentPane.add(lblSurname);
		
		JLabel lblPhone = new JLabel("phone");
		lblPhone.setFont(new Font("Arial", Font.PLAIN, 20));
		lblPhone.setBounds(40, 201, 140, 49);
		contentPane.add(lblPhone);
		
		JLabel lblClass = new JLabel("class");
		lblClass.setFont(new Font("Arial", Font.PLAIN, 20));
		lblClass.setBounds(40, 280, 140, 49);
		contentPane.add(lblClass);
		
		JLabel lblErrorText = new JLabel("");
		lblErrorText.setFont(new Font("Arial", Font.PLAIN, 15));
		lblErrorText.setForeground(Color.RED);
		lblErrorText.setBounds(25, 504, 335, 49);
		//lblErrorText.setFont(getFont());
		contentPane.add(lblErrorText);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DbConn dbConn = new DbConn();
				try {
					
					if(textFieldName.getText().isEmpty() || textFieldSurname.getText().isEmpty() || textFieldPhone.getText().isEmpty() || textFieldClass.getText().isEmpty())
					{
						lblErrorText.setText("Nie wszystkie pola zostały wypełnione");
					}	
					
					else if(textFieldPhone.getText().matches("[0-9]+") && textFieldName.getText().matches("[a-zA-Z]+")
							 && textFieldSurname.getText().matches("[a-zA-Z]+") && textFieldClass.getText().matches("[a-zA-Z0-9]+"))
					{
						String name = textFieldName.getText();
						name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
						String surname = textFieldSurname.getText().toLowerCase();
						surname = surname.substring(0,1).toUpperCase() + surname.substring(1).toLowerCase();
						
						dbConn.Connect();
						dbConn.pst = dbConn.con.prepareStatement("UPDATE `Students` SET `name` = '"+name+"', `surname` = '"+surname+"', `phone` = '"+textFieldPhone.getText()+"', `class` = '"+textFieldClass.getText()+"' WHERE `Students`.`id` = "+sqlId);
						//dbConn.pst = dbConn.con.prepareStatement("INSERT INTO `Students` (`id`, `name`, `surname`, `phone`, `class`) VALUES (NULL, '"+textFieldName.getText()+"', '"+textFieldSurname.getText()+"', '"+textFieldPhone.getText()+"', '"+textFieldClass.getText()+"')");
						dbConn.pst.execute();
						dispose();
						EditStudentsWindow editStudentsWindow = new EditStudentsWindow();
						editStudentsWindow.setVisible(true);
					}
					else 
					{
						lblErrorText.setText("Nieprawidłowe dane!");
					}
					
				} catch (SQLException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnUpdate.setFont(new Font("Arial", Font.PLAIN, 20));
		btnUpdate.setBounds(131, 416, 140, 49);
		contentPane.add(btnUpdate);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		btnBack.setFont(new Font("Arial", Font.PLAIN, 20));
		btnBack.setBounds(30, 416, 89, 49);
		contentPane.add(btnBack);
	}
}