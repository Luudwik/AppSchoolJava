package AppJava;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//MainWindow frame = new MainWindow();
					//frame.setVisible(true);
					LoginUI loginUI = new LoginUI();
					loginUI.Frame_log.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public MainWindow() throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(1200,700);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JButton btnManageStudents = new JButton("Zarządzanie uczniami");
		btnManageStudents.setBounds(10, 10, 160, 21);
		btnManageStudents.addActionListener(new ActionListener() {
			/**
			 * Open students management window
			 * @param e focus on click
			 */
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
		contentPane.setLayout(null);
		contentPane.add(btnManageStudents);
		
		JButton btnManageAttendance = new JButton("Zarządzanie frekwencją");
		btnManageAttendance.setBounds(244, 10, 197, 21);
		contentPane.add(btnManageAttendance);
		
		JButton btnManageGrades = new JButton("Zarządzanie ocenami");
		btnManageGrades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				GradesWindow gradesWindow = new GradesWindow();
				gradesWindow.setVisible(true);
			}
		});
		btnManageGrades.setBounds(526, 10, 179, 21);
		contentPane.add(btnManageGrades);
		
		JButton btnCreateTest = new JButton("Utwórz test");
		btnCreateTest.setBounds(807, 10, 146, 21);
		contentPane.add(btnCreateTest);
	}
}
