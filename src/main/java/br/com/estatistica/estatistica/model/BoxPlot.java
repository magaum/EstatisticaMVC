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
	
	public static String generateBoxPlot(Update update, String operation) throws IOException {
		long chatId = update.message().chat().id();
		DefaultBoxAndWhiskerCategoryDataset boxPlot = new DefaultBoxAndWhiskerCategoryDataset();
		List<Double> values = ModelUtils.messageToDouble(update.message().text());
		boxPlot.add(values, "", "Valores");
		String name = chatId + new Date().getTime()+"";
		JFreeChart jFreeChart = ChartFactory.createBoxAndWhiskerChart("Box Plot", operation, "Coluna", boxPlot, false);
		ChartUtilities.saveChartAsPNG(new File("files/Imgs/boxPlots/" + name + ".png"),
				jFreeChart, 800, 800);	
		return name;
	}

}
