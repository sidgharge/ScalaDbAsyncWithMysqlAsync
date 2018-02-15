package example

import com.github.mauricio.async.db.mysql.util.URLParser
import com.github.mauricio.async.db.Connection
import com.github.mauricio.async.db.mysql.MySQLConnection
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import scala.concurrent.Future
import com.github.mauricio.async.db.QueryResult
import com.github.mauricio.async.db.RowData
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.Seq

object Test {
  
   def main(args: Array[String]) {
    
    //getById(20)
    
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   insert(Seq("0", "24", "Imran", "imu@s.com"))
   insert(Seq("0", "25", "Akash", "akash@gmail.com"))
   Thread.sleep(5000)
  }

  def insert(data: Seq[String]) {
    
    val configuration = URLParser.parse("jdbc:mysql://localhost:3306/springboot?username=root&password=root")

    val connection: Connection = new MySQLConnection(configuration)

    Await.result(connection.connect, FiniteDuration(5, TimeUnit.SECONDS))
    
    
    val future: Future[QueryResult] = connection.inTransaction {
      c =>
        c.sendPreparedStatement("INSERT INTO user (id, age, name, email) VALUES (?,?,?,?)", data)
    }

    
    future.onComplete((queryResult) => {
      println("Future Thread: " + Thread.currentThread().getId)
      connection.disconnect
    })
    
    println("Thread: " + Thread.currentThread().getId)
    
  }

  def getById(id: Int) {
    val configuration = URLParser.parse("jdbc:mysql://localhost:3306/springboot?username=root&password=root")

    val connection: Connection = new MySQLConnection(configuration)

    Await.result(connection.connect, FiniteDuration(5, TimeUnit.SECONDS))


    val future: Future[QueryResult] = connection.sendPreparedStatement("SELECT * from user where id = ?", Seq(id))

    val mapResult: Future[Any] = future.map(queryResult => queryResult.rows match {
      case Some(resultSet) => {
        for (r <- resultSet) {
          println("id: " + r.apply("id") + "\tname: " + r.apply("name") + "\tage: " + r.apply("age") + "\temail: " + r.apply("email"))
        }
      }
      case None => -1
    })

    val result = Await.result(mapResult, FiniteDuration(5, TimeUnit.SECONDS))

    connection.disconnect
  }

  def getAll() {
    println("Hello")
    val configuration = URLParser.parse("jdbc:mysql://localhost:3306/springboot?username=root&password=root")

    val connection: Connection = new MySQLConnection(configuration)

    Await.result(connection.connect, FiniteDuration(5, TimeUnit.SECONDS))

    println("Hello3")

    println("Main Thread id: " + Thread.currentThread().getId)

    val future: Future[QueryResult] = connection.sendQuery("SELECT * from user")

    val mapResult: Future[Any] = future.map(queryResult => queryResult.rows match {
      case Some(resultSet) => {
        for (r <- resultSet) {
          println(r)
          println("***")
          println("Thread id: " + Thread.currentThread().getId)
        }
      }
      case None => -1
    })

    val result = Await.result(mapResult, FiniteDuration(5, TimeUnit.SECONDS))

    println(result.toString())

    connection.disconnect
  }
  
}