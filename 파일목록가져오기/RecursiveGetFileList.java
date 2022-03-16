import java.io.File;

public class Test {
	public static void main(String[] args) {
		String dirPath = "";
		RecursiveGetFileList(dirPath);
		
	}
	
	public static void RecursiveGetFileList(String dirPath) {
		File file = new File(dirPath);
		File[] fileList = file.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			File fileUnit = fileList[i];
			String path = fileUnit.getPath();
			
			if( fileUnit.isFile() ) {
				String fileName = fileUnit.getName();
				String ext = fileName.substring(fileName.lastIndexOf(".")+1);
				System.out.println("ext : " + ext + "\t" + fileName);
			}else if( fileUnit.isDirectory() ) {
				RecursiveGetFileList(path);
			}
		}
	}
}
