
public class ChessBoard 
{
    private int now[][],lastB[][],lastW[][];
    private static final int N=8,dirnum=8;
    private static final int dir[][]={{1,0},{0,1},{0,-1},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
    private boolean canUndoB,canUndoW;
	public boolean getCanUndoB()
	{
		return canUndoB;
	}
	public boolean getCanUndoW()
	{
		return canUndoW;
	}
    public void init() //重新开始
    {
        for (int i=0;i<N;i++)
            for (int j=0;j<N;j++)
                now[i][j]=0;
        now[N/2-1][N/2-1]=-1;
        now[N/2-1][N/2]=1;
        now[N/2][N/2-1]=1;
        now[N/2][N/2]=-1;
        canUndoB=false;
        canUndoW=false;
    }
    private boolean outside(int x,int y)
    {
        return (x<0)||(x>=N)||(y<0)||(y>=N);
    }
    private boolean canEat(int x,int y,int d,int color)
    {
        if (now[x][y]!=0) return false;
        int u=dir[d][0],v=dir[d][1],xFinal=x,yFinal=y;
        while ((!outside(xFinal+u,yFinal+v))&&(now[xFinal+u][yFinal+v]==-color))
        {
            xFinal+=u;
            yFinal+=v;
        }
        if ((xFinal==x)&&(yFinal==y)) return false;
        if (outside(xFinal+u,yFinal+v)) return false;
        if (now[xFinal+u][yFinal+v]!=color) return false;
        return true;
    }
    private void eat(int x,int y,int d,int color)
    {
        int u=dir[d][0],v=dir[d][1],xFinal=x,yFinal=y;
        while (now[xFinal+u][yFinal+v]==-color)
        {
            xFinal+=u;
            yFinal+=v;
            now[xFinal][yFinal]=color;
        }
    }
    public boolean put(int x,int y,int color) //在(x,y)处下color颜色(1或-1)的棋
    {
        boolean res=false;
        for (int i=0;i<N;i++)
            for (int j=0;j<N;j++)
            if (color==1) lastB[i][j]=now[i][j]; else lastW[i][j]=now[i][j];
        for (int i=0;i<dirnum;i++)
        if (canEat(x,y,i,color)) 
        {
            res=true;
            eat(x,y,i,color);
        }
        if (res) 
        {
            if (color==1) canUndoB=true; else canUndoW=true;
            now[x][y]=color;
        }
        return res;
    }
    public boolean canPut(int color) //color颜色的棋是否有地方下
    {
        for (int i=0;i<N;i++)
            for (int j=0;j<N;j++)
                for (int k=0;k<dirnum;k++)
                if (canEat(i,j,k,color)) return true;
        return false;
    }
    public boolean regret(int color) //color颜色要悔棋
    {
        boolean can;
        if (color==1) can=canUndoB; else can=canUndoW;
        if (!can) return false;
        for (int i=0;i<N;i++)
            for (int j=0;j<N;j++)
            if (color==1) now[i][j]=lastB[i][j]; else now[i][j]=lastW[i][j];
        canUndoB=false;
        canUndoW=false;
        return true;
    }
    public int winner() //判断胜者
    {
        int count1=0,count2=0;
        for (int i=0;i<N;i++)
            for (int j=0;j<N;j++)
            {
                if (now[i][j]==1) count1++;
                if (now[i][j]==-1) count2++;
            }
        if (count1>count2) return 1;
        if (count1<count2) return -1;
        return 0;
    }
    public ChessBoard()
    {
        now=new int[N][N];
        lastW=new int[N][N];
        lastB=new int[N][N];
        init();
    }
}
