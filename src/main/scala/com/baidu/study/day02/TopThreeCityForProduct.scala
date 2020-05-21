package com.baidu.study.day02

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-15 9:39
 */
/*2.1 需求简介
这里的热门商品是从点击量的维度来看的.
计算各个区域前三大热门商品，并备注上每个商品在主要城市中的分布比例，超过两个城市用其他显示。
例如:
地区	商品名称		点击次数	城市备注
华北	商品A		100000	北京21.2%，天津13.2%，其他65.6%
华北	商品P		80200	北京63.0%，太原10%，其他27.0%
华北	商品M		40000	北京63.0%，太原10%，其他27.0%
东北	商品J		92000	大连28%，辽宁17.0%，其他 55.0%
2.2 思路分析
使用 sql 来完成. 碰到复杂的需求, 可以使用 udf 或 udaf
1.	查询出来所有的点击记录, 并与 city_info 表连接, 得到每个城市所在的地区. 与 Product_info 表连接得到产品名称
2.	按照地区和商品 id 分组, 统计出每个商品在每个地区的总点击次数
3.	每个地区内按照点击次数降序排列
4.	只取前三名. 并把结果保存在数据库中
5.	城市备注需要自定义 UDAF 函数
*/
object TopThreeCityForProduct {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("TopThreeCityForProduct").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()

    import spark.implicits._

    spark.sql("use shixi")
    spark.sql(
      """
        |select
        |  c.*,
        |  p.product_name,
        |  u.click_product_id
        |from
        |  user_visit_action u
        |join
        |  city_info c
        |on
        |  u.city_id = c.city_id
        |join
        |  product_info p
        |on
        |  u.click_product_id = p.product_id;
        |""".stripMargin).show()
    spark .stop()

  }
}
