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

    public WordCount(String sourceName, String writeName) throws IOException
    {
        this.sourceFile = new File(sourceName);
        this.input = new BufferedReader(new InputStreamReader(new FileInputStream(this.sourceFile)));

        this.writeFile = new File(writeName);
        if(!this.writeFile.exists())
        {
            this.writeFile.createNewFile(); // 创建新文件
        }
        output = new BufferedWriter(new FileWriter(this.writeFile));
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
            String currentWord = "";    //当前识别的单词
            int pHead = 0;  //识别的单词首字母所在下标
            for(int i = 0; i < line.length(); i++)
            {
                if (line.charAt(i) != 32)
                {
                    //识别单词开头
                    if(i == 0)  //字符在行首为词首
                        pHead = 0;
                    else if(line.charAt(i - 1) == 32)   //字符前有分隔符为词首
                        pHead = i;
                    //识别单词结尾
                    if(i == line.length() - 1)  //字符在行末为词尾
                        stopWord.add(line.substring(pHead));
                    else if(line.charAt(i + 1) == 32)   //字符后有分隔符为词尾
                        stopWord.add(line.substring(pHead, i + 1));
                }
            }
        }

        //关闭文件
        stopInput.close();
    }

    public void Count(boolean[] functions) throws IOException  //统计
    {
        int charNum = 0;    //字符数
        int wordNum = 0;    //单词数
        int lineNum = 0;    //总行数
        while( (line = this.input.readLine()) != null)
        {
            if(functions[0])    //统计字符数
                charNum += line.length();
            if(functions[1])    //统计单词数
            {
                String currentWord = "";    //当前识别的单词
                int pHead = 0;  //识别的单词首字母所在下标
                for(int i = 0; i < line.length(); i++)
                {
                    char currentChar = line.charAt(i);  //获取当前单个字符
                    if (currentChar != 32 && currentChar != ',' && currentChar != '\t') //当前字符不是逗号、空格、'\t'时
                    {
                        if(i == 0)  //若在行首，则记一个单词开始
                        {
                            pHead = 0;
                            wordNum += 1;
                        }
                        else
                        {
                            char previousChar = line.charAt(i - 1); //前一字符
                            if(previousChar == 32 || previousChar == ',' || previousChar == '\t')   //若前面有逗号、空格、'\t'隔开，则也记一个单词开始
                            {
                                pHead = i;
                                wordNum += 1;
                            }
                        }

                        if(functions[6])    //停用词
                        {
                            if (i == line.length() - 1)  //若在行末，则记单词的结束
                            {
                                currentWord = line.substring(pHead);
                                if(stopWord.indexOf(currentWord) != -1) //识别单词在停用词列表中
                                    wordNum -= 1;
                            }
                            else
                            {
                                char behindChar = line.charAt(i + 1);   //后一字符
                                if (behindChar == 32 || behindChar == ',' || behindChar == '\t') //若后面有逗号、空格、'\t'隔开，则也记一个单词开始
                                {
                                    currentWord = line.substring(pHead, i + 1);
                                    if(stopWord.indexOf(currentWord) != -1) //识别单词在停用词列表中
                                        wordNum -= 1;
                                }
                            }
                        }
                    }
                }
            }
            if(functions[2])    //统计总行数
                lineNum += 1;
        }

        if(functions[0])    //输出字符数
            output.write(sourceFile.getName() + ", 字符数：" + charNum + "\r\n");
        if(functions[1])    //输出单词数
            output.write(sourceFile.getName() + ", 单词数：" + wordNum + "\r\n");
        if(functions[2])    //输出总行数
            output.write(sourceFile.getName() + ", 总行数：" + lineNum + "\r\n");
    }
}