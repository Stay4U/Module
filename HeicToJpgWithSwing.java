import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

public class HeicToJpg {
	private JFrame mf;
	private JLabel srcFolderLbl;
	private JButton folderOpenBtn;
	private JButton fileConvertBtn;
	private DefaultListModel<String> listModel;
	private JList<DefaultListModel<String>> srcFileListLbl;

	private void prepareGUI() {
		mf = new JFrame("Heic To Jpg");
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mf.setLocation(10, 10);
		mf.setSize(600, 600);
		mf.setLayout(null);
		
		srcFolderLbl = new JLabel("원본폴더");
		srcFolderLbl.setLocation(10, 10);
		srcFolderLbl.setSize(100, 50);
		srcFolderLbl.setBackground(Color.darkGray);
		srcFolderLbl.setOpaque(true);
		mf.add(srcFolderLbl);
		
		folderOpenBtn = new JButton("폴더열기");
		folderOpenBtn.setActionCommand("folderOpen");
		folderOpenBtn.setLocation(120, 10);
		folderOpenBtn.setSize(100, 50);
		folderOpenBtn.addActionListener(new ButtonClickListener());
		mf.add(folderOpenBtn);

		fileConvertBtn = new JButton("파일변환");
		fileConvertBtn.setActionCommand("fileConvert");
		fileConvertBtn.setLocation(330, 10);
		fileConvertBtn.setSize(100, 50);
		fileConvertBtn.addActionListener(new ButtonClickListener());
		mf.add(fileConvertBtn);
		
		mf.setVisible(true);
	}

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			if (command.equals("folderOpen")) {
				//String path = jFileChooserUtil();
				//statusLabel.setText(path);
			} else if (command.equals("fileConvert")) {
				//statusLabel.setText("Submit Button clicked.");
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
	
	public static void main(String[] args) {
		new HeicToJpg().prepareGUI();
	}
}
