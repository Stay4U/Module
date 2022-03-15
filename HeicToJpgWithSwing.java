import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.filechooser.FileSystemView;

public class HeicToJpg {
	private JFrame mf;
	private JLabel srcFolderLbl;
	private JButton folderOpenBtn;
	private JButton fileConvertBtn;
	private JList<String> srcFileList;
	private DefaultListModel<String> srcFileListModel;
	private JScrollPane scrolled;

	private void prepareGUI() {
		mf = new JFrame("Heic To Jpg");
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mf.setLocation(10, 10);
		mf.setSize(550, 850);
		mf.setLayout(null);
		
		/*
		srcFolderLbl = new JLabel("원본폴더");
		srcFolderLbl.setLocation(10, 10);
		srcFolderLbl.setSize(100, 50);
		srcFolderLbl.setBackground(Color.darkGray);
		srcFolderLbl.setOpaque(true);
		mf.add(srcFolderLbl);
		*/
		
		ButtonClickListener buttonClickListener = new ButtonClickListener();
		
		folderOpenBtn = new JButton("폴더열기");
		folderOpenBtn.setActionCommand("folderOpen");
		folderOpenBtn.setLocation(10, 15);
		folderOpenBtn.setSize(100, 50);
		folderOpenBtn.addActionListener(buttonClickListener);
		mf.add(folderOpenBtn);

		fileConvertBtn = new JButton("파일변환");
		fileConvertBtn.setActionCommand("fileConvert");
		fileConvertBtn.setLocation(120, 15);
		fileConvertBtn.setSize(100, 50);
		fileConvertBtn.addActionListener(buttonClickListener);
		mf.add(fileConvertBtn);
		
		srcFileList = new JList<String>(new DefaultListModel<String>());
		srcFileListModel = (DefaultListModel<String>)srcFileList.getModel();
		
		scrolled = new JScrollPane(srcFileList);
		scrolled.setLocation(10, 80);
		scrolled.setSize(513, 713);
		mf.add(scrolled);
		
		mf.setVisible(true);
	}

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			if (command.equals("folderOpen")) {

				//statusLabel.setText(path);
				String path = jFileChooserUtil();
				if( "".equals(path) ) {
					return;
				}
				
				if(srcFileListModel.size() > 0) {
					srcFileListModel.removeAllElements();
				}
				
				LinkedList<String> list = new LinkedList<String>();
				
				RecursiveGetFileList(path, list);
				
				for (int i = 0; i < list.size(); i++) {
					srcFileListModel.addElement(list.get(i));
				}
				
			} else if (command.equals("fileConvert")) {
				if(srcFileListModel.size() > 0) {
					srcFileListModel.removeAllElements();
				}
			} else {
				//statusLabel.setText("Cancel Button clicked.");
			}
		}
	}

	public static String jFileChooserUtil(){

		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
		chooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
		chooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용 
		chooser.setDialogTitle("select HEIC folder"); // 창의 제목
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 파일 선택 모드

		int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈

		String folderPath = "";
		if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭 
		    folderPath = chooser.getSelectedFile().toString();
		}else if(returnVal == JFileChooser.CANCEL_OPTION){ // 취소를 클릭
		    folderPath = "";
		}
		return folderPath;        
	}
	
	public static LinkedList<String> RecursiveGetFileList(String dirPath, LinkedList<String> rtnFileList) {
		
		File file = new File(dirPath);
		File[] fileList = file.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			File fileUnit = fileList[i];
			String path = fileUnit.getPath();
			
			if( fileUnit.isFile() ) {
				String fileName = fileUnit.getName();
				if("".equals(fileName)) continue;
				String ext = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
				if( "heic".equals(ext) ) {
					rtnFileList.add(fileUnit.getPath());
				}
			}else if( fileUnit.isDirectory() ) {
				RecursiveGetFileList(path, rtnFileList);
			}
		}
		return rtnFileList;
	}
	
	public static void main(String[] args) {
		new HeicToJpg().prepareGUI();
	}
}
