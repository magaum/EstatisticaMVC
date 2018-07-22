package br.com.estatistica.estatistica.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pengrad.telegrambot.model.Update;

public class Pdf {

	public static File createPdf(Update update) {
		List<Historic> userHistoric = new ArrayList<Historic>();
		try {
			userHistoric = ModelDAO.getHistoric(update);
		} catch (NullPointerException error) {
			Log.logErrorWriter("Mensagem nula, erro ao gerar PDF: " + error);
		}
		File file = null;
		if (userHistoric != null) {
			Collections.sort(userHistoric);
			Document pdf = new Document();
			file = new File("files/pdf/" + update.message().chat().id() + ".pdf");
			try {
				int request = 0;
				PdfWriter pdfWriter = PdfWriter.getInstance(pdf, new FileOutputStream(file));
				pdf.open();
				Paragraph p = new Paragraph();
				p.add(new Paragraph("Histórico de requisições"));
				p.add(new Paragraph(" "));
				p.setAlignment(Element.ALIGN_CENTER);
				for (Historic historic : userHistoric) {
					request++;
					String valores = historic.getValues().toString().replaceAll("\\[|\\]", "");
					p.add(new Paragraph("Tipo da requisicao: " + historic.getType()));
					p.add(new Paragraph("Valores: " + valores));
					if (historic.getResult() != null) {
						p.add("Resultado: " + historic.getResult());
						p.add(new Paragraph(" "));
					} else if (historic.getResultArr() != null) {
						p.add("Resultado: " + historic.getResultArr().toString().replaceAll("\\[|\\]", ""));
						p.add(new Paragraph(" "));
					} else {
						p.add("Resultado: " + historic.getModeNull());
						p.add(new Paragraph(" "));
					}
					Image image = Image.getInstance(historic.getBoxPlot().toString());
					image.scaleToFit(200, 200);
					p.add(image);
					if (request % 2 == 0 && request > 2) {
						p.add(new Paragraph(" "));
						p.add(new Paragraph(" "));
						p.add(new Paragraph(" "));
					}
					for (int i = 0; i < 9; i++) {
						if (request % 2 == 0) {
							p.add(new Paragraph(" "));
						}
					}
				}
				pdf.add(p);
				pdf.close();
				pdfWriter.close();
			} catch (IOException | DocumentException e) {
				Log.logErrorWriter("Erro ao criar pdf: " + e);
				Log.logErrorWriter("Classe: " + Pdf.class.getSimpleName());
			}
		}
		return file;
	}
}