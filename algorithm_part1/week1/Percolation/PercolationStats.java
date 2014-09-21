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
       
       Percolation p = new Percolation(mN);
           int open_num = 0;
           while (!p.percolates()) {
               int pi = randomGenerator.nextInt(mN)+1;
               int pj = randomGenerator.nextInt(mN)+1;
               if (!p.isOpen(pi, pj)) {
                   p.open(pi, pj);
                   open_num++;
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
