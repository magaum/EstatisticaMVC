package br.com.estatistica.estatistica.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class Historic implements Comparable<Historic> {

	private ArrayList<Double> values;
	private ArrayList<Double> resultArr;
	private Double result;
	private String modeNull;
	private long chatId;
	private String type;
	private File boxPlot;
	private Calendar date;
	
	public Historic() {	}

	public Historic(ArrayList<Double> valores, long chatId, String string, File boxPlot) {
		this.values = valores;
		this.chatId = chatId;
		this.type = string;
		this.boxPlot = boxPlot;
		this.date = Calendar.getInstance();
	}

	public Historic(ArrayList<Double> valores, long chatId, String string, Double resultado, File boxPlot) {
		this.values = valores;
		this.chatId = chatId;
		this.type = string;
		this.result = resultado;
		this.boxPlot = boxPlot;
		this.date = Calendar.getInstance();
	}

	public ArrayList<Double> getValues() {
		return values;
	}

	public void setValues(ArrayList<Double> values) {
		this.values = values;
	}

	public ArrayList<Double> getResultArr() {
		return resultArr;
	}

	public void setResultArr(ArrayList<Double> resultArr) {
		this.resultArr = resultArr;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getBoxPlot() {
		return boxPlot;
	}

	public void setBoxPlot(File boxPlot) {
		this.boxPlot = boxPlot;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	@Override
	public int compareTo(Historic historic) {
		return type.compareTo(historic.getType());
	}

	public void setModeNull() {
		this.modeNull = "Não existe moda";
	}

	public String getModeNull() {
		return modeNull;
	}
}
