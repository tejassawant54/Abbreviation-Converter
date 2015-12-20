package com.twtappl.myapp.abbrCnvtr;


import java.util.ArrayList;

/**
 * @author T
 * 
 */
public class DocId {
	int docId;
	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	ArrayList<Integer> positionList;

	public DocId(int did, int position) {
		docId = did;
		positionList = new ArrayList<Integer>();
		positionList.add(new Integer(position));
	}

	public void insertPosition(int position) {
		positionList.add(new Integer(position));
	}

	public String toString() {
		String docIdString = docId + ":<";
		for (Integer pos : positionList)
			docIdString += pos + ",";
		docIdString = docIdString.substring(0, docIdString.length() - 1) + ">";
		return docIdString;
	}
}

