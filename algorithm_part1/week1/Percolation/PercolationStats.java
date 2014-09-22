import java.util.Random;

public class PercolationStats {
    private int mN;
    private int mT;
    private double [] mOpenNums;
    
    // results
    private double mMean;
    private double mStddev;
    private double mConfLo;
    private double mConfHi;
    
   public PercolationStats(int N, int T)    // perform T independent computational experiments on an N-by-N grid
   {
       mN = N;
       mT = T;
       mOpenNums = new double[mT];
   }
   public double mean()                     // sample mean of percolation threshold
   {
       return mMean;
   }
   public double stddev()                   // sample standard deviation of percolation threshold
   {
       return mStddev;
   }
   public double confidenceLo()             // returns lower bound of the 95% confidence interval
   {
       return mConfLo;
   }
   public double confidenceHi()             // returns upper bound of the 95% confidence interval
   {
       return mConfHi;
   }
   private int runOneSample()
   {
       Random randomGenerator = new Random();
       int [] open_nums = new int[mN];
       int i;
       for (i = 0; i < mN; i++) {
    	   open_nums[i] = mN;
       }
       int sum = mN * mN;
       
       Percolation p = new Percolation(mN);
       int open_num = 0;
       while (!p.percolates()) {
    	   int next_order = randomGenerator.nextInt(sum)+1;
    	   int pi = 0;
    	   int pj = 0;
    	   boolean done = false;
    	   for (pi = 1; pi <= mN; pi++) {
    		   if (next_order - open_nums[pi-1] <= 0) {
    			   int sum_j = 0;
        		   for (pj = 1; pj <= mN; pj++) {
        			   if (!p.isOpen(pi, pj)) {
        				   sum_j++;
        				   if (sum_j == next_order) {
        					   done = true;
        					   open_nums[pi-1] -= 1;
        					   sum--;
        					   break;
        				   }
        			   }
        		   }
    		   } else {
    			   next_order -= open_nums[pi-1];
    		   }
    		   if (done) {
    			   break;
    		   }
    	   }
    	   // System.out.println("opening: " + pi + " " + pj + " : " + open_num);
           if (!p.isOpen(pi, pj)) {
        	   // System.out.println("opening: " + pi + " " + pj + " : " + open_num);
               p.open(pi, pj);
               open_num++;
               
               // p.printStatus();
           } else {
        	   System.out.println("ERROR for print!");
           }
       }
       return open_num;
   }
   private void calculate()
   {
       int i;
       double sum = 0;

       for (i = 0; i < mT; i++) {
           sum += mOpenNums[i];
       }
       mMean = sum / mT;
       
       double stdsq = 0;
       for (i = 0; i < mT; i++) {
           stdsq += (mOpenNums[i] - mMean) * (mOpenNums[i] - mMean);
       }
       mStddev = Math.sqrt(stdsq/(mT-1));
       
       double delta = 1.96 * mStddev / Math.sqrt(mT);
       mConfLo = mMean - delta;
       mConfHi = mMean + delta;
   }
   
   public void run()
   {
       int i;
       for (i = 0; i < mT; i++) {
           int open_num = runOneSample();
           mOpenNums[i] = (double)open_num / (mN*mN);
       }   
       calculate();
   }
   public static void main(String[] args)   // test client, described below
   {
       int N = Integer.parseInt(args[0]);
       int T = Integer.parseInt(args[1]);
       PercolationStats ps = new PercolationStats(N, T);
       ps.run();
       System.out.println("mean                    = " + ps.mean());
       System.out.println("stddev                  = " + ps.stddev());
       System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
   }
}
