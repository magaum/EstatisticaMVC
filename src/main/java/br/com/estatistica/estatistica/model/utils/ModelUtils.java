package br.com.estatistica.estatistica.model.utils;

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

import br.com.estatistica.estatistica.log.Logs;
import br.com.estatistica.estatistica.model.Historic;
import br.com.estatistica.estatistica.model.ModelDAO;

public class ModelUtils {

	public static void removerDadosMap(ArrayList<Double> modas) {
		if (modas.size() > 0) {
			for (int i = 0; i <= modas.size(); i++) {
				modas.remove(0);
			}
		}
	}

	public static ArrayList<Double> convertStringToDouble(String valor) {
		ArrayList<Double> valores = new ArrayList<Double>();
		String[] numeros = valor.replaceAll(",", ".").split(";");
		try {
			for (String numero : numeros) {
				valores.add(Double.parseDouble(numero));
			}
		} catch (Exception error) {
			Logs.logErrorWriter("Valor inválido no input: " + error);
			valores = null;
		}
		return valores;
	}

	public static boolean createPdf(Update update) {
		List<Historic> userHistoric = ModelDAO.getHistoric(update);
		if (userHistoric != null) {
			Collections.sort(userHistoric);
			Document pdf = new Document();
			try {
				int request = 0;
				PdfWriter.getInstance(pdf, new FileOutputStream(new File("pdf/historico.pdf")));
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
					Image image = Image.getInstance("boxPlots/" + historic.getChatId() + ".png");
					image.scaleToFit(200, 200);
					p.add(image);
					pdf.add(p);
				}
				pdf.close();
			} catch (IOException | DocumentException e) {
				Logs.logErrorWriter("Erro ao criar pdf: " + e);
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}