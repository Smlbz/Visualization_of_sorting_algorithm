package Sort;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
public class Main {
	private ArrayList<Integer> number = new ArrayList<Integer>();
	public Main(JFrame frame) {
		frame.setTitle("������ӻ�");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1600, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        JButton BubbleButton=new JButton("ð������");
        BubbleButton.setBounds(10,650,200,100);
        BubbleButton.setFont(new Font("��Բ",Font.PLAIN,24));
        BubbleButton.setVisible(true);
        JButton QuickButton=new JButton("��������");
        QuickButton.setBounds(230,650,200,100);
        QuickButton.setFont(new Font("��Բ",Font.PLAIN,24));
        QuickButton.setVisible(true);
        JButton HeapButton=new JButton("������");
        HeapButton.setBounds(460,650,200,100);
        HeapButton.setFont(new Font("��Բ",Font.PLAIN,24));
        HeapButton.setVisible(true);
        JButton MergeButton=new JButton("�鲢����");
        MergeButton.setBounds(690,650,200,100);
        MergeButton.setFont(new Font("��Բ",Font.PLAIN,24));
        MergeButton.setVisible(true);
        JButton BackButton=new JButton("����");
        BackButton.setBounds(0, 0, 150, 75);
        BackButton.setFont(new Font("��Բ",Font.PLAIN,24));
        BackButton.setVisible(false);
        
        JPanel panel = new JPanel(new GridBagLayout());
        Font panelFont = new Font("��Բ",Font.PLAIN,16);
        Dimension fieldDimension = new Dimension(400,50);
        JTextField textField = new JTextField();
        textField.setPreferredSize(fieldDimension);
        textField.setFont(panelFont);
        panel.add(textField);
        Dimension buttonDimension = new Dimension(70,50);
        JButton submitButton = new JButton("����");
        submitButton.setPreferredSize(buttonDimension);
        submitButton.setFont(panelFont);
        panel.add(submitButton);
        JButton resetButton = new JButton("����");
        resetButton.setPreferredSize(buttonDimension);
        resetButton.setFont(panelFont);
        panel.add(resetButton);
        
        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(panelFont);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(false);
        scrollPane.setBounds(530,100,400,200);
        frame.getContentPane().add(scrollPane);
        
        panel.setBounds(100, 100, 1400,500);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	scrollPane.setVisible(true);
            	boolean isNum=isValid(textField.getText());
            	if(isNum) {
            		extractNumber(textField.getText(),number);
            		textArea.setText("����������: "+number.toString());
            	}
            	else {
            		textArea.setText("���ӵ�������������!");
            	}
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	scrollPane.setVisible(false);
            	number.clear();
            }
        });
        
        frame.add(BubbleButton);
        frame.add(QuickButton);
        frame.add(HeapButton);
        frame.add(MergeButton);
        frame.add(BackButton);
        frame.getContentPane().add(panel);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                double ratioWidth=frame.getWidth()*1.0/1600;
                double ratioHeight=frame.getHeight()*1.0/800;
                BubbleButton.setBounds((int) (10*ratioWidth), (int)(650*ratioHeight),(int)(200*ratioWidth),(int)(100*ratioHeight));
                QuickButton.setBounds((int) (230*ratioWidth), (int)(650*ratioHeight),(int)(200*ratioWidth),(int)(100*ratioHeight));
                HeapButton.setBounds((int) (460*ratioWidth), (int)(650*ratioHeight),(int)(200*ratioWidth),(int)(100*ratioHeight));
                MergeButton.setBounds((int) (690*ratioWidth), (int)(650*ratioHeight),(int)(200*ratioWidth),(int)(100*ratioHeight));
                panel.setBounds((int) (100*ratioWidth), (int)(100*ratioHeight),(int)(1400*ratioWidth),(int)(500*ratioHeight));
                scrollPane.setBounds((int) (530*ratioWidth), (int)(100*ratioHeight),(int)(400*ratioWidth),(int)(200*ratioHeight));
            }
        });
        //����
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        // ��Ӽ��̼�������� Backspace ��,���¼����������㷨ѡ��
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    frame.getContentPane().removeAll();
                    frame.getContentPane().revalidate();
                    frame.getContentPane().repaint();
                    new Main(frame);
                }
            }
        });
        
        BackButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
                new Main(frame);
                BackButton.setVisible(true);
        	}
        });
        
        BubbleButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		BubbleButton.setVisible(false);
        		QuickButton.setVisible(false);
        		HeapButton.setVisible(false);
        		MergeButton.setVisible(false);
        		panel.setVisible(false);
        		scrollPane.setVisible(false);
        		new BubbleSort(frame,number);
        		frame.repaint();
        		BackButton.setVisible(true);
        		frame.repaint();
        	}
        });
        QuickButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		QuickButton.setVisible(false);
        		BubbleButton.setVisible(false);
        		HeapButton.setVisible(false);
        		MergeButton.setVisible(false);
        		panel.setVisible(false);
        		scrollPane.setVisible(false);
        		new QuickSort(frame,number);
        		frame.repaint();
        		BackButton.setVisible(true);
        		frame.repaint();
        	}
        });
        HeapButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		QuickButton.setVisible(false);
        		BubbleButton.setVisible(false);
        		HeapButton.setVisible(false);
        		MergeButton.setVisible(false);
        		panel.setVisible(false);
        		scrollPane.setVisible(false);
        		new HeapSort(frame,number);
        		frame.repaint();
        		BackButton.setVisible(true);
        		frame.repaint();
        	}
        });
        MergeButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		QuickButton.setVisible(false);
        		BubbleButton.setVisible(false);
        		HeapButton.setVisible(false);
        		MergeButton.setVisible(false);
        		panel.setVisible(false);
        		scrollPane.setVisible(false);
        		new MergeSort(frame,number);
        		frame.repaint();
        		BackButton.setVisible(true);
        		frame.repaint();
        	}
        });
	}
	public static boolean isValid(String input) {
    	//������ʽ,֧�ֶ���(,)�Ϳո�( )��Ϊ�ָ��,
    	//��֧�����Ķ��źͿո�,��֧�ַ�ʮ������,��֧�ֺ���ǰ��������͸���
    	//������������������60,�������������ִ���140
        String pattern = "^\\s*(0|([1-9]\\d*))(?:\\s*,\\s*(0|([1-9]\\d*)))*\\s*$";
        return Pattern.matches(pattern, input);
    }
    public static void extractNumber(String input,ArrayList<Integer> number) {
    	//���������ַ���ת��Ϊ����
        String pattern = "\\b\\d+\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        while (m.find()) {
            number.add(Integer.parseInt(m.group()));
        }
    }
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("������ӻ�");
            new Main(frame);
        });
	}
}
