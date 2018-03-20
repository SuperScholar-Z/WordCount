wc.exe -c test0.c -o result0.txt
wc.exe -c test1.c -o result1.txt
wc.exe -w test2.c -o result2.txt
wc.exe -w test3.c -o result3.txt
wc.exe -w test2.c -o result4.txt -e stoplist1.txt
wc.exe -w test2.c -o result5.txt -e stoplist2.txt
wc.exe -l test2.c -o result6.txt
wc.exe -a test4.c -o result7.txt
wc.exe -a test5.c -o result8.txt
wc.exe -a test6.c -o result9.txt
wc.exe -a test7.c -o result10.txt
wc.exe -a test8.c -o result11.txt
wc.exe -c -w -l -a test9.c -o result12.txt -e stoplist2.txt
wc.exe -c -w -l -a test9.c -e stoplist2.txt
wc.exe -c -w -l -a test\test10.c -o test\result13.txt -e test\stoplist3.txt
wc.exe -c -w -l -a *.c -o result14.txt
wc.exe -c -w -l -a -s *.c -o result15.txt
wc.exe -c -w -l -a test\*.cpp -o result16.txt
wc.exe -c -w -l -a test.c -o
wc.exe -c -w -l -a test.c -o result17.txt -e
wc.exe -c -w -l -a -o result18.txt
wc.exe -c -w -l test.c -o result19.txt -a