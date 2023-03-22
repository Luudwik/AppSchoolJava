package AppJava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;

public class LoginUI {

	
	JFrame Frame_log;
	private JTextField tx_login;
	private JPasswordField tx_password;

	/**
	 * Launch the application.
	 */
	public static void loginUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.Frame_log.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Frame_log = new JFrame();
		Frame_log.getContentPane().setSize(new Dimension(60, 60));
		Frame_log.getContentPane().setBackground(new Color(224, 255, 255));
		Frame_log.getContentPane().setLayout(null);
		Frame_log.setResizable(false);
		
		Image img_Log = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-log.png")).getImage();
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(180, 197, 375, 82);
		Frame_log.getContentPane().add(panel);
		panel.setLayout(null);
		
		tx_login = new JTextField();
		tx_login.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tx_login.setToolTipText("Podaj login: minimalnie 6 znaków, pierwsza litera z dużej");
		tx_login.setBounds(10, 11, 285, 60);
		panel.add(tx_login);
		tx_login.setColumns(10);
		
		JLabel lb_login_img = new JLabel("");
		lb_login_img.setBounds(305, 11, 60, 60);
		Image img_Login = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-user2.png")).getImage();
		lb_login_img.setIcon(new ImageIcon(img_Login));
		panel.add(lb_login_img);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(180, 342, 375, 82);
		panel_1.setLayout(null);
		Frame_log.getContentPane().add(panel_1);
		
		tx_password = new JPasswordField();
		tx_password.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tx_password.setToolTipText("Podaj hasło: minimalnie 8 znaków");
		tx_password.setBounds(10, 11, 285, 60);
		panel_1.add(tx_password);
		
		JLabel lb_password_img = new JLabel("");
		lb_password_img.setBounds(305, 11, 60, 60);
		Image img_Password = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-pass.png")).getImage();
		lb_password_img.setIcon(new ImageIcon(img_Password));
		panel_1.add(lb_password_img);
		
		JButton btn_logIn = new JButton("Zaloguj!");
		btn_logIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String login = tx_login.getText();
				@SuppressWarnings("deprecation")
				String password = tx_password.getText();
				
				try
				{					
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://student.cs.uni.opole.pl/130594","130594@student.cs.uni.opole.pl", "130594");
					
					//checking the correctness of the login and password
					PreparedStatement pst = con.prepareStatement("SELECT login FROM Teachers WHERE BINARY login = ? AND BINARY password = ?");
					pst.setString(1, login);
					pst.setString(2, password);
					ResultSet rs = pst.executeQuery();					
					
					if(rs.next())
					{
						Frame_log.dispose();
						MainWindow mainWindow = new MainWindow();	
						mainWindow.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Błędne logowanie.");
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
		
		
		
		btn_logIn.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_logIn.setBounds(10, 492, 280, 58);
		Frame_log.getContentPane().add(btn_logIn);
		
		JButton btn_rej = new JButton("Zarejestruj się");
		btn_rej.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame_log.dispose();
				RegistrationUI registrationUI = new RegistrationUI();				
				registrationUI.Frame_rej.setVisible(true);
			}
		});
		btn_rej.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_rej.setBounds(294, 492, 280, 58);
		Frame_log.getContentPane().add(btn_rej);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 0, 584, 138);
		Frame_log.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lbLoginIcon = new JLabel("");
		lbLoginIcon.setBounds(416, 5, 128, 128);
		panel_2.add(lbLoginIcon);
		lbLoginIcon.setMaximumSize(new Dimension(60, 60));
		lbLoginIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lbLoginIcon.setIcon(new ImageIcon(img_Log));
		
		JLabel lbLoginPanel = new JLabel("Panel logowania");
		lbLoginPanel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lbLoginPanel.setHorizontalAlignment(SwingConstants.CENTER);
		lbLoginPanel.setBounds(10, 11, 378, 116);
		panel_2.add(lbLoginPanel);
		
		JLabel lb_login = new JLabel("Login:");
		lb_login.setFont(new Font("Tahoma", Font.BOLD, 30));
		lb_login.setHorizontalAlignment(SwingConstants.CENTER);
		lb_login.setBounds(25, 197, 130, 82);
		Frame_log.getContentPane().add(lb_login);
		
		JLabel lb_pass = new JLabel("Hasło:");
		lb_pass.setHorizontalAlignment(SwingConstants.CENTER);
		lb_pass.setFont(new Font("Tahoma", Font.BOLD, 30));
		lb_pass.setBounds(25, 342, 130, 82);
		Frame_log.getContentPane().add(lb_pass);
		Frame_log.setBounds(250, 250, 600, 600);
		Frame_log.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
