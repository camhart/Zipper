package zipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class JarWorker {

	JarOutputStream zout;
	
	public JarWorker(File outputFilePath) throws IOException {
		if(!outputFilePath.exists())
			outputFilePath.createNewFile();
		zout = new JarOutputStream(new FileOutputStream(outputFilePath));
	}
	
	public void addFile(File file, String entry) throws IOException {
		zout.putNextEntry(new JarEntry(entry));
		
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
