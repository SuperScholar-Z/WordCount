import java.io.*;
import java.util.ArrayList;

public class WordCount  //统计字符类
{
    private File sourceFile;    //要读取的文件
    private File writeFile;     //输出结果保存文件
    private BufferedReader input;   //输入流
    private BufferedWriter output;  //输出流
    private String line = "";    //一行数据
    private ArrayList<String>stopWord = new ArrayList<String>();    //停用词

    public WordCount(String writeName) throws IOException    //初始化输出文件
    {
        this.writeFile = new File(writeName);
        if(!this.writeFile.exists())
        {
            this.writeFile.createNewFile(); // 创建新文件
        }
        output = new BufferedWriter(new FileWriter(this.writeFile));
    }
    public void setSourceFile(String sourceName) throws IOException //设置输入文件名
    {
        this.sourceFile = new File(sourceName);
        this.input = new BufferedReader(new InputStreamReader(new FileInputStream(this.sourceFile)));
    }

    public void Close() throws IOException    //关闭文件
    {
        if(output != null)
        {
            output.flush(); //把缓存区内容压入文件
            //关闭文件
            output.close();
            input.close();
        }
    }

    public void setStopFile(String stopName) throws FileNotFoundException, IOException  //获取停用词文件
    {
        //打开文件
        File stopFile = new File(stopName);
        BufferedReader stopInput = new BufferedReader(new InputStreamReader(new FileInputStream(stopFile)));

        while( (line = stopInput.readLine()) != null)   //获取所有停用词
        {
            String[] currentWord = line.split(" ");
            for(int i = 0; i < currentWord.length; i++)
            {
                if(currentWord[i] != null && currentWord[i].length() > 0) //去掉空字符串
                    stopWord.add(currentWord[i]);
            }
        }

        //关闭文件
        stopInput.close();
    }

    public void Count(boolean[] functions) throws IOException  //统计
    {
        int charNum = -1;   //字符数
        int wordNum = 0;    //单词数
        int lineNum = 0;    //总行数
        int codeLineNum = 0; //代码行
        int emptyLineNum = 0;   //空行
        int noteLineNum = 0;    //注释行
        boolean isNode = false; //下行是否为/* 与 */之间的注释行
        while( (line = this.input.readLine()) != null)
        {
            if(functions[0])    //统计字符数
                charNum += line.length() + 1;
            if(functions[1])    //统计单词数
            {
                String[] currentWord = line.split(",| |\t", -1);    //当前行识别的单词
                for(int i = 0; i < currentWord.length; i++)
                {
                    if(currentWord[i] != null && currentWord[i].length() > 0) //去掉空字符串
                    {
                        if(functions[6])    //停用词
                        {
                            if(stopWord.indexOf(currentWord[i]) == -1)
                                wordNum += 1;
                        }
                        else
                            wordNum += 1;
                    }
                }
            }
            if(functions[2])    //统计总行数
                lineNum += 1;
            if(functions[5])    //统计代码行、空行、注释行
            {
                String line = this.line.replaceAll(" ","").replace("\t","");  //去除空格和\t
                int[] noteIndex = new int[5];  //"/*"、"//"、"*/"所在下标
                noteIndex[0] = line.indexOf("/*");
                noteIndex[1] = line.indexOf("//");
                noteIndex[2] = line.indexOf("*/");
                noteIndex[3] = line.lastIndexOf("/*");
                noteIndex[4] = line.lastIndexOf("*/");
                if(noteIndex[0] == 0 || noteIndex[0] == 1)  //"/*"注释在行首
                {
                    if(noteIndex[2] == line.length() - 2)   // "/*...*/"占整行
                        noteLineNum += 1;
                    else if(noteIndex[2] == -1) //该行"/*"未结束
                    {
                        noteLineNum += 1;
                        isNode = true;
                    }
                    else
                        codeLineNum += 1;
                }
                else if(noteIndex[1] == 0 || noteIndex[1] == 1)   //"//"在行首
                    noteLineNum += 1;
                else if(noteIndex[2] != -1 && noteIndex[2] == line.length() - 2 && isNode) //"*/"注释在行尾且上一行"/*"未结束
                {
                    noteLineNum += 1;
                    isNode = false;
                }
                else if(isNode && noteIndex[2] == -1) //"/*"为未结束
                    noteLineNum += 1;
                else if(line == null || line.length() <= 1)    //空行
                    emptyLineNum += 1;
                else
                {
                    codeLineNum += 1;
                    if(isNode && noteIndex[2] != -1)
                        isNode = false;
                }
            }
        }
        if(charNum < 0)
            charNum = 0;

        if(functions[0])    //输出字符数
            output.write(sourceFile.getName() + ", 字符数：" + charNum + "\r\n");
        if(functions[1])    //输出单词数
            output.write(sourceFile.getName() + ", 单词数：" + wordNum + "\r\n");
        if(functions[2])    //输出总行数
            output.write(sourceFile.getName() + ", 总行数：" + lineNum + "\r\n");
        if(functions[5])    //输出代码行、空行、注释行
            output.write(sourceFile.getName() + ", 代码行/空行/注释行：" + codeLineNum + "/" + emptyLineNum + "/" + noteLineNum + "\r\n");
    }
}