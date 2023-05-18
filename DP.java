import java.util.*;

public class DP {

	public static void main(String[] args) {
		
	    String task = args[0];
	    List<String> taskList = Arrays.asList("1", "2", "3a","3b","4","5","6a","6b");
	    int k = 1;
	    if(task.length() == 0 || !(taskList.contains(task))) {
		    System.out.println("Please pass a valid argument - 1,2,3a,3b,4,5,6a,6b");
	    }
	    Scanner sc=new Scanner(System.in);
	    if("4".equals(task) || "5".equals(task) || "6a".equals(task) || "6b".equals(task)) {
		    System.out.println("Enter number of transactions: ");
		    k = sc.nextInt();
	    }
	    System.out.println("Enter number of rows and columns separated by space: ");
	    int rows=sc.nextInt();
	    int columns=sc.nextInt();
	    int stocks[][]=new int[rows][columns];
	    System.out.println("Enter the stocks data:");
	    for(int i=0; i<rows;i++)
        {            
           for(int j=0; j<columns;j++)
           {
               stocks[i][j]=sc.nextInt();
           }
        }
	    rows = stocks.length;
	    columns = stocks[0].length;
	    switch(task) {
	    case "1":
	    	DP.bruteForceMaximizeProfitAlg1(stocks, rows, columns);
	    	break;
	    case "2":
	    	DP.greedyMaximizeProfitAlg2(stocks, rows, columns);
	    	break;
	    case "3a":
	    	DP.dpMemoMaximizeprofitAlg3a(stocks, rows, columns);
	    	break;
	    case "3b":
	    	DP.dpBottomUpMaximizeprofitAlg3b(stocks, rows, columns);
	    	break;
	    case "4":
	    	DP.bruteMaximizeProfitAlg4(stocks,k);
	    	break;
	    case "5":
	    	DP.dpMaximizeProfitAlg5(stocks, k, rows, columns);
	    	break;
	    case "6a":
	    	DP.dpMemoMaximizeProfitpAlg6a(rows, columns, k, stocks);
	    	break;
	    case "6b":
	    	ArrayList<TransactionData> result6b  = DP.dpBottomUpMaximizeProfitpAlg6b(stocks, k);
			for(TransactionData transaction : result6b)
			    System.out.println(transaction.stock + " " + transaction.buyDay + " " + transaction.sellDay);
	    	break;
	    default: 
		    System.out.println("Not A valid Task. Please enter a task from 1,2,3a,3b,4,5,6a,6b");
	    }
	    	    
	}
	 
	
		// ################################################
		// ################ SOLUTION ######################
		// ############### PROBLEM 1 ##################### 
		// ################################################ 
		// ################TASK1##############
		public static void bruteForceMaximizeProfitAlg1(int[][] prediction, int m, int n){
			
			int maximumProfit=0;
			int currentProfit = 0;
			int stock = 0, buyDay = 0, sellDay = 0;
			
			for(int i=0; i<m;i++) {
				for(int j=0; j<n;j++) {
					for(int k=j+1;k<n;k++){
						currentProfit = prediction[i][k] - prediction[i][j];
						if(currentProfit>maximumProfit) {
							maximumProfit = currentProfit;
							stock = i;  	//represents stock number
							buyDay = j;	//represents buy day
							sellDay = k;	//represents sell day
						}
					}
				}
			}
			System.out.println(stock+"\t"+ buyDay+ "\t"+sellDay);		
		}
		
		// ################TASK2##############
		
		public static void greedyMaximizeProfitAlg2(int[][] prediction, int m, int n) {
					
			int maximumProfit = 0;
			//variables to store transaction details
			int stock = 0, buyDay = 0, probableBuyday = 0, sellDay = 0;
					
			for(int i = 0; i<m; i++) {
				//we set this to minimum every time we calculate profit for new stock
				int minimumPrice = Integer.MAX_VALUE;
				for(int j=0; j<n; j++) {
					if(prediction[i][j] < minimumPrice) {
						minimumPrice = prediction[i][j];
						probableBuyday = j;
					}
					else if(prediction[i][j] - minimumPrice > maximumProfit) {
						maximumProfit = prediction[i][j] - minimumPrice;
						buyDay = probableBuyday;
						stock = i;
						sellDay = j;
					}
				}
			}
			System.out.println(stock+"\t"+ buyDay+ "\t"+sellDay);				
		}
		
