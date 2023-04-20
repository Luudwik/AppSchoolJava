package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JSeparator;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.CardLayout;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;

public class TestCreatorWindow extends JFrame {

	private JPanel contentPane;
	private DbConn dbConn = new DbConn();
	private JTextField wordPolishQuantityTextField;
	private JTextField wordEnglishQuantityTextField;
	private JComboBox<String> categoryBox;
	private DefaultTableModel tableModelPreviewTest;
	private DefaultTableModel tableModelPreviewKeyTest;
	private JTable tablePreviewTest;
	private JTable tablePreviewKeyTest;
	private JScrollPane scrollPaneTestPreview;
	private JScrollPane scrollPaneTestKeyPreview;
	private JPanel contentPaneBodyCenter;
	private JPanel contentPaneHeader;
	private JPanel contentPaneBody;

	private JLabel lblEmpty;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestCreatorWindow frame = new TestCreatorWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static class RoundedBorder implements Border {

		private int radius;

		RoundedBorder(int radius) {
			this.radius = radius;
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
		}

		public boolean isBorderOpaque() {
			return true;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
		}
	}

	public void FillCategory() {
		try {
			Connection con = dbConn.Connect();
			PreparedStatement pst = con.prepareStatement("SELECT name FROM FlashcardCategory");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				categoryBox.addItem(name);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void CreateTest() {

		try {
			Connection con = dbConn.Connect();
			PreparedStatement pst;
			ResultSet rs;
			String category = categoryBox.getSelectedItem().toString();

			pst = con.prepareStatement("SELECT * FROM `Flashcards` JOIN `FlashcardCategory` "
					+ "ON Flashcards.id_category = FlashcardCategory.id WHERE FlashcardCategory.name='" + category
					+ "'");
			rs = pst.executeQuery();

			int rowCounter = 0;
			while (rs.next()) {
				rowCounter++;
			}
			rs = pst.executeQuery();
			int wordPolishQuantity = Integer.parseInt(wordPolishQuantityTextField.getText());
			int wordEnglishQuantity = Integer.parseInt(wordEnglishQuantityTextField.getText());
			int wordCount = wordPolishQuantity + wordEnglishQuantity;
			if ((wordPolishQuantity + wordEnglishQuantity) <= rowCounter) {
				pst = con.prepareStatement("SELECT * FROM `Flashcards` JOIN `FlashcardCategory` "
						+ "ON Flashcards.id_category = FlashcardCategory.id WHERE FlashcardCategory.name='" + category
						+ "' ORDER BY RAND() LIMIT " + wordCount);
				rs = pst.executeQuery();

				tableModelPreviewTest = new DefaultTableModel();
				tableModelPreviewTest.addColumn("Słówko po polsku");

				tableModelPreviewKeyTest = new DefaultTableModel();
				tableModelPreviewKeyTest.addColumn("Słówko po polsku");
				tableModelPreviewKeyTest.addColumn("Słówko po angielsku");

				while (rs.next()) {
					tableModelPreviewKeyTest.insertRow(0,
							new Object[] { rs.getString("wordPolish"), rs.getString("wordEnglish") });
					// System.out.println(rs.getString("wordPolish")+"
					// "+rs.getString("wordEnglish"));
					lblEmpty.setText("");
				}
				tablePreviewKeyTest = new JTable(tableModelPreviewKeyTest);
				scrollPaneTestKeyPreview = new JScrollPane(tablePreviewKeyTest);
				contentPaneBodyCenter.add(scrollPaneTestKeyPreview, BorderLayout.EAST);

			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public TestCreatorWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(1280, 720);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPaneHeader = new JPanel();
		contentPaneHeader.setBackground(Color.WHITE);
		contentPane.add(contentPaneHeader, BorderLayout.NORTH);
		contentPaneHeader.setLayout(new BorderLayout(0, 0));

		JPanel contentPaneHeaderUpper = new JPanel();
		contentPaneHeaderUpper.setBackground(new Color(88, 188, 208));
		contentPaneHeader.add(contentPaneHeaderUpper, BorderLayout.NORTH);

		contentPaneHeaderUpper.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Fiszki");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 34));
		contentPaneHeaderUpper.add(lblNewLabel, BorderLayout.CENTER);

		JLabel lblImage = new JLabel("");
		Image imgFlashcardIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-flashcard.png"))
				.getImage();
		Image scaledImgFlashcardIcon = imgFlashcardIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		lblImage.setIcon(new ImageIcon(scaledImgFlashcardIcon));
		lblImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPaneHeaderUpper.add(lblImage, BorderLayout.WEST);

		JPanel contentPaneHeaderLower = new JPanel();
		contentPaneHeaderLower.setBackground(new Color(255, 147, 150));
		contentPaneHeader.add(contentPaneHeaderLower, BorderLayout.CENTER);
		contentPaneHeaderLower.setLayout(new GridLayout(2, 4, 0, 0));
		contentPaneHeaderLower.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

		JLabel lblCategory = new JLabel("Kategoria");
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCategory.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPaneHeaderLower.add(lblCategory);

		JLabel lblWordEnglishQuantity = new JLabel("Ilość słówek po angielsku");
		lblWordEnglishQuantity.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWordEnglishQuantity.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPaneHeaderLower.add(lblWordEnglishQuantity);

		JLabel lblWordPolishQuantity = new JLabel("Ilość słówek po polsku");
		lblWordPolishQuantity.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWordPolishQuantity.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPaneHeaderLower.add(lblWordPolishQuantity);

		lblEmpty = new JLabel(" ");
		contentPaneHeaderLower.add(lblEmpty);

		categoryBox = new JComboBox<String>();
		categoryBox.setBackground(Color.LIGHT_GRAY);
		categoryBox.setForeground(Color.BLACK);
		categoryBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPaneHeaderLower.add(categoryBox);
		FillCategory();

		wordEnglishQuantityTextField = new JTextField();
		wordEnglishQuantityTextField.setBackground(Color.LIGHT_GRAY);
		contentPaneHeaderLower.add(wordEnglishQuantityTextField);

		wordPolishQuantityTextField = new JTextField();
		wordPolishQuantityTextField.setBackground(Color.LIGHT_GRAY);
		contentPaneHeaderLower.add(wordPolishQuantityTextField);

		JPanel contentPaneHeaderLowerButton = new JPanel();
		contentPaneHeaderLowerButton.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		contentPaneHeaderLowerButton.setBackground(Color.LIGHT_GRAY);
		contentPaneHeaderLower.add(contentPaneHeaderLowerButton);
		contentPaneHeaderLowerButton.setLayout(new GridLayout(1, 1, 5, 5));

		JButton btnCreate = new JButton("Utwórz");

		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 18));

		btnCreate.setBackground(new Color(68, 178, 218));
		//btnCreate.setBorder(new RoundedBorder(20));

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateTest();
			}
		});
		contentPaneHeaderLowerButton.add(btnCreate);

