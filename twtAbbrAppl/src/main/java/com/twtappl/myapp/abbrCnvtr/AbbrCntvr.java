package com.twtappl.myapp.abbrCnvtr;

import java.util.*;
import java.io.*;

import com.twtappl.myapp.AbbrPojo;

public class AbbrCntvr {

	static String[] myDocs;
	ArrayList<String> termList;
	ArrayList<ArrayList<DocId>> docLists;
	
	static Map<String, String> hm = new LinkedHashMap<String, String>();
	static SortedSet<Integer> allList = new TreeSet<Integer>();

	// Constructor for class abbrCntvr which reads the tweets document
	// and creates tokens and positional Index

	public AbbrCntvr() {

		termList = new ArrayList<String>();
		docLists = new ArrayList<ArrayList<DocId>>();
		ArrayList<DocId> docList;

		docLists = new ArrayList<ArrayList<DocId>>();
		myDocs = parse(AbbrConstants.FOLDER_NAME);

		for (int i = 0; i < myDocs.length; i++) {
			String[] tokens = myDocs[i].split("[\" ()_,?:;!'%&.-]+");
			String token;
			for (int j = 0; j < tokens.length; j++) {
				token = tokens[j];
				if (!termList.contains(token)) {
					termList.add(token);
					docList = new ArrayList<DocId>();
					DocId doid = new DocId(i, j);
					docList.add(doid);
					docLists.add(docList);
				} else { // existing term
					int index = termList.indexOf(token);
					docList = docLists.get(index);
					int k = 0;
					boolean match = false;
					// search the postings for a document id, if match, insert a
					// new position
					// number to the document id
					for (DocId doid : docList) {
						if (doid.docId == i) {
							doid.insertPosition(j);
							docList.set(k, doid);
							match = true;
							break;
						}
						k++;
					}
					// if no match, add a new document id along with the
					// position number
					if (!match) {
						DocId doid = new DocId(i, j);
						docList.add(doid);
					}
				}
			}
		}
	}

	public List<AbbrPojo> showResults(String searchText) throws Exception {
		String[] searchStr = searchText.split(" ");
		ArrayList<DocId> result = phraseQuery(searchStr);
		return abbrTweets(result);
	}

	private String[] parse(String filename) {
		String[] docs = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String allLines = new String();
			String line = null;
			while ((line = reader.readLine()) != null) {
				allLines += line.toLowerCase();

			}
			docs = allLines.split("[,?|]");
			reader.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return docs;
	}

	public String toString() {
		String matrixString = new String();
		ArrayList<DocId> docList;
		for (int i = 0; i < termList.size(); i++) {
			matrixString += String.format("%-15s", termList.get(i));
			docList = docLists.get(i);
			for (int j = 0; j < docList.size(); j++)
				matrixString += docList.get(j) + "\t";
			matrixString += "\n";
		}
		return matrixString;
	}

	private ArrayList<DocId> intersect(ArrayList<DocId> l1, ArrayList<DocId> l2) {

		ArrayList<DocId> mergedList = new ArrayList<DocId>();
		int id1 = 0, id2 = 0;
		while (id1 < l1.size() && id2 < l2.size()) {
			// if both terms appear in the same document
			if (l1.get(id1).docId == l2.get(id2).docId) {
				// get the position information for both terms
				ArrayList<Integer> pp1 = l1.get(id1).positionList;
				ArrayList<Integer> pp2 = l2.get(id2).positionList;
				int pid1 = 0, pid2 = 0;
				while (pid1 < pp1.size()) {
					boolean match = false;
					while (pid2 < pp2.size()) {
						// if the two terms appear together, we find a match
						if (Math.abs(pp1.get(pid1) - pp2.get(pid2)) <= 1) {
							match = true;
							mergedList.add(l2.get(id2));
							break;
						} else if (pp2.get(pid2) > pp1.get(pid1))
							break;
						pid2++;
					}
					if (match) // if a match if found, the search for the
								// current document can be stopped
						break;
					pid1++;
				}
				id1++;
				id2++;
			} else if (l1.get(id1).docId < l2.get(id2).docId)
				id1++;
			else
				id2++;
		}
		return mergedList;
	}

