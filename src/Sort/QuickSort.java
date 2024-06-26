package Sort;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuickSort {
    public QuickSort(JFrame frame,ArrayList<Integer>number) {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle("快速排序可视化");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(1600, 800);

            QuickSortingPanel panel = new QuickSortingPanel(number);
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
    		    "private void quickSort(int[] arr, int low, int high) {",
    		    "    if (low < high) {",
    		    "        int pi = partition(arr, low, high);",
    		    "        quickSort(arr, low, pi - 1);",
    		    "        quickSort(arr, pi + 1, high);",
    		    "    }",
    		    "}",
    		    "",
    		    "private int partition(int[] arr, int low, int high) {",
    		    "    int pivot = arr[high];",
    		    "    int i = (low - 1);",
    		    "    for (int j = low; j < high; j++) {",
    		    "        if (arr[j] < pivot) {",
    		    "            i++;",
    		    "            int temp = arr[i];",
    		    "            arr[i] = arr[j];",
    		    "            arr[j] = temp;",
    		    "        }",
    		    "    }",
    		    "    int temp = arr[i + 1];",
    		    "    arr[i + 1] = arr[high];",
    		    "    arr[high] = temp;",
    		    "    return i + 1;",
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

class QuickSortingPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private final int[] numbers;
    private final int[] pos;
    private int currentBar = -1;
    private int swapBar = -1;
    private JTextPane codePane;
    private SimpleAttributeSet defaultAttr;
    private SimpleAttributeSet highlightAttr;
    private int firstNum = -1;
    private int secondNum = -1;

    public QuickSortingPanel(ArrayList<Integer>number) {
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
        Map<Integer, Integer> elementToIndex = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            if (elementToIndex.containsKey(numbers[i])) {
                // 找到重复值，返回第一对重复值的下标
                firstNum = elementToIndex.get(numbers[i]);
                secondNum = i;
            }
            elementToIndex.put(numbers[i], i);
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
        doc.setCharacterAttributes(0, doc.getLength(), defaultAttr, false); // Remove all highlights
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
            }
            else if(pos[i] == firstNum) {
            	g.setColor(Color.RED);
            }
            else if(pos[i] == secondNum) {
            	g.setColor(Color.BLUE);
            }
            else {
                g.setColor(new Color(0xFF8C00)); // 其他条形图设置颜色
            }
            g.fillRect(x, getHeight() - height, width - 2, height); // 绘制条形图
            g.setColor(Color.BLACK);
            Font font = new Font("Century Gothic", Font.PLAIN, 16);//设置字体大小
            g.setFont(font);
            g.drawString(Integer.toString(numbers[i]), x, getHeight() - height - 5); // 在每个条形图上方绘制数字
            font = new Font("Century Gothic", Font.BOLD, 28);
            g.setFont(font);
            g.drawString(Integer.toString(pos[i]), x, getHeight() - height - 25);
        }
    }
    // 快速排序的辅助函数，用于进行分区操作
    private int partition(int[] numbers, int[]pos,int low, int high) throws InterruptedException {
        int pivot = numbers[high];
        int i = (low - 1); // 较小元素的索引
        for (int j = low; j < high; j++) {
            // 如果当前元素小于或等于 pivot
            highlightCodeLine(12); // 执行if 语句
            repaint();
            Thread.sleep(200);
            if (numbers[j] <= pivot) {
                i++;
                // 交换 numbers[i] 和 numbers[j]
                currentBar = i;
                swapBar = j;
                highlightCodeLine(13); 
                repaint();
                Thread.sleep(200);
                int temp = numbers[i];
                int poswap = pos[i];
                pos[i] = pos[j];
                pos[j] = poswap;
                numbers[i] = numbers[j];
                numbers[j] = temp;
                // 高亮正在交换的元素
                highlightCodeLine(14);
                repaint();
                Thread.sleep(200);
                highlightCodeLine(15); 
                repaint();
                Thread.sleep(200);
            }
        }
        // 交换 numbers[i+1] 和 numbers[high]（或 pivot）
        currentBar = i+1;
        swapBar = high;
        highlightCodeLine(19); 
        repaint();
        Thread.sleep(200);
        int temp = numbers[i + 1];
        int poswap = pos[i + 1];
        pos[i + 1] = pos[high];
        pos[high] = poswap;
        numbers[i + 1] = numbers[high];
        numbers[high] = temp;
        highlightCodeLine(20);
        repaint();
        Thread.sleep(200);
        highlightCodeLine(21); 
        repaint();
        Thread.sleep(200);
        return i + 1;
    }
    // 主快速排序函数
    private void quickSort(int[] numbers, int[]pos,int low, int high) throws InterruptedException {
        highlightCodeLine(0); // 高亮quickSort
        repaint();
        Thread.sleep(200);
    	highlightCodeLine(1); // 高亮if
        repaint();
        Thread.sleep(200);
    	if (low < high) {
            int pi = partition(numbers, pos,low, high);
            highlightCodeLine(2); // 高亮partition
            repaint();
            Thread.sleep(200);
            // 递归排序 pivot 两侧的元素
            quickSort(numbers, pos,low, pi - 1);
            highlightCodeLine(3); // 高亮递归
            repaint();
            Thread.sleep(200);
            quickSort(numbers,pos,pi + 1, high);
            highlightCodeLine(4); // 高亮递归
            repaint();
            Thread.sleep(200);
        }
    }

    public void sort() {
        try {
            int high = numbers.length - 1;
            quickSort(numbers,pos,0, high);
            // 清除高亮显示
            swapBar = -1;
            currentBar = -1;
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