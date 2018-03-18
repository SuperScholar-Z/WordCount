import java.io.*;

public class WordCount  //统计字符类
{
    private File sourceFile;    //要读取的文件
    private File writeFile;     //输出结果保存文件
    BufferedReader input;   //输入流
    BufferedWriter output;  //输出流
    String line = "";    //一行数据

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
            output.close(); //关闭文件
        }
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
                for(int i = 0; i < line.length(); i++)
                {
                    char currentChar = line.charAt(i);  //获取当前单个字符
                    if (currentChar != 32 && currentChar != ',' && currentChar != '\t') //当前字符不是逗号、空格、'\t'时
                    {
                        if(i == 0)  //若在行首，则记一个单词
                            wordNum += 1;
                        else
                        {
                            char previousChar = line.charAt(i - 1);
                            if(previousChar == 32 || previousChar == ',' || previousChar == '\t')   //若前面有逗号、空格、'\t'隔开，则也记一个单词
                                wordNum += 1;
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