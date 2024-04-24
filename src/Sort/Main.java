package Sort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
public class Main {
	public Main(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1600, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JButton BubbleButton=new JButton("冒泡排序");
        BubbleButton.setBounds(10,650,200,100);
        BubbleButton.setVisible(true);
        JButton QuickButton=new JButton("快速排序");
        QuickButton.setBounds(230,650,200,100);
        QuickButton.setVisible(true);
        frame.add(BubbleButton);
        frame.add(QuickButton);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                double ratioWidth=frame.getWidth()*1.0/1600;
                double ratioHeight=frame.getHeight()*1.0/800;
                BubbleButton.setBounds((int) (10*ratioWidth), (int)(650*ratioHeight),(int)(200*ratioWidth),(int)(100*ratioHeight));
                QuickButton.setBounds((int) (230*ratioWidth), (int)(650*ratioHeight),(int)(200*ratioWidth),(int)(100*ratioHeight));
            }
        });
        //返回
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
        String actionKey = "onBackspacePressed"; 
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	frame.getContentPane().removeAll();
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
                new Main(frame);
            }
        };
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();
        inputMap.put(keyStroke, actionKey);
        actionMap.put(actionKey, action);
        
        BubbleButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		BubbleButton.setVisible(false);
        		QuickButton.setVisible(false);
        		new BubbleSort(frame);
        	}
        });
        QuickButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		QuickButton.setVisible(false);
        		BubbleButton.setVisible(false);
        		new QuickSort(frame);
        	}
        });
	}
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("排序可视化");
            new Main(frame);
        });
	}
}
