package Sort;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MergeSort {
    public MergeSort(JFrame frame,ArrayList<Integer> number) {
        SwingUtilities.invokeLater(() -> {
        	frame.setTitle("归并排序可视化");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(1600, 800);

            MergeSortingPanel panel = new MergeSortingPanel(number);
            frame.add(panel, BorderLayout.CENTER);

            JTextPane codePane = new JTextPane();
            codePane.setEditable(false);
            initCodePane(codePane);
            JScrollPane codeScrollPane = new JScrollPane(codePane);
            codeScrollPane.setPreferredSize(new Dimension(600, 800)); // 设置代码区域大小
            frame.add(codeScrollPane, BorderLayout.EAST);

            panel.setCodePane(codePane); // 传递codePane到排序面板

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            new Thread(panel::sort).start();
        });
    }

    private void initCodePane(JTextPane codePane) {
        String[] codeLines = {
        		"private void merge(int arr[], int left, int middle, int right) {",
        		"    int n1 = middle - left + 1;",
        		"    int n2 = right - middle;",
        		"    int leftArr[] = new int[n1];",
        		"    int rightArr[] = new int[n2];",
        		"    for (int i = 0; i < n1; ++i)",
        		"        leftArr[i] = arr[left + i];",
        		"    for (int j = 0; j < n2; ++j)",
        		"        rightArr[j] = arr[middle + 1 + j]; ",
        		"    int i = 0, j = 0;",
        		"    int k = left;",
        		"    while (i < n1 && j < n2) {",
        		"        if (leftArr[i] <= rightArr[j]) {",
        		"            arr[k] = leftArr[i];",
        		"            i++;",
        		"        } else {",
        		"            arr[k] = rightArr[j];",
        		"            j++;",
        		"        }",
        		"        k++;",
        		"    }",
        		"    while (i < n1) {",
        		"        arr[k] = leftArr[i];",
        		"        i++;",
        		"        k++;",
        		"    }",
        		"    while (j < n2) {",
        		"        arr[k] = rightArr[j];",
        		"        j++;",
        		"        k++;",
        		"    }",
        		"}",
        		"private void mergesort(int arr[], int left, int right) {",
        		"    if (left < right) {",
        		"        int middle = (left + right) / 2;",
        		"        sort(arr, left, middle);",
        		"        sort(arr, middle + 1, right);",
        		"        merge(arr, left, middle, right);",
        		"    }",
        		"}"
        };
        StyledDocument doc = codePane.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontSize(attr,22);
        try {
            for (String line : codeLines) {
                doc.insertString(doc.getLength(), line + "\n", attr);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

class MergeSortingPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private final int[] numbers;
    private final int[] pos;
    private int currentBar = -1;
    private int swapBar = -1;
    private JTextPane codePane;
    private SimpleAttributeSet defaultAttr;
    private SimpleAttributeSet highlightAttr;

    public MergeSortingPanel(ArrayList<Integer>number) {
    	if(number.size()==0) {
            numbers = new int[20];
            pos = new int[20];
            Random rand = new Random();
            for (int i = 0; i < 20; i++) {
                numbers[i] = rand.nextInt(100) + 1;
                pos[i] = i;
            }
    	}
    	else {
    		numbers = number.stream().mapToInt(Integer::intValue).toArray();
    		int l = numbers.length;
    		pos = new int[l];
    		for(int i=0; i < l ; i++) {
    			pos[i] = i;
    		}
    	}
        initStyles();
    }

    public void setCodePane(JTextPane codePane) {
        this.codePane = codePane;
    }

    private void initStyles() {
        defaultAttr = new SimpleAttributeSet();
        StyleConstants.setBackground(defaultAttr, Color.WHITE);

        highlightAttr = new SimpleAttributeSet();
        StyleConstants.setBackground(highlightAttr, new Color(255, 255, 0));
    }

    private void highlightCodeLine(int lineNumber) {
        if (codePane == null) return;
        StyledDocument doc = codePane.getStyledDocument();
        if(lineNumber==-1) {
        	doc.setCharacterAttributes(0, doc.getLength(), defaultAttr, false);
        	return;
        }
        Element root = doc.getDefaultRootElement();
        Element lineElem = root.getElement(lineNumber);
        doc.setCharacterAttributes(0, doc.getLength(), defaultAttr, false);
        if (lineElem != null) {
            doc.setCharacterAttributes(lineElem.getStartOffset(), lineElem.getEndOffset() - lineElem.getStartOffset(), highlightAttr, false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / numbers.length; // 计算每个条形图的宽度
        for (int i = 0; i < numbers.length; i++) {
            int height = numbers[i] * 5; // 设置条形图的高度
            int x = i * width;
            if (i == currentBar || i == swapBar) {
                g.setColor(new Color(0xFFB6C1)); // 将正在比较或交换的条形图设置颜色
            } else {
                g.setColor(new Color(0xFF8C00)); // 其他条形图设置颜色
            }
            g.fillRect(x, getHeight() - height, width - 2, height); // 绘制条形图
            Font font = new Font("Century Gothic", Font.PLAIN, 16);//设置字体大小
            g.setFont(font);
            g.drawString(Integer.toString(numbers[i]), x, getHeight() - height - 5); // 在每个条形图上方绘制数字
            font = new Font("Century Gothic", Font.BOLD, 28);
            g.setFont(font);
            g.drawString(Integer.toString(pos[i]), x, getHeight() - height - 25);
        }
    }
    private void merge(int arr[], int pos[],int left, int middle, int right) throws InterruptedException {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        int leftArr[] = new int[n1];
        int rightArr[] = new int[n2];
        int leftPos[] = new int[n1];
        int rightPos[] = new int[n2];
        highlightCodeLine(5);
        repaint();
        Thread.sleep(200); 
        for (int i = 0; i < n1; ++i) {
        	currentBar=left+i;
            highlightCodeLine(6);
            repaint();
            Thread.sleep(200); 
            leftArr[i] = arr[left + i];
            leftPos[i] = pos[left + i];
        }
        highlightCodeLine(8);
        repaint();
        Thread.sleep(200); 
        for (int j = 0; j < n2; ++j) {
        	currentBar=middle+1+j;
            highlightCodeLine(9);
            repaint();
            Thread.sleep(200); 
            rightArr[j] = arr[middle + 1 + j]; 
            rightPos[j] = pos[middle + 1 + j];
        }
        int i = 0, j = 0;
        int k = left;
        highlightCodeLine(13);
        repaint();
        Thread.sleep(200); 
        while (i < n1 && j < n2) {
            highlightCodeLine(14);
            repaint();
            Thread.sleep(200); 
            if (leftArr[i] <= rightArr[j]) {
                currentBar=k;
                highlightCodeLine(15);
                repaint();
                Thread.sleep(200); 
                arr[k] = leftArr[i];
                pos[k] = leftPos[i];
                i++;
            }
            else {
                currentBar=k;
                highlightCodeLine(19);
                repaint();
                Thread.sleep(200); 
                arr[k] = rightArr[j];
                pos[k] = rightPos[j];
                j++;
            }
            k++;
        }
        highlightCodeLine(24);
        repaint();
        Thread.sleep(200); 
        while (i < n1) {
            currentBar=k;
            highlightCodeLine(25);
            repaint();
            Thread.sleep(200); 
            arr[k] = leftArr[i];
            pos[k] = leftPos[i];
            i++;
            k++;
        }
        highlightCodeLine(29);
        repaint();
        Thread.sleep(200); 
        while (j < n2) {
            currentBar=k;
            highlightCodeLine(30);
            repaint();
            Thread.sleep(200); 
            arr[k] = rightArr[j];
            pos[k] = rightPos[j];
            j++;
            k++;
        }
    }
    private void mergesort(int arr[], int pos[],int left, int right) throws InterruptedException {
        if (left < right) {
            int middle = (left + right) / 2;
            highlightCodeLine(35);
            repaint();
            Thread.sleep(200); 
            mergesort(arr,pos,left, middle);
            highlightCodeLine(36);
            repaint();
            Thread.sleep(200); 
            mergesort(arr,pos,middle + 1, right);
            highlightCodeLine(37);
            repaint();
            Thread.sleep(200); 
            highlightCodeLine(0);
            repaint();
            Thread.sleep(200); 
            merge(arr, pos,left, middle, right);
        }
    }
    public void sort() {
        try {
            int n = numbers.length;
            highlightCodeLine(32);
            repaint();
            Thread.sleep(200); 
            mergesort(numbers,pos,0,n-1);
        	swapBar = -1;
            currentBar=-1;
            highlightCodeLine(-1);
            while(true) {
            	repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sort();
    }
}