	private ArrayList<DocId> phraseQuery(String[] query) {

		ArrayList<DocId> result = null;
		result = search(query[0]);

		int termId = 1;
		while (termId < query.length) {
			ArrayList<DocId> result1 = search(query[termId]);
			result = intersect(result, result1);
			termId++;
		}
		return result;
	}

	private ArrayList<DocId> search(String query) {

		int index = termList.indexOf(query);
		if (index < 0)
			return null;
		return docLists.get(index);
	}

	private List<AbbrPojo> abbrTweets(ArrayList<DocId> result) throws Exception {
		List<AbbrPojo> finalResult = null;
		BufferedReader br = new BufferedReader(new FileReader(AbbrConstants.ABR_LIST));
		String line = "";
		System.out.println("\n-------START OF ABBRV DICTIONARY------\n");
		while ((line = br.readLine()) != null) {

			String[] tokens = line.split("\t");
			for (int i = 0; i < tokens.length; i++)
				// System.out.println(""+tokens[i]);
				hm.put(tokens[0], tokens[1]);
		}
		br.close();

		Set set = hm.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			System.out.print(me.getKey() + "\t:\t");
			System.out.println(me.getValue());
		}

		String[] matchedDocs = null;
		String[] tokens = null;
		if (result != null) {
			matchedDocs = new String[result.size()];
			int i1 = 0;
			for (DocId d : result) {
				matchedDocs[i1++] = myDocs[d.docId];
				System.out.println(myDocs[d.docId]);
			}
			System.out.println("Number of Tweets Fetched:" + matchedDocs.length);
			
			finalResult = new ArrayList<AbbrPojo>();
			
			for (int i11 = 0; i11 < matchedDocs.length; i11++) {
				tokens = matchedDocs[i11].split("[\" ()_,?:;!'%&.-]+");
				String token = null;
				String concat = new String();
				for (int j = 0; j < tokens.length; j++) {
					token = tokens[j];
					Iterator ii = set.iterator();
					while (ii.hasNext()) {
						Map.Entry me = (Map.Entry) ii.next();

						if (me.getKey().toString().compareTo(token.toUpperCase()) == 0) {
							System.out.println(matchedDocs[i11]+" ......contains a slang");
							System.out.println(token + " is a Slang.");
							tokens[j] = me.getValue().toString()+" ";
							token = tokens[j];

						}

					}
					concat += token+" ";
				}
				
				System.out.println("concat: "+concat);
				
				if(!finalResult.contains(concat))
					if(matchedDocs[i11].length() != concat.length()-1){	
						//System.out.println("----"+matchedDocs[i11].length()+"-----"+concat.length());
						AbbrPojo abbr = new AbbrPojo();
						abbr.setSrNo(i11+1);
						abbr.setAbbrText(matchedDocs[i11]);
						abbr.setNonAbbrText(concat);
						finalResult.add(abbr);
						
						/*finalResult.add("\n");
						finalResult.add("Abbreviated Text: "+matchedDocs[i11]);
						finalResult.add("Non-Abbreviated Text: "+concat);
						finalResult.add("------------------------------------------------------------------------------------------------------");*/
					}
					else{	
						AbbrPojo abbr = new AbbrPojo();
						abbr.setSrNo(i11+1);
						abbr.setAbbrText(matchedDocs[i11]);
						abbr.setNonAbbrText("No Slang Detected!");
						finalResult.add(abbr);
						
						//System.out.println(""+matchedDocs[i11].length()+"-----"+concat.length());
						/*finalResult.add("Abbreviated Text: "+matchedDocs[i11]);
						finalResult.add("Non-Abbreviated Text: No Slang Detected!!");
						finalResult.add("------------------------------------------------------------------------------------------------------");*/
					}
				
					
				
			}
//			System.out.println("final Result Tweets: "+finalResult);
//			System.out.println("final Result: "+finalResult.size());
		}
		return finalResult;
	}
}