		// ################TASK3a##############

	public static void dpMemoMaximizeprofitAlg3a(int[][] prediction, int m, int n){
        int max_profit = 0;
		int stock = 0;
        int[] values_out = new int[]{ 0,0,0, 0 }; 
        for(int i = 0; i < m; i++) {
            int[] values = new int[]{ 0,0,0,0 };
            values = memo_profit(prediction[i], 0, Integer.MAX_VALUE, 0, values);            
            if(max_profit <= values[0] || max_profit < 0){
            	max_profit = values[0];
            	stock = i+1;
                values_out = values;
            }
        }
		System.out.println("dp Memorization method output: ");
		System.out.println("Stock\t"+ "Buyday\t"+"Sellday");
		System.out.println(stock+"\t"+ values_out[1]+ "\t"+values_out[2]);	
	}
	
	public static int[] memo_profit(int[] profits, int indice, int minimumStockValue, int profit, int[] values) {
		if (profits.length == indice) {
	    	 values[0] = profit;
	         return values;
	    }
	    int price_Difference =  profits[indice] - minimumStockValue;
	    if (price_Difference > profit) {
	    	profit = price_Difference;
	    	values[2] = indice+1;
	    	values[1] = values[3];
	    }
	    else if (minimumStockValue > profits[indice]) {
	    	minimumStockValue = profits[indice];
	    	values[3] = indice+1;
	    }
	    return memo_profit(profits, indice+1 , minimumStockValue, profit, values );
	}
	    
	// ################TASK3b##############

	public static void dpBottomUpMaximizeprofitAlg3b(int[][] prediction, int m, int n){
			
			//memo table to store profit of each transaction - stores max profit upto ith stock on jth day
			int[][] profit = new int[m][n];
			// profit is zero for first day, first stock
			profit[0][0] = 0; 		
			//variables to store transaction details
			int stock = 0, buyDay = 0,probableBuyday = 0, sellDay = 0;
			
			for(int i =0; i<m; i++) {
				//we set minimum to the stock's price on first day
				int minimumPrice = prediction[i][0];
				// for next stock, we start with maximumProfit as maximum profit till last stock
				if(i>0) profit[i][0] = profit[i-1][n-1];
				
				probableBuyday = 1;
				
				for(int j=1;j<n;j++) {
					// if stock price is less than the minimum value, it can give us maximum profit in future if we buy stock on this day
					if(prediction[i][j] < minimumPrice) {
						minimumPrice = prediction[i][j];
						profit[i][j] = profit[i][j-1];
						probableBuyday = j;
					}
					// if profit of current transaction is greater than previous profit, update transaction details of maximum profit
					else if(profit[i][j-1] < prediction[i][j]-minimumPrice){
						profit[i][j] = prediction[i][j]-minimumPrice;
						buyDay = probableBuyday;
						stock = i;
						sellDay = j;
					}
					//if we don't do any transaction and the profit remains same
					else{
						profit[i][j] = profit[i][j-1];
					}
				}
			}

			System.out.println("dp bottom up method output: ");
			System.out.println("Stock\t"+ "buyday\t"+"sellday");
			System.out.println(stock+"\t"+ buyDay+ "\t"+sellDay);	
		
	}	
	
	// ################################################
	// ################ SOLUTION ######################
	// ############### PROBLEM 2 ##################### 
	// ################################################ 
	
	// ################TASK4##############
	
