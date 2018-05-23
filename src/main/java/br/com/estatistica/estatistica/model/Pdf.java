package br.com.estatistica.estatistica.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.log.Log;

public class Pdf {

	public static boolean createPdf(Update update) {
		List<Historic> userHistoric = ModelDAO.getHistoric(update);
		if (userHistoric != null) {
			Collections.sort(userHistoric);
			Document pdf = new Document();
			try {
				int request = 0;
				PdfWriter.getInstance(pdf,
						new FileOutputStream(new File("files/pdf/" + update.message().chat().id() + ".pdf")));
				pdf.open();
				Paragraph p = new Paragraph();
				p.add(new Paragraph("Histórico de requisições"));
				p.add(new Paragraph(" "));
				p.setAlignment(Element.ALIGN_CENTER);
				for (Historic historic : userHistoric) {
					request++;
					String valores = historic.getValues().toString().replaceAll("\\[|\\]", "");
					p.add(new Paragraph("Requisição " + request));
					p.add(new Paragraph("Tipo da requisicao: " + historic.getType()));
					p.add(new Paragraph("Valores: " + valores));
					if (historic.getResult() != null) {
						p.add("Resultado: " + historic.getResult());
						p.add(new Paragraph(" "));
					} else {
						p.add("Resultado: " + historic.getResultArr());
						p.add(new Paragraph(" "));
					}
					Image image = Image.getInstance(historic.getBoxPlot().toString());
					image.scaleToFit(200, 200);
					p.add(image);
				}
				pdf.add(p);
				pdf.close();
			} catch (IOException | DocumentException e) {
				Log.logErrorWriter("Erro ao criar pdf: " + e);
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
