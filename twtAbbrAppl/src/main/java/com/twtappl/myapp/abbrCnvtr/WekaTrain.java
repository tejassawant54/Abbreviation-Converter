package com.twtappl.myapp.abbrCnvtr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaTrain {

	static Instances trainData;

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
		return inputReader;
	}

	public static void main(String[] args) throws Exception {

		BufferedReader datafile = readDataFile(AbbrConstants.FILE_PATH+"/data/tweets_train.arff");
		ArffReader aR = new ArffReader(datafile);

		trainData = aR.getData();

		FilteredClassifier fc;
		StringToWordVector filter;

		trainData.setClassIndex(1);
		filter = new StringToWordVector();
		filter.setAttributeIndices("first");
		fc = new FilteredClassifier();
		fc.setFilter(filter);
		fc.setClassifier(new SMO());
		fc.buildClassifier(trainData);
		Evaluation validation = new Evaluation(trainData);
		validation.crossValidateModel(fc, trainData, 10, new Random(1));
		System.out.println("--------------" + fc.getClass().getName()
				+ "--------------");
		System.out
				.println("Model OutPut: " + validation.toClassDetailsString());
		System.out.println("" + fc.toString());
		System.out.println(validation.toSummaryString());

		 weka.core.SerializationHelper.write(AbbrConstants.FILE_PATH+"/models/SMO.model",fc);

	}

}
