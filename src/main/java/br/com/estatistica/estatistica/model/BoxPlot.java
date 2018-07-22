package br.com.estatistica.estatistica.model;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import com.pengrad.telegrambot.model.Update;

import br.com.estatistica.estatistica.model.utils.ModelUtils;

public class BoxPlot {

	public static File generateBoxPlot(Update update, String operation) {
		DefaultBoxAndWhiskerCategoryDataset boxPlot = new DefaultBoxAndWhiskerCategoryDataset();
		List<Double> values = ModelUtils.messageToDouble(update.message().text());
		boxPlot.add(values, "", "Valores");
		JFreeChart jFreeChart = ChartFactory.createBoxAndWhiskerChart("Box Plot", operation, "Coluna", boxPlot, false);
		File file = null;
		try {
			long chatId = update.message().chat().id();
			String fileName = chatId + new Date().getTime() + "";
			file = new File("files/imgs/boxPlots/" + fileName + ".png");
			ChartUtilities.saveChartAsPNG(file, jFreeChart, 800, 800);
		} catch (IOException e) {
			Log.logErrorWriter("Erro ao gerar boxPlot: " + e);
			return file;
		}
		return file;
	}

}
