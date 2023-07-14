package AppJava;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;

/**
 * The AddStudentWindow class represents a window for adding a new student to the database.
 * It allows the user to enter the student's name, surname, phone number, and class.
 */
public class AddStudentWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldSurname;
	private JTextField textFieldPhone;
	private JTextField textFieldClass;
	private DbConn dbConn = new DbConn();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddStudentWindow frame = new AddStudentWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddStudentWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		contentPane.add(lblErrorText);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			/**
			 * Adding a new student to the database
			 * @param e focus on click
			 */
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
						
						Connection con =  dbConn.Connect();
						PreparedStatement pst;
						lblErrorText.setText("");
						pst = con.prepareStatement("INSERT INTO `Students` (`id`, `name`, `surname`, `phone`, `id_class`) VALUES (NULL, '"+name+"', '"+surname+"', '"+textFieldPhone.getText()+"', '"+textFieldClass.getText()+"')");
						pst.execute();
						dispose();
						EditStudentsWindow editStudentsWindow = new EditStudentsWindow();
						editStudentsWindow.setVisible(true);
					}
					else 
					{
						lblErrorText.setText("Nieprawidłowe dane!");
					}
					
					
				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnCreate.setFont(new Font("Arial", Font.PLAIN, 20));
		btnCreate.setBounds(131, 416, 140, 49);
		contentPane.add(btnCreate);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			/**
			 * back to previous window
			 * @param e focus on click
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
				EditStudentsWindow editStudentsWindow;
				try {
					editStudentsWindow = new EditStudentsWindow();
					editStudentsWindow.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 20));
		btnBack.setBounds(25, 416, 94, 49);
		contentPane.add(btnBack);
		
	}
}
