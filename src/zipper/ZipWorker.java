package zipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWorker {

	ZipOutputStream zout;
	
	public ZipWorker(File outputFilePath) throws IOException {
		if(!outputFilePath.exists())
			outputFilePath.createNewFile();
		zout = new ZipOutputStream(new FileOutputStream(outputFilePath));
	}
	
	public void addFile(File file, String entry) throws IOException {
		zout.putNextEntry(new ZipEntry(entry));
		
		FileInputStream fin = new FileInputStream(file);
		
		byte[] b = new byte[2048];
		int count = 0;
		
		while((count = fin.read(b)) > 0) {
			zout.write(b, 0, count);
		}
		
		fin.close();
	}
	
	public void close() throws IOException {
		zout.close();
	}
}