		contentPaneBody = new JPanel();
		contentPaneBody.setBackground(new Color(217, 238, 250));
		contentPane.add(contentPaneBody, BorderLayout.CENTER);
		contentPaneBody.setLayout(new BorderLayout(0, 0));

		JPanel contentPaneBodyUpper = new JPanel();
		contentPaneBodyUpper.setBackground(new Color(180, 180, 180, 70));
		contentPaneBody.add(contentPaneBodyUpper, BorderLayout.NORTH);
		contentPaneBodyUpper.setLayout(new BorderLayout(0, 0));

		JLabel lblTestPreview = new JLabel("Podgląd testu");
		lblTestPreview.setFont(new Font("Tahoma", Font.PLAIN, 18));

		lblTestPreview.setBorder(BorderFactory.createEmptyBorder(0, 190, 0, 0));
		contentPaneBodyUpper.add(lblTestPreview, BorderLayout.WEST);

		JLabel lblTestKeyPreview = new JLabel("Podgląd klucza odpowiedzi");
		lblTestKeyPreview.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestKeyPreview.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 150));
		contentPaneBodyUpper.add(lblTestKeyPreview, BorderLayout.EAST);

		contentPaneBodyCenter = new JPanel();
		contentPaneBodyCenter.setBackground(new Color(217, 238, 250));
		contentPaneBody.add(contentPaneBodyCenter, BorderLayout.CENTER);
		contentPaneBodyCenter.setLayout(new BorderLayout(0, 0));

		scrollPaneTestPreview = new JScrollPane();
		contentPaneBodyCenter.add(scrollPaneTestPreview, BorderLayout.WEST);

	}
}
