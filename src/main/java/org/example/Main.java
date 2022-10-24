package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class Main {

	public static String toTitleCase(String givenString) {
		String[] arr = givenString.split(" ");
		StringBuilder sb = new StringBuilder();

		for (String s : arr) {
			sb.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).append(" ");
		}
		return sb.toString().trim();
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Enter the item name you want to inspect");
		Scanner itemScanner = new Scanner(System.in);
		String item = toTitleCase(itemScanner.nextLine());

		InputStream file = Main.class.getResourceAsStream("/Logs.txt");
		String[] logs = new String(Objects.requireNonNull(file).readAllBytes()).split("\n");

		List<String> boughtList = new ArrayList<>();
		List<String> soldList = new ArrayList<>();
		for (String str : logs) {
			if (str.contains(item) && str.contains("bought")) boughtList.add(str);
			if (str.contains(item) && str.contains("sold")) soldList.add(str);
		}


		int fullBoughtAmounts = 0;
		int fullSoldAmounts = 0;
		double fullBoughtPrices = 0D;
		double fullSoldPrices = 0D;
		List<Double> boughtPrices = new ArrayList<>();
		List<Double> boughtAmounts = new ArrayList<>();
		List<Double> soldPrices = new ArrayList<>();
		int successRows = 0;
		int errorRows = 0;
		for (String str : boughtList) {
			successRows += 1;
			int boughtIndex = str.indexOf("bought");
			int itemIndex = str.indexOf(item);
			try {
				String singleBoughtAmount = str.substring(boughtIndex, itemIndex).replace("bought", "").strip();
				fullBoughtAmounts += Integer.parseInt(singleBoughtAmount);
				boughtAmounts.add(Double.parseDouble(singleBoughtAmount));
			} catch (Throwable throwable) {
				errorRows += 1;
				//Debug
				//System.out.println(str);
				continue;
			}
			String onePrice = str.substring(str.indexOf("for"), str.indexOf("from")).replace("for ", "").strip();
			boughtPrices.add(Double.parseDouble(onePrice));
			fullBoughtPrices += Double.parseDouble(onePrice);
		}
		for (String str : soldList) {
			successRows += 1;
			int soldIndex = str.indexOf("sold");
			int itemIndex = str.indexOf(item);
			//Magenta Banner#1D4
			//Testing
			try {
				String singleSoldAmount = str.substring(soldIndex, itemIndex).replace("sold", "").strip();
				fullSoldAmounts += Integer.parseInt(singleSoldAmount);
			} catch (Throwable throwable) {
				errorRows += 1;
				//Debug
				//System.out.println(str);
				continue;
			}
			String oneSoldPrice;
			try {
				oneSoldPrice = str.substring(str.indexOf("for"), str.indexOf("to")).replace("for ", "").strip();
			} catch (Throwable throwable) {
				continue;
			}
			soldPrices.add(Double.parseDouble(oneSoldPrice));
			fullSoldPrices += Double.parseDouble(oneSoldPrice);
		}

		double averagePrice = fullBoughtPrices / fullBoughtAmounts;
		boughtPrices = boughtPrices.stream().sorted().toList();
		soldPrices = soldPrices.stream().sorted().toList();
		List<Double> medianAmounts = boughtAmounts.stream().sorted().toList();
		OptionalDouble averageAmount = boughtAmounts
				.stream()
				.mapToDouble(a -> a)
				.average();

		System.out.println();
		System.out.println("The median transaction per " + item + " sold is: " + boughtPrices.get(boughtPrices.size() / 2) + " per " + medianAmounts.get(boughtAmounts.size() / 2));
		System.out.println("The average transaction per " + item + " sold is: " + (String.format("%.4f", averagePrice) + " per " + String.format("%.1f", averageAmount.getAsDouble())).replace(",", "."));
		System.out.println("The average price per stack of item sold is: " + averagePrice * 64);
		System.out.println();
		System.out.println("The median price per " + item + " bought is: " + soldPrices.get(soldPrices.size() / 2));
		System.out.println("The average price per " + item + " bought is: " + String.format("%.4f", averagePrice).replace(",", "."));
		System.out.println("The average price per stack of item bought is: " + averagePrice * 64);
		System.out.println();
		System.out.println("I have analysed " + successRows + " transactions");
		System.out.println(errorRows > 0 ? "I wasn't able to analyze " + errorRows + " transactions" : "");

	}
}