import java.util.Random;

public class Pair {
	public int mX;
	public int mY;

	
	public Pair() {
		mX = -1;
		mY = =1;
	}
	public Pair(int x, int y) {
		mX = x;
		mY = y;
	}
	
	public int x() {
		return mX-1;
	}
	public int y() {
		return mY-1;
	}
}

public class Percolation {
    private int mN;
    public int[][] mParents_i, mParents_j;
    public boolean[][] mOpenStatus;
    public int[][] mSize;
    
   public Percolation(int N)                // create N-by-N grid, with all sites blocked
   {
	   if (N <= 0) {
		   throw new java.lang.IllegalArgumentException();
	   }
       mN = N;
       mParents_i = new int[N][N];
       mParents_j = new int[N][N];
       mOpenStatus = new boolean[N][N];
       mSize = new int[N][N];
       int i, j;
       
       for (j = 0; j < N; j++) {
           mParents_i[0][j] = -1;
           mParents_j[0][j] = -1;
       }
       for (i = 1; i < N; i++) {
           for (j = 0; j < N; j++) {
               mParents_i[i][j] = i;
               mParents_j[i][j] = j;
           }
       }

       for (i = 0; i < N; i++) {
           for (j = 0; j < N; j++) {
               mOpenStatus[i][j] = false;
               mSize[i][j] = 0;
           }
       }
   }
   private void connect(int i0, int j0, int i1, int j1) {
       if (i0 >= 0 && i0 <= mN-1 && j0 >= 0 && j0 <= mN-1 && mOpenStatus[i0][j0] && mOpenStatus[i1][j1]) {
           int [] r0 = root(i0, j0);
           int [] r1 = root(i1, j1);
           
           if (r0[0] == -1) {
               if (r1[0] != -1) {
                   mParents_i[r1[0]][r1[1]] = -1;
                   mParents_j[r1[0]][r1[1]] = -1;
               }
           } else if (r1[0] == -1) {
               mParents_i[r0[0]][r0[1]] = -1;
               mParents_j[r0[0]][r0[1]] = -1;
           } else {
               if (r0[0] == r1[0] && r0[1] == r1[1]) {
               } else {
                   
                   int size0 = mSize[i0][j0];
                   int size1 = mSize[i1][j1];
                   if (size0 < size1) {
                       mParents_i[r0[0]][r0[1]] = r1[0];
                       mParents_j[r0[0]][r0[1]] = r1[1];
                   } else if (size0 > size1) {
                       mParents_i[r1[0]][r1[1]] = r0[0];
                       mParents_j[r1[0]][r1[1]] = r0[1];
                   } else {                       
                       mParents_i[r1[0]][r1[1]] = r0[0];
                       mParents_j[r1[0]][r1[1]] = r0[1];
                       
                       mSize[r0[0]][r0[1]] += 1;
                   }
               }
           }
       }
   }
   
   private int[] root(int i, int j) {
       if (i == -1) {
           int [] res = new int[2];
           res[0] = -1;
           res[1] = -1;
           return res;
       }
       if (!(mParents_i[i][j] == i && mParents_j[i][j] == j)) {
           int [] res = root(mParents_i[i][j], mParents_j[i][j]);
           mParents_i[i][j] = res[0];
           mParents_j[i][j] = res[1];
           return res;
       }
       int [] res = new int[2];
       res[0] = mParents_i[i][j];
       res[1] = mParents_j[i][j];
       return res;
   }
   
   private void p_open(int i, int j)           // open site (row i, column j) if it is not already
   {
       mOpenStatus[i][j] = true;
       connect(i-1, j, i, j);
       connect(i+1, j, i, j);
       connect(i, j-1, i, j);
       connect(i, j+1, i, j);
   }
   private boolean p_isOpen(int i, int j)      // is site (row i, column j) open?
   {
       return mOpenStatus[i][j];
   }
   private boolean p_isFull(int i, int j)      // is site (row i, column j) full?
   {
	   if (!p_isOpen(i, j)) {
		   return false;
	   }
       int [] res = root(i,j);
       return res[0] == -1;
   }
   
   public void open(int i, int j)           // open site (row i, column j) if it is not already
   {
	   p_open(i-1, j-1);
   }
   public boolean isOpen(int i, int j)      // is site (row i, column j) open?
   {
	   return p_isOpen(i-1, j-1);
   }
   public boolean isFull(int i, int j)      // is site (row i, column j) full?
   {
	   return p_isFull(i-1, j-1);
   }
   
   public boolean percolates()              // does the system percolate?
   {
       int j;
       for (j = 0; j < mN; j++) {

           int [] res = root(mN-1,j);
           int v = mN-1;
           if (p_isFull(mN-1, j)) {
               return true;
           }
       }
       return false;
   }
   public void printStatus() {
	    int i;
	    int j;
	    for (i = 0; i < mN; i++) {
	    	for (j = 0; j < mN; j++) {
	    		System.out.print("[" + mParents_i[i][j] + "\t:" + mParents_j[i][j] + "\t]");
	    	}
	    	System.out.println();
	    }
	    for (i = 0; i < mN; i++) {
	    	for (j = 0; j < mN; j++) {
	    		if (mOpenStatus[i][j]) {
	    			System.out.print(".");
	    		} else {
	    			System.out.print("X");
	    		}
	    	}
	    	System.out.println();
	    }
   }
   
   public static void main(String[] args)   // test client, optional
   {
	   /*
       int n = 10;
       Percolation p = new Percolation(n);
       
       Random randomGenerator = new Random();
       int k;
       for (k = 0; k < 50; k++) {
           int i = randomGenerator.nextInt(n)+1;
           int j = randomGenerator.nextInt(n)+1;
           p.open(i, j);
       } */
   }
}
