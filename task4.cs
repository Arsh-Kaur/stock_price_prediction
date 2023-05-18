using System;
namespace MyApp // Note: actual namespace depends on the project name.
{
    internal class stock_info
    {
        public int buyDay, sellDay, stockNumber;

    }
    internal class Program
    {
        int N, M;
        int[,] A = new int[3, 10];
        //int buyDay =0, sellDay =0, stockNumber;
        Dictionary<int, stock_info> info = new Dictionary<int, stock_info>();

        void updateStockInfo(int b, int s, int k)
        {
            if(info.ContainsKey(k))
            {
                stock_info inf = info[k];
            }
        }

        int findMaxProfit(int day, int stock, int k, bool buy, int buyIdx)
        {
            if (k == 0)
                return 0;

            if (day == N)
                return 0;

            int ret = -999, sameDay, nextDay, sellSameDay, sellNextDay;
            if(buy)
            {
                for(int j = 0; j < M; ++j)
                {
                    sameDay = findMaxProfit(day + 1, j, k, false, day);
                    if (ret < (-A[j, day] + sameDay) )
                    {
                        ret = -A[j, day] + sameDay;
                        /*if(info.ContainsKey(k))
                        {
                            (info[k] as stock_info).buyDay = day;

                            (info[k] as stock_info).stockNumber = j;
                        }
                        else
                        {
                            info[k] = new stock_info { buyDay = day, stockNumber = j };
                        }
                        */
                    }
                }

                nextDay =  findMaxProfit(day + 1, 0, k, true, buyIdx);
                if (ret < nextDay)
                {
                    ret = nextDay;
                    /*if (info.ContainsKey(k))
                    {
                        (info[k] as stock_info).buyDay = day + 1;
                    }
                    else
                    {
                        info[k] = new stock_info { buyDay = day };
                    }*/
                }
            }
            else
            {
                sellSameDay = A[stock, day] + findMaxProfit(day, 0, k - 1, true, -1);
                sellNextDay = findMaxProfit(day + 1, stock, k, false, buyIdx);
                if(buyIdx == 0 && day == 2)
                {
                    //tran1
                }
                if(buyIdx == 3 && day == 6)
                {
                    // tran2
                }
                if(buyIdx == 6 && day == 7)
                {
                    // t3
                }
                if (sellSameDay >= sellNextDay) //should it be greter than = ?
                {
                    ret = sellSameDay;

                    if(info.ContainsKey(k))
                    {
                        (info[k] as stock_info).buyDay = buyIdx;
                        (info[k] as stock_info).sellDay = day;
                        (info[k] as stock_info).stockNumber = stock;
                    }
                    else
                    {
                        info[k] = new stock_info { buyDay = buyIdx, sellDay = day, stockNumber = stock };
                    }
                    //sellDay = day;
                    //stockNumber = stock;
                }
                else
                {

                    ret = sellNextDay;
                    //sellDay = day+1;
                    /*if (info.ContainsKey(k))
                    {
                        (info[k] as stock_info).sellDay = day + 1;
                        (info[k] as stock_info).stockNumber = stock;
                    }
                    else
                    {
                        info[k] = new stock_info { buyDay = buyIdx, sellDay = day + 1, stockNumber = stock };
                    }*/
                }
            }

            return  ret;
        }

        void runProgram()
        {
            A = new int[,]
            {
                 {4, 13, 94, 22, 41, 21, 65, 66, 1, 6},
                 {68, 8, 79, 8, 45, 13, 79, 71, 22, 16},
                {16, 6, 67, 78, 25, 15, 6, 73, 15, 50}
            };
            N = 10;
            M = 3;
            int ans = findMaxProfit(0, 0, 4, true, -1);
            Console.Write(ans);
            Console.ReadLine();

        }
        static void Main(string[] args)
        {
            Program p = new Program();
            p.runProgram();
            Console.WriteLine("Hello World!");
        }
    }
}