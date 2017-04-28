/**
* Copyright 2017 Gustavo Arjones (@arjones)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
import org.apache.spark.rdd.RDD

/**
 * Created by arjones on 9/11/15.
 */
class TopURLsPipeline extends DummyLog {

  def run(input: RDD[LogRecord]): Unit = {

    val urls = input.
      map { log =>
      val url = ListRecord(log.clientId, log.datetime.toString("yyyy-MM-dd HH:00:00"), log.url)
      (url.key, url)
    }

    val urlsCounter = urls.
      reduceByKey(_ + _).
      map(rec => rec._2)

    import com.datastax.spark.connector._
    urlsCounter.saveToCassandra("analytics", "top_urls")
  }

}