	public static void  bruteMaximizeProfitAlg4(int[][] arr, int k){
		//This is implemented in C++ and is kept in the project zip
	}
	
	
	// ################TASK5##############
	public static void dpMaximizeProfitAlg5(int[][] prediction, int k, int m, int n){
		String[][] transactions = new String[k+1][n+1];
		int[][] profits = new int[k + 1][n + 1];
		
		for (int i = 0; i <= n; i++) {
			transactions[0][i] = "";
			profits[0][i] = 0;
		}

		for (int j = 1; j <= k; j++) {
			transactions[j][0] = "";
		    profits[j][0] = 0;
		}
		
		for (int x = 1; x <= k; x++) {
		    for(int y = 0; y < m; y++) {
		        for (int z = 1; z < n; z++) {
		            String outputString = "";
		        	int profit = 0;
		            for (int a = 0; a < z; a++) {
		                int diff = prediction[y][z] - prediction[y][a] + profits[x - 1][a];
		                if(diff > profit){
		                	outputString = String.format("%s %s %s", y, a, z) + ((transactions[x-1][a] != "")?","+transactions[x-1][a]:"" );
		                }
		                profit = Math.max(profit, diff);
		            }
		            if(profits[x][z-1] > profit)
		            	outputString = transactions[x][z-1];
		                int max_profit = Math.max(profits[x][z - 1], profit);
		                if(max_profit < profits[x][z])
		                	outputString = transactions[x][z];
		                profits[x][z] = Math.max(max_profit, profits[x][z]);
		                transactions[x][z] = outputString;
		          }
		    }
	    }
		
		String[] arrOfStr = transactions[k][n-1].split(",");
        List<List<String>> trans = new ArrayList<>();
        List<String> Transactionlist = new ArrayList<>();
        for (String s : arrOfStr) {
            String[] x = s.split(" ");
            for (int i = 0; i < x.length; i++) {
            	Transactionlist.add(x[i]);
            }
            trans.add(Transactionlist);
            Transactionlist = new ArrayList<>();
        }
        trans.sort(Comparator.comparing(strings -> strings.get(1))); 
        for (int i = 0; i< trans.size();i++) {
        	String tempString = "";
	        for (int j = 0; j< trans.get(i).size();j++) {
	        	tempString += " "+(trans.get(i).get(j)).toString();
	        }
	        System.out.println(tempString);
        }		
	
	}
    //####################TASK 6a#######################

	public static void dpMemoMaximizeProfitpAlg6a(int m, int n, int k,int[][]predictions){
		int[][] table = new int[k+1][n]; // represents the memoization by storing results duiring recursion.
		
		for(int i = 0; i < k + 1; ++i)
		    for(int j = 0; j < n;  ++j)
			    table[i][j] = -1;
			
	    for (int i = 0; i < n; ++i)
	        table[0][i] = 0;
        
	    for (int i=0; i<k+1; i++)
	        table[i][0] = 0;
	    
	    int[][] maximumDifference = new int[k+1][m];
		
		for(int i = 0; i < k+1; ++i)
        {
			for(int j = 0; j < m; ++j)
            {
				maximumDifference[i][j] = 0;
			}
		}

		for (int j = 0; j < k + 1; ++j)
	    {
	        for(int i = 0; i < m; ++i)
	        {
	        	maximumDifference[j][i] = -predictions[i][0];    
	        }
	    }
		
		int maxProfit = GetMaximumProfit(predictions, m, n-1, k, table, maximumDifference);
	    
        List<List<Integer>> transactions =  findTransactions(m, n, k, predictions, table);

	    for (int i = 0; i < transactions.size(); ++i)
	    {
	        for (int j = 0; j < transactions.get(i).size(); ++j)
	        {
	            System.out.print(transactions.get(j).get(j)-1);
	        }
	        System.out.println();
	    }
	}
	
	public static int GetMaximumProfit(int [][] predictions, int m, int n, int k, int[][]table, int [][] maximumDifference)
    {
        if(table[k][n] == -1)
        {
            int maxProfit = GetMaximumProfit(predictions, m, n - 1, k, table, maximumDifference);
            for(int stock = 0; stock < m; stock++)
            {
                maximumDifference[k][stock] =
                    Math.max(maximumDifference[k][stock], GetMaximumProfit(predictions, m, n, k - 1, table, maximumDifference) - predictions[stock][n]);
                maxProfit = Math.max(maxProfit, predictions[stock][n] + maximumDifference[k][stock]);
            }
            table[k][n]=maxProfit;
            return maxProfit;
        }
        return table[k][n];
    }
	
