package AppJava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	JPanel contentPane;
	private static DbConn dbConn = new DbConn();
	private static int teacherId;
	private String teacherName;
	
	public MainWindow(int teacherId) {
		MainWindow.teacherId = teacherId;
		System.out.println(teacherId);
	}

	
	public String find_teacherName()
	{
	try {
		Connection con = dbConn.Connect();
		PreparedStatement pst = con.prepareStatement(
				"SELECT name FROM Teachers WHERE id = ?");
		pst.setInt(1, teacherId);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			teacherName = rs.getString("name");
		}
		
	} catch (SQLException e) {
		JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + e.getMessage());
	} catch (ClassNotFoundException e1) {
		e1.printStackTrace();
	}		
	return teacherName;
	}
	
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
	 * @param id_teacher 
	 * @param id_teacher 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public MainWindow() throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(1200,700);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setBackground(new Color(224, 255, 255));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

			
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setBackground(new Color(240, 240, 240));
		panel.setPreferredSize(new Dimension(0, (int) (screenSize.height * 0.2)));
		JLabel lb_menu = new JLabel("Menu");
		lb_menu.setHorizontalAlignment(SwingConstants.CENTER);
		panel.setLayout(new BorderLayout());
		lb_menu.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		panel.add(lb_menu, BorderLayout.CENTER);
		
		JLabel lbl_icon_menu_1 = new JLabel();
		panel.add(lbl_icon_menu_1, BorderLayout.EAST);
		Image img_menu = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-menu.png")).getImage();
		Image scaledImg = img_menu.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
		lbl_icon_menu_1.setIcon(new ImageIcon(scaledImg));
		lbl_icon_menu_1.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
		
		JLabel lbl_icon_menu_2 = new JLabel();
		panel.add(lbl_icon_menu_2, BorderLayout.WEST);
		lbl_icon_menu_2.setIcon(new ImageIcon(scaledImg));
		lbl_icon_menu_2.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout((LayoutManager) new BoxLayout(panel_2, BoxLayout.X_AXIS));
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setBackground(new Color(224, 255, 255));
		panel_2.setBorder(BorderFactory.createEmptyBorder(0, 25, 30, 25));

		JLabel lblNewLabel = new JLabel("Witaj " + teacherName);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		lblNewLabel.setBackground(new Color(224, 255, 255));

		// Panel na lewo
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.setBackground(new Color(224, 255, 255));
		leftPanel.add(lblNewLabel);

		panel_2.add(leftPanel);

		// Panel na prawo
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setBackground(new Color(224, 255, 255));

		JLabel secondLabel = new JLabel("Select subject: ");
		secondLabel.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		rightPanel.add(secondLabel);

		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setPreferredSize(new Dimension(150, lblNewLabel.getPreferredSize().height));
		// Dodaj elementy do JComboBox
		rightPanel.add(comboBox);

		panel_2.add(rightPanel);
		
		
		
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setBackground(new Color(224, 255, 255));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 50, 50); // Tworzenie FlowLayout z odstępem między przyciskami
		panel_1.setLayout(flowLayout);
		panel_1.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
		
		JButton btnManageStudents = new JButton();
		ImageIcon img_student_btn = new ImageIcon(MainWindow.class.getClassLoader().getResource("icon/icon-student_btn.png"));
		Image img_icon_student_btn = img_student_btn.getImage();
		Image scaledImg_student_btn = img_icon_student_btn.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon_img_student_btn = new ImageIcon(scaledImg_student_btn);
		btnManageStudents.setIcon(scaledIcon_img_student_btn);
		
		btnManageStudents.addActionListener(new ActionListener() {
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
		//panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		//panel_1.add(btnManageStudents);
		
		JButton btnManageAttendance = new JButton();
		
		ImageIcon img_attendance_btn = new ImageIcon(MainWindow.class.getClassLoader().getResource("icon/icon-attendance.png"));
		Image img_icon_attendance_btn = img_attendance_btn.getImage();
		Image scaledImg_attendance_btn = img_icon_attendance_btn.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon_img_attendance_btn = new ImageIcon(scaledImg_attendance_btn);
		btnManageAttendance.setIcon(scaledIcon_img_attendance_btn);
		
		
		btnManageAttendance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AttendanceManagementWindow attendanceManagementWindow = null;
				try {
					attendanceManagementWindow = new AttendanceManagementWindow();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				attendanceManagementWindow.setVisible(true);			
			}
		});
		//panel_1.add(btnManageAttendance);
		
		JButton btnManageGrades = new JButton();
		
		ImageIcon img_manage_grades_btn = new ImageIcon(MainWindow.class.getClassLoader().getResource("icon/icon-manageGrades.png"));
		Image img_icon_manage_grades_btn = img_manage_grades_btn.getImage();
		Image scaledImg_manage_grades_btn = img_icon_manage_grades_btn.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon_img_manage_grades_btn = new ImageIcon(scaledImg_manage_grades_btn);
		btnManageGrades.setIcon(scaledIcon_img_manage_grades_btn);
		
		btnManageGrades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				GradesWindow gradesWindow = new GradesWindow();
				gradesWindow.setVisible(true);
			}
		});
		//panel_1.add(btnManageGrades);
		
		JButton btnCreateTest = new JButton();
		
		ImageIcon img_create_test_btn = new ImageIcon(MainWindow.class.getClassLoader().getResource("icon/icon-createTest.png"));
		Image img_icon_create_test_btn = img_create_test_btn.getImage();
		Image scaledImg_create_test_btn = img_icon_create_test_btn.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon_img_create_test_btn = new ImageIcon(scaledImg_create_test_btn);
		btnCreateTest.setIcon(scaledIcon_img_create_test_btn);
		
		btnCreateTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestCreatorWindow testCreatorWindow = new TestCreatorWindow();
				testCreatorWindow.setVisible(true);
				dispose();
			}
		});
		//panel_1.add(btnCreateTest);
		
		JPanel buttonManageStudentWithLabelPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		buttonManageStudentWithLabelPanel.setLayout(gridBagLayout);

		JLabel labelManageStudent = new JLabel("Manage student");
		labelManageStudent.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));

		labelManageStudent.setHorizontalAlignment(SwingConstants.CENTER);
		labelManageStudent.setVerticalAlignment(SwingConstants.CENTER);

		buttonManageStudentWithLabelPanel.add(btnManageStudents);
		buttonManageStudentWithLabelPanel.add(labelManageStudent);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weighty = 1.0;

		//buttonManageStudentWithLabelPanel.add(btnManageStudents, constraints);

		constraints.gridy = 1;
		constraints.weighty = 0.0;

		buttonManageStudentWithLabelPanel.add(labelManageStudent, constraints);

		panel_1.add(buttonManageStudentWithLabelPanel);


		JPanel buttonManageAttendanceWithLabelPanel = new JPanel();
		GridBagLayout gridBagLayout2 = new GridBagLayout();
		buttonManageAttendanceWithLabelPanel.setLayout(gridBagLayout2);

		JLabel labelManageAttendance = new JLabel("Attendance");
		labelManageAttendance.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));

		labelManageAttendance.setHorizontalAlignment(SwingConstants.CENTER);
		labelManageAttendance.setVerticalAlignment(SwingConstants.CENTER);

		buttonManageAttendanceWithLabelPanel.add(btnManageAttendance);
		buttonManageAttendanceWithLabelPanel.add(labelManageAttendance);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weighty = 1.0;

		//buttonManageAttendanceWithLabelPanel.add(btnManageAttendance, constraints);

		constraints.gridy = 1;
		constraints.weighty = 0.0;

		buttonManageAttendanceWithLabelPanel.add(labelManageAttendance, constraints);

		panel_1.add(buttonManageAttendanceWithLabelPanel);


		JPanel buttonManageGradesWithLabelPanel = new JPanel();
		GridBagLayout gridBagLayout3 = new GridBagLayout();
		buttonManageGradesWithLabelPanel.setLayout(gridBagLayout3);

		JLabel labelManageGrades = new JLabel("Manage grades");
		labelManageGrades.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));

		labelManageGrades.setHorizontalAlignment(SwingConstants.CENTER);
		labelManageGrades.setVerticalAlignment(SwingConstants.CENTER);

		buttonManageGradesWithLabelPanel.add(btnManageGrades);
		buttonManageGradesWithLabelPanel.add(labelManageGrades);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weighty = 1.0;

		//buttonManageGradesWithLabelPanel.add(btnManageGrades, constraints);

		constraints.gridy = 1;
		constraints.weighty = 0.0;

		buttonManageGradesWithLabelPanel.add(labelManageGrades, constraints);

		panel_1.add(buttonManageGradesWithLabelPanel);


		JPanel buttonCreateTestWithLabelPanel = new JPanel();
		GridBagLayout gridBagLayout4 = new GridBagLayout();
		buttonCreateTestWithLabelPanel.setLayout(gridBagLayout4);

		JLabel labelCreateTest = new JLabel("Create test");
		labelCreateTest.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));

		labelCreateTest.setHorizontalAlignment(SwingConstants.CENTER);
		labelCreateTest.setVerticalAlignment(SwingConstants.CENTER);

		buttonCreateTestWithLabelPanel.add(btnCreateTest);
		buttonCreateTestWithLabelPanel.add(labelCreateTest);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weighty = 1.0;

		//buttonCreateTestWithLabelPanel.add(btnCreateTest, constraints);

		constraints.gridy = 1;
		constraints.weighty = 0.0;

		buttonCreateTestWithLabelPanel.add(labelCreateTest, constraints);

		panel_1.add(buttonCreateTestWithLabelPanel);
		
		
		//panel_3.add(panel_2);
		//panel_3.add(panel_1);
		
		
		
		
	}
}
