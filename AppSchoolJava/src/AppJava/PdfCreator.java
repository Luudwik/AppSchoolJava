package AppJava;

import java.io.FileOutputStream;
import java.util.stream.Stream;
import javax.swing.JTable;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class represents a PDF creator that generates PDF documents from a JTable.
 */
public class PdfCreator extends Document {

    /**
     * Constructs a PdfCreator object with the specified filename.
     * 
     * @param filename the name of the PDF file to be created
     */
    public PdfCreator(String filename) {
        super(PageSize.A4);
        try {
            PdfWriter.getInstance(this, new FileOutputStream(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a PDF document from the given JTable.
     * 
     * @param table the JTable to be converted to PDF
     */
    public void createPdf(JTable table) {
        try {
            open();
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            Stream.of(table.getColumnName(0), table.getColumnName(1)).forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                // header.setBackgroundColor(new java.awt.Color(240, 240, 240));
                header.setBorderWidth(1);
                header.setPhrase(new Paragraph(columnTitle));
                pdfTable.addCell(header);
            });
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    pdfTable.addCell(table.getValueAt(i, j).toString());
                }
            }
            add(pdfTable);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