	public static List<List<Integer>> findTransactions(int m, int n, int k, int[][] predictions, int[][] table)
    {   
        int tranNo = k, currentDay = n - 1;
        List<List<Integer>> transactions = new ArrayList<>();

        while(true)
        {
            if(tranNo == 0 || currentDay == 0)
                break;
            
            if(table[tranNo][currentDay] == table[tranNo][currentDay - 1])
                currentDay = currentDay - 1;

            else
            {
                boolean foundTransactionDetails = false;
                for (int stock = 0; stock < m; ++stock)
                {
                    int currentProfit = table[tranNo][currentDay] - predictions[stock][currentDay];
                    for(int j = currentDay - 1; j >= 0; --j)
                    {
                        if(table[tranNo-1][j] - predictions[stock][j] == currentProfit)
                        {
                            List<Integer> info = new ArrayList<>();
                            info.add(currentDay + 1);
                            info.add(stock + 1);
                            info.add(j + 1);
                            
                            transactions.add(info);
                            tranNo--;
                            currentDay = j;
                            foundTransactionDetails = true;
                            break;
                        }
                    }
                    if(foundTransactionDetails)
                        break;
                }
            }
        }
        return transactions;
    }

	//###################TASK 6b########################
	
public static ArrayList<TransactionData> dpBottomUpMaximizeProfitpAlg6b(int[][] prediction, int k) {
		
		
		int m = prediction.length;
		int n = prediction[0].length;
		
		int[][] scores = new int[m+1][n+1];
		for(int i = 0; i <= m; i++)
			scores[i][0] = Integer.MAX_VALUE;
		for(int j = 0; j <= n; j++)
			scores[0][j] = Integer.MAX_VALUE;
		for(int i = 1; i <= m; i++){
		    for(int j = 1; j <= n; j++)
		    	scores[i][j]=prediction[i-1][j-1];
		}
		
		int[][] profit =  new int[k+1][n+1];
	    int[] difference = new int[m+1];
	
	    for(int i = 0; i <= m; i++)
	    	difference[i] = Integer.MIN_VALUE;
	    
	    // i'th row represents i number of transactions and column j represents j'th day
	    for(int i = 0; i <= k; i++)
	    	profit[i][0] = 0;
	    for(int j = 0; j <= n; j++)
	    	profit[0][j] = 0;
	    
	    for(int i = 1; i <= k; i++){
	        for(int j = 1; j <= n; j++){
	            int maxProfit = Integer.MIN_VALUE;
	            for(int l = 1; l <= m; l++){
	            	difference[l] = Math.max(difference[l], profit[i-1][j-1] - scores[l][j-1]);
	                maxProfit = Math.max(maxProfit, difference[l] + scores[l][j]);
	            }
	            profit[i][j] = Math.max(maxProfit, profit[i][j-1]);
	        }
	    }
	    
	    return dfs(scores, profit);
	}
	
	public static ArrayList<TransactionData> dfs(int[][] prediction, int[][] profit) {
	    ArrayList<TransactionData> result = new ArrayList<TransactionData>();
	    int t = profit.length - 1;
	    int d = profit[0].length - 1;
	
	    while (t > 0 && d > 0) {
	        while (d > 0 && profit[t][d] == profit[t][d - 1]) {
	            d--;
	        }
	
	        int sellDay = d;

	        int sprofit = profit[t][d];
	        if (--t >= 0 && --d > 0) {
	            while (d > 0) {
	                boolean b = false;
	                for (int l = 1; l < prediction.length; l++) {
	                    if (prediction[l][sellDay] - prediction[l][d] == sprofit - profit[t][d]) {
	                    	TransactionData transaction = new TransactionData(l, d, sellDay);
	                    	result.add(transaction);
	                        b = true;
	                        break;
	                    }
	                }
	                if (b)
	                    break;
	                d--;
	            }
	        }
	    }
	
	    Collections.reverse(result);
	    return result;
	}
	
}

class TransactionData{
	int stock;
	int buyDay;
	int sellDay;
	
	TransactionData(int s, int bd, int sd) {
		this.stock = s;
		this.buyDay = bd;
		this.sellDay = sd;
	}
}
