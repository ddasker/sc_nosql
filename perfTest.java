import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import java.util.Random;

/*from  w  w  w.  j  a va2  s.  c  om*/
public class perfTest {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/bulk?useLegacyDatetimeCode=false&serverTimezone=America/New_York&useSSL=false";
	static final String USER = "root";
	static final String PASS = "password";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();


			//      Statement st = conn.createStatement();
			//st.executeUpdate("INSERT INTO blah2 VALUES (10000)");

			String sql;  // = "SELECT count(*) from bulk.blah1";


			ResultSet rs;  // = stmt.executeQuery(sql);

			//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
			//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`


			System.out.println("Starting Big Loop");

			Statement stu = conn.createStatement();

			int max_loop_counter=200;
			int max_sleep=50;

			int execute_update=1;
			int execute_single_row_select=1;
			int execute_sum_rows=1;

			long loop_time_max=0;
			long loop_time_min=1000000;
			long loop_time_elapsed=0;
			float loop_time_average=0;
			

			Statement st = conn.createStatement();

			for (int loop_counter=1; loop_counter<max_loop_counter; loop_counter++)
			{
				Random rand = new Random();
				int sleep_time = rand.nextInt(max_sleep)+1;
				Thread.sleep(sleep_time );
				//System.out.println("sleep");

				long lStartTime = new Date().getTime();


				if (execute_update==1) 
				{
               				int pickedNumber1 = rand.nextInt(1000)+1;
                			stu.executeUpdate("update blah3 set c2=10 where c1='"+pickedNumber1+"'");
				}
				
				if (execute_single_row_select==1)
                                {
                                        int pickedNumber1 = rand.nextInt(1000)+1;
                                	sql = "SELECT c2 from bulk.blah3 where c1='"+pickedNumber1+"'";
                                	rs = stmt.executeQuery(sql);
                                }


				if (execute_sum_rows==1)
                                {

                                	int pickedNumber1 = rand.nextInt(1000)+1;
                                	int pickedNumber2 = rand.nextInt(1000)+1;
                                	sql = "SELECT sum(c2) from bulk.blah3 where c1 in ( '"+pickedNumber1+"' , '"+pickedNumber2+"' ) ";
                                	rs = stmt.executeQuery(sql);
                                }

        long lEndTime = new Date().getTime();
        long output = lEndTime - lStartTime;

	loop_time_elapsed = loop_time_elapsed + output;
	if (output > loop_time_max) loop_time_max=output;
	if (output < loop_time_min) loop_time_min=output;

	}





	loop_time_average = loop_time_elapsed / max_loop_counter;
	System.out.println("All Done");
	System.out.println("loop_time_max (ms): "+loop_time_max);
	System.out.println("loop_time_min (ms): "+loop_time_min);
	System.out.println("loop_time_average (ms): "+loop_time_average);
	System.out.println("loop_time_elapsed (seconds): "+loop_time_elapsed/1000);

//    while (rs.next()) {
//      }
//      rs.close();
//      stmt.close();





      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
