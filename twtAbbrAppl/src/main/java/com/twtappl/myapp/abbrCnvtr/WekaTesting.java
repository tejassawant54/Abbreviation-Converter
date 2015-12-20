package com.twtappl.myapp.abbrCnvtr;

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.springframework.core.io.FileSystemResource;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/**
 * @category --> Yes and 1.0 is ---> No
 * 
 */
public class WekaTesting {
	BufferedReader inputReader = null;
	public static Map<Instance, Double> hm = null;
	public WekaTesting(){
		try {
			System.out.println("in WekaTesting");
			FileSystemResource resource = new FileSystemResource(AbbrConstants.FILE_PATH+"/data/tweets_test.arff");
			File file =resource.getFile();
			hm = readDataFile(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Map<Instance, Double> readDataFile(File filename) throws Exception {
		System.out.println("In readDataFile");
		// public static void readDataFile(String filename)throws Exception {
		 Map<Instance,Double> hm = new LinkedHashMap<Instance, Double>();
		// BufferedReader inputReader = null;
		try {
			System.out.println(filename);
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
		ArffReader reader = new ArffReader(inputReader);
		Instances datatest = new Instances(reader.getData());
		datatest.setClassIndex(1);

		try {
			Classifier classi = (Classifier) weka.core.SerializationHelper
					.read(AbbrConstants.FILE_PATH+"/models/SMO.model");
			File file = new File(AbbrConstants.FILE_PATH+"/data/file.csv");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// System.out.println(datatest.numInstances());
			for (int i = 0; i < datatest.numInstances(); i++) {
				// System.out.println(datatest.instance(i));
				double str = classi.classifyInstance(datatest.instance(i));
				// System.out.println("word: "+
				// datatest.instance(i).stringValue(i));
				// System.out.println("class--------" + str);
				System.out.println("Writing to file.csv ");
				if (str == 0.0) {
					bw.write(datatest.instance(i).toString() + "|" + "\n");
				}
				hm.put(datatest.instance(i), str);

			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hm;
	}

}