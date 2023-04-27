package AppJava;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import javax.swing.JTable;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class GradesWindow extends JFrame {

	private JPanel GradesWindow;
	public static JTable table_student;
	private static DbConn dbConn = new DbConn();
	private JComboBox<String> cb_class;
	private static JComboBox<String> cb_student;
	private String chooseClass;
	public String choosedMarkTxt = "";
	public String choosedTypeTxt = "";
	public static String firstName;
	public static String surname;
	public int choosedIDInt;
	public int id_teacher;
	

	public GradesWindow(String choosedMarkTxt, String choosedTypeTxt, String firstName, String surname) {
		this.choosedMarkTxt = choosedMarkTxt;
		this.choosedTypeTxt = choosedTypeTxt;
		this.firstName = firstName;
		this.surname = surname;		
	}
	public GradesWindow(String firstName, String surname)
	{
		this.firstName = firstName;
		this.surname = surname;
	}
	
	

	static // DefaultTableModel model;
	DefaultTableModel tableModel = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GradesWindow frame = new GradesWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void fillCb_class() {
		try {
			Connection con = dbConn.Connect();
			cb_class.removeAllItems();
			PreparedStatement pst = con.prepareStatement("SELECT number FROM Class");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String class_name = rs.getString("number");
				cb_class.addItem(class_name);
			}
			cb_class.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fillCb_Students();
				}
			});

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void fillCb_Students() {
		try {
			int chooseClass = Integer.parseInt(cb_class.getSelectedItem().toString());
			Connection con = dbConn.Connect();
			cb_student.removeAllItems();
			PreparedStatement pst = con.prepareStatement(
					"SELECT name, surname FROM Students WHERE id_class = (SELECT id FROM Class WHERE number = ?)");
			pst.setInt(1, chooseClass);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("name");
				surname = rs.getString("surname");
				cb_student.addItem(firstName + " " + surname);
				//AddGradesWindow addGradesWindow = new AddGradesWindow(firstName, surname);
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}		
	}

	public String getClassValue() {
		chooseClass = (String) cb_class.getSelectedItem();
		return chooseClass;
	}


	
	public static void refreshTable() {
		try {
			tableModel.setRowCount(0);
			String selectedStudent = cb_student.getSelectedItem().toString();
			String[] nameParts = selectedStudent.split(" ");
			firstName = nameParts[0];
			surname = nameParts[1];
			System.out.println(firstName);
			System.out.println(surname);
			AddGradesWindow addGradesWindow = new AddGradesWindow(firstName, surname);
			Connection con = dbConn.Connect();
			PreparedStatement pst = con.prepareStatement(
					"SELECT Grades.id, Grades.mark, GradeType.type FROM Students JOIN Grades ON Students.id = Grades.id_student JOIN GradeType ON Grades.id_grade_type = GradeType.id WHERE Students.name = ? AND Students.surname = ?");
			pst.setString(1, firstName);
			pst.setString(2, surname);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				int mark = rs.getInt("mark");
				String type = rs.getString("type");
				tableModel.addRow(new Object[] { id, mark, type });
			}

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	

	/**
	 * Create the frame.
	 */

	public GradesWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GradesWindow = new JPanel();
		setLocationRelativeTo(null);

		// Pobranie rozmiaru ekranu
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// Ustawienie rozmiaru okna na rozmiar ekranu
		setSize(screenSize.width, screenSize.height);
		// Ustawienie zachowania okna na pełny ekran
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		GradesWindow.setBorder(new EmptyBorder(0, 0, 0, 0));
		GradesWindow.setBackground(new Color(224, 255, 255));
		setContentPane(GradesWindow);
		GradesWindow.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		GradesWindow.add(panel, BorderLayout.NORTH);
		panel.setBackground(new Color(240, 240, 240));
		panel.setPreferredSize(new Dimension(0, (int) (screenSize.height * 0.2)));
		panel.setLayout(new BorderLayout());
		JLabel lb_grades = new JLabel("Grades");
		lb_grades.setHorizontalAlignment(SwingConstants.CENTER);
		lb_grades.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.09)));
		panel.add(lb_grades, BorderLayout.CENTER);

		JLabel lbl_icon_2 = new JLabel();
		panel.add(lbl_icon_2, BorderLayout.EAST);
		Image img_Grades = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-grade.png")).getImage();
		Image scaledImg = img_Grades.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
		lbl_icon_2.setIcon(new ImageIcon(scaledImg));
		lbl_icon_2.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		JLabel lbl_icon_1 = new JLabel();
		panel.add(lbl_icon_1, BorderLayout.WEST);
		lbl_icon_1.setIcon(new ImageIcon(scaledImg));
		lbl_icon_1.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JPanel panel_1 = new JPanel();
		GradesWindow.add(panel_1, BorderLayout.CENTER);
		panel_1.setBackground(new Color(224, 255, 255));
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_3 = new JPanel(new BorderLayout());
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setBackground(new Color(224, 255, 255));
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		Component hs_1 = Box.createHorizontalStrut((panel_3.getWidth() / 3) + 30);
		panel_3.add(hs_1);

		JLabel lbl_ChooseClass = new JLabel("Choose the appropriate class");
		lbl_ChooseClass.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		panel_3.add(lbl_ChooseClass);

		Component hs_2 = Box.createHorizontalStrut((panel_3.getWidth() / 3) + 15);
		panel_3.add(hs_2);

		cb_class = new JComboBox<String>();
		cb_class.setFont(new Font("Bernard MT Condensed", Font.BOLD | Font.ITALIC, 30));
		cb_class.setBackground(new Color(224, 255, 255));
		cb_class.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel_3.add(cb_class);

		Component hs_3 = Box.createHorizontalStrut((panel_3.getWidth() / 3) + 20);
		panel_3.add(hs_3);

		JLabel lbl_ChooseStudent = new JLabel("Choose a student");
		lbl_ChooseStudent.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		panel_3.add(lbl_ChooseStudent, BorderLayout.NORTH);

		Component hs_4 = Box.createHorizontalStrut((panel_3.getWidth() / 3) + 15);
		panel_3.add(hs_4);

		cb_student = new JComboBox<String>();
		cb_student.setFont(new Font("Bernard MT Condensed", Font.BOLD | Font.ITALIC, 30));
		cb_student.setBackground(new Color(224, 255, 255));
		cb_student.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel_3.add(cb_student, BorderLayout.NORTH);

		
		
		Component hs_5 = Box.createHorizontalStrut((panel_3.getWidth() / 3) + 30);
		panel_3.add(hs_5);

		JButton btn_accept = new JButton();
		btn_accept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTable();
				/*try {
					tableModel.setRowCount(0);
					String selectedStudent = cb_student.getSelectedItem().toString();
					String[] nameParts = selectedStudent.split(" ");
					firstName = nameParts[0];
					surname = nameParts[1];
					System.out.println(firstName);
					System.out.println(surname);
					Connection con = dbConn.Connect();
					PreparedStatement pst = con.prepareStatement(
							"SELECT Grades.id, Grades.mark, GradeType.type FROM Students JOIN Grades ON Students.id = Grades.id_student JOIN GradeType ON Grades.id_grade_type = GradeType.id WHERE Students.name = ? AND Students.surname = ?");
					pst.setString(1, firstName);
					pst.setString(2, surname);
					ResultSet rs = pst.executeQuery();

					while (rs.next()) {
						int id = rs.getInt("id");
						int mark = rs.getInt("mark");
						String type = rs.getString("type");
						tableModel.addRow(new Object[] { id, mark, type });
					}

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}*/
			}
		});
		btn_accept.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		btn_accept.setText("Show grades");
		btn_accept.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btn_accept.setBackground(new Color(208, 255, 255));
		panel_3.add(btn_accept);

		Component hs_6 = Box.createHorizontalStrut((panel_3.getWidth() / 3) + 30);
		panel_3.add(hs_6);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout());

		panel_1.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int panelHeight = (int) (panel_1.getHeight() * 0.9);
				panel_2.setPreferredSize(new Dimension(0, panelHeight));
				panel_3.setPreferredSize(new Dimension(0, (int) (panel_1.getHeight() * 0.1)));
			}
		});

		tableModel.addColumn("id");
		tableModel.addColumn("Mark");
		tableModel.addColumn("Type");

		table_student = new JTable(tableModel);
		ListSelectionModel selectionModel = table_student.getSelectionModel();
		table_student.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_student.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, 30));
		table_student.setBackground(new Color(255, 255, 255));
		table_student.setPreferredScrollableViewportSize(new Dimension(650, 100));
		table_student.setFillsViewportHeight(true);
		table_student.getTableHeader().setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, 30));
		table_student.setRowHeight(40);

		// wysrodkowanie
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table_student.getColumnCount(); i++) {
			table_student.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JScrollPane scrollPane = new JScrollPane(table_student);
		panel_2.add(scrollPane);

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));
		panel_4.setBackground(new Color(224, 255, 255));

		JButton btnNewButton = new JButton("Edit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = selectionModel.getMinSelectionIndex();

				if (selectedRowIndex >= 0) {
					Object choosedID = tableModel.getValueAt(selectedRowIndex, 0);
					Object choosedMark = tableModel.getValueAt(selectedRowIndex, 1);
					Object choosedType = tableModel.getValueAt(selectedRowIndex, 2);
					String choosedIDTxt = choosedID.toString();
					choosedMarkTxt = choosedMark.toString();
					choosedTypeTxt = choosedType.toString();
					choosedIDInt = Integer.parseInt(choosedIDTxt);

					if (choosedMarkTxt != null && choosedTypeTxt != null && firstName != null && surname != null) {
						EditGradesWindow editGradesWindow = new EditGradesWindow(choosedMarkTxt, choosedTypeTxt,
								firstName, surname, choosedIDInt);
						editGradesWindow.setVisible(true);
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		btnNewButton.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
		btnNewButton.setBackground(new Color(208, 255, 255));
		panel_4.add(btnNewButton, BorderLayout.WEST);

		JButton btnNewButton_1 = new JButton("Add");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				refreshTable();
				AddGradesWindow addGradesWindow = new AddGradesWindow();
				addGradesWindow.setVisible(true);
			}
		});
		btnNewButton_1.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		btnNewButton_1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btnNewButton_1.setBackground(new Color(208, 255, 255));
		panel_4.add(btnNewButton_1, BorderLayout.CENTER);

		JButton btnNewButton_2 = new JButton("Delete");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int selectedRowIndex = selectionModel.getMinSelectionIndex();

					if (selectedRowIndex >= 0) {
						Object choosedID = tableModel.getValueAt(selectedRowIndex, 0);
						Object choosedMark = tableModel.getValueAt(selectedRowIndex, 1);
						Object choosedType = tableModel.getValueAt(selectedRowIndex, 2);
						String choosedIDTxt = choosedID.toString();
						choosedMarkTxt = choosedMark.toString();
						choosedTypeTxt = choosedType.toString();
						choosedIDInt = Integer.parseInt(choosedIDTxt);
					}
				Connection con = dbConn.Connect();
				PreparedStatement pst = con.prepareStatement(
						"DELETE FROM Grades, GradeType "
						+ "USING Grades "
						+ "JOIN GradeType ON Grades.id_grade_type = GradeType.id "
						+ "WHERE Grades.id = ?");
				pst.setInt(1, choosedIDInt);
				boolean rs = pst.execute();

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
				refreshTable();
			}
		});
		btnNewButton_2.setFont(new Font("Bodoni MT Condensed", Font.BOLD | Font.ITALIC, (int) (getWidth() * 0.03)));
		btnNewButton_2.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
		btnNewButton_2.setBackground(new Color(208, 255, 255));
		panel_4.add(btnNewButton_2, BorderLayout.EAST);

		fillCb_class();
		fillCb_Students();
		table_student.repaint();
		
		
		

	}
	
	public int getChoosedIDInt() {
		return choosedIDInt;
	}
	public void setChoosedIDInt(int choosedIDInt) {
		this.choosedIDInt = choosedIDInt;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

}
