package com.baidu.study.day01

import java.io

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext, sql}


/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-14 15:19
 */
object MyApp01 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("MyApp01")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._

    val inputDF: DataFrame = spark.read.json("JSON.txt")
    inputDF.createTempView("Person")
    val frame: DataFrame = spark.sql("select * from Person where age >20")
    frame.show()
    //val userDS: Dataset[User] = inputDF.as[User]
    //userDS.foreach(user=>{println(user.friends(0))})

  }

}
