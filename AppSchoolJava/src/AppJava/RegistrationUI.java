package AppJava;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class RegistrationUI {

	 JFrame Frame_rej;
	 private JTextField tx_name;
	 private JTextField tx_surname;
	 private JTextField tx_login;
	 private JPasswordField tx_password;

	/**
	 * Launch the application.
	 */
	public static void RegistrationsUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationUI window = new RegistrationUI();
					window.Frame_rej.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegistrationUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Frame_rej = new JFrame();
		Frame_rej.setBounds(400, 100, 600, 650);
		Frame_rej.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame_rej.getContentPane().setSize(60, 60);
		Frame_rej.getContentPane().setBackground(new Color(224, 255, 255));
		Frame_rej.getContentPane().setLayout(null);
		Frame_rej.setResizable(false);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(240, 240, 240));
		panel_4.setBounds(0, 0, 585, 138);
		Frame_rej.getContentPane().add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lb_registrationPanel = new JLabel("Rejestracja użytkownika");
		lb_registrationPanel.setHorizontalAlignment(SwingConstants.CENTER);
		lb_registrationPanel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lb_registrationPanel.setBounds(10, 11, 443, 116);
		panel_4.add(lb_registrationPanel);
		
		JLabel lb_iconUser = new JLabel("");
		lb_iconUser.setHorizontalAlignment(SwingConstants.CENTER);
		lb_iconUser.setBounds(463, 11, 110, 110);
		Image img_Rej = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-user2.png")).getImage();
		lb_iconUser.setIcon(new ImageIcon(img_Rej));
		panel_4.add(lb_iconUser);
		
		JLabel lb_name = new JLabel("Imię:");
		lb_name.setHorizontalAlignment(SwingConstants.CENTER);
		lb_name.setFont(new Font("Tahoma", Font.BOLD, 30));
		lb_name.setBounds(10, 165, 200, 60);
		Frame_rej.getContentPane().add(lb_name);
		
		JLabel lb_surname = new JLabel("Nazwisko:");
		lb_surname.setHorizontalAlignment(SwingConstants.CENTER);
		lb_surname.setFont(new Font("Tahoma", Font.BOLD, 30));
		lb_surname.setBounds(10, 255, 200, 60);
		Frame_rej.getContentPane().add(lb_surname);
		
		JLabel lb_login = new JLabel("Login:");
		lb_login.setFont(new Font("Tahoma", Font.BOLD, 30));
		lb_login.setHorizontalAlignment(SwingConstants.CENTER);
		lb_login.setBounds(10, 345, 200, 60);
		Frame_rej.getContentPane().add(lb_login);
		
		JLabel lb_password = new JLabel("Hasło:");
		lb_password.setFont(new Font("Tahoma", Font.BOLD, 30));
		lb_password.setHorizontalAlignment(SwingConstants.CENTER);
		lb_password.setBounds(10, 435, 200, 60);
		Frame_rej.getContentPane().add(lb_password);
		
		tx_name = new JTextField();
		tx_name.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tx_name.setBounds(220, 165, 290, 60);
		Frame_rej.getContentPane().add(tx_name);
		tx_name.setColumns(10);
		
		tx_surname = new JTextField();
		tx_surname.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tx_surname.setColumns(10);
		tx_surname.setBounds(220, 255, 290, 60);
		Frame_rej.getContentPane().add(tx_surname);
		
		tx_login = new JTextField();
		tx_login.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tx_login.setToolTipText("Podaj login: minimalnie 6 znaków");
		tx_login.addFocusListener(new FocusAdapter() {});
		tx_login.setColumns(10);
		tx_login.setBounds(220, 345, 290, 60);
		Frame_rej.getContentPane().add(tx_login);
		
		tx_password = new JPasswordField();
		tx_password.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tx_password.setToolTipText("Podaj hasło: minimalnie 8 znaków, 1 wielka litera");
		tx_password.addFocusListener(new FocusAdapter() {});
		tx_password.setBounds(220, 435, 290, 60);
		Frame_rej.getContentPane().add(tx_password);
		
		JButton btn_register = new JButton("Zarejestruj!");
		btn_register.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String login = tx_login.getText();				
				String password = tx_password.getText();
				try
				{					
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://student.cs.uni.opole.pl/130594","130594@student.cs.uni.opole.pl", "130594");
					
					//Checking if such a login already exists
					PreparedStatement pst = con.prepareStatement("SELECT login FROM Teachers WHERE BINARY login = ?");
					pst.setString(1, login);
					ResultSet rs = pst.executeQuery();					
					
					if(rs.next())
					{
						JOptionPane.showMessageDialog(null, "Konto dla podanego loginu już isnieje.");
					}
					else
					{
						//Checking if all data is correct
						Boolean isCorrect =true;
						if((tx_login.getText().length()<=0) || (tx_password.getText().length()<=0) || (tx_name.getText().length()<=0) || (tx_surname.getText().length()<=0))
						{isCorrect=false;  JOptionPane.showMessageDialog(null, "Niepoprawna długość danych");}
						
						if(login.length()<6)
						{isCorrect=false; JOptionPane.showMessageDialog(null, "Nieprawidłowa długość loginu");}
						
						if(password.length()<8 || !password.matches(".*[A-Z].*"))
						{isCorrect=false; JOptionPane.showMessageDialog(null, "Nieprawidłowy format hasła");}
						
						//Adding data
						if(isCorrect)
						{
						PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Teachers (name, surname, login, password) VALUES (?,?,?,?)");					
						preparedStatement.setString(1, tx_name.getText());
						preparedStatement.setString(2, tx_surname.getText());
						preparedStatement.setString(3, login);
						preparedStatement.setString(4, password);
						preparedStatement.executeUpdate();
						Frame_rej.dispose();
						MainWindow mainWindow = new MainWindow();	
						mainWindow.setVisible(true);
						}
					}
				}
				catch(SQLException ex)
				{
					JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
				} 
				catch (ClassNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Nie znaleziono sterownika MySQL:\n" + e1.getMessage());
				}
				
			}
		});
		btn_register.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_register.setBounds(10, 527, 360, 73);
		Frame_rej.getContentPane().add(btn_register);
		
		JButton btn_back = new JButton("Wstecz");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame_rej.dispose();
				LoginUI loginUI = new LoginUI();
				loginUI.Frame_log.setVisible(true);
			}
		});
		btn_back.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_back.setBounds(380, 527, 194, 73);
		Frame_rej.getContentPane().add(btn_back);
	}
}
