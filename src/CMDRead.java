import java.io.*;
import java.util.ArrayList;

public class CMDRead
{
    private static void getAllFile(ArrayList<String>sourceNameArray, String source, String rootPath, boolean function_s)   //获取所有文件
    {
        if(function_s)
        {
            File rootFile = new File(rootPath);
            File[] files = rootFile.listFiles();
            if (files != null)
            {
                for (File f : files)
                {
                    if (f.isDirectory()) //判断是文件夹
                        getAllFile(sourceNameArray, source, f.getPath(), true);
                    else if (source.equals(f.getName()))
                        sourceNameArray.add(f.getPath());
                }
            }
        }
        else
            sourceNameArray.add(rootPath + source);
    }
    private static void getAllFile(ArrayList<String>sourceNameArray, String source, String rootPath, String keyword, boolean function_s)   //获取所有文件("*.c")
    {
        File rootFile = new File(rootPath);
        File[] files = rootFile.listFiles();
        if (files != null)
        {
            for (File f : files)
            {
                if (f.isDirectory() && function_s) //判断是文件夹
                    getAllFile(sourceNameArray, source, f.getPath(), keyword, true);
                else if (f.getName().indexOf(keyword) == f.getName().length() - 2)
                    sourceNameArray.add(f.getPath());
            }
        }
    }


    public static void main(String[] args) throws IOException
    {
        ArrayList<String> sourceNameArray = new ArrayList<String>();  //读取输入路径
        String writeName = "result.txt";   //输出结果保存路径
        String stopName = "";   //停用词文件路径
        boolean[] functions = new boolean[7];   //标记功能是否使用，分别为-c,-w,-l,-o,-s,-a,-e，true为使用该功能
        for (int i = 0; i < functions.length; i++)  //初始化所有functions为false
            functions[i] = false;
        boolean inputError = false; //输入格式错误

        for (int i = 0; i < args.length; i++)   //读取命令行指令
        {
            if(args[i].equals("-c"))    //'-c'字符数
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
                    sourceNameArray.add(args[i]);
                }
            }
            else if(args[i].equals("-w"))   //'-w'单词数
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
                    sourceNameArray.add(args[i]);
                }
            }
            else if(args[i].equals("-l"))   //'-l'总行数
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
                    sourceNameArray.add(args[i]);
                }
            }
            else if(args[i].equals("-o"))   //'-o'指定输出文件名
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
            else if(args[i].equals("-s"))   //'-s'递归处理子文件夹
            {
                functions[4] = true;
                if (i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                if (args[i + 1].charAt(0) != '-')
                {
                    i++;
                    sourceNameArray.add(args[i]);
                }
            }
            else if(args[i].equals("-a"))   //'-a'代码行/空行/注释行
            {
                functions[5] = true;
                if(i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                if(args[i + 1].charAt(0) != '-')
                {
                    i++;
                    sourceNameArray.add(args[i]);
                }
            }
            else if(args[i].equals("-e"))   //停用词表
            {
                functions[6] = true;
                if(i + 1 == args.length)
                {
                    inputError = true;
                    break;
                }
                i++;
                stopName = args[i];
            }
            else
            {
                inputError = true;
                break;
            }
        }

        if(sourceNameArray == null || sourceNameArray.size() == 0)
            inputError = true;
        else    //读取所有文件名
        {
            String source = sourceNameArray.get(0);
            int indexOfStar = source.indexOf("*.");
            String rootPath = "./";   //根目录
            String keyword = "";    //匹配关键字
            if (indexOfStar == source.length() - 3) //有"*.c"时搜寻所有文件
            {
                if (indexOfStar > 0)
                    rootPath = source.substring(0, indexOfStar);
                keyword = source.substring(indexOfStar + 1);
                sourceNameArray.remove(0);
                getAllFile(sourceNameArray, source, rootPath, keyword, functions[4]);
            }
            else
            {
                if(source.lastIndexOf("\\") != -1)
                {
                    rootPath = source.substring(0, source.lastIndexOf("\\") + 1);
                    source = source.substring(source.lastIndexOf("\\") + 1);
                }
                sourceNameArray.remove(0);
                getAllFile(sourceNameArray, source, rootPath, functions[4]);
            }
        }

        if(inputError || sourceNameArray == null || sourceNameArray.size() == 0)
        {
            System.out.println("输入格式有误！");
            File errorFile = new File("error.txt");
            if(errorFile.exists())
            {
                errorFile.createNewFile(); // 创建新文件
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(errorFile));
            output.write("输入格式有误！");
            output.flush();
            output.close();
        }
        else
        {
            WordCount wordcount = new WordCount(writeName); //统计字符类
            if(functions[6])    //设置停用词
                wordcount.setStopFile(stopName);
            for(int i = 0; i < sourceNameArray.size(); i++)
            {
                wordcount.setSourceFile(sourceNameArray.get(i));
                wordcount.Count(functions); //计数
            }
            wordcount.Close();
        }
    }
}