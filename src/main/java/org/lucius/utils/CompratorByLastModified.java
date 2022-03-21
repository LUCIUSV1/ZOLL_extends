package org.lucius.utils;

import java.io.File;
import java.util.Comparator;

public class CompratorByLastModified implements Comparator<File> {
	public int compare(File f1, File f2) {
		long diff = f2.lastModified() - f1.lastModified();
		if (diff > 0)
			return -1;// �����������
		else if (diff == 0)
			return 0;
		else
			return 1;// �����������
	}

	public boolean equals(Object obj) {
		return true;
	}

}
