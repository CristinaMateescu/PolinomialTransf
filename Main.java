//Mateescu Cristina-Ramona, 321CB, tema2
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import static java.lang.Integer.parseInt;
import java.util.Scanner;

/**
 *
 * @author Cristina-Ramona
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        //Set input file, output file
        Scanner scan = new Scanner(new File("graph.in"));
        PrintStream out = new PrintStream("bexpr.out");
        System.setOut(out);
        
        //Citeste n
        int n = scan.nextInt();
        
        //Declara si citeste matricea de adiacenta
        //Declara vectorul de grade
        int[][] muchii = new int[n+1][n+1];
        int[] grade = new int[n+1];
        
        int u = scan.nextInt(),v;
        while ( u!=-1 ){
            v = scan.nextInt();
            muchii[u][v]=1;
            muchii[v][u]=1;
            grade[u]++;
            grade[v]++;
            u=scan.nextInt();
        }
        
        //Conditie0 : gradul fiecarui nod sa fie minimum 2
        if (!VerificaGrade(grade,n)){
            System.out.println("x1-1 & ~x1-1\n");
            return;
        }
        
        //Formula 1 = sat <=> fiecare nod are exact 2 muchii incidente
        Formula1(n,grade,muchii);
        
        //Formula 2 = sat <=> 
        //exista un drum de lungime maxima n/2+1 de la 1 la orice nod
        Formula2(n);
        
        //Formula 3 = sat <=> graful este neorientat
        Formula3(n,muchii);
        
        //Formula 4 = sat <=> coeficientii ai-j sunt corecti
        Formula4(n,muchii,grade);
        
    }

    private static boolean VerificaGrade(int[] grade, int n) {
        for ( int i=1; i<=n; i++ ){
            if (grade[i]<2)
                return false;
        }
        return true;
    }

    private static void Formula1(int n, int[] grade, int[][] muchii) {
        
     for ( int nod=1; nod<=n; nod++ ){
        
         System.out.print( "(" );
         int part_nr = grade[nod]*(grade[nod]-1)/2;
         
         //Pentru fiecare nod
         //Alegem doua muchii incidente m1, m2
         for ( int m1=1; m1<=n; m1++ )
           for ( int m2=m1+1; m2<=n; m2++ )
              if ( nod!=m1 && nod!=m2 && m1!=m2 && 
                  muchii[nod][m1]==1 && muchii[nod][m2]==1 ){
                    
                   System.out.print("(x"+nod+"-"+m1+"&x"+nod+"-"+m2);
                   
                   //Afisarea muchiilor incidente care nu au fost alese
                   for (int m=1; m<=n; m++ )
                   if ( m!=nod && m!=m1 && m!=m2 && muchii[nod][m]==1 )
                      System.out.print( "&~x"+nod+"-"+m );

                   System.out.print( ")" );
                   part_nr--;

                   if( part_nr>0 )
                       System.out.print("|");
              }
        System.out.print(")&");
    }
    
  }

    private static void Formula2(int n) {
        
      for ( int i=2; i<=n; i++ ){
        
        System.out.print("(");
        for ( int j=1; j<=n/2+1; j++ ){
            
            System.out.print( "a"+j+"-"+i );
            if( j!=n/2+1 )
               System.out.print("|");
        }
        if (i!=n)
            System.out.print( ")&" );
        else
            System.out.print(")");
      }
    }

    private static void Formula3(int n, int[][] muchii) {
       
      for ( int i=1; i<=n; i++ )
        for ( int j=i+1; j<=n; j++ )
            if ( muchii[i][j]==1 )
                System.out.print("&((x"+i+"-"+j+"|~x"+j+"-"+i+")&"
                                +"(~x"+i+"-"+j+"|x"+j+"-"+i+"))");
    }

    private static void Formula4(int n, int[][] muchii, int[] grade) {
        
    //Particularizare i=1
    for (int j=1; j<=n; j++ )
    {
        if ( muchii[1][j]==1 )
            System.out.print("&((a1-"+j+"|~x1-"+j+")&(~a1-"+j+"|x1-"+j+"))");
        else
            System.out.print("&~a1-"+j);
    }

    //Coeficientii ai-j cu i>1
    for (int  i=2; i<=n/2+1; i++ )
       for (int j=2; j<=n; j++ ){
        
        //a - prima parte   
        System.out.print("&((a"+i+"-"+j+"|~((");
        int grad = grade[j];
        if( muchii[j][1]==1 )grad--;
        for ( int m=2; m<=n; m++ ){

            if ( muchii[j][m]==1 ){
                
                System.out.print("(a"+(i-1)+"-"+m+"&x"+m+"-"+j+")");
                        grad--;
                        if ( grad > 0 && grad!=grade[j] )
                        System.out.print("|");
            }
        }
        //~a - a doua parte
        System.out.print(")&~(");
        for ( int m=i-1; m>0; m-- ){

            System.out.print("a"+m+"-"+j);
            if ( m!=1 )
                System.out.print("|");
        }

        System.out.print(")))&(~a"+i+"-"+j+"|((");
        grad = grade[j];
        if( muchii[j][1]==1 )
            grad--;
        
        for ( int m=2; m<=n; m++ ){

            if ( muchii[j][m]==1 ){

                System.out.print("(a"+(i-1)+"-"+m+"&x"+m+"-"+j+")");
                grad--;
                 if ( grad > 0 && grad != grade[j] )
                    System.out.print("|");
            }
        }
        System.out.print(")&~(");
        for ( int m=i-1; m>0; m-- ){

            System.out.print("a"+m+"-"+j);
            if ( m!=1 ) 
                System.out.print("|");
        }
        System.out.print("))))");
    }
  }
}
