import java.util.Random;

public class PercolationStats {
    private int mN;
    private int mT;
    private double [] mOpenNums;
    private int [] mLeftnums;
    private Random randomGenerator;
    
    private Percolation [] mPers;
    
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
       mPers = new Percolation[mT];
       int i;
       for (i = 0; i < mT; i++) {
    	   mPers[i] = new Percolation(mN);
       }
       mOpenNums = new double[mT];
       mLeftnums = new int[mN];
       randomGenerator = new Random();
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
   private int [] getRandomOpen(int sum, Percolation p) {
       int nextorder = randomGenerator.nextInt(sum)+1;
       int [] res = new int[2];
       res[0] = 0; res[1] = 0;
       
       for (res[0] = 1; res[0] <= mN; res[0]++) {
           if (nextorder - mLeftnums[res[0]-1] <= 0) {
               int sum_j = 0;
               for (res[1] = 1; res[1] <= mN; res[1]++) {
                   if (!p.isOpen(res[0], res[1])) {
                       sum_j++;
                       if (sum_j == nextorder) {
                           mLeftnums[res[0]-1] -= 1;
                           return res;
                       }
                   }
               }
           } else {
               nextorder -= mLeftnums[res[0]-1];
           }
       }
       return res;
   }
   
   private int runOneSample()
   {
       // int [] opennums = new int[mN];
       int i;
       for (i = 0; i < mN; i++) {
           mLeftnums[i] = mN;
       }
       int sum = mN * mN;
       
       Percolation p = new Percolation(mN);
       int opennum = 0;
       while (!p.percolates()) {
           int [] newpair = getRandomOpen(sum, p);
           sum--;
           int pi = newpair[0];
           int pj = newpair[1];
           
           if (!p.isOpen(pi, pj)) {
               p.open(pi, pj);
               opennum++;
           } else {
               System.out.println("ERROR for print!");
           }
       }
       return opennum;
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
   
   private void run()
   {
       int i;
       for (i = 0; i < mT; i++) {
           int opennum = runOneSample();
           mOpenNums[i] = (double) opennum / (mN * mN);
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
