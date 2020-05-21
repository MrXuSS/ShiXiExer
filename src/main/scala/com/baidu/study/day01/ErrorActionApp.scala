package com.baidu.study.day01

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-09 13:57
 */
object ErrorActionApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("ErrorActionApp")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[String] = sc.textFile("DeviceData.txt")
    val filterRDD: RDD[String] = inputRDD.filter {
      input => {
        val array: Array[String] = input.split(",")
        array.length == 3
      }
    }
    val deviceRDD: RDD[DeviceData] = filterRDD.map {
      line =>
        val words: Array[String] = line.split(",")
        DeviceData(words(0), words(1), words(2))
    }
    val errorRDD: RDD[DeviceData] = deviceRDD.filter(_.deviceData.toInt > 220)

    val groupByIdRDD: RDD[(String, Iterable[DeviceData])] = errorRDD.groupBy(_.deviceId)

    val array: Array[Iterable[DeviceData]] = groupByIdRDD.values.collect()
    // 获取日期连续的数组
    val list = new util.ArrayList[util.ArrayList[DeviceData]]()
    for (elem <- array) {
      val deviceList: List[DeviceData] = elem.toList
      var index = 0
      while (index < deviceList.length){
        val group = new util.ArrayList[DeviceData]()
        group.add(deviceList(index))
        while (index+1 < deviceList.length && deviceList(index+1).dataTime.toInt == deviceList(index).dataTime.toInt + 1){
          group.add(deviceList(index+1))
          index += 1
        }
        list.add(group)
        index += 1
      }
        //[[DeviceData(D0000001,2020040203,230)],
      // [DeviceData(D0000001,2020040205,230),
      // DeviceData(D0000001,2020040206,230),
      // DeviceData(D0000001,2020040207,270),
      // DeviceData(D0000001,2020040208,270),
      // DeviceData(D0000001,2020040209,270),
      // DeviceData(D0000001,2020040210,270)],
      // [DeviceData(D0000001,2020040214,290)]]
      }
    for(i <-0 to list.size() - 1){
      if(list.get(i).size() >= 3){
        for (j<- 0 to list.get(i).size()- 1){
          println(list.get(i).get(j))
          //DeviceData(D0000001,2020040205,230)
          //DeviceData(D0000001,2020040206,230)
          //DeviceData(D0000001,2020040207,270)
          //DeviceData(D0000001,2020040208,270)
          //DeviceData(D0000001,2020040209,270)
          //DeviceData(D0000001,2020040210,270)
        }
      }
    }
  }
}
