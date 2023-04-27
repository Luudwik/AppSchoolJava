package AppJava;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class EditGradesWindow extends JFrame {

	private JPanel jpanel_EditGradesWindow;
	private JTextField tx_enterMark;
	private JTextField tx_enterType;
	private DbConn dbConn = new DbConn();
	public static String choosedMarkTxt;
	public static String choosedTypeTxt;
	public static String firstName;
	public static String surname;
	public static int choosedIDInt;

	/*
	 * public EditGradesWindow(String choosedMarkTxt, String choosedTypeTxt) {
	 * textField = new JTextField(choosedMarkTxt); textField_1 = new
	 * JTextField(choosedTypeTxt); }
	 */

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					EditGradesWindow frame = new EditGradesWindow(choosedMarkTxt, choosedTypeTxt, firstName, surname, choosedIDInt);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param choosedID 
	 */
	public EditGradesWindow(String choosedMarkTxt, String choosedTypeTxt, String firstName, String surname, int choosedIDInt) {

		this.choosedIDInt = choosedIDInt;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 680, 560);
		jpanel_EditGradesWindow = new JPanel();
		jpanel_EditGradesWindow.setBackground(new Color(224, 255, 255));
		setResizable(false);
		setLocationRelativeTo(null);

		setContentPane(jpanel_EditGradesWindow);
		jpanel_EditGradesWindow.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 240, 240));
		jpanel_EditGradesWindow.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lbl_icon_1 = new JLabel();
		panel.add(lbl_icon_1, BorderLayout.WEST);
		Image img_EditGrades = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-editGrade.png"))
				.getImage();
		Image scaledImg = img_EditGrades.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		lbl_icon_1.setIcon(new ImageIcon(scaledImg));
		lbl_icon_1.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JLabel lbl_addGrades = new JLabel("Edit grade");
		lbl_addGrades.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_addGrades.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		panel.add(lbl_addGrades, BorderLayout.CENTER);

		JLabel lbl_icon_2 = new JLabel();
		panel.add(lbl_icon_2, BorderLayout.EAST);
		lbl_icon_2.setIcon(new ImageIcon(scaledImg));
		lbl_icon_2.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(224, 255, 255));
		jpanel_EditGradesWindow.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(224, 255, 255));
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		JLabel lbl_enterMark = new JLabel("Mark:");
		lbl_enterMark.setBounds(17, 35, 175, 72);
		lbl_enterMark.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		lbl_enterMark.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
		panel_2.add(lbl_enterMark);

		// System.out.println(gradesWindow.choosedMarkTxt);
		tx_enterMark = new JTextField(choosedMarkTxt);
		tx_enterMark.setBounds(190, 16, 440, 111);
		tx_enterMark.setBackground(new Color(224, 255, 255));
		tx_enterMark.setFont(lbl_enterMark.getFont());
		tx_enterMark.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		tx_enterMark.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		panel_2.add(tx_enterMark);
		tx_enterMark.setColumns(10);

		JLabel lbl_enterType = new JLabel("Type:");
		lbl_enterType.setBounds(17, 197, 175, 72);
		lbl_enterType.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		lbl_enterType.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
		panel_2.add(lbl_enterType);

		tx_enterType = new JTextField(choosedTypeTxt);
		tx_enterType.setBounds(190, 178, 440, 111);
		tx_enterType.setBackground(new Color(224, 255, 255));
		tx_enterType.setFont(lbl_enterMark.getFont());
		tx_enterType.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		tx_enterType.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		panel_2.add(tx_enterType);
		tx_enterType.setColumns(10);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(224, 255, 255));
		jpanel_EditGradesWindow.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		JButton btn_save = new JButton("Save changes");
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Connection con = dbConn.Connect();
					PreparedStatement pst = con.prepareStatement("UPDATE Grades AS g "
							+ "JOIN Students AS s ON g.id_student = s.id "
							+ "JOIN GradeType AS gt ON g.id_grade_type = gt.id " + "SET g.mark = ?, gt.type = ? "
							+ "WHERE s.name = ? AND s.surname = ? AND g.id = ? AND gt.type = ?;");
					pst.setInt(1, Integer.parseInt(tx_enterMark.getText()));
					pst.setString(2, tx_enterType.getText());
					pst.setString(3, firstName);
					pst.setString(4, surname);
					pst.setInt(5, EditGradesWindow.choosedIDInt);
					pst.setString(6, choosedTypeTxt);

					int rowsAffected = pst.executeUpdate();
					if (rowsAffected <= 0) {
						JOptionPane.showMessageDialog(null, "No grades were updated.");	
					}

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null,
							"Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				GradesWindow.refreshTable();
				dispose();

			}
		});
		btn_save.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		btn_save.setBackground(new Color(208, 255, 255));
		panel_4.add(btn_save);

		// System.out.println(choosedMarkTxt1);
	}
}
