package AppJava;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class AddGradesWindow extends JFrame {

	private JPanel jpanel_AddGradesWindow;
	private JTextField tx_mark;
	private JTextField tx_type;
	private String mark;
	private String type;
	public static String firstName;
	public static String surname;
	private DbConn dbConn = new DbConn();
	private static int id_teacher;
	private int id_subject, id_student;
	private int id_type;

	
	private LoginUI loginUI = new LoginUI();
	
	int idTeacher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddGradesWindow frame = new AddGradesWindow();
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
	
	public AddGradesWindow(int id_teacher) {
		this.id_teacher = id_teacher;
		//System.out.println(id_teacher);
	}
	
	public AddGradesWindow(String firstName, String surname )
	{
		AddGradesWindow.firstName = firstName;
		AddGradesWindow.surname = surname;
		//System.out.println(AddGradesWindow.firstName);
		//System.out.println(AddGradesWindow.surname);
	}
	
	
	
	public AddGradesWindow() {
		
		//System.out.println(id_teacher);
		this.idTeacher = id_teacher;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 680, 560);
		jpanel_AddGradesWindow = new JPanel();
		jpanel_AddGradesWindow.setBackground(new Color(240, 240, 240));
		setResizable(false);
		setLocationRelativeTo(null);
		
		setContentPane(jpanel_AddGradesWindow);
		jpanel_AddGradesWindow.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 240, 240));
		jpanel_AddGradesWindow.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lbl_icon_1 = new JLabel();
		panel.add(lbl_icon_1, BorderLayout.WEST);
		Image img_AddGrades = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-addGrade.png")).getImage();
		Image scaledImg = img_AddGrades.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		lbl_icon_1.setIcon(new ImageIcon(scaledImg));
		lbl_icon_1.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		JLabel lbl_addGrade = new JLabel("Add grade");
		lbl_addGrade.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_addGrade.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		panel.add(lbl_addGrade, BorderLayout.CENTER);
		
		JLabel lbl_icon_2 = new JLabel();
		lbl_icon_2.setIcon(new ImageIcon(scaledImg));
		lbl_icon_2.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		panel.add(lbl_icon_2, BorderLayout.EAST);		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(224, 255, 255));
		jpanel_AddGradesWindow.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(224, 255, 255));
		panel_1.add(panel_3);
		panel_3.setLayout(null);
				
		JLabel lbl_mark = new JLabel("Mark:");
		lbl_mark.setBounds(17, 35, 175, 72);
		lbl_mark.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		lbl_mark.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
		panel_3.add(lbl_mark);
		
		tx_mark = new JTextField();
		tx_mark.setBounds(190, 16, 440, 111);
		tx_mark.setBackground(new Color(224, 255, 255));
		tx_mark.setFont(lbl_mark.getFont());
		tx_mark.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		tx_mark.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		panel_3.add(tx_mark);
		tx_mark.setColumns(10);
		
		JLabel lbl_type = new JLabel("Type:");
		lbl_type.setBounds(17, 197, 175, 72);
		lbl_type.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		lbl_type.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
		panel_3.add(lbl_type);
		
		tx_type = new JTextField();
		tx_type.setBounds(190, 178, 440, 111);
		tx_type.setBackground(new Color(224, 255, 255));
		tx_type.setFont(lbl_mark.getFont());
		tx_type.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		tx_type.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		tx_type.setColumns(10);
		panel_3.add(tx_type);
		
		//pobieranie wartosci id_subject, dodać rozpoznawanie jaki to przedmiot (dwa lub więcej id)
		try {
			Connection con = dbConn.Connect();
			PreparedStatement pst = con.prepareStatement("SELECT id_subject FROM TeacherSubject WHERE id_teacher = ?");
			pst.setInt(1, AddGradesWindow.id_teacher); //teacherId
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				id_subject = rs.getInt("id_subject");
				//System.out.println("id_subject: " + id_subject);
			}
			
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null,
						"Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		
		try {
			Connection con = dbConn.Connect();
			PreparedStatement pst = con.prepareStatement("SELECT id FROM Students WHERE name = ? AND surname = ?");
			pst.setString(1, AddGradesWindow.firstName); 
			System.out.println("fn: " +AddGradesWindow.firstName);
			System.out.println("sn: " + AddGradesWindow.surname);
			pst.setString(2, AddGradesWindow.surname); 
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				id_student = rs.getInt("id");
				System.out.println("id_student: " + id_student);
			}
			
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null,
						"Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(224, 255, 255));
		jpanel_AddGradesWindow.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JButton btn_Save = new JButton("Save adding");
		btn_Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mark = tx_mark.getText();
				type = tx_mark.getText();

				try {
				    Connection con = dbConn.Connect();
				    PreparedStatement pst = con.prepareStatement("INSERT INTO GradeType (type) VALUES (?) " +
				                 "ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(); ", Statement.RETURN_GENERATED_KEYS);
				    pst.setString(1, tx_type.getText());     
				    pst.executeUpdate();
				    
				    ResultSet rs = pst.getGeneratedKeys(); 
				    if (rs.next()) { 
				    }
				    
				} catch (SQLException ex) {
				    JOptionPane.showMessageDialog(null,
				            "Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
				} catch (ClassNotFoundException e1) {
				    e1.printStackTrace();
				}
				
				try {
				    Connection con = dbConn.Connect();
				    PreparedStatement pst = con.prepareStatement("SELECT id FROM GradeType ORDER BY id DESC LIMIT 1;");     
				    ResultSet rs = pst.executeQuery(); 
				    if (rs.next()) {
				    	id_type = rs.getInt("id"); 
				    	//System.out.println("ID_TYPE: " + id_type);
				        
				    }
				    
				} catch (SQLException ex) {
				    JOptionPane.showMessageDialog(null,
				            "Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
				} catch (ClassNotFoundException e1) {
				    e1.printStackTrace();
				}
				
				
				try {
					Connection con = dbConn.Connect();
					PreparedStatement pst = con.prepareStatement("INSERT INTO Grades (id_teacher, id_student, id_subject, mark, id_grade_type) " +
			                 "SELECT ?, ?, ?, ?, ? " +
			                 "FROM Students s " +
			                 "JOIN Class c ON s.id_class = c.id " +			                 
			                 "WHERE s.id=? "
			                 + "ON DUPLICATE KEY UPDATE Grades.id = Grades.id");
		
		
					pst.setInt(1, AddGradesWindow.id_teacher); 
					//System.out.println(AddGradesWindow.id_teacher);
					pst.setInt(2, id_student);
					System.out.println("idstd: " +id_student);
					pst.setInt(3, id_subject);
					//System.out.println(id_subject);
					pst.setInt(4, Integer.parseInt(tx_mark.getText()));
					//System.out.println(Integer.parseInt(tx_mark.getText()));
					pst.setInt(5, id_type);
					//System.out.println(id_student);
					pst.setInt(6, AddGradesWindow.id_teacher);
					//System.out.println(AddGradesWindow.id_teacher);
					pst.executeUpdate();
					
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null,
							"Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btn_Save.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		btn_Save.setBackground(new Color(240, 240, 240));
		panel_2.add(btn_Save);
		
		//System.out.println("TojesttO: "+AddGradesWindow.id_teacher);
		
	}
	

}
