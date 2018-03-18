import java.io.*;

public class CMDRead
{
    public static void main(String[] args) throws IOException
    {
        String sourceName = null;  //读取的文件路径
        String writeName = "result.txt";   //输出结果保存路径
        boolean[] functions = new boolean[7];   //标记功能是否使用，分别为-c,-w,-l,-o,-s,-a,-e，true为使用该功能
        for (int i = 0; i < functions.length; i++)  //初始化所有functions为false
            functions[i] = false;
        boolean inputError = false; //输入格式错误

        for (int i = 0; i < args.length; i++)   //读取命令行指令
        {
            if(args[i].equals("-c"))
            {
                functions[0] = true;
                if(i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                if(args[i + 1].charAt(0) != '-')
                {
                    i++;
                    sourceName = args[i];
                }
            }
            else if(args[i].equals("-w"))
            {
                functions[1] = true;
                if(i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                if(args[i + 1].charAt(0) != '-')
                {
                    i++;
                    sourceName = args[i];
                }
            }
            else if(args[i].equals("-l"))
            {
                functions[2] = true;
                if(i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                if(args[i + 1].charAt(0) != '-')
                {
                    i++;
                    sourceName = args[i];
                }
            }
            else if(args[i].equals("-o"))
            {
                functions[3] = true;
                if(i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                i++;
                writeName = args[i];
            }
            else if(args[i].equals("-s"))
                functions[4] = true;
            else if(args[i].equals("-a"))
                functions[5] = true;
            else if(args[i].equals("-e"))
                functions[6] = true;
        }

        if(sourceName == null || inputError)
            System.out.println("输入格式有误！");
        else
        {
            WordCount wordcount = new WordCount(sourceName, writeName); //统计字符类
            wordcount.Count(functions);
            wordcount.Close();
        }
    }
}