import java.util.Random;

public class PercolationStats {
    private int mN;
    private int mT;
    private double [] mOpenNums;
    private int [] mLeftnums;
    
    // results
    private double mMean;
    private double mStddev;
    private double mConfLo;
    private double mConfHi;
    
   public PercolationStats(int N, int T)    // perform T independent computational experiments on an N-by-N grid
   {
	   if (N <= 0) {
		   throw new java.lang.IllegalArgumentException();
	   }
	   if (T <= 0) {
		   throw new java.lang.IllegalArgumentException();
	   }
       mN = N;
       mT = T;
       mOpenNums = new double[mT];
       mLeftnums = new int[mN];
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
   private int getRandomOpen(int sum, Percolation p) {
	   int next_order = randomGenerator.nextInt(sum)+1;
	   int [] res = new int[2];
	   res[0] = 0; res[1] = 0;
	   
	   for (res[0] = 1; res[0] <= mN; res[0]++) {
		   if (next_order - mLeftnums[res[0]-1] <= 0) {
			   int sum_j = 0;
    		   for (res[1] = 1; res[1] <= mN; res[1]++) {
    			   if (!p.isOpen(res[0], res[1])) {
    				   sum_j++;
    				   if (sum_j == next_order) {
    					   done = true;
    					   mLeftnums[res[0]-1] -= 1;
    					   return res;
    				   }
    			   }
    		   }
		   } else {
			   next_order -= mLeftnums[res[0]-1];
		   }
	   }
	   return res;
   }
   
   private int runOneSample()
   {
       Random randomGenerator = new Random();
       // int [] open_nums = new int[mN];
       int i;
       for (i = 0; i < mN; i++) {
    	   mLeftnums[i] = mN;
       }
       int sum = mN * mN;
       
       Percolation p = new Percolation(mN);
       int open_num = 0;
       while (!p.percolates()) {
//    	   int next_order = randomGenerator.nextInt(sum)+1;
//    	   int pi = 0;
//    	   int pj = 0;
//    	   boolean done = false;
//    	   for (pi = 1; pi <= mN; pi++) {
//    		   if (next_order - open_nums[pi-1] <= 0) {
//    			   int sum_j = 0;
//        		   for (pj = 1; pj <= mN; pj++) {
//        			   if (!p.isOpen(pi, pj)) {
//        				   sum_j++;
//        				   if (sum_j == next_order) {
//        					   done = true;
//        					   open_nums[pi-1] -= 1;
//        					   sum--;
//        					   break;
//        				   }
//        			   }
//        		   }
//    		   } else {
//    			   next_order -= open_nums[pi-1];
//    		   }
//    		   if (done) {
//    			   break;
//    		   }
//    	   }
    	   int [] new_open_pair = getRandomOpen(sum, p);
    	   sum--;
    	   
           if (!p.isOpen(pi, pj)) {
               p.open(pi, pj);
               open_num++;
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
       
       if (mT == 1) {
    	   mStddev = 0;
       } else {
           double stdsq = 0;
           for (i = 0; i < mT; i++) {
               stdsq += (mOpenNums[i] - mMean) * (mOpenNums[i] - mMean);
           }
           mStddev = Math.sqrt(stdsq/(mT-1));
       }
       
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
