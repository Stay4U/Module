package com.heicToJpg;

import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class HeicToJpg {

    private JFrame mf;
    private JPanel mp;
    private JLabel folderPathTextLb;
    private JLabel folderPathLb;
    private JButton folderOpenBtn;
    private JButton fileConvertBtn;
    private JButton fileDeletetBtn;
    private JList<String> srcFileList;
    private DefaultListModel<String> srcFileListModel;
    private JScrollPane scrolledPane;

    private void prepareGUI() {
        mf = new JFrame("Heic To Jpg");
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setLocation(10, 10);
        mf.setSize(550, 850);
        mf.setLayout(new BorderLayout());

        mp = new JPanel();

        ButtonClickListener buttonClickListener = new ButtonClickListener();

        folderOpenBtn = new JButton("폴더열기");
        folderOpenBtn.setActionCommand("folderOpen");
        folderOpenBtn.addActionListener(buttonClickListener);
        mp.add(folderOpenBtn);

        fileConvertBtn = new JButton("파일변환");
        fileConvertBtn.setActionCommand("fileConvert");
        fileConvertBtn.addActionListener(buttonClickListener);
        mp.add(fileConvertBtn);

        fileDeletetBtn = new JButton("파일삭제");
        fileDeletetBtn.setActionCommand("fileDelete");
        fileDeletetBtn.addActionListener(buttonClickListener);
        mp.add(fileDeletetBtn);

        mf.add(mp, BorderLayout.NORTH);

        folderPathTextLb = new JLabel("경로 : ");
        mp.add(folderPathTextLb);

        folderPathLb = new JLabel("");
        mp.add(folderPathLb);

        srcFileList = new JList<String>(new DefaultListModel<String>());
        srcFileListModel = (DefaultListModel<String>)srcFileList.getModel();

        scrolledPane = new JScrollPane(srcFileList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mf.add(scrolledPane, BorderLayout.CENTER);

        mf.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("folderOpen")) {

                String path = jFileChooserUtil();
                if( "".equals(path) ) {
                    return;
                }

                folderPathLb.setText(path);

                if(srcFileListModel.size() > 0) {
                    srcFileListModel.removeAllElements();
                }

                LinkedList<String> list = new LinkedList<String>();

                RecursiveGetFileList(path, list);

                for (int i = 0; i < list.size(); i++) {
                    srcFileListModel.addElement(list.get(i));
                }

            } else if (command.equals("fileConvert")) {
                String path = folderPathLb.getText();
                if( "".equals(path) ){
                    JOptionPane.showMessageDialog(new JFrame(), "폴더를 선택하세요.", "알림", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int fileListSize = srcFileListModel.size();
                if( fileListSize <= 0 ){
                    JOptionPane.showMessageDialog(new JFrame(), "변환 대상 파일이 없습니다.", "알림", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (int i = 0; i < fileListSize; i++) {
                    String filePath = srcFileListModel.get(i);
                    heicToJpg(filePath);
                }

                JOptionPane.showMessageDialog(new JFrame(), "변환작업이 종료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } else if( command.equals("fileDelete") ){
                int confirmResult = JOptionPane.showConfirmDialog(null, "정말로 삭제하시겠습니까?", "Confirm", JOptionPane.YES_NO_OPTION);
                if( confirmResult == JOptionPane.YES_OPTION ){

                    int fileListSize = srcFileListModel.size();
                    for (int i = 0; i < fileListSize; i++) {
                        String filePath = srcFileListModel.get(i);
                        File file = new File(filePath);
                        if( file.exists() ){
                            file.delete();
                        }
                    }
                }
                if(srcFileListModel.size() > 0) {
                    srcFileListModel.removeAllElements();
                }
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

    public static void heicToJpg(String path){

        //magick 설치 후 전체경로
        String magick_path = "C:/Program Files/ImageMagick-7.1.0-Q16-HDRI/magick.exe";

        //원본이미지 전체경로
        String org_file = path;

        //저장될 이미지 전체경로
        String new_file = path.substring(0, path.lastIndexOf(".")+1) + "jpg";

        try {
            Process process = new ProcessBuilder("cmd", "/c", magick_path, "convert", org_file, new_file).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HeicToJpg().prepareGUI();
    }
}
